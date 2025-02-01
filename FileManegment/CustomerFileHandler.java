package Kursovaya.FileManegment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFileHandler {
    private static final String FILE_PATH = "./Customers.txt";
    private static final String DELIMITER = ";";
    private static final String HEADER = "ID;INN;Name;Address;Phone";

    public static List<Customer> loadCustomers() throws IOException {
        List<Customer> customers = new ArrayList<>();
        File file = new File(FILE_PATH);

        // Создаем файл если не существует
        if (!file.exists()) {
            createNewFile();
            return customers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            // Пропускаем заголовок
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = parseLine(line);
                if (customer != null) {
                    customers.add(customer);
                }
            }
        }
        return customers;
    }

    public static void saveCustomers(List<Customer> customers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();

            for (Customer customer : customers) {
                writer.write(convertToLine(customer));
                writer.newLine();
            }
        }
    }

    private static Customer parseLine(String line) {
        String[] parts = line.split(DELIMITER);
        if (parts.length != 5) {
            System.err.println("Некорректная строка: " + line);
            return null;
        }

        return new Customer(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim()
        );
    }

    private static String convertToLine(Customer customer) {
        return String.join(DELIMITER,
                customer.getId(),
                customer.getInn(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone()
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

    // Тестирование
    public static void main(String[] args) {
        try {
            // Пример использования
            List<Customer> customers = new ArrayList<>();
            customers.add(new Customer("C001", "7712345678", "ООО Ромашка", "Москва", "+79991234567"));
            customers.add(new Customer("C002", "7754321876", "ИП Иванов", "СПб", "+78122345678"));

            saveCustomers(customers);
            System.out.println("Данные успешно сохранены!");

            List<Customer> loaded = loadCustomers();
            System.out.println("Загруженные данные:");
            loaded.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}