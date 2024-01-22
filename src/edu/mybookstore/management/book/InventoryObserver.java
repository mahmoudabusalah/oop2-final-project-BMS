package edu.mybookstore.management.book;

import java.util.List;

public interface InventoryObserver {
    void updateInventory(List<Book> books);
}
