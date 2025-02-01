package Kursovaya.FileManegment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseFileHandler {
    private static final String FILE_PATH = "./Warehouses.txt";
    private static final String DELIMITER = ";";
    private static final String HEADER = "Address;Capacity;CurrentStock";

    public static List<Warehouse> loadWarehouses() throws IOException {
        List<Warehouse> warehouses = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createNewFile();
            return warehouses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // Пропуск заголовка

            String line;
            while ((line = reader.readLine()) != null) {
                Warehouse warehouse = parseLine(line);
                if (warehouse != null) {
                    warehouses.add(warehouse);
                }
            }
        }
        return warehouses;
    }

    public static void updateStock(List<String> wineIds, List<Integer> quantities,
                                   boolean isAddition, String warehouseAddress)
            throws IOException {

        List<Warehouse> warehouses = loadWarehouses();
        Warehouse targetWarehouse = warehouses.stream()
                .filter(w -> w.getAddress().equals(warehouseAddress))
                .findFirst()
                .orElseThrow(() -> new IOException("Склад не найден!"));

        for (int i = 0; i < wineIds.size(); i++) {
            String wineId = wineIds.get(i);
            int quantity = quantities.get(i);

            if (isAddition) {
                targetWarehouse.setCurrentStock(targetWarehouse.getCurrentStock() + quantity);
            } else {
                if (targetWarehouse.getCurrentStock() < quantity) {
                    throw new IOException("Недостаточно товара '" + wineId + "' на складе!");
                }
                targetWarehouse.setCurrentStock(targetWarehouse.getCurrentStock() - quantity);
            }
        }

        saveWarehouses(warehouses);
    }

    public static void saveWarehouses(List<Warehouse> warehouses) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();

            for (Warehouse warehouse : warehouses) {
                writer.write(convertToLine(warehouse));
                writer.newLine();
            }
        }
    }

    private static Warehouse parseLine(String line) {
        String[] parts = line.split(DELIMITER);
        if (parts.length != 3) {
            System.err.println("Ошибка формата строки: " + line);
            return null;
        }

        try {
            return new Warehouse(
                    parts[0].trim(),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim())
            );
        } catch (NumberFormatException e) {
            System.err.println("Ошибка парсинга чисел: " + e.getMessage());
            return null;
        }
    }

    private static String convertToLine(Warehouse warehouse) {
        return String.join(DELIMITER,
                warehouse.getAddress(),
                String.valueOf(warehouse.getCapacity()),
                String.valueOf(warehouse.getCurrentStock())
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
            List<Warehouse> warehouses = new ArrayList<>();
            warehouses.add(new Warehouse("ул. Винная, 15", 1000, 250));
            warehouses.add(new Warehouse("пр. Бочковой, 42", 2000, 1500));

            // Сохранение
            saveWarehouses(warehouses);
            System.out.println("Данные складов сохранены!");

            // Загрузка
            List<Warehouse> loaded = loadWarehouses();
            System.out.println("\nЗагруженные склады:");
            loaded.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}