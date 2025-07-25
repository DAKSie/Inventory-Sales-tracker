package tabComponents;

import javax.swing.*;
import java.awt.*;

public class Tabs {
    private String currentUserName;

    public Tabs(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public JPanel ProductManager() {
        // Return the refactored ProductManagerPanel
        return new ProductManagerPanel(currentUserName);
    }

    public JPanel InventoryManager() {
        // Return the refactored InventoryManagerPanel
        return new InventoryManagerPanel(currentUserName);
    }
}
