package project.swing;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static FileManager instance;
    private final String filePath = "C:\\Users\\KnightK0der\\Desktop\\swing.txt";

    private FileManager() {}

    public static synchronized FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public synchronized void writeToFile(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                if (book instanceof FictionBook) {
                    writer.println("Fiction," + book.getTitle() + "," + book.getAuthor() + "," + book.getPrice());
                } else if (book instanceof NonFictionBook) {
                    writer.println("NonFiction," + book.getTitle() + "," + book.getAuthor() + "," + book.getPrice());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<Book> readFromFile() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String type = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    double price = Double.parseDouble(parts[3]);

                    if (type.equalsIgnoreCase("Fiction")) {
                        books.add(new FictionBook(title, author, price));
                    } else if (type.equalsIgnoreCase("NonFiction")) {
                        books.add(new NonFictionBook(title, author, price));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return books;
    }
}


