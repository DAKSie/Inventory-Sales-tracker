package utils;

import java.awt.Font;
import javax.swing.*;

public class BetterLabels extends JLabel {
    public BetterLabels(int posX, int posY, String text) {
        super(text);
        setBounds(posX, posY, 100, 20);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}

/*
setName(text);
setMaximumSize(new Dimension(140, 36));
setAlignmentX(CENTER_ALIGNMENT);
setBackground(new Color(255, 255, 255));
setForeground(Color.BLACK);
setFont(new Font("Segoe UI", Font.PLAIN, 14));
setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true));
setBounds(posX, posY, 100, 20);
*/
