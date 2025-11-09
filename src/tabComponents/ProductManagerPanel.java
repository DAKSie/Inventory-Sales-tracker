package tabComponents;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import utils.*;

public class ProductManagerPanel extends JPanel implements PropertyChangeListener {
    @SuppressWarnings("unused")
    private String currentUserName;
    private DefaultTableModel inventoryTableModel;
    private JTable inventoryTable;
    private BetterInputs itemIdInput, itemNameInput, brandInput, priceInput;
    private JComboBox<String> markupCombo;

    public ProductManagerPanel(String currentUserName) {
        this.currentUserName = currentUserName;
        setLayout(null);
        JPanel tablePanel = createTablePanel();
        JPanel inputPanel = createInputPanel();
        add(tablePanel);
        add(inputPanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("inventoryUpdated".equals(evt.getPropertyName()) || "productsFileUpdated".equals(evt.getPropertyName())) {
            updateStockFromInventoryDetails();
        }
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 247, 250)); // Modern light background
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int tablePanelWidth = 825, tablePanelHeight = 475;
        tablePanel.setBounds(310, 10, tablePanelWidth, tablePanelHeight);

        String[] columnNames = {"Item ID", "Item Name", "Brand", "Price", "Price Markup", "In Stock"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        loadProductsFromFile(inventoryTableModel);
        inventoryTable = new JTable(inventoryTableModel) {
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
        JComboBox<String> markupCombo = new JComboBox<>(new String[]{"0%", "10%", "15%", "20%", "25%"});
        styleModernComboBox(markupCombo);
        inventoryTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(markupCombo));
        tablePanel.add(tableScrollPane);
        return tablePanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBackground(new Color(252, 253, 255));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int inputPanelWidth = 270, inputPanelHeight = 475;
        inputPanel.setBounds(10, 10, inputPanelWidth, inputPanelHeight);
        int labelY = 10, labelX = 20, labelOffset = 32;
        inputPanel.add(new BetterLabels(labelX, labelY, "Item ID: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Item Name: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Brand: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Price: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Price Markup: "));
        int textFieldY = 10, textFieldX = 120, textFieldOffset = 32;
        itemIdInput = new BetterInputs(textFieldX, textFieldY, "itemId", "");
        itemIdInput.setEditable(false);
        itemIdInput.setText(generateNextItemId());
        textFieldY += textFieldOffset;
        itemNameInput = new BetterInputs(textFieldX, textFieldY, "itemName", "");
        textFieldY += textFieldOffset;
        brandInput = new BetterInputs(textFieldX, textFieldY, "brand", "");
        textFieldY += textFieldOffset;
        priceInput = new BetterInputs(textFieldX, textFieldY, "price", "");
        // Price validation
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
        textFieldY += textFieldOffset;
        markupCombo = new JComboBox<>(new String[]{"10%", "15%", "20%", "25%"});
        styleModernComboBox(markupCombo);
        markupCombo.setBounds(textFieldX, textFieldY, 120, 28);
        markupCombo.setSelectedIndex(0);
        inputPanel.add(itemIdInput);
        inputPanel.add(itemNameInput);
        inputPanel.add(brandInput);
        inputPanel.add(priceInput);
        inputPanel.add(markupCombo);
        int buttonY = 250, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add Item");
        buttonY += buttonOffset;
        BetterButtons updateButton = new BetterButtons(buttonX, buttonY, "updateButton", "Update Item");
        buttonY += buttonOffset;
        BetterButtons deleteButton = new BetterButtons(buttonX, buttonY, "deleteButton", "Delete Item");
        buttonY += buttonOffset;
        BetterButtons clearButton = new BetterButtons(buttonX, buttonY, "clearButton", "Clear Fields");
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addItem());
        updateButton.addActionListener(e -> updateItem());
        deleteButton.addActionListener(e -> removeItem());
        clearButton.addActionListener(e -> clearFields());
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        return inputPanel;
    }

    private void addItem() {
        String itemId = itemIdInput.getText().trim();
        String itemName = itemNameInput.getText().trim();
        String brand = brandInput.getText().trim();
        String price = priceInput.getText().trim();
        String markup = (String)markupCombo.getSelectedItem();
        String inStock = "Out of stock";
        String[] entries = {itemId, itemName, brand, price, markup, inStock};
        inventoryTableModel.addRow(new Object[] {itemId, itemName, brand, price, markup, inStock});
        saveToFile(entries, "database/products.txt");
        itemIdInput.setText(generateNextItemId());
    }

    private void clearFields() {
        itemIdInput.setText(generateNextItemId());
        itemNameInput.setText("");
        brandInput.setText("");
        priceInput.setText("");
        markupCombo.setSelectedIndex(0);
    }

    private void updateItem() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow != -1) {
            java.io.File file = new java.io.File("database/products.txt");
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
                for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
                    String rowItemId = inventoryTableModel.getValueAt(i, 0).toString();
                    String rowItemName = inventoryTableModel.getValueAt(i, 1).toString();
                    String rowBrand = inventoryTableModel.getValueAt(i, 2).toString();
                    String rowPrice = inventoryTableModel.getValueAt(i, 3).toString();
                    String rowMarkup = inventoryTableModel.getValueAt(i, 4).toString();
                    String rowStock = inventoryTableModel.getValueAt(i, 5).toString();
                    String line = String.join(":", rowItemId, rowItemName, rowBrand, rowPrice, rowMarkup, rowStock);
                    writer.write(line);
                    writer.newLine();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeItem() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow != -1) {
            String itemIdToRemove = inventoryTableModel.getValueAt(selectedRow, 0).toString();
            inventoryTableModel.removeRow(selectedRow);
            java.io.File file = new java.io.File("database/products.txt");
            java.io.File tempFile = new java.io.File("database/products_temp.txt");
            try (
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(tempFile))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length > 0 && !parts[0].trim().equals(itemIdToRemove)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            if (!file.delete() || !tempFile.renameTo(file)) {
                System.out.println("Failed to update products.txt after deletion.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadProductsFromFile(DefaultTableModel model) {
        // Only read products.txt and display the stock from the file
        java.io.File file = new java.io.File("database/products.txt");
        if(!file.exists()) return;
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 6) {
                    String itemId = parts[0].trim();
                    String itemName = parts[1].trim();
                    String brand = parts[2].trim();
                    String price = parts[3].trim();
                    String markup = parts[4].trim();
                    String stockStr = parts[5].trim();
                    String inStockDisplay = (stockStr.equals("Out of stock") || stockStr.equals("0")) ? "Out of stock" : stockStr;
                    model.addRow(new Object[] { itemId, itemName, brand, price, markup, inStockDisplay });
                }
            }
        } catch (Exception ex) {
            System.out.println("Error reading products file: " + ex.getMessage());
        }
    }

    private void saveToFile(String[] entries, String filepath) {
        String line = String.join(":", entries);
        try (java.io.FileWriter writer = new java.io.FileWriter(filepath, true)) {
            writer.write(line + System.lineSeparator());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        // --- Notify InventoryManagerPanel to refresh its Item ID dropdown if needed ---
        firePropertyChange("productsFileUpdated", false, true);
    }

    private String generateNextItemId() {
        java.io.File file = new java.io.File("database/products.txt");
        int maxId = 0;
        if (file.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length > 0) {
                        try {
                            int id = Integer.parseInt(parts[0].trim());
                            if (id > maxId) maxId = id;
                        } catch (NumberFormatException ignored) {}
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error reading products file for ID generation: " + ex.getMessage());
            }
        }
        return String.valueOf(maxId + 1);
    }

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

    public void updateStockFromInventoryDetails() {
        // Only read products.txt and display the stock from the file
        java.io.File productsFile = new java.io.File("database/products.txt");
        java.util.List<String[]> products = new java.util.ArrayList<>();
        if (productsFile.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(productsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 6) {
                        String itemId = parts[0].trim();
                        String itemName = parts[1].trim();
                        String brand = parts[2].trim();
                        String price = parts[3].trim();
                        String markup = parts[4].trim();
                        String stockStr = parts[5].trim();
                        String inStockDisplay = (stockStr.equals("Out of stock") || stockStr.equals("0")) ? "Out of stock" : stockStr;
                        products.add(new String[]{itemId, itemName, brand, price, markup, inStockDisplay});
                    } else {
                        products.add(parts);
                    }
                }
            } catch (Exception ignored) {}
        }
        // Refresh the Product Manager table UI
        if (inventoryTableModel != null) {
            inventoryTableModel.setRowCount(0);
            for (String[] prod : products) {
                if (prod.length == 6) {
                    inventoryTableModel.addRow(prod);
                }
            }
        }
    }
}
