package hospital.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;

public class update_patient_details extends JFrame {

    // Healing Hands Premium Palette
    private final Color HEADER_BLUE = new Color(41, 128, 185);
    private final Color ACCENT_TEAL = new Color(21, 133, 139);
    private final Color TEXT_MAIN = new Color(44, 62, 80);
    private final Color TEXT_LIGHT = new Color(127, 140, 141);
    private final Color BG_LIGHT = new Color(245, 246, 247);
    private final Color ERROR_RED = new Color(231, 76, 60);

    public update_patient_details() {
        setUndecorated(true);
        setSize(950, 550);
        setLocation(350, 200);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(ACCENT_TEAL, 1));
        add(mainPanel);

        // --- HEADER ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 18));
        header.setBackground(HEADER_BLUE);
        header.setPreferredSize(new Dimension(950, 70));

        JLabel title = new JLabel("PATIENT INFORMATION UPDATE");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        title.setForeground(Color.WHITE);
        header.add(title);
        mainPanel.add(header, BorderLayout.NORTH);

        // --- CONTENT BODY ---
        JPanel body = new JPanel(null);
        body.setBackground(Color.WHITE);
        mainPanel.add(body, BorderLayout.CENTER);

        JLabel subTitle = new JLabel("Patient Record Details");
        subTitle.setBounds(330, 30, 300, 30);
        subTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subTitle.setForeground(ACCENT_TEAL);
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);
        body.add(subTitle);

        // --- FORM FIELDS ---
        JLabel lblName = new JLabel("Search Patient:");
        lblName.setBounds(250, 90, 150, 25);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(TEXT_MAIN);
        body.add(lblName);

        Choice patientChoice = new Choice();
        patientChoice.setBounds(420, 90, 250, 25);
        patientChoice.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        body.add(patientChoice);

        JTextField txtRoom = createStyledField(body, "Room Number:", 150);
        JTextField txtTime = createStyledField(body, "Admission Time:", 200);
        JTextField txtAmount = createStyledField(body, "Amount Paid (Rs):", 250);

        JLabel lblPendingTitle = new JLabel("Outstanding Balance:");
        lblPendingTitle.setBounds(250, 310, 200, 30);
        lblPendingTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPendingTitle.setForeground(TEXT_LIGHT);
        body.add(lblPendingTitle);

        JLabel lblPendingValue = new JLabel("Rs. 0.00");
        lblPendingValue.setBounds(420, 305, 300, 40);
        lblPendingValue.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 28));
        lblPendingValue.setForeground(ERROR_RED);
        body.add(lblPendingValue);

        // --- FOOTER BUTTONS ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 25));
        footer.setBackground(BG_LIGHT);
        mainPanel.add(footer, BorderLayout.SOUTH);

        JButton btnCheck = createBtn("VERIFY DETAILS", ACCENT_TEAL);
        JButton btnUpdate = createBtn("SAVE CHANGES", new Color(38, 166, 154));
        JButton btnBack = createBtn("CANCEL", new Color(33, 43, 52));

        footer.add(btnCheck);
        footer.add(btnUpdate);
        footer.add(btnBack);

        // --- DATABASE LOGIC ---
        try {
            con c = new con();
            ResultSet rs = c.statement.executeQuery("select * from Patient_Info");
            while (rs.next()) patientChoice.add(rs.getString("Name"));
        } catch (Exception e) { e.printStackTrace(); }

        btnCheck.addActionListener(e -> {
            try {
                con c = new con();
                ResultSet rs = c.statement.executeQuery("select * from Patient_Info where Name = '"+patientChoice.getSelectedItem()+"'");
                if (rs.next()) {
                    txtRoom.setText(rs.getString("Room_Number"));
                    txtTime.setText(rs.getString("Time"));
                    txtAmount.setText(rs.getString("Deposite"));

                    ResultSet rsRoom = c.statement.executeQuery("select * from room where room_no = '"+txtRoom.getText()+"'");
                    if (rsRoom.next()) {
                        int total = Integer.parseInt(rsRoom.getString("Price"));
                        int paid = Integer.parseInt(txtAmount.getText());
                        lblPendingValue.setText("Rs. " + (total - paid));
                    }
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                con c = new con();
                c.statement.executeUpdate("update Patient_Info set Room_Number = '"+txtRoom.getText()+"', Time = '"+txtTime.getText()+"', Deposite = '"+txtAmount.getText()+"' where name = '"+patientChoice.getSelectedItem()+"'");

                // Using the Premium Custom Pop-up
                showCustomDialog("Success", "Patient Record Updated Successfully!", new Color(38, 166, 154));
                setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                showCustomDialog("Error", "Failed to update record.", ERROR_RED);
            }
        });

        btnBack.addActionListener(e -> setVisible(false));
        setVisible(true);
    }

    // --- PREMIUM POP-UP METHOD ---
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
        btnOk.setFocusPainted(false);
        btnOk.setBorderPainted(false);
        btnOk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOk.addActionListener(ev -> dialog.dispose());
        panel.add(btnOk);

        dialog.setVisible(true);
    }

    private JTextField createStyledField(JPanel p, String label, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(250, y, 150, 25);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXT_MAIN);
        p.add(lbl);

        JTextField field = new JTextField();
        field.setBounds(420, y, 250, 30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        p.add(field);
        return field;
    }

    private JButton createBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(190, 45));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void main(String[] args) { new update_patient_details(); }
}