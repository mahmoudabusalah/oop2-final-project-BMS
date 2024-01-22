package edu.mybookstore.management.book;

public abstract class Book {
    public abstract String getBookType();
    private static int nextId = 1;
    protected int id;
    protected String title;
    protected String author;
    protected double price;

    public Book(String title, String author, double price) {
        this.id = nextId++;
        this.title = title;
        this.author = author;
        this.price = price;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return  getBookType() +" Book{" +
                " Title: " + title + "\t" +
                ", Author: " + author + "\t" +
                ", Price: " + price +
                '}';
    }

}
