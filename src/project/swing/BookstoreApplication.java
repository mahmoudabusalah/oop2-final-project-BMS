package project.swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookstoreApplication {

    private final InventoryManager inventoryManager;
    private JFrame mainFrame;
    private JList<Book> bookList;
    private final DefaultListModel<Book> bookListModel;

    private static final String ADD_BUTTON_LABEL = "Add Book";
    private static final String UPDATE_BUTTON_LABEL = "Update Book";
    private static final String DELETE_BUTTON_LABEL = "Delete Book";
    private static final String SHOW_ALL_BUTTON_LABEL = "Show All Books";
    private static final String SEARCH_BUTTON_LABEL = "Search by Title";
    private static final String EXIT_BUTTON_LABEL = "Exit";

    public BookstoreApplication() {
        inventoryManager = new InventoryManager();
        bookListModel = new DefaultListModel<>();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setupMainFrame();
        setupBookList();
        setupButtonPanel();
        setupObservers();

        mainFrame.setSize(700, 600);
        mainFrame.setVisible(true);
    }

    private void setupMainFrame() {
        mainFrame = new JFrame("Bookstore Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
    }

    private void setupBookList() {
        bookList = new JList<>(bookListModel);
        Font largerFont = new Font(bookList.getFont().getName(), Font.PLAIN, 23);
        bookList.setFont(largerFont);
        mainFrame.add(new JScrollPane(bookList), BorderLayout.CENTER);
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
        addButton(buttonPanel, ADD_BUTTON_LABEL, this::addBook);
        addButton(buttonPanel, UPDATE_BUTTON_LABEL, this::updateBook);
        addButton(buttonPanel, DELETE_BUTTON_LABEL, this::deleteBook);
        addButton(buttonPanel, SHOW_ALL_BUTTON_LABEL, this::showAllBooks);
        addButton(buttonPanel, SEARCH_BUTTON_LABEL, this::searchByTitle);
        addButton(buttonPanel, EXIT_BUTTON_LABEL, this::exitApplication);

        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addButton(JPanel panel, String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    private void setupObservers() {
        inventoryManager.addObserver(this::updateBookList);
    }

    private void updateBookList(List<Book> books) {
        SwingUtilities.invokeLater(() -> {
            bookListModel.clear();
            for (Book book : books) {
                bookListModel.addElement(book);
            }
        });
    }

    private void addBook() {
        JPanel panel = createBookInputPanel();
        int result = JOptionPane.showConfirmDialog(null, panel, "Add a New Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String title = getFieldText(panel, "Title:");
            String author = getFieldText(panel, "Author:");
            String priceString = getFieldText(panel, "Price:");
            String type = (String) getComboBoxSelectedItem(panel);

            try {
                assert priceString != null;
                double price = Double.parseDouble(priceString);
                assert type != null;
                Book book = BookFactory.createBook(type, title, author, price);

                if (book != null) {
                    inventoryManager.addBook(book);
                } else {
                    showErrorDialog("Invalid book type");
                }

            } catch (NumberFormatException e) {
                showErrorDialog("Invalid price format");
            }
        }
    }


    private void updateBook() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(mainFrame, "No book selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField titleField = new JTextField(selectedBook.getTitle());
        JTextField authorField = new JTextField(selectedBook.getAuthor());
        JTextField priceField = new JTextField(String.valueOf(selectedBook.getPrice()));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Fiction", "Non-Fiction"});
        typeBox.setSelectedItem(selectedBook instanceof FictionBook ? "Fiction" : "Non-Fiction");

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Type:"));
        panel.add(typeBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            String priceString = priceField.getText();
            String type = (String) typeBox.getSelectedItem();

            try {
                double price = Double.parseDouble(priceString);
                selectedBook.setTitle(title);
                selectedBook.setAuthor(author);
                selectedBook.setPrice(price);

                inventoryManager.updateBook(selectedBook);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "Invalid price format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteBook() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook != null) {
            inventoryManager.deleteBook(selectedBook.getTitle());
        }
    }

    private void showAllBooks() {
        List<Book> allBooks = inventoryManager.getAllBooks();
        StringBuilder message = new StringBuilder("All Books:\n");

        for (Book book : allBooks) {
            message.append(book.getTitle()).append(" by ").append(book.getAuthor()).append(" - $").append(book.getPrice()).append("\n");
        }

        JOptionPane.showMessageDialog(mainFrame, message.toString(), "All Books", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchByTitle() {
        String searchTerm = JOptionPane.showInputDialog(mainFrame, "Enter the title to search:", "Search by Title", JOptionPane.QUESTION_MESSAGE);
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            List<Book> searchResults = inventoryManager.searchBooksByTitle(searchTerm);
            displaySearchResults(searchResults);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Invalid title entered", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displaySearchResults(List<Book> searchResults) {
        if (!searchResults.isEmpty()) {
            StringBuilder message = new StringBuilder("Search Results:\n");
            for (Book book : searchResults) {
                message.append(book).append("\n");
            }
            JOptionPane.showMessageDialog(mainFrame, message.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "No matching books found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exitApplication() {
        int result = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }


    private JPanel createBookInputPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Fiction", "NonFiction"});

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Type:"));
        panel.add(typeBox);

        return panel;
    }

    private String getFieldText(JPanel panel, String label) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel && ((JLabel) component).getText().equals(label)) {
                JTextField textField = (JTextField) panel.getComponent(panel.getComponentZOrder(component) + 1);
                return textField.getText();
            }
        }
        return null;
    }

    private Object getComboBoxSelectedItem(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel && ((JLabel) component).getText().equals("Type:")) {
                JComboBox<?> comboBox = (JComboBox<?>) panel.getComponent(panel.getComponentZOrder(component) + 1);
                return comboBox.getSelectedItem();
            }
        }
        return null;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookstoreApplication::new);
    }
}

