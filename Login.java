/*
package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.*;
import java.sql.ResultSet;

public class Login extends JFrame {
    private JTextField textField;
    private JPasswordField jPasswordField;
    private JButton b1, b2;
    private JCheckBox showPassword;
    private JPanel mainPanel;

    public Login() {
        setUndecorated(true);
        setSize(850, 500);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

        // Main Container with Rounded Corners
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        mainPanel.setOpaque(false);
        setContentPane(mainPanel);

        // --- LEFT PANEL: BRANDING ---
        JPanel sidePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 140, 150), 0, getHeight(), new Color(10, 50, 70));
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(0, 0, 350, 500, 30, 30));
                g2.fillRect(320, 0, 30, 500); // Sharp edge to connect with right panel
                g2.dispose();
            }
        };
        sidePanel.setBounds(0, 0, 350, 500);
        mainPanel.add(sidePanel);

        JLabel title = new JLabel("Healing Hands");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBounds(60, 210, 250, 50);
        sidePanel.add(title);

        JLabel motto = new JLabel("Care Beyond Cure");
        motto.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        motto.setForeground(new Color(200, 230, 230));
        motto.setBounds(100, 250, 200, 30);
        sidePanel.add(motto);

        // --- RIGHT PANEL: FORM ---
        JLabel loginHeader = new JLabel("Welcome Back");
        loginHeader.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 28));
        loginHeader.setForeground(new Color(50, 50, 50));
        loginHeader.setBounds(420, 60, 300, 40);
        mainPanel.add(loginHeader);

        setupField("USERNAME", 140, textField = new JTextField());
        setupField("PASSWORD", 230, jPasswordField = new JPasswordField());

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(420, 295, 150, 20);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword.setForeground(new Color(100, 100, 100));
        showPassword.setBackground(Color.WHITE);
        showPassword.setFocusPainted(false);
        showPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) jPasswordField.setEchoChar((char) 0);
            else jPasswordField.setEchoChar('•');
        });
        mainPanel.add(showPassword);

        b1 = new JButton("ACCESS SYSTEM");
        b1.setBounds(420, 350, 350, 45);
        b1.setBackground(new Color(20, 140, 150));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b1.setFocusPainted(false);
        b1.setBorder(null);
        b1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b1.addActionListener(e -> performLogin()); // Action Trigger
        mainPanel.add(b1);

        b2 = new JButton("Close System");
        b2.setBounds(420, 410, 350, 20);
        b2.setForeground(new Color(150, 150, 150));
        b2.setContentAreaFilled(false);
        b2.setBorderPainted(false);
        b2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b2.addActionListener(e -> System.exit(0));
        mainPanel.add(b2);

        setVisible(true);
    }

    private void setupField(String labelText, int y, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(new Color(120, 120, 120));
        label.setBounds(420, y, 100, 20);
        mainPanel.add(label);

        field.setBounds(420, y + 25, 350, 35);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        field.setBackground(Color.WHITE);
        mainPanel.add(field);
    }

    private void showToast(String message, boolean success) {
        final JDialog dialog = new JDialog(this);
        dialog.setUndecorated(true);
        dialog.setSize(320, 60);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = success ? new Color(33, 147, 176) : new Color(235, 51, 73);
                Color c2 = success ? new Color(109, 213, 237) : new Color(244, 92, 67);
                g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), 0, c2));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        String icon = success ? "✔ " : "✘ ";
        JLabel lbl = new JLabel(icon + message, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        p.add(lbl, BorderLayout.CENTER);

        dialog.add(p);
        dialog.setLocationRelativeTo(this);

        Timer t = new Timer(1500, e -> dialog.dispose());
        t.setRepeats(false);
        t.start();
        dialog.setVisible(true);
    }

    private void performLogin() {
        String user = textField.getText();
        String pass = new String(jPasswordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            showToast("Fields cannot be empty", false);
            return;
        }

        try {
            con c = new con();
            String q = "select * from login where ID = '" + user + "' and PW = '" + pass + "'";
            ResultSet resultSet = c.statement.executeQuery(q);

            if (resultSet.next()) {
                showToast("Login Successful!", true);

                // Transition logic: Short delay so the toast is visible, then swap frames
                Timer timer = new Timer(1000, e -> {
                    new Reception(); // Open main dashboard
                    this.dispose();  // Destroy login window completely
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                showToast("Invalid Credentials", false);
            }
        } catch (Exception ex) {
            showToast("Database Error", false);
        }
    }

    public static void main(String[] args) {
        //System.setProperty("awt.useSystemAAFontSettings","on");
        //System.setProperty("swing.aatext", "true");
        new Login();
    }
}
*/















































































