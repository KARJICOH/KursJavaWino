package Kursovaya;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class WineSupplyApp extends JFrame {

    public WineSupplyApp() {
        initUI();
    }

    private void initUI() {
        setTitle("Управление поставками вин");
        setSize(300, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создание панели с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Шрифт для кнопок
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        // Добавление кнопок
        addButton(buttonPanel, "Новая закупка", e -> openPurchaseWindow(), buttonFont);
        addButton(buttonPanel, "Новая продажа", e -> openSaleWindow(), buttonFont);
        addButton(buttonPanel, "Подтвердить закупки", e -> openConfirmationWindow(true), buttonFont);
        addButton(buttonPanel, "Подтвердить продажи", e -> openConfirmationWindow(false), buttonFont);
        addButton(buttonPanel, "Справочники", e -> openDataManagementWindow(), buttonFont);
        addButton(buttonPanel, "Отчёты", e -> openReportsWindow(), buttonFont);
        addButton(buttonPanel, "Выход", e -> System.exit(0), buttonFont);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void addButton(JPanel panel, String text, ActionListener listener, Font font) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(font);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.addActionListener(listener);
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    // Методы открытия окон
    private void openPurchaseWindow() {
        new PurchaseWindow().setVisible(true);
    }

    private void openSaleWindow() {
        new SaleWindow().setVisible(true);
    }

    private void openConfirmationWindow(boolean isPurchase) {
        new ConfirmationWindow(isPurchase).setVisible(true);
    }

    private void openDataManagementWindow() {
        new DataManagementWindow().setVisible(true);
    }

    private void openReportsWindow() {
        new ReportsWindow().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new WineSupplyApp().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}