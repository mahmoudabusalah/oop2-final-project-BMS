package edu.mybookstore.management.book;

class FictionBook extends Book {
    public FictionBook(String title, String author, double price) {
        super(title, author, price);
    }

    @Override
    public String getBookType() {
        return "Fiction";
    }
}