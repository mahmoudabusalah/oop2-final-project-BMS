package project.swing;

class BookFactory {
    public static Book createBook(String type, String title, String author, double price) {
        if (type.equalsIgnoreCase("Fiction")) {
            return new FictionBook(title, author, price);
        } else if (type.equalsIgnoreCase("NonFiction")) {
            return new NonFictionBook(title, author, price);
        }
        return null;
    }
}
