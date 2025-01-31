package Kursovaya;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Kursovaya.FileManegment.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsWindow extends JFrame {
    private JComboBox<String> reportTypeComboBox;
    private JPanel paramsPanel;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JPanel chartPanel;

    public ReportsWindow() {
        setTitle("Генерация отчетов");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Основная панель
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель выбора типа отчета
        JPanel topPanel = new JPanel();
        reportTypeComboBox = new JComboBox<>(new String[]{
                "Продажи за период",
                "Статистика по вину",
                "Наличие на складах",
                "Поиск вина"
        });
        reportTypeComboBox.addActionListener(this::updateReportUI);
        topPanel.add(new JLabel("Тип отчета:"));
        topPanel.add(reportTypeComboBox);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Панель параметров и результатов
        paramsPanel = new JPanel();
        chartPanel = new JPanel(new BorderLayout());
        reportTable = new JTable();

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(paramsPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(reportTable), BorderLayout.CENTER);
        centerPanel.add(chartPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        updateReportUI(null);
    }

    private void updateReportUI(ActionEvent e) {
        paramsPanel.removeAll();
        chartPanel.removeAll();
        String reportType = (String) reportTypeComboBox.getSelectedItem();

        switch (reportType) {
            case "Продажи за период":
                addDateRangeFields();
                break;
            case "Статистика по вину":
                addWineSelectionField();
                break;
            case "Наличие на складах":
                addWarehouseSelectionField();
                break;
            case "Поиск вина":
                addWineSearchField();
                break;
        }

        paramsPanel.revalidate();
        paramsPanel.repaint();
        generateReport();
    }

    // Добавление полей для выбора периода
    private void addDateRangeFields() {
        JPanel datePanel = new JPanel();

        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd.MM.yyyy"));

        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "dd.MM.yyyy"));

        JButton generateBtn = new JButton("Сформировать");
        generateBtn.addActionListener(e -> generateReport());

        datePanel.add(new JLabel("С:"));
        datePanel.add(startDateSpinner);
        datePanel.add(new JLabel("По:"));
        datePanel.add(endDateSpinner);
        datePanel.add(generateBtn);

        paramsPanel.add(datePanel);
    }

    // Генерация отчета
    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        try {
            switch (reportType) {
                case "Продажи за период":
                    generateSalesReport();
                    break;
                case "Статистика по вину":
                    generateWineStatistics();
                    break;
                case "Наличие на складах":
                    generateStockReport();
                    break;
                case "Поиск вина":
                    generateWineSearch();
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage());
        }
    }

    // Отчет: Продажи за период
    private void generateSalesReport() throws IOException {
        List<CompletedSale> sales = CompletedSaleFileHandler.loadCompletedSales();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        sales.forEach(sale -> {
            String wine = String.join(", ", sale.getWineIds());
            dataset.addValue(sale.getTotalPrice(), "Продажи", wine);
        });

        updateTable(sales, new String[]{"ID", "Дата", "Покупатель", "Вина", "Количество", "Сумма"});
        updateChart(ChartFactory.createBarChart(
                "Продажи по винам", "Вино", "Сумма (руб.)", dataset
        ));
    }

    // Обновление таблицы
    private void updateTable(List<?> data, String[] columns) {
        tableModel = new DefaultTableModel(columns, 0);
        data.forEach(item -> {
            if (item instanceof CompletedSale sale) {
                tableModel.addRow(new Object[]{
                        sale.getSaleId(),
                        sale.getCompletionDate(),
                        sale.getCustomerId(),
                        String.join(", ", sale.getWineIds()),
                        sale.getQuantities().stream().mapToInt(Integer::intValue).sum(),
                        String.format("%.2f руб.", sale.getTotalPrice())
                });
            }
            // Аналогично для других типов данных
        });
        reportTable.setModel(tableModel);
    }

    // Обновление графика
    private void updateChart(JFreeChart chart) {
        chartPanel.removeAll();
        chartPanel.add(new ChartPanel(chart));
        chartPanel.revalidate();
    }

    private void generateWineStatistics() throws IOException {
        List<CompletedSale> sales = CompletedSaleFileHandler.loadCompletedSales();
        DefaultPieDataset dataset = new DefaultPieDataset();

        Map<String, Double> wineSales = sales.stream()
                .flatMap(s -> s.getWineIds().stream())
                .collect(Collectors.groupingBy(
                        wineId -> getWineNameById(wineId),
                        Collectors.summingDouble(w -> 1.0)
                ));

        wineSales.forEach(dataset::setValue);
        updateChart(ChartFactory.createPieChart("Распределение продаж", dataset));
    }

    private String getWineNameById(String wineId) throws IOException {
        return WineFileHandler.loadWines().stream()
                .filter(w -> w.getId().equals(wineId))
                .findFirst()
                .map(Wine::getBrand)
                .orElse("Неизвестное вино");
    }

    private void addWineSelectionField() {
        JPanel panel = new JPanel();
        JComboBox<String> wineCombo = new JComboBox<>();
        try {
            WineFileHandler.loadWines().forEach(w -> wineCombo.addItem(w.getBrand()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        panel.add(new JLabel("Выберите вино:"));
        panel.add(wineCombo);
        panel.add(createGenerateButton());
        paramsPanel.add(panel);
    }

    private JButton createGenerateButton() {
        JButton btn = new JButton("Сформировать");
        btn.addActionListener(e -> generateReport());
        return btn;
    }

    private void generateStockReport() throws IOException {
        List<Warehouse> warehouses = WarehouseFileHandler.loadWarehouses();
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Склад", "Вино", "Количество"}, 0
        );

        warehouses.forEach(wh -> {
            // Логика получения информации о винах на складе
        });

        reportTable.setModel(model);
        chartPanel.removeAll();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new ReportsWindow().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}