import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class reference extends JFrame {

    public reference() {
        //------------------------------------setup
        setTitle("Manager");
        setSize(1200, 1000);
        setResizable(true); // Allow resizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // Use BorderLayout for main frame

        ImageIcon image = new ImageIcon("art/inventory-sales.png");
        setIconImage(image.getImage());
        //------------------------------------setup
        System.out.println("Manager Dashboard is running...");
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inventory", Inventory());
        tabbedPane.addTab("Sales", Sales());
        tabbedPane.addTab("Reports", Reports());
        add(tabbedPane, BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabbedPane.setForeground(new Color(30, 30, 30));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                tabAreaInsets.left = 12;
            }
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected) {
                    g2.setColor(new Color(230, 240, 255));
                    g2.fillRoundRect(x+2, y+2, w-4, h-4, 18, 18);
                } else {
                    g2.setColor(new Color(245, 245, 245));
                    g2.fillRoundRect(x+2, y+2, w-4, h-4, 18, 18);
                }
            }
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected) {
                    g2.setColor(new Color(0, 122, 255));
                    g2.drawRoundRect(x+1, y+1, w-3, h-3, 16, 16);
                } else {
                    g2.setColor(new Color(220, 220, 220));
                    g2.drawRoundRect(x+1, y+1, w-3, h-3, 16, 16);
                }
            }
            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {}
        });

        setVisible(true);
    }

