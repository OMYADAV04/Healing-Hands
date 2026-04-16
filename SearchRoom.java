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

public class SearchRoom extends JFrame {

    // Theme Palette
    private final Color HEADER_BLUE = new Color(41, 128, 185);
    private final Color ACCENT_TEAL = new Color(21, 133, 139);
    private final Color BG_LIGHT = new Color(245, 246, 247);
    private final Color TEXT_MAIN = new Color(44, 62, 80);

    public SearchRoom() {
        setUndecorated(true);
        setSize(1000, 600);
        // Centers the window in your Administrative workspace
        setLocation(330, 190);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(ACCENT_TEAL, 1));
        add(mainPanel);

        // --- TOP HEADER ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        header.setBackground(HEADER_BLUE);
        header.setPreferredSize(new Dimension(1000, 60));

        JLabel title = new JLabel("HOSPITAL ROOM AVAILABILITY SEARCH");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        mainPanel.add(header, BorderLayout.NORTH);

        // --- FILTER CONTROL BAR ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        controlPanel.setBackground(BG_LIGHT);
        controlPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JLabel lblStatus = new JLabel("Filter by Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setForeground(TEXT_MAIN);

        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All Rooms", "Available", "Occupied"});
        statusFilter.setPreferredSize(new Dimension(150, 30));
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblSearch = new JLabel("Search Anything:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(TEXT_MAIN);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setToolTipText("Enter Room No, Price, or Bed Type");

        controlPanel.add(lblStatus);
        controlPanel.add(statusFilter);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlPanel.add(lblSearch);
        controlPanel.add(searchField);

        mainPanel.add(controlPanel, BorderLayout.CENTER);

        // --- TABLE SECTION ---
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new EmptyBorder(20, 40, 20, 40));

        JTable table = new JTable();
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 235, 235));

        // Styling the Header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(100, 45));

        // Center Text Renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Add table container to a separate panel to allow layout management
        JPanel midPanel = new JPanel(new BorderLayout());
        midPanel.add(controlPanel, BorderLayout.NORTH);
        midPanel.add(tableContainer, BorderLayout.CENTER);
        mainPanel.add(midPanel, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 20));
        footer.setBackground(BG_LIGHT);

        JButton btnBack = new JButton("BACK TO MENU");
        btnBack.setPreferredSize(new Dimension(160, 40));
        btnBack.setBackground(new Color(33, 43, 52));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        footer.add(btnBack);
        mainPanel.add(footer, BorderLayout.SOUTH);

        // --- DATA LOGIC & DYNAMIC FILTERING ---
        try {
            con c = new con();
            ResultSet rs = c.statement.executeQuery("select * from room");
            table.setModel(DbUtils.resultSetToTableModel(rs));

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);

            // Instant Filter Action
            ActionListener filterAction = e -> {
                String selected = (String) statusFilter.getSelectedItem();
                if (selected.equals("All Rooms")) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(selected));
                }
            };
            statusFilter.addActionListener(filterAction);

            // Search Field Action
            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String text = searchField.getText();
                    if (text.length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }
            });

        } catch (Exception e) { e.printStackTrace(); }

        btnBack.addActionListener(e -> setVisible(false));
        setVisible(true);
    }

    public static void main(String[] args) { new SearchRoom(); }
}