package Kursovaya;

import Kursovaya.FileManegment.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManagementWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private List<Wine> wines = new ArrayList<>();
    private List<Supplier> suppliers = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Warehouse> warehouses = new ArrayList<>();
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
    private List<SalesOrder> salesOrders = new ArrayList<>();
    private List<CompletedPurchase> completedPurchases = new ArrayList<>();
    private List<CompletedSale> completedSales = new ArrayList<>();

    public DataManagementWindow() {
        setTitle("Управление справочниками");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        initTabs();
        add(tabbedPane, BorderLayout.CENTER);
        addControlButtons();
        loadData();
    }

    private void initTabs() {
        tabbedPane.addTab("Вино", createTablePanel(
                new String[]{"ID", "Бренд", "Тип", "Сорт", "Происхождение", "Дата розлива", "Алк.%", "Поставщик", "Цена зак.", "Цена прод."},
                wines
        ));

        tabbedPane.addTab("Поставщики", createTablePanel(
                new String[]{"ID", "ИНН", "Название", "Адрес", "Телефон"},
                suppliers
        ));

        tabbedPane.addTab("Покупатели", createTablePanel(
                new String[]{"ID", "ИНН", "Название", "Адрес", "Телефон"},
                customers
        ));

        tabbedPane.addTab("Склады", createTablePanel(
                new String[]{"Адрес", "Вместимость", "Текущий запас"},
                warehouses
        ));

        tabbedPane.addTab("Закупки", createOrderPanel(
                new String[]{"ID заказа", "Поставщик", "Вина", "Количество", "Адрес", "Сумма", "Дата"},
                purchaseOrders
        ));

        tabbedPane.addTab("Продажи", createOrderPanel(
                new String[]{"ID заказа", "Покупатель", "Вина", "Количество", "Склад", "Адрес", "Сумма", "Дата"},
                salesOrders
        ));

        tabbedPane.addTab("Заверш. закупки", createCompletedPanel(
                new String[]{"ID", "Дата", "Поставщик", "Вина", "Кол-во", "Адрес", "Сумма"},
                completedPurchases
        ));

        tabbedPane.addTab("Заверш. продажи", createCompletedPanel(
                new String[]{"ID", "Дата", "Покупатель", "Вина", "Кол-во", "Склад", "Адрес", "Сумма"},
                completedSales
        ));
    }

    private JPanel createTablePanel(String[] columns, List<?> data) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton("Добавить", e -> addRow(table)));
        buttonPanel.add(createButton("Изменить", e -> editRow(table)));
        buttonPanel.add(createButton("Удалить", e -> deleteRow(table)));

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createOrderPanel(String[] columns, List<?> data) {
        // Аналогично createTablePanel с дополнительными полями
        return createTablePanel(columns, data);
    }

    private JPanel createCompletedPanel(String[] columns, List<?> data) {
        // Панель только для просмотра
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setEnabled(false);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.addActionListener(listener);
        return btn;
    }

    private void addControlButtons() {
        JPanel bottomPanel = new JPanel();
        JButton saveButton = new JButton("Сохранить все изменения");
        saveButton.addActionListener(e -> saveAllData());
        bottomPanel.add(saveButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        try {
            wines = WineFileHandler.loadWines();
            suppliers = SupplierFileHandler.loadSuppliers();
            customers = CustomerFileHandler.loadCustomers();
            warehouses = WarehouseFileHandler.loadWarehouses();
            purchaseOrders = PurchaseOrderFileHandler.loadOrders();
            salesOrders = SalesOrderFileHandler.loadOrders();
            completedPurchases = CompletedPurchaseFileHandler.loadCompletedPurchases();
            completedSales = CompletedSaleFileHandler.loadCompletedSales();

            updateAllTables();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage());
        }
    }

    private void updateAllTables() {
        updateTable(0, wines);
        updateTable(1, suppliers);
        updateTable(2, customers);
        updateTable(3, warehouses);
        updateTable(4, purchaseOrders);
        updateTable(5, salesOrders);
        updateTable(6, completedPurchases);
        updateTable(7, completedSales);
    }

    private void updateTable(int tabIndex, List<?> data) {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(tabIndex)).getComponent(0)).getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Object item : data) {
            model.addRow(convertItemToRow(item));
        }
    }

    private Object[] convertItemToRow(Object item) {
        // Реализация преобразования объекта в массив данных для таблицы
        // (пример для Wine)
        if (item instanceof Wine wine) {
            return new Object[]{
                    wine.getId(),
                    wine.getBrand(),
                    wine.getType(),
                    wine.getVariety(),
                    wine.getOrigin(),
                    wine.getBottlingDate(),
                    wine.getAlcoholContent(),
                    wine.getSupplierId(),
                    wine.getPurchasePrice(),
                    wine.getSalePrice()
            };
        }
        // Аналогично для других классов
        return new Object[0];
    }

    private void addRow(JTable table) {
        // Реализация диалога добавления новой записи
    }

    private void editRow(JTable table) {
        // Реализация редактирования выбранной записи
    }

    private void deleteRow(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) return;
        ((DefaultTableModel) table.getModel()).removeRow(row);
    }

    private void saveAllData() {
        try {
            // Преобразование данных таблиц в объекты
            WineFileHandler.saveWines(wines);
            SupplierFileHandler.saveSuppliers(suppliers);
            CustomerFileHandler.saveCustomers(customers);
            WarehouseFileHandler.saveWarehouses(warehouses);
            PurchaseOrderFileHandler.saveOrders(purchaseOrders);
            SalesOrderFileHandler.saveOrders(salesOrders);

            JOptionPane.showMessageDialog(this, "Данные успешно сохранены!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new DataManagementWindow().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
