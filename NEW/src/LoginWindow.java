import java.awt.Color;
import java.io.*;
import javax.swing.*;

public class LoginWindow extends JFrame{
    public static String currentUser;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginLoginButton;
    private JButton loginRegisterButton;
    
    public LoginWindow() {
        setTitle("Login");
        setSize(370, 320);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apple-style background (very light gray)
        getContentPane().setBackground(new Color(242, 242, 247));

        // Application Logo (centered, circular, subtle shadow)
        ImageIcon image = new ImageIcon("art/logo.png");
        setIconImage(image.getImage());
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(image.getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH)));
        logoLabel.setBounds(155, 22, 60, 60);
        logoLabel.setOpaque(true);
        logoLabel.setBackground(new Color(255, 255, 255, 220));
        logoLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 235), 1, true),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(logoLabel);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 100, 90, 22);
        usernameLabel.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 15));
        usernameLabel.setForeground(new Color(60, 60, 67, 200));
        add(usernameLabel);

        // Username Field (rounded, subtle shadow)
        loginUsernameField = new JTextField();
        loginUsernameField.setBounds(140, 97, 180, 30);
        loginUsernameField.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 16));
        loginUsernameField.setBackground(new Color(255, 255, 255));
        loginUsernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        add(loginUsernameField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 145, 90, 22);
        passwordLabel.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 15));
        passwordLabel.setForeground(new Color(60, 60, 67, 200));
        add(passwordLabel);

        // Password Field (rounded, subtle shadow)
        loginPasswordField = new JPasswordField();
        loginPasswordField.setBounds(140, 142, 180, 30);
        loginPasswordField.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 16));
        loginPasswordField.setBackground(new Color(255, 255, 255));
        loginPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        add(loginPasswordField);

        // Login Button (rounded, blue gradient, shadow, bold font)
        loginLoginButton = new JButton("Sign In");
        loginLoginButton.setBounds(50, 195, 270, 38);
        loginLoginButton.setFont(new java.awt.Font("San Francisco", java.awt.Font.BOLD, 17));
        loginLoginButton.setBackground(new Color(0, 122, 255));
        loginLoginButton.setForeground(Color.WHITE);
        loginLoginButton.setFocusPainted(true);
        loginLoginButton.setFocusCycleRoot(rootPaneCheckingEnabled);
        loginLoginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true),
            BorderFactory.createEmptyBorder(8, 0, 8, 0)
        ));
        loginLoginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginLoginButton.setOpaque(true);
        add(loginLoginButton);
        loginLoginButton.addActionListener(e -> loginHandler());

        // Register Button (flat, blue text, no border, Apple style link)
        loginRegisterButton = new JButton("Register");
        loginRegisterButton.setContentAreaFilled(false);
        loginRegisterButton.setBorderPainted(false);
        loginRegisterButton.setFocusPainted(false);
        loginRegisterButton.setForeground(new Color(0, 122, 255));
        loginRegisterButton.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 14));
        loginRegisterButton.setBounds(120, 245, 130, 28);
        loginRegisterButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginRegisterButton.setHorizontalAlignment(SwingConstants.CENTER);
        add(loginRegisterButton);
        loginRegisterButton.addActionListener(e -> showRegisterWindow());

        setVisible(true);
    }

    public void showRegisterWindow() {
        new RegisterWindow();
        this.dispose();
    }

    

    public void loginHandler() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());
        currentUser = username;

        System.out.println(username + " " + password);
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean userFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(RegisterWindow.USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length > 1 && parts[4].equals(username) && parts[5].equals(password)) {
                    userFound = true;
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user data.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userFound) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new MainWindow(); // Open the main dashboard
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loginUsernameField.setText(username);
        loginPasswordField.setText(password);
    }
}
