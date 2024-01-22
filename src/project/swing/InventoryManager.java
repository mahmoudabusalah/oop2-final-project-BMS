package project.swing;

import java.util.ArrayList;
import java.util.List;

class InventoryManager {
    private final List<InventoryObserver> observers = new ArrayList<>();
    private final List<Book> books;

    public InventoryManager() {
        books = FileManager.getInstance().readFromFile();
    }

    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
        observer.update(books);
    }

    private void notifyObservers() {
        for (InventoryObserver observer : observers) {
            observer.update(books);
        }
    }

    public synchronized void addBook(Book book) {
        books.add(book);
        FileManager.getInstance().writeToFile(books);
        notifyObservers();
    }

    public void updateBook(Book updatedBook) {
        int index = books.indexOf(updatedBook);
        if (index != -1) {
            books.set(index, updatedBook);
            FileManager.getInstance().writeToFile(books);
            notifyObservers();
        } else {
            System.out.println("Book not found for updating.");
        }
    }

    public synchronized void deleteBook(String title) {
        books.removeIf(book -> book.getTitle().equals(title));
        FileManager.getInstance().writeToFile(books);
        notifyObservers();
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> searchBooksByTitle(String title) {
        List<Book> searchResults = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                searchResults.add(book);
            }
        }
        return searchResults;
    }

}
