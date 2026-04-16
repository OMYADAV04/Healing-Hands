package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.Date;

public class patient_discharge extends JFrame {

    // Healing Hands Premium Palette
    private final Color HEADER_BLUE = new Color(41, 128, 185);
    private final Color TEXT_DARK = new Color(52, 73, 94);
    private final Color ACCENT_TEAL = new Color(21, 133, 139);
    private final Color BUTTON_RED = new Color(231, 76, 60);
    private final Color BG_LIGHT = new Color(248, 249, 250);

    private JLabel displayRoom, displayInTime, displayOutTime, displayName;

    patient_discharge(){
        setUndecorated(true);
        setSize(850, 550);
        setLocation(400, 220);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(ACCENT_TEAL, 1));
        add(mainPanel);

        // --- HEADER ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        header.setBackground(HEADER_BLUE);
        header.setPreferredSize(new Dimension(850, 65));

        JLabel title = new JLabel("PATIENT CHECK-OUT / DISCHARGE");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        mainPanel.add(header, BorderLayout.NORTH);

        // --- CONTENT BODY ---
        JPanel body = new JPanel(null);
        body.setBackground(Color.WHITE);
        mainPanel.add(body, BorderLayout.CENTER);

        JLabel lblSearch = new JLabel("SELECT PATIENT ID / NUMBER:");
        lblSearch.setBounds(100, 40, 250, 25);
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(ACCENT_TEAL);
        body.add(lblSearch);

        Choice patientChoice = new Choice();
        patientChoice.setBounds(360, 40, 200, 25);
        patientChoice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        body.add(patientChoice);

        addInfoRow(body, "Patient Name:", 100, displayName = new JLabel("---"));
        addInfoRow(body, "Allocated Room Number:", 150, displayRoom = new JLabel("---"));
        addInfoRow(body, "Date of Admission:", 200, displayInTime = new JLabel("---"));
        addInfoRow(body, "Date of Discharge:", 250, displayOutTime = new JLabel("" + new Date()));

        displayOutTime.setForeground(new Color(127, 140, 141));

        // --- FOOTER BUTTONS ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 25));
        footer.setBackground(BG_LIGHT);
        mainPanel.add(footer, BorderLayout.SOUTH);

        JButton btnFetch = createModernBtn("VERIFY DETAILS", ACCENT_TEAL);
        JButton btnDischarge = createModernBtn("CONFIRM DISCHARGE", BUTTON_RED);
        JButton btnClose = createModernBtn("CLOSE", new Color(33, 43, 52));

        footer.add(btnFetch);
        footer.add(btnDischarge);
        footer.add(btnClose);

        // Database Load
        try {
            con c = new con();
            ResultSet rs = c.statement.executeQuery("select * from Patient_Info");
            while (rs.next()) patientChoice.add(rs.getString("number"));
        } catch (Exception e) { e.printStackTrace(); }

        // Logic
        btnFetch.addActionListener(e -> {
            try {
                con c = new con();
                ResultSet rs = c.statement.executeQuery("select * from Patient_Info where number = '"+patientChoice.getSelectedItem()+"'");
                if (rs.next()) {
                    displayName.setText(rs.getString("name").toUpperCase());
                    displayRoom.setText("ROOM " + rs.getString("Room_Number"));
                    displayInTime.setText(rs.getString("Time"));
                    displayName.setForeground(TEXT_DARK);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnDischarge.addActionListener(e -> {
            if(displayRoom.getText().equals("---")) {
                showCustomDialog("Incomplete Process", "Please verify patient details first.", BUTTON_RED);
                return;
            }
            try {
                con c = new con();
                String roomNum = displayRoom.getText().replace("ROOM ", "");
                c.statement.executeUpdate("delete from Patient_Info where number = '"+patientChoice.getSelectedItem()+"'");
                c.statement.executeUpdate("update room set Availability = 'Available' where room_no = '"+roomNum+"'");

                showCustomDialog("Success", "Patient Discharged Successfully. Room " + roomNum + " is now available.", ACCENT_TEAL);
                setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                showCustomDialog("Error", "Discharge process failed.", Color.BLACK);
            }
        });

        btnClose.addActionListener(e -> setVisible(false));
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

    private void addInfoRow(JPanel p, String labelText, int y, JLabel valueLabel) {
        JLabel lbl = new JLabel(labelText);
        lbl.setBounds(100, y, 200, 30);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(100, 110, 120));

        valueLabel.setBounds(360, y, 400, 30);
        valueLabel.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 18));
        valueLabel.setForeground(new Color(44, 62, 80));

        p.add(lbl);
        p.add(valueLabel);
    }

    private JButton createModernBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(200, 45));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void main(String[] args) { new patient_discharge(); }
}