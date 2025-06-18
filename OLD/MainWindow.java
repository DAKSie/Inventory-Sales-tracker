import javax.swing.*;
import java.awt.*;

import utils.BetterButtons;
import utils.BetterInputs;
import utils.BetterLabels;

@SuppressWarnings("unused")
public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("Main Window");
        setSize(1200, 600);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon("art/inventory-sales.png");
        setIconImage(image.getImage());

        System.out.println("Manager Dashboard is running...");
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inventory", Inventory());
        tabbedPane.addTab("Sales", Sales());
        tabbedPane.addTab("Reports", Reports());
        add(tabbedPane, BorderLayout.CENTER);

        

        setVisible(true);
    }

    //button actions
    public void test() {
        System.out.println("Test!");
    }
}

/*
----------------------------------------------------- Adding Buttons
String[] buttons = {"one", "two", "three"};
int buttonY = 10
for (int i = 0; i < buttons.length; i++) {
    final int index = i;
    BetterButtons button = new BetterButtons(10, buttonY + (index * 30), "button" + index, buttons[index], e -> test());
    add(button);

----------------------------------------------------- Adding Text Fields
String[] textFields = {"one", "two", "three"};
int textFieldsY = 120
for (int i = 0; i < textFields.length; i++) {
    final int index = i;
    BetterInputs textFielda = new BetterInputs(10, textFieldsY + (index * 30), textFields[index] , "");
    add(textFielda);

----------------------------------------------------- Adding Labels
String[] Labels = {"one", "two", "three"};
int LabelY = 210
for (int i = 0; i < Labels.length; i++) {
    final int index = i;
    BetterLabels Label = new BetterLabels(10, LabelY + (index * 30), Labels[index]);
    add(Label);
}
*/
