package project.swing;

import java.io.Serializable;


class NonFictionBook extends Book implements Serializable {
    public NonFictionBook(String title, String author, double price) {
        super(title, author, price);
    }
}
