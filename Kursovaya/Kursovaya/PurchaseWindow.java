package Kursovaya;

import Kursovaya.FileManegment.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseWindow extends JFrame {
    private JComboBox<String> supplierComboBox;
    private JComboBox<String> wineComboBox;
    private JComboBox<String> warehouseComboBox;
    private JSpinner quantitySpinner;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    private List<Supplier> suppliers;
    private List<Wine> wines;
    private List<Warehouse> warehouses;

    public PurchaseWindow() {
        setTitle("Новая закупка");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        loadData();
        initComponents();
    }

    private void loadData() {
        try {
            suppliers = SupplierFileHandler.loadSuppliers();
            wines = WineFileHandler.loadWines();
            warehouses = WarehouseFileHandler.loadWarehouses();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage());
        }
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Верхняя панель выбора данных
        JPanel selectionPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        // Поставщики
        selectionPanel.add(new JLabel("Поставщик:"));
        supplierComboBox = new JComboBox<>(suppliers.stream()
                .map(Supplier::getName)
                .toArray(String[]::new));
        supplierComboBox.addActionListener(this::updateWineList);
        selectionPanel.add(supplierComboBox);

        // Вина
        selectionPanel.add(new JLabel("Вино:"));
        wineComboBox = new JComboBox<>();
        selectionPanel.add(wineComboBox);

        // Склады
        selectionPanel.add(new JLabel("Склад:"));
        warehouseComboBox = new JComboBox<>(warehouses.stream()
                .map(Warehouse::getAddress)
                .toArray(String[]::new));
        selectionPanel.add(warehouseComboBox);

        // Количество
        selectionPanel.add(new JLabel("Количество:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        selectionPanel.add(quantitySpinner);

        mainPanel.add(selectionPanel, BorderLayout.NORTH);

        // Центральная панель с таблицей
        String[] columns = {"Вино", "Количество", "Цена зак.", "Сумма"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Нижняя панель управления
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton addButton = new JButton("Добавить");
        JButton removeButton = new JButton("Удалить");
        JButton saveButton = new JButton("Сохранить");

        addButton.addActionListener(this::addItemToOrder);
        removeButton.addActionListener(this::removeItemFromOrder);
        saveButton.addActionListener(this::savePurchaseOrder);

        controlPanel.add(addButton);
        controlPanel.add(removeButton);


        // Панель итоговой суммы
        totalLabel = new JLabel("Итого: 0.00 руб.");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Компоновка
        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(totalLabel, BorderLayout.SOUTH);
        bottomPanel.add(saveButton, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void updateWineList(ActionEvent e) {
        wineComboBox.removeAllItems();
        String supplierName = (String) supplierComboBox.getSelectedItem();
        Supplier supplier = suppliers.stream()
                .filter(s -> s.getName().equals(supplierName))
                .findFirst()
                .orElse(null);

        if (supplier != null) {
            wines.stream()
                    .filter(w -> w.getSupplierId().equals(supplier.getId()))
                    .map(Wine::getBrand)
                    .forEach(wineComboBox::addItem);
        }
    }

    private void addItemToOrder(ActionEvent e) {
        String wineName = (String) wineComboBox.getSelectedItem();
        int quantity = (int) quantitySpinner.getValue();

        Wine selectedWine = wines.stream()
                .filter(w -> w.getBrand().equals(wineName))
                .findFirst()
                .orElse(null);

        if (selectedWine == null) {
            JOptionPane.showMessageDialog(this, "Выберите вино!");
            return;
        }

        double price = selectedWine.getPurchasePrice();
        double total = price * quantity;

        tableModel.addRow(new Object[]{
                wineName,
                quantity,
                String.format("%.2f руб.", price),
                String.format("%.2f руб.", total)
        });

        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String sumStr = (String) tableModel.getValueAt(i, 3);
            total += Double.parseDouble(sumStr.replaceAll("[^\\d.]", ""));
        }
        totalLabel.setText(String.format("Итого: %.2f руб.", total));
    }

    private void removeItemFromOrder(ActionEvent e) {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Выберите позицию в таблице!",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Удалить выбранную позицию?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            updateTotal();
        }
    }

    private void savePurchaseOrder(ActionEvent e) {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Добавьте хотя бы одну позицию!");
            return;
        }

        try {
            // Сбор данных
            String supplierName = (String) supplierComboBox.getSelectedItem();
            Supplier supplier = suppliers.stream()
                    .filter(s -> s.getName().equals(supplierName))
                    .findFirst()
                    .orElseThrow();

            List<String> wineIds = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String wineName = (String) tableModel.getValueAt(i, 0);
                Wine wine = wines.stream()
                        .filter(w -> w.getBrand().equals(wineName))
                        .findFirst()
                        .orElseThrow();
                wineIds.add(wine.getId());
                quantities.add((Integer) tableModel.getValueAt(i, 1));
            }

            // Создание заявки
            PurchaseOrder order = new PurchaseOrder(
                    "PO-" + System.currentTimeMillis(),
                    supplier.getId(),
                    wineIds,
                    quantities,
                    (String) warehouseComboBox.getSelectedItem(),
                    Double.parseDouble(totalLabel.getText().replaceAll("[^\\d.]", "")),
                    java.time.LocalDate.now()
            );

            // Сохранение
            List<PurchaseOrder> orders = PurchaseOrderFileHandler.loadOrders();
            orders.add(order);
            PurchaseOrderFileHandler.saveOrders(orders);

            JOptionPane.showMessageDialog(this, "Заявка сохранена!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new PurchaseWindow().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}