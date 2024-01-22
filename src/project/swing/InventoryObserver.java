package project.swing;

import java.util.List;

interface InventoryObserver {
    void update(List<Book> books);
}
