import javax.swing.*;
import java.awt.*;




import utils.BetterButtons;
import utils.BetterInputs;
import utils.BetterLabels;
import tabComponents.Tabs;
import tabComponents.ProductManagerPanel;
import tabComponents.SalesManagerPanel;
import tabComponents.InventoryManagerPanel;
import tabComponents.ReportsPanel;

@SuppressWarnings("unused")
public class MainWindow extends JFrame {
    //get current logged in user
    private String currentUser = LoginWindow.currentUser;

    public MainWindow() {
        setTitle("Manager");
        setSize(1200, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon("art/logo.png");
        System.out.println(image.getImageLoadStatus());
        System.out.println(image);
        setIconImage(image.getImage());

        JTabbedPane tabbedPane = new JTabbedPane();
        Tabs tabs = new Tabs(currentUser);

        ProductManagerPanel productManagerPanel = new ProductManagerPanel(currentUser);
        InventoryManagerPanel inventoryManagerPanel = new InventoryManagerPanel(currentUser);
        SalesManagerPanel salesManagerPanel = new SalesManagerPanel(currentUser);
        ReportsPanel reportsPanel = new ReportsPanel();

        inventoryManagerPanel.setProductManagerPanel(productManagerPanel);
        // Register ProductManagerPanel as a listener for real-time updates
        salesManagerPanel.addPropertyChangeListener(productManagerPanel);
        inventoryManagerPanel.addPropertyChangeListener(productManagerPanel);
        // Register SalesManagerPanel as a listener for product changes (for real-time dropdown update)
        productManagerPanel.addPropertyChangeListener(salesManagerPanel);

        tabbedPane.addTab("Product Manager", productManagerPanel);
        tabbedPane.addTab("Inventory Manager", inventoryManagerPanel);
        tabbedPane.addTab("Sales Manager", salesManagerPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabbedPane.setForeground(new Color(30, 30, 30));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        //---------------------Design of tabs
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                tabAreaInsets.left = 12;
            }
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                paintCustomTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            }
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                paintCustomTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            }
            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {}

            private void paintCustomTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
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

            private void paintCustomTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
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
        });
        //---------------------Design of tabs
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
    //button actions
}