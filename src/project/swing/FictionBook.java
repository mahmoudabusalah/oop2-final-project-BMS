package project.swing;

import java.io.Serializable;

class FictionBook extends Book implements Serializable {
    public FictionBook(String title, String author, double price) {
        super(title, author, price);
    }
}