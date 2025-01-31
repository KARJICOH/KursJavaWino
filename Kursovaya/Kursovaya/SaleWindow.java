package Kursovaya;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SaleWindow extends JFrame {
    private JComboBox<String> customerComboBox;
    private JComboBox<String> wineComboBox;
    private JComboBox<String> warehouseComboBox;
    private JSpinner quantitySpinner;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public SaleWindow() {
        setTitle("Создание заявки на продажу");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Инициализация компонентов
        String[] customers = {"Покупатель 1", "Покупатель 2"};
        String[] wines = {"Вино 1", "Вино 2"};
        String[] warehouses = {"Склад А", "Склад Б"};

        customerComboBox = new JComboBox<>(customers);
        wineComboBox = new JComboBox<>(wines);
        warehouseComboBox = new JComboBox<>(warehouses);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        // Кнопки
        JButton addButton = new JButton("Добавить");
        JButton deleteButton = new JButton("Удалить");
        JButton createOrderButton = new JButton("Составить заявку");

        // Настройка таблицы
        String[] columns = {"Вино", "Количество", "Цена продажи", "Сумма"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Итоговая сумма
        totalLabel = new JLabel("Итого: 0.00 руб.");
        totalLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Основная панель
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель элементов управления
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Строки элементов
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Покупатель:"));
        row1.add(customerComboBox);
        row1.add(Box.createHorizontalStrut(20));
        row1.add(new JLabel("Склад отгрузки:"));
        row1.add(warehouseComboBox);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Вино:"));
        row2.add(wineComboBox);
        row2.add(Box.createHorizontalStrut(20));
        row2.add(new JLabel("Количество:"));
        row2.add(quantitySpinner);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(addButton);
        row3.add(deleteButton);
        row3.add(Box.createHorizontalStrut(20));
        row3.add(createOrderButton);

        // Сборка интерфейса
        controlPanel.add(row1);
        controlPanel.add(row2);
        controlPanel.add(row3);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        tablePanel.add(totalLabel, BorderLayout.SOUTH);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);

        // Обработчики событий
        addButton.addActionListener(e -> addItem());
        deleteButton.addActionListener(e -> deleteItem());
        createOrderButton.addActionListener(e -> createSale());
    }

    private void addItem() {
        String wine = (String) wineComboBox.getSelectedItem();
        int quantity = (int) quantitySpinner.getValue();
        double price = 2000.00; // Цена продажи

        Object[] rowData = {
                wine,
                quantity,
                price,
                quantity * price
        };

        tableModel.addRow(rowData);
        updateTotal();
    }

    private void deleteItem() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите позицию для удаления!");
            return;
        }

        tableModel.removeRow(selectedRow);
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            total += (double) tableModel.getValueAt(i, 3);
        }
        totalLabel.setText(String.format("Итого: %.2f руб.", total));
    }

    private void createSale() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Добавьте позиции для продажи!");
            return;
        }

        // Логика проверки наличия товара на складе
        boolean hasEnoughStock = true; // Заглушка для проверки

        if (!hasEnoughStock) {
            JOptionPane.showMessageDialog(this, "Недостаточно товара на складе!");
            return;
        }

        JOptionPane.showMessageDialog(this, "Продажа успешно оформлена!");
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SaleWindow().setVisible(true);
        });
    }
}
