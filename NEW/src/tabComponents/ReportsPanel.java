package tabComponents;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.BetterButtons;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class ReportsPanel extends JPanel {
    private JLabel summaryLabel;
    private JTable productsTable, inventoryTable, inventoryDetailsTable, salesTable;
    private DefaultTableModel productsModel, inventoryModel, inventoryDetailsModel, salesModel;

    public ReportsPanel() {
        setLayout(null);
        setBackground(new Color(245, 247, 250));
        JPanel summaryPanel = createSummaryPanel();
        JTabbedPane tabbedPane = createTabbedPane();
        summaryPanel.setBounds(10, 10, 1120, 60);
        tabbedPane.setBounds(10, 80, 1120, 350);
        BetterButtons export =  new BetterButtons(10, 440, "export", "Export to CSV");
        export.addActionListener(e -> exportToCSV());
        add(export);
        add(summaryPanel);
        add(tabbedPane);
        // Auto-refresh on show
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshAllTablesAndSummary();
            }
        });
        refreshAllTablesAndSummary();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(252, 253, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        summaryLabel = new JLabel();
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryLabel.setForeground(new Color(40, 40, 40));
        panel.add(summaryLabel, BorderLayout.CENTER);
        return panel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 247, 250));
        tabbedPane.addTab("Products", createTablePanel("products"));
        tabbedPane.addTab("Inventory", createTablePanel("inventory"));
        tabbedPane.addTab("Inventory Details", createTablePanel("inventoryDetails"));
        tabbedPane.addTab("Sales", createTablePanel("sales"));
        return tabbedPane;
    }

    private JPanel createTablePanel(String type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        JTable table;
        DefaultTableModel model;
        switch (type) {
            case "products":
                productsModel = new DefaultTableModel(
                        new String[]{"Item ID", "Item Name", "Brand", "Price", "Markup", "In Stock"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                productsTable = new JTable(productsModel);
                styleTable(productsTable);
                table = productsTable;
                model = productsModel;
                break;
            case "inventory":
                inventoryModel = new DefaultTableModel(
                        new String[]{"OR Number", "Date", "User", "Supplier", "Total"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                inventoryTable = new JTable(inventoryModel);
                styleTable(inventoryTable);
                table = inventoryTable;
                model = inventoryModel;
                break;
            case "inventoryDetails":
                inventoryDetailsModel = new DefaultTableModel(
                        new String[]{"Inventory ID", "Item ID", "Quantity", "Unit Price", "Total Price"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                inventoryDetailsTable = new JTable(inventoryDetailsModel);
                styleTable(inventoryDetailsTable);
                table = inventoryDetailsTable;
                model = inventoryDetailsModel;
                break;
            case "sales":
                salesModel = new DefaultTableModel(
                        new String[]{"Sales ID", "Item ID", "Item Name", "Price", "Quantity", "Total", "User"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                salesTable = new JTable(salesModel);
                styleTable(salesTable);
                table = salesTable;
                model = salesModel;
                break;
            default:
                return panel;
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(60, 60, 60));
        table.setGridColor(new Color(235, 235, 235));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(new Color(30, 30, 30));
        // Alternating row colors
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                if (r % 2 == 0) comp.setBackground(new Color(252, 253, 255));
                else comp.setBackground(new Color(240, 243, 247));
                return comp;
            }
        });
    }

    private void refreshAllTablesAndSummary() {
        loadProducts();
        loadInventory();
        loadInventoryDetails();
        loadSales();
        updateSummary();
    }

    private void loadProducts() {
        productsModel.setRowCount(0);
        File file = new File("database/products.txt");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 6) {
                    productsModel.addRow(parts);
                }
            }
        } catch (Exception ignored) {}
    }

    private void loadInventory() {
        inventoryModel.setRowCount(0);
        File file = new File("database/inventory.txt");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 4) {
                    inventoryModel.addRow(parts);
                }
            }
        } catch (Exception ignored) {}
    }

    private void loadInventoryDetails() {
        inventoryDetailsModel.setRowCount(0);
        File file = new File("database/inventoryDetails.txt");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" : ");
                if (parts.length >= 5) {
                    inventoryDetailsModel.addRow(parts);
                }
            }
        } catch (Exception ignored) {}
    }

    private void loadSales() {
        salesModel.setRowCount(0);
        File file = new File("database/sales.txt");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" : ");
                if (parts.length >= 7) {
                    salesModel.addRow(parts);
                }
            }
        } catch (Exception ignored) {}
    }

    private void updateSummary() {
        int totalProducts = productsModel.getRowCount();
        int totalInventory = 0;
        for (int i = 0; i < inventoryModel.getRowCount(); i++) {
            try {
                totalInventory += Integer.parseInt(inventoryModel.getValueAt(i, 2).toString());
            } catch (Exception ignored) {}
        }
        int totalSales = 0;
        double totalSalesAmount = 0.0;
        for (int i = 0; i < salesModel.getRowCount(); i++) {
            try {
                totalSales += Integer.parseInt(salesModel.getValueAt(i, 4).toString());
                totalSalesAmount += Double.parseDouble(salesModel.getValueAt(i, 5).toString());
            } catch (Exception ignored) {}
        }
        summaryLabel.setText(String.format(
                "Total Products: %d    |    Total Inventory Added: %d    |    Total Sales: %d    |    Total Sales Amount: %.2f",
                totalProducts, totalInventory, totalSales, totalSalesAmount));
    }

    private void exportToCSV() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());
        String fileName = "outputs/report_" + today + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            // --- Products Summary ---
            writer.write("--- Products Summary ---\n");
            writer.write("Item ID,Item Name,Price,Stock\n");
            for (int i = 0; i < productsModel.getRowCount(); i++) {
                String id = productsModel.getValueAt(i, 0).toString();
                String name = productsModel.getValueAt(i, 1).toString();
                String price = productsModel.getValueAt(i, 3).toString();
                String stock = productsModel.getValueAt(i, 5).toString();
                writer.write(String.format("%s,%s,%s,%s\n", id, name, price, stock));
            }
            writer.write("\n");
            // --- Inventory Summary ---
            writer.write("--- Inventory Summary ---\n");
            writer.write("OR Number,Date,User,Supplier,Total\n");
            for (int i = 0; i < inventoryModel.getRowCount(); i++) {
                for (int j = 0; j < inventoryModel.getColumnCount(); j++) {
                    writer.write(inventoryModel.getValueAt(i, j).toString());
                    if (j < inventoryModel.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("\n");
            // --- Inventory Details Summary ---
            writer.write("--- Inventory Details Summary ---\n");
            writer.write("Inventory ID,Item ID,Quantity,Unit Price,Total Price\n");
            for (int i = 0; i < inventoryDetailsModel.getRowCount(); i++) {
                for (int j = 0; j < inventoryDetailsModel.getColumnCount(); j++) {
                    writer.write(inventoryDetailsModel.getValueAt(i, j).toString());
                    if (j < inventoryDetailsModel.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("\n");
            // --- Sales Summary ---
            writer.write("--- Sales Summary ---\n");
            writer.write("Sales ID,Item ID,Item Name,Price,Quantity,Total,User\n");
            for (int i = 0; i < salesModel.getRowCount(); i++) {
                for (int j = 0; j < salesModel.getColumnCount(); j++) {
                    writer.write(salesModel.getValueAt(i, j).toString());
                    if (j < salesModel.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("\n");
            JOptionPane.showMessageDialog(this, "Report exported to " + fileName, "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to export report: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

