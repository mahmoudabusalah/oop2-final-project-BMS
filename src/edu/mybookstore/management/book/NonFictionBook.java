package edu.mybookstore.management.book;

class NonFictionBook extends Book {
    public NonFictionBook(String title, String author, double price) {
        super(title, author, price);
    }

    @Override
    public String getBookType() {
        return "Non-Fiction";
    }
}