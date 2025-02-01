package Kursovaya.FileManegment;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalesOrderFileHandler {
    private static final String FILE_PATH = "./SalesOrders.txt";
    private static final String DELIMITER = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String HEADER = "OrderID;CustomerID;WineIDs;Quantities;WarehouseAddress;DeliveryAddress;TotalPrice;OrderDate";

    public static List<SalesOrder> loadOrders() throws IOException {
        List<SalesOrder> orders = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createNewFile();
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // Пропуск заголовка

            String line;
            while ((line = reader.readLine()) != null) {
                SalesOrder order = parseLine(line);
                if (order != null) orders.add(order);
            }
        }
        return orders;
    }

    public static void saveOrders(List<SalesOrder> orders) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();

            for (SalesOrder order : orders) {
                writer.write(convertToLine(order));
                writer.newLine();
            }
        }
    }

    private static SalesOrder parseLine(String line) {
        String[] parts = line.split(DELIMITER, -1);
        if (parts.length != 8) {
            System.err.println("Некорректная строка: " + line);
            return null;
        }

        try {
            List<String> wineIds = parseList(parts[2]);
            List<Integer> quantities = parseIntegerList(parts[3]);

            return new SalesOrder(
                    parts[0].trim(),
                    parts[1].trim(),
                    wineIds,
                    quantities,
                    parts[4].trim(),
                    parts[5].trim(),
                    Double.parseDouble(parts[6].trim()),
                    LocalDate.parse(parts[7].trim(), DATE_FORMATTER)
            );
        } catch (Exception e) {
            System.err.println("Ошибка парсинга: " + e.getMessage());
            return null;
        }
    }

    private static String convertToLine(SalesOrder order) {
        return String.join(DELIMITER,
                order.getOrderId(),
                order.getCustomerId(),
                String.join(",", order.getWineIds()),
                convertIntegerList(order.getQuantities()),
                order.getWarehouseAddress(),
                order.getDeliveryAddress(),
                String.valueOf(order.getTotalPrice()),
                order.getOrderDate().format(DATE_FORMATTER)
        );
    }

    private static List<String> parseList(String str) {
        return Arrays.asList(str.split(","));
    }

    private static List<Integer> parseIntegerList(String str) {
        List<Integer> list = new ArrayList<>();
        for (String s : str.split(",")) {
            list.add(Integer.parseInt(s.trim()));
        }
        return list;
    }

    private static String convertIntegerList(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            sb.append(i).append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length()-1) : "";
    }

    private static void createNewFile() throws IOException {
        File directory = new File("Data");
        if (!directory.exists()) directory.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();
        }
    }

    // Тестовый метод
    public static void main(String[] args) {
        try {
            // Пример данных
            List<SalesOrder> orders = new ArrayList<>();
            orders.add(new SalesOrder(
                    "SO-001",
                    "CUST001",
                    List.of("W001", "W003"),
                    List.of(5, 3),
                    "Склад Б",
                    "Москва, ул. Центральная 10",
                    1750.0,
                    LocalDate.now()
            ));

            // Сохранение
            saveOrders(orders);
            System.out.println("Заявки на продажу сохранены!");

            // Загрузка
            List<SalesOrder> loaded = loadOrders();
            System.out.println("\nЗагруженные заявки:");
            loaded.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
