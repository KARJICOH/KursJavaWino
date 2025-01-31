package Kursovaya.FileManegment;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompletedPurchaseFileHandler {
    private static final String FILE_PATH = "./CompletedPurchases.txt";
    private static final String DELIMITER = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String HEADER = "PurchaseID;CompletionDate;SupplierID;WineIDs;Quantities;DeliveryAddress;TotalPrice";

    public static List<CompletedPurchase> loadCompletedPurchases() throws IOException {
        List<CompletedPurchase> purchases = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createNewFile();
            return purchases;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // Пропуск заголовка

            String line;
            while ((line = reader.readLine()) != null) {
                CompletedPurchase purchase = parseLine(line);
                if (purchase != null) purchases.add(purchase);
            }
        }
        return purchases;
    }

    public static void saveCompletedPurchase(CompletedPurchase purchase) throws IOException {
        List<CompletedPurchase> purchases = loadCompletedPurchases();
        purchases.add(purchase);
        saveCompletedPurchases(purchases);
    }

    public static void saveCompletedPurchases(List<CompletedPurchase> purchases) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();

            for (CompletedPurchase purchase : purchases) {
                writer.write(convertToLine(purchase));
                writer.newLine();
            }
        }
    }

    private static CompletedPurchase parseLine(String line) {
        String[] parts = line.split(DELIMITER, -1);
        if (parts.length != 7) {
            System.err.println("Некорректная строка: " + line);
            return null;
        }

        try {
            return new CompletedPurchase(
                    parts[0].trim(),
                    LocalDate.parse(parts[1].trim(), DATE_FORMATTER),
                    parts[2].trim(),
                    parseList(parts[3]),
                    parseIntegerList(parts[4]),
                    parts[5].trim(),
                    Double.parseDouble(parts[6].trim())
            );
        } catch (Exception e) {
            System.err.println("Ошибка парсинга: " + e.getMessage());
            return null;
        }
    }

    private static String convertToLine(CompletedPurchase purchase) {
        return String.join(DELIMITER,
                purchase.getPurchaseId(),
                purchase.getCompletionDate().format(DATE_FORMATTER),
                purchase.getSupplierId(),
                String.join(",", purchase.getWineIds()),
                convertIntegerList(purchase.getQuantities()),
                purchase.getDeliveryAddress(),
                String.valueOf(purchase.getTotalPrice())
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
            List<CompletedPurchase> purchases = new ArrayList<>();
            purchases.add(new CompletedPurchase(
                    "CP-001",
                    LocalDate.now(),
                    "SUP001",
                    List.of("W001", "W002"),
                    List.of(10, 5),
                    "Склад А",
                    2000.0
            ));

            // Сохранение
            saveCompletedPurchases(purchases);
            System.out.println("Завершенные закупки сохранены!");

            // Загрузка
            List<CompletedPurchase> loaded = loadCompletedPurchases();
            System.out.println("\nЗагруженные данные:");
            loaded.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
