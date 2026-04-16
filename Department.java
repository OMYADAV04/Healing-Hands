package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class Department extends JFrame {

    private final Color HEADER_BLUE = new Color(41, 128, 185);
    private final Color BUTTON_DARK = new Color(33, 43, 52);
    private final Color BG_LIGHT = new Color(245, 246, 247);
    private final Color SIDEBAR_TEAL = new Color(21, 133, 139);
    private final Color ACCENT_RED = new Color(231, 76, 60);

    private JTable table;
    private JTextField txtDeptName, txtPhone;
    private TableRowSorter<TableModel> sorter;

    public Department() {
        setUndecorated(true);
        setSize(1050, 650);
        setLocation(320, 180);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        add(mainPanel);

        // --- TOP HEADER ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(HEADER_BLUE);
        topPanel.setPreferredSize(new Dimension(1050, 60));
        topPanel.setBorder(new EmptyBorder(0, 25, 0, 25));

        JLabel title = new JLabel("HOSPITAL DEPARTMENTS");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);

        // --- SEARCH SECTION (FIXED LOGIC) ---
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        searchBox.setOpaque(false);

        JLabel searchLabel = new JLabel("FILTER:");
        searchLabel.setForeground(new Color(235, 235, 235));
        searchLabel.setFont(new Font("Segoe UI Bold", Font.PLAIN, 12));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
                BorderFactory.createEmptyBorder(2, 10, 2, 5)));

        // Real-time search using DocumentListener for better stability
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            public void changedUpdate(DocumentEvent e) { performSearch(); }

            private void performSearch() {
                String text = searchField.getText();
                if (sorter != null) {
                    if (text.trim().isEmpty()) {
                        sorter.setRowFilter(null);
                    } else {
                        // Case-insensitive regex filter
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
                    }
                }
            }
        });

        searchBox.add(searchLabel);
        searchBox.add(searchField);
        topPanel.add(searchBox, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- CENTER: TABLE ---
        table = new JTable();
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(235, 245, 251));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(isSelected ? new Color(212, 230, 241) : (row % 2 == 0 ? Color.WHITE : new Color(250, 251, 252)));
                setBorder(noFocusBorder);
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(25, 25, 25, 25));
        scrollPane.getViewport().setBackground(BG_LIGHT);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- RIGHT: CRUD PANEL ---
        JPanel sidePanel = new JPanel(null);
        sidePanel.setPreferredSize(new Dimension(340, 650));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(225, 225, 225)));
        mainPanel.add(sidePanel, BorderLayout.EAST);

        JLabel lblForm = new JLabel("MANAGE DEPARTMENT");
        lblForm.setBounds(30, 30, 250, 30);
        lblForm.setFont(new Font("Segoe UI Bold", Font.PLAIN, 16));
        lblForm.setForeground(SIDEBAR_TEAL);
        sidePanel.add(lblForm);

        txtDeptName = createInput(sidePanel, "Department Name", 80);
        txtPhone = createInput(sidePanel, "Contact Number", 145);

        JButton btnAdd = createActionBtn("ADD DEPARTMENT", 220, SIDEBAR_TEAL);
        JButton btnDelete = createActionBtn("REMOVE ENTRY", 275, ACCENT_RED);
        JButton btnBack = createActionBtn("CLOSE WINDOW", 330, BUTTON_DARK);

        sidePanel.add(btnAdd);
        sidePanel.add(btnDelete);
        sidePanel.add(btnBack);

        loadData();

        // Mouse Listener for Table selection
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    // Always convert view index to model index when using a Sorter
                    int modelRow = table.convertRowIndexToModel(row);
                    txtDeptName.setText(table.getModel().getValueAt(modelRow, 0).toString());
                    txtPhone.setText(table.getModel().getValueAt(modelRow, 1).toString());
                }
            }
        });

        // Add Logic with ALL FIELDS MANDATORY
        btnAdd.addActionListener(e -> {
            if (txtDeptName.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty()) {
                showCustomDialog("Input Required", "<b>All fields are mandatory.</b><br>Please provide Department Name and Phone.", ACCENT_RED);
                return;
            }
            try {
                con c = new con();
                String query = "insert into department values('"+txtDeptName.getText()+"', '"+txtPhone.getText()+"')";
                c.statement.executeUpdate(query);
                showCustomDialog("Success", "New Department has been successfully registered.", SIDEBAR_TEAL);
                clearFields();
                loadData();
            } catch (Exception ex) {
                showCustomDialog("Error", "Check for duplicate entries or DB connection.", ACCENT_RED);
            }
        });

        // Delete Logic with SELECTION MANDATORY
        btnDelete.addActionListener(e -> {
            if (table.getSelectedRow() == -1) {
                showCustomDialog("No Selection", "Please select a department from the table to delete.", ACCENT_RED);
                return;
            }
            try {
                con c = new con();
                String query = "delete from department where Department = '"+txtDeptName.getText()+"'";
                c.statement.executeUpdate(query);
                showCustomDialog("Deleted", "Department has been removed from records.", ACCENT_RED);
                clearFields();
                loadData();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnBack.addActionListener(e -> setVisible(false));
        setVisible(true);
    }

    private void styleTableHeader() {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Bold", Font.PLAIN, 13));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(100, 100, 100));
        header.setPreferredSize(new Dimension(100, 45));
    }

    private void loadData() {
        try {
            con c = new con();
            ResultSet rs = c.statement.executeQuery("select * from department");
            table.setModel(DbUtils.resultSetToTableModel(rs));

            // RE-INITIALIZE SORTER: This is the fix for search not working after update
            sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);

            styleTableHeader();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearFields() {
        txtDeptName.setText("");
        txtPhone.setText("");
        table.clearSelection();
    }

    private void showCustomDialog(String title, String message, Color themeColor) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));
        dialog.add(panel);

        JPanel topBar = new JPanel();
        topBar.setBackground(themeColor);
        topBar.setBounds(0, 0, 400, 6);
        panel.add(topBar);

        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(themeColor);
        lblTitle.setBounds(25, 25, 350, 30);
        panel.add(lblTitle);

        JLabel lblMsg = new JLabel("<html>" + message + "</html>");
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMsg.setForeground(new Color(70, 70, 70));
        lblMsg.setBounds(25, 60, 350, 50);
        panel.add(lblMsg);

        JButton btnOk = new JButton("OKAY");
        btnOk.setBounds(280, 140, 100, 35);
        btnOk.setBackground(themeColor);
        btnOk.setForeground(Color.WHITE);
        btnOk.setFont(new Font("Segoe UI Bold", Font.PLAIN, 12));
        btnOk.setFocusPainted(false);
        btnOk.setBorderPainted(false);
        btnOk.addActionListener(ev -> dialog.dispose());
        panel.add(btnOk);

        dialog.setVisible(true);
    }

    private JTextField createInput(JPanel p, String label, int y) {
        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setBounds(30, y, 200, 20);
        lbl.setFont(new Font("Segoe UI Bold", Font.PLAIN, 11));
        lbl.setForeground(new Color(140, 140, 140));
        p.add(lbl);

        JTextField tf = new JTextField();
        tf.setBounds(30, y + 22, 280, 35);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225), 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 8)));
        p.add(tf);
        return tf;
    }

    private JButton createActionBtn(String text, int y, Color bg) {
        JButton b = new JButton(text);
        b.setBounds(30, y, 280, 45);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI Bold", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });
        return b;
    }

    public static void main(String[] args) {
        new Department();
    }
}