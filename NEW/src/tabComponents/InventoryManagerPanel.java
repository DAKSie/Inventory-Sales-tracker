package tabComponents;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import utils.*;

public class InventoryManagerPanel extends JPanel {
    private String currentUserName;
    private JComboBox<String> itemIdCombo;
    private ProductManagerPanel productManagerPanel; // Reference for real-time updates

    public InventoryManagerPanel(String currentUserName) {
        this.currentUserName = currentUserName;
        setLayout(null);
        buildPanel();
    }

    private void buildPanel() {
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 247, 250)); // Lighter, modern background
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int tablePanelWidth = 825, tablePanelHeight = 475;
        tablePanel.setBounds(310, 10, tablePanelWidth, tablePanelHeight);

        String[] columnNames = {"OR Number", "Item ID", "Item Name", "Supplier", "Quantity", "Price", "Date", "User"};
        DefaultTableModel inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        JTable inventoryTable = new JTable(inventoryTableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    Color evenColor = new Color(250, 252, 255); // very light blue tint
                    Color oddColor = new Color(240, 245, 250);  // slightly darker tint
                    c.setBackground(row % 2 == 0 ? evenColor : oddColor);
                } else {
                    c.setBackground(new Color(230, 240, 255));
                }
                return c;
            }
        };
        inventoryTable.setShowHorizontalLines(false);
        inventoryTable.setShowVerticalLines(false);
        inventoryTable.setIntercellSpacing(new Dimension(0, 0));
        inventoryTable.setSelectionBackground(new Color(230, 240, 255));
        inventoryTable.setSelectionForeground(new Color(30, 30, 30));
        inventoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inventoryTable.setRowHeight(26);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        inventoryTable.getTableHeader().setBackground(new Color(245, 247, 250));
        inventoryTable.getTableHeader().setForeground(new Color(60, 60, 60));
        inventoryTable.setGridColor(new Color(235, 235, 235));
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        tableScrollPane.setBounds(10, 10, tablePanelWidth - 20, tablePanelHeight - 20);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        tablePanel.add(tableScrollPane);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBackground(new Color(252, 253, 255));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int inputPanelWidth = 270, inputPanelHeight = 475;
        inputPanel.setBounds(10, 10, inputPanelWidth, inputPanelHeight);

        // Labels
        int labelY = 10;
        int labelX = 20;
        int labelOffset = 32;
        BetterLabels orLabel = new BetterLabels(labelX, labelY, "OR Number: ");
        labelY += labelOffset;
        BetterLabels itemIdLabel = new BetterLabels(labelX, labelY, "Item ID: ");
        labelY += labelOffset;
        BetterLabels itemNameLabel = new BetterLabels(labelX, labelY, "Item Name: ");
        labelY += labelOffset;
        BetterLabels supplierLabel = new BetterLabels(labelX, labelY, "Supplier: ");
        labelY += labelOffset;
        BetterLabels quantityLabel = new BetterLabels(labelX, labelY, "Quantity: ");
        labelY += labelOffset;
        BetterLabels priceLabel = new BetterLabels(labelX, labelY, "Price: ");
        inputPanel.add(orLabel);
        inputPanel.add(itemIdLabel);
        inputPanel.add(supplierLabel);
        inputPanel.add(quantityLabel);
        inputPanel.add(priceLabel);
        inputPanel.add(itemNameLabel);

        // Inputs
        int textFieldY = 10;
        int textFieldX = 120;
        int textFieldOffset = 32;
        BetterInputs orInput = new BetterInputs(textFieldX, textFieldY, "orNumber", "");
        textFieldY += textFieldOffset;
        itemIdCombo = new JComboBox<>(getAllProductIds());
        styleModernComboBox(itemIdCombo);
        itemIdCombo.setBounds(textFieldX, textFieldY, 120, 28);
        textFieldY += textFieldOffset;
        BetterInputs itemNameInput = new BetterInputs(textFieldX, textFieldY, "itemName", "");
        itemNameInput.setEditable(false);
        inputPanel.add(itemNameInput);
        updateItemNameField(itemIdCombo, itemNameInput);
        itemIdCombo.addActionListener(e -> updateItemNameField(itemIdCombo, itemNameInput));
        textFieldY += textFieldOffset;
        BetterInputs supplierInput = new BetterInputs(textFieldX, textFieldY, "supplier", "");
        textFieldY += textFieldOffset;
        BetterInputs quantityInput = new BetterInputs(textFieldX, textFieldY, "quantity", "");
        ((JTextField)quantityInput).setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String current = getText(0, getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.insert(offs, str);
                if (sb.toString().matches("\\d*")) {
                    super.insertString(offs, str, a);
                }
            }
            @Override
            public void replace(int offs, int length, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                String current = getText(0, getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.replace(offs, offs + length, str == null ? "" : str);
                if (sb.toString().matches("\\d*")) {
                    super.replace(offs, length, str, a);
                }
            }
        });
        textFieldY += textFieldOffset;
        BetterInputs priceInput = new BetterInputs(textFieldX, textFieldY, "price", "");
        ((JTextField)priceInput).setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String current = getText(0, getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.insert(offs, str);
                if (isValidPriceInput(sb.toString())) {
                    super.insertString(offs, str, a);
                }
            }
            @Override
            public void replace(int offs, int length, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                String current = getText(0, getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.replace(offs, offs + length, str == null ? "" : str);
                if (isValidPriceInput(sb.toString())) {
                    super.replace(offs, length, str, a);
                }
            }
        });
        ((JTextField)priceInput).addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String text = ((JTextField)priceInput).getText().trim();
                if (!text.isEmpty()) {
                    try {
                        double value = Double.parseDouble(text);
                        ((JTextField)priceInput).setText(String.format("%.2f", value));
                    } catch (NumberFormatException ignored) {}
                }
            }
        });
        inputPanel.add(priceInput);
        inputPanel.add(orInput);
        inputPanel.add(itemIdCombo);
        inputPanel.add(supplierInput);
        inputPanel.add(quantityInput);
        inputPanel.add(itemNameInput);
        

        // Buttons
        int buttonY = 250;
        int buttonX = inputPanelWidth / 2 - 50;
        int buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add Item");
        buttonY += buttonOffset;
        BetterButtons deleteButton = new BetterButtons(buttonX, buttonY, "deleteButton", "Delete Item");
        buttonY += buttonOffset;
        BetterButtons clearButton = new BetterButtons(buttonX, buttonY, "clearButton", "Clear Fields");
        buttonY += buttonOffset;
        BetterButtons confirmButton = new BetterButtons(buttonX, buttonY, "confirmButton", "Confirm");
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.WHITE);
        inputPanel.add(confirmButton);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);

        // Action Listeners
        addButton.addActionListener(e -> {
            String orNumber = ((JTextField)orInput).getText().trim();
            String itemId = (String)itemIdCombo.getSelectedItem();
            String itemName = ((JTextField)itemNameInput).getText().trim();
            String supplier = ((JTextField)supplierInput).getText().trim();
            String quantity = ((JTextField)quantityInput).getText().trim();
            String price = ((JTextField)priceInput).getText().trim();
            String date = java.time.LocalDate.now().toString();
            String user = currentUserName;
            inventoryTableModel.addRow(new Object[]{orNumber, itemId, itemName, supplier, quantity, price, date, user});
            // --- Update products.txt stock ---
            if (itemId != null && !itemId.isEmpty() && quantity != null && !quantity.isEmpty()) {
                try {
                    int addQty = Integer.parseInt(quantity);
                    java.io.File productsFile = new java.io.File("database/products.txt");
                    java.util.List<String> lines = new java.util.ArrayList<>();
                    if (productsFile.exists()) {
                        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(productsFile))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.split(":");
                                if (parts.length == 6 && parts[0].trim().equals(itemId)) {
                                    int oldStock = 0;
                                    try { oldStock = Integer.parseInt(parts[5].trim()); } catch (Exception ignored) {}
                                    int newStock = oldStock + addQty;
                                    parts[5] = String.valueOf(newStock);
                                    lines.add(String.join(":", parts));
                                } else {
                                    lines.add(line);
                                }
                            }
                        }
                        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(productsFile, false))) {
                            for (String l : lines) {
                                writer.write(l);
                                writer.newLine();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // Fire property change for real-time update
                firePropertyChange("inventoryUpdated", false, true);
            }
        });
        clearButton.addActionListener(e -> {
            ((JTextField)orInput).setText("");
            itemIdCombo.setSelectedIndex(0);
            updateItemNameField(itemIdCombo, itemNameInput);
            supplierInput.setText("");
            quantityInput.setText("");
            ((JTextField)priceInput).setText("");
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                inventoryTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(inventoryTable, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        confirmButton.addActionListener(e -> {
            saveInventoryAndDetails(inventoryTableModel);
            updateProductStockFromInventoryDetails();
            if (productManagerPanel != null) {
                productManagerPanel.updateStockFromInventoryDetails();
            }
            JOptionPane.showMessageDialog(this, "Inventory saved and stock updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        add(tablePanel);
        add(inputPanel);
    }

    private String[] getAllProductIds() {
        java.util.List<String> ids = new java.util.ArrayList<>();
        java.io.File file = new java.io.File("database/products.txt");
        if (file.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length > 0) ids.add(parts[0].trim());
                }
            } catch (Exception ignored) {}
        }
        return ids.toArray(new String[0]);
    }

    private void updateItemNameField(JComboBox<String> itemIdCombo, JTextField itemNameInput) {
        String itemId = (String)itemIdCombo.getSelectedItem();
        String name = "";
        java.io.File file = new java.io.File("database/products.txt");
        if (file.exists() && itemId != null) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length > 1 && parts[0].trim().equals(itemId)) {
                        name = parts[1].trim();
                        break;
                    }
                }
            } catch (Exception ignored) {}
        }
        itemNameInput.setText(name);
    }

    private void saveInventoryAndDetails(DefaultTableModel inventoryTableModel) {
        java.util.Map<String, Double> orTotals = new java.util.HashMap<>();
        java.util.Map<String, String[]> orHeader = new java.util.HashMap<>();
        java.util.List<String[]> details = new java.util.ArrayList<>();
        for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
            String orNumber = inventoryTableModel.getValueAt(i, 0).toString();
            String itemId = inventoryTableModel.getValueAt(i, 1).toString();
            String supplier = inventoryTableModel.getValueAt(i, 3).toString();
            String quantityStr = inventoryTableModel.getValueAt(i, 4).toString();
            String priceStr = inventoryTableModel.getValueAt(i, 5).toString();
            String date = inventoryTableModel.getValueAt(i, 6).toString();
            String user = inventoryTableModel.getValueAt(i, 7).toString();
            int qty = 0;
            double price = 0.0;
            try { qty = Integer.parseInt(quantityStr); } catch (Exception ignored) {}
            try { price = Double.parseDouble(priceStr); } catch (Exception ignored) {}
            double subtotal = price * qty;
            orTotals.put(orNumber, orTotals.getOrDefault(orNumber, 0.0) + subtotal);
            orHeader.put(orNumber, new String[]{orNumber, date, user, supplier});
            details.add(new String[]{orNumber, itemId, quantityStr, priceStr, String.format("%.2f", subtotal)});
        }
        java.util.Set<String> writtenOrs = new java.util.HashSet<>();
        for (String orNumber : orTotals.keySet()) {
            String[] header = orHeader.get(orNumber);
            String total = String.format("%.2f", orTotals.get(orNumber));
            String inventoryLine = String.join(" : ", header[0], header[1], header[2], header[3], total);
            if (!writtenOrs.contains(orNumber)) {
                saveLineToFile("database/inventory.txt", inventoryLine, orNumber);
                writtenOrs.add(orNumber);
            }
        }
        for (String[] d : details) {
            String detailLine = String.join(" : ", d);
            saveLineToFile("database/inventoryDetails.txt", detailLine, null);
        }
    }

    private void saveLineToFile(String filePath, String line, String orNumberKey) {
        java.io.File file = new java.io.File(filePath);
        java.util.List<String> lines = new java.util.ArrayList<>();
        boolean replaced = false;
        if (file.exists() && orNumberKey != null) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String l;
                while ((l = reader.readLine()) != null) {
                    if (l.startsWith(orNumberKey + " : ")) {
                        lines.add(line);
                        replaced = true;
                    } else {
                        lines.add(l);
                    }
                }
            } catch (Exception ignored) {}
        }
        if (!replaced && orNumberKey != null) {
            lines.add(line);
        }
        if (orNumberKey == null) {
            try (java.io.FileWriter writer = new java.io.FileWriter(file, true)) {
                writer.write(line + System.lineSeparator());
            } catch (Exception ignored) {}
        } else {
            try (java.io.FileWriter writer = new java.io.FileWriter(file, false)) {
                for (String l : lines) {
                    writer.write(l + System.lineSeparator());
                }
            } catch (Exception ignored) {}
        }
    }

    private void updateProductStockFromInventoryDetails() {
        java.util.Map<String, Integer> stockMap = new java.util.HashMap<>();
        java.io.File detailsFile = new java.io.File("database/inventoryDetails.txt");
        if (detailsFile.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(detailsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" : ");
                    if (parts.length >= 3) {
                        String itemId = parts[1].trim();
                        int qty = 0;
                        try { qty = Integer.parseInt(parts[2].trim()); } catch (Exception ignored) {}
                        stockMap.put(itemId, stockMap.getOrDefault(itemId, 0) + qty);
                    }
                }
            } catch (Exception ignored) {}
        }
        java.io.File productsFile = new java.io.File("database/products.txt");
        java.util.List<String[]> products = new java.util.ArrayList<>();
        if (productsFile.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(productsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 5) {
                        String itemId = parts[0].trim();
                        int qty = stockMap.getOrDefault(itemId, 0);
                        String stockStr = qty > 0 ? String.valueOf(qty) : "Out of stock";
                        products.add(new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), stockStr});
                    } else {
                        products.add(parts);
                    }
                }
            } catch (Exception ignored) {}
        }
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(productsFile, false))) {
            for (String[] prod : products) {
                writer.write(String.join(":", prod));
                writer.newLine();
            }
        } catch (Exception ignored) {}
    }

    // Helper for price input validation
    private boolean isValidPriceInput(String text) {
        if (text.isEmpty()) return true;
        return text.matches("\\d*(\\.\\d{0,2})?");
    }

    private void styleModernComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(30, 30, 30));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        comboBox.setFocusable(false);
        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI());
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                java.awt.Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                c.setBackground(isSelected ? new Color(230,240,255) : Color.WHITE);
                c.setForeground(new Color(30, 30, 30));
                return c;
            }
        });
    }

    private void refreshItemIdCombo() {
        String selected = (String) itemIdCombo.getSelectedItem();
        itemIdCombo.setModel(new DefaultComboBoxModel<>(getAllProductIds()));
        if (selected != null) {
            itemIdCombo.setSelectedItem(selected);
        }
    }

    public void setProductManagerPanel(ProductManagerPanel panel) {
        this.productManagerPanel = panel;
        if (panel != null) {
            panel.addPropertyChangeListener("productsFileUpdated", evt -> refreshItemIdCombo());
        }
    }
} // End of InventoryManagerPanel class
