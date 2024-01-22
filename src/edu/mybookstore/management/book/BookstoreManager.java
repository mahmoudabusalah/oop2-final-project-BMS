package edu.mybookstore.management.book;


import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import java.io.IOException;
import java.util.List;

public class BookstoreManager implements InventoryObserver {
    private static final int ADD_BOOK = 1;
    private static final int SHOW_BOOKS = 2;
    private static final int REMOVE_BOOK = 3;
    private static final int UPDATE_BOOK = 4;
    private static final int SEARCH_BOOK_BY_TITLE = 5;
    private static final int EXIT = 6;

    private InventoryManager inventoryManager;
    private Scanner scanner;

    public BookstoreManager() {
        inventoryManager = new InventoryManager();
        inventoryManager.addObserver(this);
        scanner = new Scanner(System.in);
        loadBooksFromFile();
    }
    private void loadBooksFromFile() {
        try {
            inventoryManager.setNotifyEnabled(false);

            List<Book> booksFromFile = BookFileHandler.getInstance().readBooks();
            for (Book book : booksFromFile) {
                inventoryManager.addBook(book);
            }

            inventoryManager.setNotifyEnabled(true);
            inventoryManager.notifyObservers();

        } catch (IOException e) {
            System.out.println("Error occurred while loading books from file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void start() {
        printWelcomeMessage();
        boolean quit = false;
        while (!quit) {
            showMenuOptions();
            int choice = getUserInput();

            switch (choice) {
                case ADD_BOOK:
                    addBook();
                    break;
                case SHOW_BOOKS:
                    displayBooks();
                    break;
                case REMOVE_BOOK:
                    removeBook();
                    break;
                case UPDATE_BOOK:
                    updateBook();
                    break;
                case SEARCH_BOOK_BY_TITLE:
                    searchByTitleName();
                    break;
                case EXIT:
                    quit = true;
                    break;
                default:
                    printInvalidOptionMessage();
            }
        }
        printExitMessage();
    }

    private void printWelcomeMessage() {
        System.out.println("\n******************** Welcome to the BMS! ********************");
    }

    private void showMenuOptions() {
        System.out.println("\nSelect From The Following Options:");
        System.out.println("[1] Add Book");
        System.out.println("[2] Show Books");
        System.out.println("[3] Remove Book");
        System.out.println("[4] Update Book");
        System.out.println("[5] Search Book by Title");
        System.out.println("[6] Exit");
        System.out.print("\nChoose an option: ");
    }

    private int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void printInvalidOptionMessage() {
        System.out.println("Invalid option. Please try again.");
    }

    private void printExitMessage() {
        System.out.println("Thank you for using the Bookstore Management System!");
    }

    private void addBook() {
        System.out.print("Enter book type (Fiction/NonFiction): ");
        String type = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for price. Please enter a number.");
            return;
        }

        Book book = BookFactory.createBook(type, title, author, price);
        if (book != null) {
            try {
                BookFileHandler.getInstance().writeBook(book);
                inventoryManager.addBook(book);
                System.out.println("Book added successfully.");
            } catch (IOException e) {
                System.out.println("Error occurred while adding the book: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid book type.");
        }

    }


    private void displayBooks() {
        try {
            List<Book> books = BookFileHandler.getInstance().readBooks();
            if (books.isEmpty()) {
                System.out.println("\n[ No books available. ]\n");
            } else {
                System.out.println("\n********************************* Book List *********************************");
                System.out.printf("%-26s | %-20s | %-15s | %-10s\n", "Title", "Author", "Type", "Price");
                System.out.println("-----------------------------------------------------------------------------");
                for (Book book : books) {
                    System.out.printf(" %-25s | %-20s | %-15s | $%.2f\n", book.getTitle(), book.getAuthor(), book.getBookType(), book.getPrice());
                }
                System.out.println("*****************************************************************************");
            }
        } catch (IOException e) {
            System.out.println("Error occurred while displaying the books: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void removeBook() {
        System.out.print("Enter the title of the book to remove: ");
        String title = scanner.nextLine();

        try {
            List<Book> books = BookFileHandler.getInstance().readBooks();

            boolean isRemoved = books.removeIf(book -> book.getTitle().equals(title));
            if (isRemoved) {
                BookFileHandler.getInstance().updateBooks(books);
                inventoryManager.removeBook(title);
                System.out.println("Book removed successfully.");
            } else {
                System.out.println("No book found with the title: " + title);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while removing the book: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void updateBook() {
        System.out.print("Enter the title of the book to update: ");
        String oldTitle = scanner.nextLine();

        System.out.print("Enter new book type (Fiction/NonFiction): ");
        String type = scanner.nextLine();
        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new author: ");
        String author = scanner.nextLine();
        System.out.print("Enter new price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for price. Please enter a number.");
            return;
        }

        Book newBook = BookFactory.createBook(type, title, author, price);
        if (newBook != null) {
            inventoryManager.updateBook(oldTitle, newBook);
            try {

                BookFileHandler.getInstance().updateBooks(inventoryManager.getBooks());
                System.out.println("Book updated successfully.");
            } catch (IOException e) {
                System.out.println("Error occurred while updating the book: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid book type.");
        }
    }


    private void searchByTitleName() {
        System.out.print("Enter Book Title:");
        String bookTitle = scanner.nextLine();

        List<Book> foundBooks = inventoryManager.searchBooksByTitle(bookTitle);

        if (foundBooks.isEmpty()) {
            System.out.println("No Books with the title '" + bookTitle + "' Found.");
        } else {
            for (Book book : foundBooks) {
                System.out.println(book);
            }
        }
    }



    @Override
    public void updateInventory(List<Book> books) {
        System.out.println("Inventory Updated: " + books);
    }

    public static void main(String[] args) {
        new BookstoreManager().start();
    }
}
