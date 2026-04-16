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

public class Employee_info extends JFrame {

    private final Color HEADER_BLUE = new Color(41, 128, 185);
    private final Color BUTTON_DARK = new Color(33, 43, 52);
    private final Color BG_LIGHT = new Color(245, 246, 247);
    private final Color SIDEBAR_TEAL = new Color(21, 133, 139);
    private final Color ACCENT_RED = new Color(231, 76, 60);

    private JTable table;
    private JTextField txtName, txtAge, txtPhone, txtSalary, txtGmail, txtAadhaar;
    private TableRowSorter<TableModel> sorter;

    public Employee_info() {
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

        JLabel title = new JLabel("EMPLOYEE MANAGEMENT SYSTEM");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);

        // Search Section
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        searchBox.setOpaque(false);
        JLabel searchLabel = new JLabel("SEARCH:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI Bold", Font.PLAIN, 12));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }
            private void search() {
                String text = searchField.getText();
                if (sorter != null) {
                    if (text.trim().isEmpty()) sorter.setRowFilter(null);
                    else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
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
        table.setShowVerticalLines(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(isSelected ? new Color(212, 230, 241) : (row % 2 == 0 ? Color.WHITE : new Color(250, 251, 252)));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(25, 25, 10, 25)); // Adjusted bottom padding
        scrollPane.getViewport().setBackground(BG_LIGHT);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- RIGHT: CONTROL PANEL ---
        JPanel sidePanel = new JPanel(null);
        sidePanel.setPreferredSize(new Dimension(340, 650));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(225, 225, 225)));
        mainPanel.add(sidePanel, BorderLayout.EAST);

        JLabel lblForm = new JLabel("EMPLOYEE DETAILS");
        lblForm.setBounds(30, 15, 250, 30);
        lblForm.setFont(new Font("Segoe UI Bold", Font.PLAIN, 16));
        lblForm.setForeground(SIDEBAR_TEAL);
        sidePanel.add(lblForm);

        // Adjusted Y positions for better spacing and visibility
        txtName = createInput(sidePanel, "Name", 55);
        txtAge = createInput(sidePanel, "Age", 115);
        txtPhone = createInput(sidePanel, "Phone Number", 175);
        txtSalary = createInput(sidePanel, "Salary", 235);
        txtGmail = createInput(sidePanel, "Gmail", 295);
        txtAadhaar = createInput(sidePanel, "Aadhaar Number", 355); // More space below this field now

        // Buttons shifted up slightly for guaranteed visibility
        JButton btnAdd = createActionBtn("REGISTER EMPLOYEE", 435, SIDEBAR_TEAL);
        JButton btnDelete = createActionBtn("REMOVE EMPLOYEE", 485, ACCENT_RED);
        JButton btnBack = createActionBtn("CLOSE INTERFACE", 535, BUTTON_DARK);

        sidePanel.add(btnAdd);
        sidePanel.add(btnDelete);
        sidePanel.add(btnBack);

        loadData();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row != -1) {
                    int modelRow = table.convertRowIndexToModel(row);
                    txtName.setText(table.getModel().getValueAt(modelRow, 0).toString());
                    txtAge.setText(table.getModel().getValueAt(modelRow, 1).toString());
                    txtPhone.setText(table.getModel().getValueAt(modelRow, 2).toString());
                    txtSalary.setText(table.getModel().getValueAt(modelRow, 3).toString());
                    txtGmail.setText(table.getModel().getValueAt(modelRow, 4).toString());
                    txtAadhaar.setText(table.getModel().getValueAt(modelRow, 5).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty() || txtAadhaar.getText().trim().isEmpty()) {
                showCustomDialog("Input Required", "<b>All fields are mandatory.</b><br>Please fill details before registration.", ACCENT_RED);
                return;
            }
            try {
                con c = new con();
                String query = "insert into EMP_INFO values('"+txtName.getText()+"', '"+txtAge.getText()+"', '"+txtPhone.getText()+"', '"+txtSalary.getText()+"', '"+txtGmail.getText()+"', '"+txtAadhaar.getText()+"')";
                c.statement.executeUpdate(query);
                showCustomDialog("Success", "Employee registered successfully.", SIDEBAR_TEAL);
                clearFields();
                loadData();
            } catch (Exception ex) {
                showCustomDialog("Error", "Registration failed.", ACCENT_RED);
            }
        });

        btnDelete.addActionListener(e -> {
            if (table.getSelectedRow() == -1) {
                showCustomDialog("No Selection", "Please select an employee to remove.", ACCENT_RED);
                return;
            }
            try {
                con c = new con();
                String query = "delete from EMP_INFO where Aadhaar_Number = '"+txtAadhaar.getText()+"'";
                c.statement.executeUpdate(query);
                showCustomDialog("Removed", "Employee record deleted.", ACCENT_RED);
                clearFields();
                loadData();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnBack.addActionListener(e -> setVisible(false));
        setVisible(true);
    }

    private void loadData() {
        try {
            con c = new con();
            ResultSet rs = c.statement.executeQuery("select * from EMP_INFO");
            table.setModel(DbUtils.resultSetToTableModel(rs));
            sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);

            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI Bold", Font.PLAIN, 13));
            header.setBackground(Color.WHITE);
            header.setPreferredSize(new Dimension(100, 40));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearFields() {
        txtName.setText(""); txtAge.setText(""); txtPhone.setText("");
        txtSalary.setText(""); txtGmail.setText(""); txtAadhaar.setText("");
        table.clearSelection();
    }

    private void showCustomDialog(String title, String message, Color themeColor) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));
        dialog.add(panel);
        JPanel topBar = new JPanel();
        topBar.setBackground(themeColor);
        topBar.setBounds(0, 0, 400, 5);
        panel.add(topBar);
        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(themeColor);
        lblTitle.setBounds(25, 15, 350, 30);
        panel.add(lblTitle);
        JLabel lblMsg = new JLabel("<html>" + message + "</html>");
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMsg.setBounds(25, 45, 350, 50);
        panel.add(lblMsg);
        JButton btnOk = new JButton("OK");
        btnOk.setBounds(290, 125, 80, 30);
        btnOk.setBackground(themeColor);
        btnOk.setForeground(Color.WHITE);
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
        tf.setBounds(30, y + 20, 280, 30);
        tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225, 225, 225)), BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        p.add(tf);
        return tf;
    }

    private JButton createActionBtn(String text, int y, Color bg) {
        JButton b = new JButton(text);
        b.setBounds(30, y, 280, 40);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI Bold", Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        return b;
    }

    public static void main(String[] args) {
        new Employee_info();
    }
}