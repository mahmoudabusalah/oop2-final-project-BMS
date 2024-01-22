package edu.mybookstore.management.book;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class InventoryManager {
    private List<InventoryObserver> observers = new ArrayList<>();
    private List<Book> books = new ArrayList<>();

    private boolean notifyEnabled = true;

    public void setNotifyEnabled(boolean notifyEnabled) {
        this.notifyEnabled = notifyEnabled;
    }

    public void notifyObservers() {
        if (notifyEnabled) {
            for (InventoryObserver observer : observers) {
                observer.updateInventory(new ArrayList<>(books));
            }
        }
    }

    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }


    public void addBook(Book book) {
        books.add(book);
        notifyObservers();
    }

    public void removeBook(String title) {
        books.removeIf(book -> Objects.equals(book.getTitle(), title));
        notifyObservers();
    }


    public void updateBook(String oldTitle, Book newBook) {
        for (int i = 0; i < books.size(); i++) {
            if (Objects.equals(books.get(i).getTitle(), oldTitle)) {
                books.set(i, newBook);
                notifyObservers();
                return;
            }
        }
        System.out.print("Book not found: " + oldTitle);
    }


    public List<Book> getBooks() {
        return books;
    }

    public List<Book> searchBooksByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }
}
