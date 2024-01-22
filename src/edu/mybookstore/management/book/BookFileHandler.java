package edu.mybookstore.management.book;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookFileHandler {
    private static BookFileHandler instance;
    private static final String FILE_PATH = BookFileHandler.class.getResource("Book_data.txt").getFile();

    private BookFileHandler() {}

    public static synchronized BookFileHandler getInstance() {
        if (instance == null) {
            instance = new BookFileHandler();
        }
        return instance;
    }

    public List<Book> readBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Book book = BookFactory.createBook(data[0], data[1], data[2], Double.parseDouble(data[3]));
                if (book != null) {
                    books.add(book);
                    // System.out.println("Loaded book: " + book);
                }
            }
        }
        return books;
    }


    public void writeBook(Book book) throws IOException {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            String bookData = buildBookDataString(book);
            writer.write(bookData);
            writer.newLine();
        }
    }

    public void updateBooks(List<Book> books) throws IOException {
        try (FileWriter fw = new FileWriter(FILE_PATH, false);
             BufferedWriter writer = new BufferedWriter(fw)) {
            for (Book book : books) {
                String bookData = buildBookDataString(book);
                writer.write(bookData);
                writer.newLine();
            }
        }
    }

    private String buildBookDataString(Book book) {
        return (book instanceof FictionBook ? "Fiction" : "NonFiction") + ","
                + book.getTitle() + ","
                + book.getAuthor() + ","
                + book.getPrice();
    }

}