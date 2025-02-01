package Kursovaya.FileManegment;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompletedSaleFileHandler {
    private static final String FILE_PATH = "./CompletedSales.txt";
    private static final String DELIMITER = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String HEADER = "SaleID;CompletionDate;CustomerID;WineIDs;Quantities;WarehouseAddress;DeliveryAddress;TotalPrice";

    public static List<CompletedSale> loadCompletedSales() throws IOException {
        List<CompletedSale> sales = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createNewFile();
            return sales;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // Пропуск заголовка

            String line;
            while ((line = reader.readLine()) != null) {
                CompletedSale sale = parseLine(line);
                if (sale != null) sales.add(sale);
            }
        }
        return sales;
    }

    public static void saveCompletedSale(CompletedSale sale) throws IOException {
        List<CompletedSale> sales = loadCompletedSales();
        sales.add(sale);
        saveCompletedSales(sales);
    }

    public static void saveCompletedSales(List<CompletedSale> sales) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();

            for (CompletedSale sale : sales) {
                writer.write(convertToLine(sale));
                writer.newLine();
            }
        }
    }

    private static CompletedSale parseLine(String line) {
        String[] parts = line.split(DELIMITER, -1);
        if (parts.length != 8) {
            System.err.println("Некорректная строка: " + line);
            return null;
        }

        try {
            return new CompletedSale(
                    parts[0].trim(),
                    LocalDate.parse(parts[1].trim(), DATE_FORMATTER),
                    parts[2].trim(),
                    parseList(parts[3]),
                    parseIntegerList(parts[4]),
                    parts[5].trim(),
                    parts[6].trim(),
                    Double.parseDouble(parts[7].trim())
            );
        } catch (Exception e) {
            System.err.println("Ошибка парсинга: " + e.getMessage());
            return null;
        }
    }

    private static String convertToLine(CompletedSale sale) {
        return String.join(DELIMITER,
                sale.getSaleId(),
                sale.getCompletionDate().format(DATE_FORMATTER),
                sale.getCustomerId(),
                String.join(",", sale.getWineIds()),
                convertIntegerList(sale.getQuantities()),
                sale.getWarehouseAddress(),
                sale.getDeliveryAddress(),
                String.valueOf(sale.getTotalPrice())
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
        return list.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
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
            List<CompletedSale> sales = new ArrayList<>();
            sales.add(new CompletedSale(
                    "CS-001",
                    LocalDate.now(),
                    "CUST001",
                    List.of("W001", "W003"),
                    List.of(5, 3),
                    "Склад Б",
                    "Москва, ул. Центральная 10",
                    1750.0
            ));

            // Сохранение
            saveCompletedSales(sales);
            System.out.println("Завершенные продажи сохранены!");

            // Загрузка
            List<CompletedSale> loaded = loadCompletedSales();
            System.out.println("\nЗагруженные данные:");
            loaded.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