package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;

public class Login extends JFrame {
    private JTextField textField;
    private JPasswordField jPasswordField;
    private JButton b1, b2;
    private JCheckBox showPassword;
    private JPanel mainPanel;

    public Login() {
        // 1. Window Configuration
        setTitle("Healing Hands - Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Opens the application in full screen (Maximized)
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // 2. Background Wrapper (The "Website" background)
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(230, 240, 240));
        setContentPane(wrapperPanel);

        // 3. Main Login Card
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        mainPanel.setPreferredSize(new Dimension(850, 500));
        mainPanel.setOpaque(false);
        wrapperPanel.add(mainPanel);

        // --- LEFT PANEL: BRANDING ---
        JPanel sidePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 140, 150), 0, getHeight(), new Color(10, 50, 70));
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(0, 0, 350, 500, 30, 30));
                g2.fillRect(320, 0, 30, 500);
                g2.dispose();
            }
        };
        sidePanel.setBounds(0, 0, 350, 500);
        mainPanel.add(sidePanel);

        JLabel title = new JLabel("Healing Hands");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBounds(60, 210, 250, 50);
        sidePanel.add(title);

        JLabel motto = new JLabel("Care Beyond Cure");
        motto.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        motto.setForeground(new Color(200, 230, 230));
        motto.setBounds(100, 250, 200, 30);
        sidePanel.add(motto);

        // --- RIGHT PANEL: FORM ---
        JLabel loginHeader = new JLabel("Welcome Back");
        loginHeader.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 28));
        loginHeader.setForeground(new Color(50, 50, 50));
        loginHeader.setBounds(420, 60, 300, 40);
        mainPanel.add(loginHeader);

        setupField("USERNAME", 140, textField = new JTextField());
        setupField("PASSWORD", 230, jPasswordField = new JPasswordField());

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(420, 295, 150, 20);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword.setForeground(new Color(100, 100, 100));
        showPassword.setBackground(Color.WHITE);
        showPassword.setFocusPainted(false);
        showPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) jPasswordField.setEchoChar((char) 0);
            else jPasswordField.setEchoChar('•');
        });
        mainPanel.add(showPassword);

        b1 = new JButton("ACCESS SYSTEM");
        b1.setBounds(420, 350, 350, 45);
        b1.setBackground(new Color(20, 140, 150));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b1.setFocusPainted(false);
        b1.setBorder(null);
        b1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b1.addActionListener(e -> performLogin());
        mainPanel.add(b1);

        b2 = new JButton("Close System");
        b2.setBounds(420, 410, 350, 20);
        b2.setForeground(new Color(150, 150, 150));
        b2.setContentAreaFilled(false);
        b2.setBorderPainted(false);
        b2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b2.addActionListener(e -> System.exit(0));
        mainPanel.add(b2);

        setVisible(true);
    }

    private void setupField(String labelText, int y, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(new Color(120, 120, 120));
        label.setBounds(420, y, 100, 20);
        mainPanel.add(label);

        field.setBounds(420, y + 25, 350, 35);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        field.setBackground(Color.WHITE);
        mainPanel.add(field);
    }

    private void showToast(String message, boolean success) {
        final JDialog dialog = new JDialog(this);
        dialog.setUndecorated(true);
        dialog.setSize(320, 60);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = success ? new Color(33, 147, 176) : new Color(235, 51, 73);
                Color c2 = success ? new Color(109, 213, 237) : new Color(244, 92, 67);
                g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), 0, c2));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        String icon = success ? "✔ " : "✘ ";
        JLabel lbl = new JLabel(icon + message, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        p.add(lbl, BorderLayout.CENTER);

        dialog.add(p);
        dialog.setLocationRelativeTo(this);

        Timer t = new Timer(1500, e -> dialog.dispose());
        t.setRepeats(false);
        t.start();
        dialog.setVisible(true);
    }

    private void performLogin() {
        String user = textField.getText();
        String pass = new String(jPasswordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            showToast("Fields cannot be empty", false);
            return;
        }

        try {
            con c = new con();
            String q = "select * from login where ID = '" + user + "' and PW = '" + pass + "'";
            ResultSet resultSet = c.statement.executeQuery(q);

            if (resultSet.next()) {
                showToast("Login Successful!", true);

                // Transition logic
                Timer timer = new Timer(1000, e -> {
                    try {
                        new Reception(); // Opens your Reception frame
                        this.dispose();  // Closes Login frame
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error loading Reception: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                showToast("Invalid Credentials", false);
            }
        } catch (Exception ex) {
            showToast("Database Error", false);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new Login());
    }
}