//----------------------------------InventoryTab

    // Inventory tab fields and model
    JTextField ORNumberField;
    JTextField itemIdField;
    JTextField itemNameField;
    JTextField priceField;
    JTextField quantityField;
    JComboBox<String> statusCombo;
    DefaultTableModel inventoryTableModel;
    JTable inventoryTable;

    public JPanel Inventory() {
        JPanel inventoryPanel = new JPanel(new BorderLayout(10, 10));
        inventoryPanel.setBackground(Color.WHITE);
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input panel (left)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(250, 250, 250));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel orNumberLabel = new JLabel("OR Number:");
        inputPanel.add(orNumberLabel, gbc);
        gbc.gridx = 1;
        ORNumberField = new JTextField();
        inputPanel.add(ORNumberField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        JLabel itemIdLabel = new JLabel("Item ID:");
        inputPanel.add(itemIdLabel, gbc);
        gbc.gridx = 1;
        itemIdField = new JTextField();
        inputPanel.add(itemIdField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        JLabel itemNameLabel = new JLabel("Item Name:");
        inputPanel.add(itemNameLabel, gbc);
        gbc.gridx = 1;
        itemNameField = new JTextField();
        inputPanel.add(itemNameField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        JLabel supplierLabel = new JLabel("Supplier:");
        inputPanel.add(supplierLabel, gbc);
        gbc.gridx = 1;
        JTextField supplierField = new JTextField();
        inputPanel.add(supplierField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        JLabel brandLabel = new JLabel("Brand:");
        inputPanel.add(brandLabel, gbc);
        gbc.gridx = 1;
        JTextField brandField = new JTextField();
        inputPanel.add(brandField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        JLabel priceLabel = new JLabel("Price:");
        inputPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        priceField = new JTextField();
        inputPanel.add(priceField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        JLabel quantityLabel = new JLabel("Quantity:");
        inputPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        quantityField = new JTextField();
        inputPanel.add(quantityField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        JLabel statusLabel = new JLabel("Status:");
        inputPanel.add(statusLabel, gbc);
        gbc.gridx = 1;
        String[] statusOptions = {"Available", "Not Available"};
        statusCombo = new JComboBox<>(statusOptions);
        inputPanel.add(statusCombo, gbc);
        gbc.gridx = 0; gbc.gridy++;

        // Buttons panel (Inventory tab)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JButton addButton = new JButton("Add Item");
        JButton removeButton = new JButton("Remove Item");
        JButton updateButton = new JButton("Update Item");
        // Define button size for both panels
        Dimension btnSize = new Dimension(140, 36);
        for (JButton btn : new JButton[]{addButton, removeButton, updateButton}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(new Color(0, 122, 255));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonPanel.add(btn);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        buttonPanel.remove(buttonPanel.getComponentCount() - 1); // Remove last extra space
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        // Set up Add Item button to trigger easter egg and add item
        addButton.addActionListener(e -> {
            handleAddEasterEgg();
            addItem();
        });
        removeButton.addActionListener(e -> removeItem());

        // Table for displaying inventory items (center)
        String[] columnNames = {"OR Number", "Item ID", "Item Name", "Supplier", "Brand", "Price", "Quantity", "Status"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0);
        loadInventoryFromFile();
        inventoryTable = new JTable(inventoryTableModel);
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        // Set Status column as a dropdown (JComboBox) in the table
        JComboBox<String> statusEditorCombo = new JComboBox<>(statusOptions);
        inventoryTable.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(statusEditorCombo));
        inventoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inventoryTable.setRowHeight(22);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        inventoryTable.getTableHeader().setBackground(new Color(240, 240, 240));
        inventoryTable.getTableHeader().setForeground(new Color(60, 60, 60));
        inventoryTable.setGridColor(new Color(230, 230, 230));
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));

        // Add panels to inventoryPanel
        inventoryPanel.add(inputPanel, BorderLayout.WEST);
        inventoryPanel.add(tableScrollPane, BorderLayout.CENTER);
        return inventoryPanel;
    }

    DefaultTableModel salesTableModel;
    JTable salesTable;

    public JPanel Sales() {
        JPanel salesPanel = new JPanel(new BorderLayout(10, 10));
        salesPanel.setBackground(Color.WHITE);
        salesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input panel (left)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(250, 250, 250));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Prepare inventory data for suggestions
        java.util.List<String> itemIdList = new java.util.ArrayList<>();
        java.util.List<String> itemNameList = new java.util.ArrayList<>();
        java.util.Map<String, String[]> idToRow = new java.util.HashMap<>();
        java.util.Map<String, String[]> nameToRow = new java.util.HashMap<>();
        for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
            String[] row = new String[inventoryTableModel.getColumnCount()];
            for (int j = 0; j < row.length; j++) row[j] = inventoryTableModel.getValueAt(i, j).toString();
            itemIdList.add(row[1]);
            itemNameList.add(row[2]);
            idToRow.put(row[1], row);
            nameToRow.put(row[2], row);
        }

        // Item ID ComboBox
        JLabel itemIdLabel = new JLabel("Item ID:");
        inputPanel.add(itemIdLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> SalesItemIdCombo = new JComboBox<>(itemIdList.toArray(new String[0]));
        SalesItemIdCombo.setEditable(true);
        inputPanel.add(SalesItemIdCombo, gbc);
        gbc.gridx = 0; gbc.gridy++;

        // Item Name ComboBox
        JLabel itemNameLabel = new JLabel("Item Name:");
        inputPanel.add(itemNameLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> SalesItemNameCombo = new JComboBox<>(itemNameList.toArray(new String[0]));
        SalesItemNameCombo.setEditable(true);
        inputPanel.add(SalesItemNameCombo, gbc);
        gbc.gridx = 0; gbc.gridy++;

        // Price
        JLabel priceLabel = new JLabel("Price:");
        inputPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        JTextField SalesPriceField = new JTextField();
        SalesPriceField.setEditable(false);
        inputPanel.add(SalesPriceField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        // Quantity
        JLabel quantityLabel = new JLabel("Quantity:");
        inputPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        JTextField SalesQuantityField = new JTextField();
        inputPanel.add(SalesQuantityField, gbc);
        gbc.gridx = 0; gbc.gridy++;

        // Buttons panel (Sales tab)
        JPanel saleButtonPanel = new JPanel();
        saleButtonPanel.setLayout(new BoxLayout(saleButtonPanel, BoxLayout.Y_AXIS));
        saleButtonPanel.setOpaque(false);
        saleButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JButton addSaleButton = new JButton("Add Sale");
        JButton removeSaleButton = new JButton("Remove Sale");
        JButton updateSaleButton = new JButton("Update Sale");
        // Define button size for both panels
        Dimension btnSize = new Dimension(140, 36);
        for (JButton btn : new JButton[]{addSaleButton, removeSaleButton, updateSaleButton}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(new Color(0, 122, 255));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            saleButtonPanel.add(btn);
            saleButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        saleButtonPanel.remove(saleButtonPanel.getComponentCount() - 1);
        gbc.gridwidth = 2;
        inputPanel.add(saleButtonPanel, gbc);

        // Style all labels and fields
        for (Component comp : inputPanel.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                comp.setForeground(new Color(60, 60, 60));
            } else if (comp instanceof JTextField) {
                comp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                ((JTextField) comp).setBackground(new Color(245, 245, 245));
                ((JTextField) comp).setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
            }
        }

        // Table for displaying sales items (center)
        String[] salesColumnNames = {"Item ID", "Item Name", "Price", "Quantity", "Date"};
        salesTableModel = new DefaultTableModel(salesColumnNames, 0);
        salesTable = new JTable(salesTableModel);
        JScrollPane tableScrollPane = new JScrollPane(salesTable);
        salesTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        salesTable.setRowHeight(22);
        salesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        salesTable.getTableHeader().setBackground(new Color(240, 240, 240));
        salesTable.getTableHeader().setForeground(new Color(60, 60, 60));
        salesTable.setGridColor(new Color(230, 230, 230));
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));

        // Load sales from file on startup (must be after model is created)
        loadSalesFromFile();

        // Auto-fill Item Name and Price when Item ID is entered
        SalesItemIdCombo.addActionListener(e -> {
            String itemId = SalesItemIdCombo.getSelectedItem() != null ? SalesItemIdCombo.getSelectedItem().toString().trim() : "";
            if (!itemId.isEmpty()) {
                String[] item = findInventoryItemById(itemId);
                if (item != null) {
                    SalesItemNameCombo.setSelectedItem(item[2]);
                    SalesPriceField.setText(item[5]);
                } else {
                    SalesItemNameCombo.setSelectedItem("");
                    SalesPriceField.setText("");
                }
            }
        });
        // Auto-fill logic for Item Name ComboBox
        SalesItemNameCombo.addActionListener(e -> {
            Object selected = SalesItemNameCombo.getSelectedItem();
            if (selected != null && nameToRow.containsKey(selected.toString())) {
                String[] row = nameToRow.get(selected.toString());
                SalesItemIdCombo.setSelectedItem(row[1]);
                SalesPriceField.setText(row[5]);
            }
        });

        // Button actions
        addSaleButton.addActionListener(e -> {
            String itemId = SalesItemIdCombo.getSelectedItem() != null ? SalesItemIdCombo.getSelectedItem().toString().trim() : "";
            String itemName = SalesItemNameCombo.getSelectedItem() != null ? SalesItemNameCombo.getSelectedItem().toString().trim() : "";
            String price = SalesPriceField.getText().trim();
            String quantityStr = SalesQuantityField.getText().trim();
            if (itemId.isEmpty() || itemName.isEmpty() || price.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int inventoryRow = findInventoryRowById(itemId);
            if (inventoryRow == -1) {
                JOptionPane.showMessageDialog(this, "Item ID not found in inventory.", "Inventory Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int invQty = Integer.parseInt(inventoryTableModel.getValueAt(inventoryRow, 6).toString());
            if (quantity > invQty) {
                JOptionPane.showMessageDialog(this, "Not enough quantity in inventory.", "Inventory Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            inventoryTableModel.setValueAt(String.valueOf(invQty - quantity), inventoryRow, 6);
            saveInventoryToFile();
            String date = java.time.LocalDate.now().toString();
            salesTableModel.addRow(new Object[]{itemId, itemName, price, quantity, date});
            saveSalesToFile();
            SalesItemIdCombo.setSelectedItem("");
            SalesItemNameCombo.setSelectedItem("");
            SalesPriceField.setText("");
            SalesQuantityField.setText("");
        });

        removeSaleButton.addActionListener(e -> {
            //skibidi
            int selectedRow = salesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Add back the quantity to inventory
            String itemId = salesTableModel.getValueAt(selectedRow, 0).toString();
            int saleQty = Integer.parseInt(salesTableModel.getValueAt(selectedRow, 3).toString());
            int inventoryRow = findInventoryRowById(itemId);
            if (inventoryRow != -1) {
                int invQty = Integer.parseInt(inventoryTableModel.getValueAt(inventoryRow, 6).toString());
                inventoryTableModel.setValueAt(String.valueOf(invQty + saleQty), inventoryRow, 6);
                saveInventoryToFile();
            }
            salesTableModel.removeRow(selectedRow);
            saveSalesToFile();
        });

        updateSaleButton.addActionListener(e -> {
            // For every row in the sales table, recalculate inventory and save both tables to file
            // First, reset inventory quantities to match inventory.txt (reload from file)
            // But to avoid losing unsaved changes, we will only update inventory quantities for items present in sales table
            // 1. Create a map of itemId to total sold quantity from the sales table
            java.util.Map<String, Integer> soldMap = new java.util.HashMap<>();
            for (int i = 0; i < salesTableModel.getRowCount(); i++) {
                String itemId = salesTableModel.getValueAt(i, 0).toString();
                int qty = Integer.parseInt(salesTableModel.getValueAt(i, 3).toString());
                soldMap.put(itemId, soldMap.getOrDefault(itemId, 0) + qty);
            }
            // 2. For each inventory row, set quantity = original quantity - total sold (if any)
            // First, reload inventory from file to get original quantities
            java.util.List<String[]> inventoryRows = new java.util.ArrayList<>();
            java.io.File file = new java.io.File("database/inventory.txt");
            if (file.exists()) {
                try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":", -1);
                        if (parts.length == 8) {
                            inventoryRows.add(parts);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error loading inventory from file: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Now update the inventoryTableModel to match the file, then subtract sold quantities
            inventoryTableModel.setRowCount(0);
            for (String[] row : inventoryRows) {
                String itemId = row[1];
                int origQty = Integer.parseInt(row[6]);
                int soldQty = soldMap.getOrDefault(itemId, 0);
                int newQty = Math.max(0, origQty - soldQty);
                row[6] = String.valueOf(newQty);
                inventoryTableModel.addRow(row);
            }
            saveInventoryToFile();
            saveSalesToFile();
            JOptionPane.showMessageDialog(this, "Sales and inventory updated and saved to file.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add panels to salesPanel
        salesPanel.add(inputPanel, BorderLayout.WEST);
        salesPanel.add(tableScrollPane, BorderLayout.CENTER);
        return salesPanel;
    }

    public JPanel Reports() {
        JPanel reportsPanel = new JPanel(new BorderLayout(10, 10));
        reportsPanel.setBackground(Color.WHITE);
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Title
        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 122, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        reportsPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabbed pane for report types
        JTabbedPane reportTabs = new JTabbedPane();
        reportTabs.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Inventory Report Tab
        JPanel inventoryReportPanel = new JPanel(new BorderLayout(8, 8));
        inventoryReportPanel.setBackground(Color.WHITE);
        String[] inventoryColumns = {"OR Number", "Item ID", "Item Name", "Supplier", "Brand", "Price", "Quantity", "Status"};
        JTable inventoryReportTable = new JTable(inventoryTableModel);
        inventoryReportTable.setEnabled(false);
        inventoryReportTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inventoryReportTable.setRowHeight(22);
        inventoryReportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane inventoryScroll = new JScrollPane(inventoryReportTable);
        inventoryScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        inventoryReportPanel.add(inventoryScroll, BorderLayout.CENTER);

        // Export Inventory Button
        JButton exportInventoryBtn = new JButton("Export Inventory Report");
        exportInventoryBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportInventoryBtn.setBackground(new Color(0, 122, 255));
        exportInventoryBtn.setForeground(Color.WHITE);
        exportInventoryBtn.setFocusPainted(false);
        exportInventoryBtn.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true));
        exportInventoryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportInventoryBtn.addActionListener(e -> {
            exportTableToCSV(inventoryTableModel, "inventory_report.csv", inventoryColumns);
        });
        JPanel inventoryBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inventoryBtnPanel.setOpaque(false);
        inventoryBtnPanel.add(exportInventoryBtn);
        inventoryReportPanel.add(inventoryBtnPanel, BorderLayout.SOUTH);

        // Sales Report Tab
        JPanel salesReportPanel = new JPanel(new BorderLayout(8, 8));
        salesReportPanel.setBackground(Color.WHITE);
        String[] salesColumns = {"Item ID", "Item Name", "Price", "Quantity", "Date"};
        JTable salesReportTable = new JTable(salesTableModel);
        salesReportTable.setEnabled(false);
        salesReportTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        salesReportTable.setRowHeight(22);
        salesReportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane salesScroll = new JScrollPane(salesReportTable);
        salesScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        salesReportPanel.add(salesScroll, BorderLayout.CENTER);

        // Export Sales Button
        JButton exportSalesBtn = new JButton("Export Sales Report");
        exportSalesBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportSalesBtn.setBackground(new Color(0, 122, 255));
        exportSalesBtn.setForeground(Color.WHITE);
        exportSalesBtn.setFocusPainted(false);
        exportSalesBtn.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true));
        exportSalesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportSalesBtn.addActionListener(e -> {
            exportTableToCSV(salesTableModel, "sales_report.csv", salesColumns);
        });
        JPanel salesBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salesBtnPanel.setOpaque(false);
        salesBtnPanel.add(exportSalesBtn);
        salesReportPanel.add(salesBtnPanel, BorderLayout.SOUTH);

        // Summary Tab
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel summaryTitle = new JLabel("Summary");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        summaryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.add(summaryTitle);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Inventory summary
        int totalInventoryItems = inventoryTableModel.getRowCount();
        int totalInventoryQuantity = 0;
        double totalInventoryValue = 0.0;
        for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
            try {
                int qty = Integer.parseInt(inventoryTableModel.getValueAt(i, 6).toString());
                double price = Double.parseDouble(inventoryTableModel.getValueAt(i, 5).toString());
                totalInventoryQuantity += qty;
                totalInventoryValue += qty * price;
            } catch (Exception ignored) {}
        }
        JLabel inventorySummary = new JLabel("Inventory Items: " + totalInventoryItems +
                " | Total Quantity: " + totalInventoryQuantity +
                " | Total Value: ₱" + String.format("%.2f", totalInventoryValue));
        inventorySummary.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        inventorySummary.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.add(inventorySummary);

        // Sales summary
        int totalSales = salesTableModel.getRowCount();
        int totalSalesQuantity = 0;
        double totalSalesValue = 0.0;
        for (int i = 0; i < salesTableModel.getRowCount(); i++) {
            try {
                int qty = Integer.parseInt(salesTableModel.getValueAt(i, 3).toString());
                double price = Double.parseDouble(salesTableModel.getValueAt(i, 2).toString());
                totalSalesQuantity += qty;
                totalSalesValue += qty * price;
            } catch (Exception ignored) {}
        }
        JLabel salesSummary = new JLabel("Sales Records: " + totalSales +
                " | Total Sold: " + totalSalesQuantity +
                " | Total Sales: ₱" + String.format("%.2f", totalSalesValue));
        salesSummary.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        salesSummary.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.add(salesSummary);

        reportTabs.addTab("Inventory Report", inventoryReportPanel);
        reportTabs.addTab("Sales Report", salesReportPanel);
        reportTabs.addTab("Summary", summaryPanel);

        reportsPanel.add(reportTabs, BorderLayout.CENTER);
        return reportsPanel;
    }

    // Helper method to export table data to CSV
    private void exportTableToCSV(DefaultTableModel model, String fileName, String[] columns) {
        try {
            java.io.File file = new java.io.File(fileName);
            java.io.PrintWriter pw = new java.io.PrintWriter(file);
            // Write header
            for (int i = 0; i < columns.length; i++) {
                pw.print(columns[i]);
                if (i < columns.length - 1) pw.print(",");
            }
            pw.println();
            // Write rows
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < columns.length; j++) {
                    pw.print(model.getValueAt(i, j));
                    if (j < columns.length - 1) pw.print(",");
                }
                pw.println();
            }
            pw.close();
            JOptionPane.showMessageDialog(this, "Report exported to " + file.getAbsolutePath(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error exporting report: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//-------------------------------------------------mehtods for buttons
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

    private void addItem() {
        String orNumber = ORNumberField.getText().trim();
        String itemId = itemIdField.getText().trim();
        String itemName = itemNameField.getText().trim();
        String supplier = ((JTextField) ((JPanel) ORNumberField.getParent()).getComponent(7)).getText().trim();
        String brand = ((JTextField) ((JPanel) ORNumberField.getParent()).getComponent(9)).getText().trim();
        String price = priceField.getText().trim();
        String quantity = quantityField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();
        if (orNumber.isEmpty() || itemId.isEmpty() || itemName.isEmpty() || supplier.isEmpty() || brand.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        inventoryTableModel.addRow(new Object[]{orNumber, itemId, itemName, supplier, brand, price, quantity, status});
        ORNumberField.setText("");
        itemIdField.setText("");
        itemNameField.setText("");
        ((JTextField) ((JPanel) ORNumberField.getParent()).getComponent(7)).setText("");
        ((JTextField) ((JPanel) ORNumberField.getParent()).getComponent(9)).setText("");
        priceField.setText("");
        quantityField.setText("");
        statusCombo.setSelectedIndex(0);
        // Save all table data to file
        saveInventoryToFile();
    }

    private void saveInventoryToFile() {
        try {
            java.io.File dir = new java.io.File("database");
            if (!dir.exists()) dir.mkdir();
            java.io.File file = new java.io.File(dir, "inventory.txt");
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file));
            for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < inventoryTableModel.getColumnCount(); j++) {
                    sb.append(inventoryTableModel.getValueAt(i, j));
                    if (j < inventoryTableModel.getColumnCount() - 1) sb.append(":");
                }
                writer.write(sb.toString());
                writer.newLine();
            }
            writer.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving inventory to file: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeItem() {
        System.out.println("Skibidi");
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        inventoryTableModel.removeRow(selectedRow);
        // Save updated table to file after removal
        saveInventoryToFile();
    }

    @SuppressWarnings("unused")
    private void updateItem() {
        saveInventoryToFile();
        JOptionPane.showMessageDialog(this, "Inventory updated and saved to file.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadInventoryFromFile() {
        java.io.File file = new java.io.File("database/inventory.txt");
        if (!file.exists()) return;
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", -1);
                if (parts.length == 8) {
                    inventoryTableModel.addRow(parts);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading inventory from file: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load sales table from sales.txt
    private void loadSalesFromFile() {
        java.io.File file = new java.io.File("database/sales.txt");
        if (!file.exists()) return;
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", -1);
                if (parts.length == 5) {
                    salesTableModel.addRow(parts);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading sales from file: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper: find inventory item by Item ID (returns String[] row or null)
    private String[] findInventoryItemById(String itemId) {
        for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
            if (inventoryTableModel.getValueAt(i, 1).toString().equals(itemId)) {
                String[] row = new String[inventoryTableModel.getColumnCount()];
                for (int j = 0; j < row.length; j++) {
                    row[j] = inventoryTableModel.getValueAt(i, j).toString();
                }
                return row;
            }
        }
        return null;
    }
    // Helper: find inventory row index by Item ID
    private int findInventoryRowById(String itemId) {
        for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
            if (inventoryTableModel.getValueAt(i, 1).toString().equals(itemId)) {
                return i;
            }
        }
        return -1;
    }
    // Save sales table to sales.txt
    private void saveSalesToFile() {
        try {
            java.io.File dir = new java.io.File("database");
            if (!dir.exists()) dir.mkdir();
            java.io.File file = new java.io.File(dir, "sales.txt");
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file));
            for (int i = 0; i < salesTableModel.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < salesTableModel.getColumnCount(); j++) {
                    sb.append(salesTableModel.getValueAt(i, j));
                    if (j < salesTableModel.getColumnCount() - 1) sb.append(":");
                }
                writer.write(sb.toString());
                writer.newLine();
            }
            writer.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving sales to file: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Easter egg fields
    private int rapidAddCount = 0;
    private long lastAddTime = 0;
    private javax.swing.Timer dvdBounceTimer = null;
    private javax.swing.Timer rgbColorTimer = null;
    private int dvdDX = 6, dvdDY = 5;
    private Color originalBgColor = null;
    private float rgbHue = 0f;

    private void handleAddEasterEgg() {
        long now = System.currentTimeMillis();
        if (now - lastAddTime > 1200) {
            rapidAddCount = 0;
        }
        rapidAddCount++;
        lastAddTime = now;
        if (rapidAddCount >= 6) { // 6 rapid presses
            startDVDBounceMode();
            rapidAddCount = 0;
        }
    }

    private void startDVDBounceMode() {
        if (dvdBounceTimer != null && dvdBounceTimer.isRunning()) return;
        java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if (originalBgColor == null) originalBgColor = getContentPane().getBackground();
        dvdBounceTimer = new javax.swing.Timer(16, e -> {
            java.awt.Point loc = getLocation();
            int nextX = loc.x + dvdDX;
            int nextY = loc.y + dvdDY;
            if (nextX < 0) { nextX = 0; dvdDX = -dvdDX; }
            else if (nextX + getWidth() > screen.width) { nextX = screen.width - getWidth(); dvdDX = -dvdDX; }
            if (nextY < 0) { nextY = 0; dvdDY = -dvdDY; }
            else if (nextY + getHeight() > screen.height) { nextY = screen.height - getHeight(); dvdDY = -dvdDY; }
            setLocation(nextX, nextY);
        });
        dvdBounceTimer.start();
        // Start RGB color cycling
        rgbColorTimer = new javax.swing.Timer(20, e -> {
            rgbHue += 0.01f;
            if (rgbHue > 1f) rgbHue = 0f;
            Color rgbColor = Color.getHSBColor(rgbHue, 1f, 1f);
            getContentPane().setBackground(rgbColor);
        });
        rgbColorTimer.start();
        // Listen for Ctrl+C to stop
        new Thread(() -> {
            try {
                while (true) {
                    int c = System.in.read();
                    if (c == 3) { // Ctrl+C
                        if (dvdBounceTimer != null) dvdBounceTimer.stop();
                        if (rgbColorTimer != null) rgbColorTimer.stop();
                        if (originalBgColor != null) getContentPane().setBackground(originalBgColor);
                        break;
                    }
                }
            } catch (Exception ignored) {}
        }).start();
    }
}