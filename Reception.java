package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Reception extends JFrame {

    private final Color SIDEBAR_COLOR = new Color(20, 140, 150);
    private final Color HEADER_COLOR = new Color(15, 100, 110); // Darker teal for top bar
    private final Color HOVER_COLOR = new Color(30, 180, 190);
    private final Color BG_COLOR = new Color(240, 245, 245);
    private final Color TEXT_COLOR = Color.WHITE;

    // Variables to handle window dragging
    private int mouseX, mouseY;

    public Reception() {
        setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLayout(new BorderLayout());

        // --- 1. CUSTOM TITLE BAR (The system you requested) ---
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(HEADER_COLOR);
        titleBar.setPreferredSize(new Dimension(screenSize.width, 40));

        // Dragging Logic
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });

        // Left side of Title Bar: Branding/Title
        JLabel systemTitle = new JLabel("  Healing Hands | Care Beyond Cure");
        systemTitle.setForeground(Color.WHITE);
        systemTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleBar.add(systemTitle, BorderLayout.WEST);

        // Right side: Window Controls (Minimize, Maximize, Close)
        JPanel windowControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        windowControls.setOpaque(false);

        JButton btnMin = createWinButton("—", Color.WHITE);
        //JButton btnMax = createWinButton("❒", Color.WHITE); // Maximize/Restore icon
        JButton btnClose = createWinButton("✕", new Color(255, 80, 80)); // Red for Close

        // Functionality for buttons
        btnMin.addActionListener(e -> setState(Frame.ICONIFIED));

//        btnMax.addActionListener(e -> {
//            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
//                setExtendedState(JFrame.NORMAL);
//            } else {
//                setExtendedState(JFrame.MAXIMIZED_BOTH);
//            }
//        });

        btnClose.addActionListener(e -> System.exit(0));

        windowControls.add(btnMin);
        //windowControls.add(btnMax);
        windowControls.add(btnClose);
        titleBar.add(windowControls, BorderLayout.EAST);

        // --- 2. SIDEBAR PANEL ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(300, screenSize.height));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Main Brand Label
        JLabel brand = new JLabel("Healing Hands");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brand.setForeground(TEXT_COLOR);
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(brand);

// Small spacing between Brand and Subtitle
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));

// Subtitle Label
        JLabel subtitle = new JLabel("Care Beyond Cure");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 13)); // Smaller and Italicized for a professional touch
        subtitle.setForeground(new Color(200, 230, 235)); // A slightly lighter/faded version of your text color
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(subtitle);

// Larger gap before the navigation buttons
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        String[] menuItems = {
                "Add New Patient", "Room Status", "Department Info",
                "All Employee Info", "Patient Records", "Patient Discharge",
                "Update Details", "Hospital Ambulance", "Search Room"
        };

        for (String item : menuItems) {
            sidebar.add(createMenuButton(item));
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebar.add(Box.createVerticalGlue());
        JButton logoutBtn = createMenuButton("Logout");
        logoutBtn.setBackground(new Color(180, 50, 50));
        logoutBtn.addActionListener(e -> {
            setVisible(false);
            new Login();
        });
        sidebar.add(logoutBtn);

        // --- 3. MAIN WORKSPACE ---
        JPanel mainContent = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, BG_COLOR, getWidth(), getHeight(), Color.WHITE);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        JLabel welcome = new JLabel("Administrative Portal");
        welcome.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 32));
        welcome.setForeground(new Color(50, 50, 50));
        welcome.setBounds(50, 40, 500, 50);
        mainContent.add(welcome);

        // --- FINAL ASSEMBLY ---
        add(titleBar, BorderLayout.NORTH); // Added Title Bar to North
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);

        setVisible(true);
    }


    // Helper to create small window control buttons (Min/Max/Close)
    private JButton createWinButton(String text, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(fgColor);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(280, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(!text.equals("Logout")) btn.setBackground(HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                if(!text.equals("Logout")) btn.setBackground(SIDEBAR_COLOR);
            }
        });

        btn.addActionListener(e -> {
            switch(text) {
                case "Add New Patient": new New_patient(); break;
                case "Room Status": new Room(); break;
                case "Department Info": new Department(); break;
                case "All Employee Info": new Employee_info(); break;
                case "Patient Records": new ALL_Patient_Info(); break;
                case "Patient Discharge": new patient_discharge(); break;
                case "Update Details": new update_patient_details(); break;
                case "Hospital Ambulance": new Ambulance(); break;
                case "Search Room": new SearchRoom(); break;
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        new Reception();
    }
}