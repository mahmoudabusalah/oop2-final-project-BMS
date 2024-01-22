package edu.mybookstore.management.book;

public class BookFactory {
    public static Book createBook(String type, String title, String author, double price) {
        return switch (type.toLowerCase()) {
            case "fiction" -> new FictionBook(title, author, price);
            case "nonfiction" -> new NonFictionBook(title, author, price);
            default -> null;
        };
    }
}
