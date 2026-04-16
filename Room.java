package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class Room extends JFrame {

    // Theme Colors matching your Administrative Portal
    private final Color HEADER_BLUE = new Color(41, 128, 185);
    private final Color BUTTON_DARK = new Color(33, 43, 52);
    private final Color BG_LIGHT = new Color(245, 246, 247);
    private final Color SIDEBAR_TEAL = new Color(21, 133, 139);

    public Room() {
        setUndecorated(true);
        setSize(1050, 650);

        // Positioned to align perfectly in your workspace
        setLocation(320, 180);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);
        mainPanel.setBorder(BorderFactory.createLineBorder(SIDEBAR_TEAL, 1));
        add(mainPanel);

        // --- TOP HEADER (With Integrated Search) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(HEADER_BLUE);
        topPanel.setPreferredSize(new Dimension(1050, 55));
        topPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("ROOM RECORDS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);

        // Search Section
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 13));
        searchBox.setOpaque(false);

        JLabel searchLabel = new JLabel("Filter Rooms:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 28));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        searchBox.add(searchLabel);
        searchBox.add(searchField);
        topPanel.add(searchBox, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- TABLE SECTION ---
        JTable table = new JTable();
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));

        // Center Text & Zebra Striping
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    // Alternating row colors for readability
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 251));
                } else {
                    c.setBackground(new Color(174, 214, 241));
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHeader.setPreferredSize(new Dimension(100, 45));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(25, 25, 25, 25));
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- DATA LOADING & SEARCH LOGIC ---
        try {
            con c = new con();
            ResultSet rs = c.statement.executeQuery("select * from room");
            table.setModel(DbUtils.resultSetToTableModel(rs));

            // Setup Real-time Search
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);

            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String text = searchField.getText();
                    if (text.trim().isEmpty()) {
                        sorter.setRowFilter(null);
                    } else {
                        // Filters across all columns (Room No, Price, Availability, etc.)
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }
            });
        } catch (Exception e) { e.printStackTrace(); }

        // --- FOOTER ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        footer.setBackground(new Color(240, 242, 245));

        JButton backBtn = new JButton("BACK TO MAIN");
        backBtn.setPreferredSize(new Dimension(160, 40));
        backBtn.setBackground(BUTTON_DARK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.addActionListener(e -> setVisible(false));

        footer.add(backBtn);
        mainPanel.add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Room();
    }
}