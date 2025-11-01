package tabComponents;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import utils.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class SalesManagerPanel extends JPanel implements PropertyChangeListener {
    private String currentUserName;
    private JComboBox<String> itemIdCombo;
    private BetterInputs itemNameInput, quantityInput;
    private DefaultTableModel salesTableModel;
    private JTable salesTable;
    private Map<String, String[]> productInfoMap = new HashMap<>(); // ItemID -> [Name, Price, Markup]
    private Map<String, Integer> stockMap = new HashMap<>(); // ItemID -> Stock

    public SalesManagerPanel(String currentUserName) {
        this.currentUserName = currentUserName;
        setLayout(null);
        loadProductInfo();
        JPanel tablePanel = createTablePanel();
        JPanel inputPanel = createInputPanel();
        add(tablePanel);
        add(inputPanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("productsFileUpdated".equals(evt.getPropertyName())) {
            productInfoMap.clear();
            stockMap.clear();
            loadProductInfo();
            refreshDropdownAndFields();
        }
    }

    private void loadProductInfo() {
        File file = new File("database/products.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length >= 6) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String price = parts[3].trim();
                        String markup = parts[4].trim();
                        String stockStr = parts[5].trim();
                        productInfoMap.put(id, new String[]{name, price, markup});
                        int stock = 0;
                        try { stock = Integer.parseInt(stockStr); } catch (Exception e) {}
                        stockMap.put(id, stock);
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 247, 250));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int tablePanelWidth = 825, tablePanelHeight = 475;
        tablePanel.setBounds(310, 10, tablePanelWidth, tablePanelHeight);

        String[] columnNames = {"Sales ID", "Item ID", "Item Name", "Price", "Quantity", "Total", "User"};
        salesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        salesTable = new JTable(salesTableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    Color evenColor = new Color(250, 252, 255);
                    Color oddColor = new Color(240, 245, 250);
                    c.setBackground(row % 2 == 0 ? evenColor : oddColor);
                } else {
                    c.setBackground(new Color(230, 240, 255));
                }
                return c;
            }
        };
        salesTable.setShowHorizontalLines(false);
        salesTable.setShowVerticalLines(false);
        salesTable.setIntercellSpacing(new Dimension(0, 0));
        salesTable.setSelectionBackground(new Color(230, 240, 255));
        salesTable.setSelectionForeground(new Color(30, 30, 30));
        salesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        salesTable.setRowHeight(26);
        salesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        salesTable.getTableHeader().setBackground(new Color(245, 247, 250));
        salesTable.getTableHeader().setForeground(new Color(60, 60, 60));
        salesTable.setGridColor(new Color(235, 235, 235));
        JScrollPane tableScrollPane = new JScrollPane(salesTable);
        tableScrollPane.setBounds(10, 10, tablePanelWidth - 20, tablePanelHeight - 20);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
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
        inputPanel.add(new BetterLabels(labelX, labelY, "Quantity: "));
        int textFieldY = 10, textFieldX = 120, textFieldOffset = 32;
        itemIdCombo = new JComboBox<>(getAllProductIds());
        styleModernComboBox(itemIdCombo);
        itemIdCombo.setBounds(textFieldX, textFieldY, 120, 28);
        textFieldY += textFieldOffset;
        itemNameInput = new BetterInputs(textFieldX, textFieldY, "itemName", "");
        itemNameInput.setEditable(false);
        inputPanel.add(itemNameInput);
        updateItemNameField();
        itemIdCombo.addActionListener(e -> updateItemNameField());
        textFieldY += textFieldOffset;
        quantityInput = new BetterInputs(textFieldX, textFieldY, "quantity", "");
        ((JTextField)quantityInput).setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String current = getText(0, getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.insert(offs, str);
                if (sb.toString().matches("\\d*")) {
                    int maxStock = getAvailableStock();
                    int val = 0;
                    try { val = Integer.parseInt(sb.toString()); } catch (Exception e) {}
                    if (val > maxStock) {
                        super.remove(0, getLength());
                        super.insertString(0, String.valueOf(maxStock), a);
                    } else {
                        super.insertString(offs, str, a);
                    }
                }
            }
            @Override
            public void replace(int offs, int length, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                String current = getText(0, getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.replace(offs, offs + length, str == null ? "" : str);
                if (sb.toString().matches("\\d*")) {
                    int maxStock = getAvailableStock();
                    int val = 0;
                    try { val = Integer.parseInt(sb.toString()); } catch (Exception e) {}
                    if (val > maxStock) {
                        super.remove(0, getLength());
                        super.insertString(0, String.valueOf(maxStock), a);
                    } else {
                        super.replace(offs, length, str, a);
                    }
                }
            }
        });
        inputPanel.add(quantityInput);
        inputPanel.add(itemIdCombo);
        // Buttons
        int buttonY = 250, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add");
        buttonY += buttonOffset;
        BetterButtons removeButton = new BetterButtons(buttonX, buttonY, "removeButton", "Remove Item");
        buttonY += buttonOffset;
        BetterButtons confirmButton = new BetterButtons(buttonX, buttonY, "confirmButton", "Confirm");
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        removeButton.setBackground(new Color(220, 53, 69));
        removeButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.WHITE);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        inputPanel.add(confirmButton);
        // Action Listeners
        addButton.addActionListener(e -> addSaleRow());
        removeButton.addActionListener(e -> removeSelectedRows());
        confirmButton.addActionListener(e -> saveSalesToFile());
        return inputPanel;
    }

    private String[] getAllProductIds() {
        return productInfoMap.keySet().toArray(new String[0]);
    }

    private void updateItemNameField() {
        String itemId = (String) itemIdCombo.getSelectedItem();
        if (itemId != null && productInfoMap.containsKey(itemId)) {
            itemNameInput.setText(productInfoMap.get(itemId)[0]);
        } else {
            itemNameInput.setText("");
        }
    }

    private double getProductPriceWithMarkup(String itemId) {
        if (itemId == null || !productInfoMap.containsKey(itemId)) return 0.0;
        try {
            double basePrice = Double.parseDouble(productInfoMap.get(itemId)[1]);
            String markupStr = productInfoMap.get(itemId)[2].replace("%", "");
            double markup = Double.parseDouble(markupStr) / 100.0;
            return basePrice * (1 + markup);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String generateSalesId(String itemId) {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        int count = 1;
        // Count existing sales for this item on this date in the table
        for (int i = 0; i < salesTableModel.getRowCount(); i++) {
            String rowId = (String) salesTableModel.getValueAt(i, 1);
            String rowSalesId = (String) salesTableModel.getValueAt(i, 0);
            if (rowId.equals(itemId) && rowSalesId.startsWith("S" + dateStr + itemId)) {
                count++;
            }
        }
        return "S" + dateStr + itemId + count;
    }

    private int getAvailableStock() {
        String itemId = (String) itemIdCombo.getSelectedItem();
        if (itemId != null && stockMap.containsKey(itemId)) {
            int usedInTable = 0;
            for (int i = 0; i < salesTableModel.getRowCount(); i++) {
                String rowId = (String) salesTableModel.getValueAt(i, 1);
                if (rowId.equals(itemId)) {
                    try { usedInTable += Integer.parseInt(salesTableModel.getValueAt(i, 4).toString()); } catch (Exception e) {}
                }
            }
            return stockMap.get(itemId) - usedInTable;
        }
        return 0;
    }

    private void addSaleRow() {
        String itemId = (String) itemIdCombo.getSelectedItem();
        String itemName = itemNameInput.getText().trim();
        String quantityStr = quantityInput.getText().trim();
        if (itemId == null || itemName.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int quantity = 0;
        try { quantity = Integer.parseInt(quantityStr); } catch (Exception e) {}
        int availableStock = getAvailableStock();
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (quantity > availableStock) {
            quantity = availableStock;
            quantityInput.setText(String.valueOf(availableStock));
            JOptionPane.showMessageDialog(this, "Quantity exceeds available stock. Set to max available.", "Stock Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double price = getProductPriceWithMarkup(itemId);
        double total = price * quantity;
        String salesId = generateSalesId(itemId);
        salesTableModel.addRow(new Object[]{salesId, itemId, itemName, String.format("%.2f", price), quantity, String.format("%.2f", total), currentUserName});
        quantityInput.setText("");
    }

    private void removeSelectedRows() {
        int[] selectedRows = salesTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            salesTableModel.removeRow(selectedRows[i]);
        }
    }

    private void saveSalesToFile() {
        File file = new File("database/sales.txt");
        try (FileWriter writer = new FileWriter(file, true)) {
            for (int i = 0; i < salesTableModel.getRowCount(); i++) {
                String salesId = (String) salesTableModel.getValueAt(i, 0);
                String itemId = (String) salesTableModel.getValueAt(i, 1);
                String itemName = (String) salesTableModel.getValueAt(i, 2);
                String price = salesTableModel.getValueAt(i, 3).toString();
                String quantity = salesTableModel.getValueAt(i, 4).toString();
                String total = salesTableModel.getValueAt(i, 5).toString();
                String user = (String) salesTableModel.getValueAt(i, 6);
                String line = String.join(" : ", salesId, itemId, itemName, price, quantity, total, user);
                writer.write(line + System.lineSeparator());
                // Deduct stock in memory
                int qty = 0;
                try { qty = Integer.parseInt(quantity); } catch (Exception e) {}
                if (stockMap.containsKey(itemId)) {
                    stockMap.put(itemId, stockMap.get(itemId) - qty);
                }
            }
            salesTableModel.setRowCount(0);
            updateProductStockFileAndUI();
            // Fire property change for all listeners (ProductManagerPanel, InventoryManagerPanel, etc.)
            firePropertyChange("inventoryUpdated", false, true);
            firePropertyChange("productsFileUpdated", false, true); // for backward compatibility
            // Reload product info and refresh UI
            productInfoMap.clear();
            stockMap.clear();
            loadProductInfo();
            refreshDropdownAndFields();
            JOptionPane.showMessageDialog(this, "Sales saved and stock updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to save sales.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper to refresh dropdown and fields after stock update
    private void refreshDropdownAndFields() {
        String selected = (String) itemIdCombo.getSelectedItem();
        itemIdCombo.setModel(new DefaultComboBoxModel<>(getAllProductIds()));
        if (selected != null && productInfoMap.containsKey(selected)) {
            itemIdCombo.setSelectedItem(selected);
        } else if (itemIdCombo.getItemCount() > 0) {
            itemIdCombo.setSelectedIndex(0);
        }
        updateItemNameField();
        quantityInput.setText("");
    }

    private void updateProductStockFileAndUI() {
        // Update products.txt with new stock values
        File productsFile = new File("database/products.txt");
        List<String[]> products = new ArrayList<>();
        if (productsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 6) {
                        String itemId = parts[0].trim();
                        String[] prod = Arrays.copyOf(parts, 6);
                        if (stockMap.containsKey(itemId)) {
                            prod[5] = String.valueOf(stockMap.get(itemId));
                        }
                        products.add(prod);
                    } else {
                        products.add(parts);
                    }
                }
            } catch (Exception ignored) {}
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFile, false))) {
            for (String[] prod : products) {
                writer.write(String.join(":", prod));
                writer.newLine();
            }
        } catch (Exception ignored) {}
        // Fire property change for ProductManagerPanel to update UI
        firePropertyChange("productsFileUpdated", false, true);
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
}
