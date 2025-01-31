package Kursovaya.FileManegment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierFileHandler {
    private static final String FILE_PATH = "./Suppliers.txt";
    private static final String DELIMITER = ";";
    private static final String HEADER = "ID;INN;Name;Address;Phone";

    public static List<Supplier> loadSuppliers() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createNewFile();
            return suppliers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // Пропуск заголовка

            String line;
            while ((line = reader.readLine()) != null) {
                Supplier supplier = parseLine(line);
                if (supplier != null) {
                    suppliers.add(supplier);
                }
            }
        }
        return suppliers;
    }

    public static void saveSuppliers(List<Supplier> suppliers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();

            for (Supplier supplier : suppliers) {
                writer.write(convertToLine(supplier));
                writer.newLine();
            }
        }
    }

    private static Supplier parseLine(String line) {
        String[] parts = line.split(DELIMITER);
        if (parts.length != 5) {
            System.err.println("Некорректная строка: " + line);
            return null;
        }

        return new Supplier(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim()
        );
    }

    private static String convertToLine(Supplier supplier) {
        return String.join(DELIMITER,
                supplier.getId(),
                supplier.getInn(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getPhone()
        );
    }

    private static void createNewFile() throws IOException {
        File directory = new File("Data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();
        }
    }

    // Тестовый метод
    public static void main(String[] args) {
        try {
            // Пример данных
            List<Supplier> suppliers = new ArrayList<>();
            suppliers.add(new Supplier("SUP001", "7712345678", "ООО Виноград", "Москва, ул. Винная 15", "+79991234567"));
            suppliers.add(new Supplier("SUP002", "7754321876", "ИП Петров", "СПб, Невский пр. 100", "+78122345678"));

            // Сохранение
            saveSuppliers(suppliers);
            System.out.println("Данные поставщиков сохранены!");

            // Загрузка
            List<Supplier> loadedSuppliers = loadSuppliers();
            System.out.println("\nЗагруженные поставщики:");
            loadedSuppliers.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}