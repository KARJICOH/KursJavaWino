package Kursovaya.FileManegment;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WineFileHandler {
    private static final String FILE_PATH = "./Wines.txt";
    private static final String DELIMITER = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String HEADER = "ID;Brand;Type;Variety;Origin;BottlingDate;AlcoholContent;SupplierID;PurchasePrice;SalePrice";

    public static List<Wine> loadWines() throws IOException {
        List<Wine> wines = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createNewFile();
            return wines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // Пропуск заголовка

            String line;
            while ((line = reader.readLine()) != null) {
                Wine wine = parseLine(line);
                if (wine != null) wines.add(wine);
            }
        }
        return wines;
    }

    public static void saveWines(List<Wine> wines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();

            for (Wine wine : wines) {
                writer.write(convertToLine(wine));
                writer.newLine();
            }
        }
    }

    private static Wine parseLine(String line) {
        String[] parts = line.split(DELIMITER, -1);
        if (parts.length != 10) {
            System.err.println("Некорректная строка: " + line);
            return null;
        }

        try {
            return new Wine(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    LocalDate.parse(parts[5].trim(), DATE_FORMATTER),
                    Double.parseDouble(parts[6].trim()),
                    parts[7].trim(),
                    Double.parseDouble(parts[8].trim()),
                    Double.parseDouble(parts[9].trim())
            );
        } catch (Exception e) {
            System.err.println("Ошибка парсинга: " + e.getMessage());
            return null;
        }
    }

    private static String convertToLine(Wine wine) {
        return String.join(DELIMITER,
                wine.getId(),
                wine.getBrand(),
                wine.getType(),
                wine.getVariety(),
                wine.getOrigin(),
                wine.getBottlingDate().format(DATE_FORMATTER),
                String.valueOf(wine.getAlcoholContent()),
                wine.getSupplierId(),
                String.valueOf(wine.getPurchasePrice()),
                String.valueOf(wine.getSalePrice())
        );
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
            List<Wine> wines = new ArrayList<>();
            wines.add(new Wine(
                    "W001",
                    "Château Margaux",
                    "Красное",
                    "Каберне Совиньон",
                    "Франция",
                    LocalDate.of(2015, 10, 15),
                    13.5,
                    "SUP001",
                    150.0,
                    250.0
            ));

            // Сохранение
            saveWines(wines);
            System.out.println("Данные о винах сохранены!");

            // Загрузка
            List<Wine> loadedWines = loadWines();
            System.out.println("\nЗагруженные вина:");
            loadedWines.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
