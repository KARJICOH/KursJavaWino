package Kursovaya;
import Kursovaya.FileManegment.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmationWindow extends JFrame {
    private final boolean isPurchase;
    private DefaultTableModel tableModel;
    private JTable ordersTable;

    public ConfirmationWindow(boolean isPurchase) {
        this.isPurchase = isPurchase;
        setTitle(isPurchase ? "Подтверждение закупок" : "Подтверждение продаж");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadData();
    }

    private void initUI() {
        // Настройка таблицы
        String[] columns = isPurchase ?
                new String[]{"ID заказа", "Поставщик", "Сумма", "Дата заказа"} :
                new String[]{"ID заказа", "Покупатель", "Сумма", "Дата заказа"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable = new JTable(tableModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Кнопка подтверждения
        JButton confirmButton = new JButton("Подтвердить выбранное");
        confirmButton.addActionListener(this::handleConfirmation);

        // Компоновка
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        mainPanel.add(confirmButton, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadData() {
        try {
            if (isPurchase) {
                List<PurchaseOrder> orders = PurchaseOrderFileHandler.loadOrders();
                orders.forEach(order -> tableModel.addRow(new Object[]{
                        order.getOrderId(),
                        order.getSupplierId(),
                        String.format("%.2f руб.", order.getTotalPrice()),
                        order.getOrderDate()
                }));
            } else {
                List<SalesOrder> orders = SalesOrderFileHandler.loadOrders();
                orders.forEach(order -> tableModel.addRow(new Object[]{
                        order.getOrderId(),
                        order.getCustomerId(),
                        String.format("%.2f руб.", order.getTotalPrice()),
                        order.getOrderDate()
                }));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage());
        }
    }

    private void handleConfirmation(ActionEvent e) {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите заявку из списка!");
            return;
        }

        String orderId = (String) tableModel.getValueAt(selectedRow, 0);
        try {
            if (isPurchase) {
                confirmPurchase(orderId);
            } else {
                confirmSale(orderId);
            }
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Операция успешно подтверждена!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
        }
    }

    private void confirmPurchase(String orderId) throws IOException {
        // Получение данных закупки
        List<PurchaseOrder> orders = PurchaseOrderFileHandler.loadOrders();
        PurchaseOrder order = orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow();

        // Обновление склада
        WarehouseFileHandler.updateStock(
                order.getWineIds(),
                order.getQuantities(),
                true,
                order.getDeliveryAddress()
        );

        // Перенос в завершённые
        CompletedPurchase completed = new CompletedPurchase(
                orderId,
                LocalDate.now(),
                order.getSupplierId(),
                order.getWineIds(),
                order.getQuantities(),
                order.getDeliveryAddress(),
                order.getTotalPrice()
        );
        CompletedPurchaseFileHandler.saveCompletedPurchase(completed);

        // Удаление из текущих заявок
        List<PurchaseOrder> updatedOrders = orders.stream()
                .filter(o -> !o.getOrderId().equals(orderId))
                .collect(Collectors.toList());
        PurchaseOrderFileHandler.saveOrders(updatedOrders);
    }

    private void confirmSale(String orderId) throws IOException {
        // Получение данных продажи
        List<SalesOrder> orders = SalesOrderFileHandler.loadOrders();
        SalesOrder order = orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow();

        // Проверка и обновление склада
        WarehouseFileHandler.updateStock(
                order.getWineIds(),
                order.getQuantities(),
                false,
                order.getWarehouseAddress()
        );

        // Перенос в завершённые
        CompletedSale completed = new CompletedSale(
                orderId,
                LocalDate.now(),
                order.getCustomerId(),
                order.getWineIds(),
                order.getQuantities(),
                order.getWarehouseAddress(),
                order.getDeliveryAddress(),
                order.getTotalPrice()
        );
        CompletedSaleFileHandler.saveCompletedSale(completed);

        // Удаление из текущих заявок
        List<SalesOrder> updatedOrders = orders.stream()
                .filter(o -> !o.getOrderId().equals(orderId))
                .collect(Collectors.toList());
        SalesOrderFileHandler.saveOrders(updatedOrders);
    }
}