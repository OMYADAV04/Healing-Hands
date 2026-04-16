package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

public class New_patient extends JFrame implements ActionListener {
    private JComboBox<String> comboBox;
    private JTextField textFieldNumber, textName, textFieldDisease, textFieldDeposite;
    private JRadioButton r1, r2;
    private ButtonGroup genderGroup;
    private Choice roomChoice;
    private JLabel dateLabel;
    private JButton b1, b2;

    // Premium Color Palette
    private final Color PRIMARY_COLOR = new Color(32, 178, 170);   // Medical Teal
    private final Color DARK_SLATE = new Color(44, 62, 80);        // Deep Slate
    private final Color BG_COLOR = new Color(245, 246, 247);       // Off-White
    private final Color ERROR_RED = new Color(231, 76, 60);

    public New_patient() {
        // --- Frame Settings ---
        setUndecorated(true);
        setSize(850, 550);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);

        // --- Main Glass-Effect Card ---
        JPanel card = new JPanel();
        card.setBounds(25, 25, 800, 500);
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        add(card);

        // --- Header Section ---
        JLabel labelHeading = new JLabel("PATIENT ADMISSION FORM");
        labelHeading.setBounds(40, 20, 400, 35);
        labelHeading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        labelHeading.setForeground(DARK_SLATE);
        card.add(labelHeading);

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 65, 720, 10);
        sep.setForeground(new Color(220, 220, 220));
        card.add(sep);

        // --- Form Layout ---
        int col1X = 40, col2X = 420;
        int fieldWidth = 340, fieldHeight = 35;
        int row1 = 100, row2 = 180, row3 = 260, row4 = 340;

        // ID Type & Number
        addLabel(card, "Identification Type", col1X, row1);
        comboBox = new JComboBox<>(new String[]{"Aadhar Card", "Voter Id", "Driving License"});
        comboBox.setBounds(col1X, row1 + 25, fieldWidth, fieldHeight);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(comboBox);

        addLabel(card, "ID Number", col2X, row1);
        textFieldNumber = createStyledTextField(col2X, row1 + 25, fieldWidth, fieldHeight);
        card.add(textFieldNumber);

        // Name & Gender
        addLabel(card, "Patient Full Name", col1X, row2);
        textName = createStyledTextField(col1X, row2 + 25, fieldWidth, fieldHeight);
        card.add(textName);

        addLabel(card, "Gender", col2X, row2);
        r1 = new JRadioButton("Male");
        r1.setBounds(col2X, row2 + 25, 80, 35);
        r1.setBackground(Color.WHITE);
        r1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        r2 = new JRadioButton("Female");
        r2.setBounds(col2X + 100, row2 + 25, 100, 35);
        r2.setBackground(Color.WHITE);
        r2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        genderGroup = new ButtonGroup();
        genderGroup.add(r1); genderGroup.add(r2);
        card.add(r1); card.add(r2);

        // Disease & Room
        addLabel(card, "Diagnosis / Disease", col1X, row3);
        textFieldDisease = createStyledTextField(col1X, row3 + 25, fieldWidth, fieldHeight);
        card.add(textFieldDisease);

        addLabel(card, "Allocate Room", col2X, row3);
        roomChoice = new Choice();
        roomChoice.setBounds(col2X, row3 + 25, fieldWidth, fieldHeight);
        roomChoice.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadRoomData(); // Efficient single database call
        card.add(roomChoice);

        // Date & Deposit
        addLabel(card, "Registration Date & Time", col1X, row4);
        dateLabel = new JLabel(new Date().toString());
        dateLabel.setBounds(col1X, row4 + 25, fieldWidth, fieldHeight);
        dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        dateLabel.setForeground(Color.GRAY);
        card.add(dateLabel);

        addLabel(card, "Initial Deposit (INR)", col2X, row4);
        textFieldDeposite = createStyledTextField(col2X, row4 + 25, fieldWidth, fieldHeight);
        card.add(textFieldDeposite);

        // --- Action Buttons ---
        b1 = createStyledButton("ADD PATIENT", 240, 430, PRIMARY_COLOR);
        b2 = createStyledButton("CLOSE", 420, 430, ERROR_RED);
        card.add(b1);
        card.add(b2);

        setVisible(true);
    }

    private void addLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setBounds(x, y, 200, 20);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(120, 120, 120));
        panel.add(label);
    }

    private JTextField createStyledTextField(int x, int y, int w, int h) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        return tf;
    }

    private JButton createStyledButton(String text, int x, int y, Color color) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 160, 45);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }

    private void loadRoomData() {
        try {
            con c = new con();
            ResultSet rs = c.statement.executeQuery("select * from Room where Availability = 'Available'");
            while (rs.next()) {
                roomChoice.add(rs.getString("room_no"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- CUSTOM POP-UP DIALOG ---
    private void showCustomDialog(String title, String message, Color themeColor) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));
        dialog.add(panel);

        // Accent bar at the top
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
        btnOk.addActionListener(e -> dialog.dispose());
        panel.add(btnOk);

        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            String idType = (String) comboBox.getSelectedItem();
            String idNum = textFieldNumber.getText();
            String name = textName.getText();
            String gender = r1.isSelected() ? "Male" : (r2.isSelected() ? "Female" : "");
            String disease = textFieldDisease.getText();
            String room = roomChoice.getSelectedItem();
            String date = dateLabel.getText();
            String deposit = textFieldDeposite.getText();

            if (idNum.isEmpty() || name.isEmpty() || gender.isEmpty()) {
                showCustomDialog("Action Required", "Please fill in all mandatory fields.", ERROR_RED);
                return;
            }

            try {
                con c = new con();
                String q1 = "insert into Patient_Info values ('"+idType+"', '"+idNum+"','"+name+"','"+gender+"', '"+disease+"', '"+room+"', '"+date+"', '"+deposit+"')";
                String q2 = "update room set Availability = 'Occupied' where room_no = '"+room+"'";
                c.statement.executeUpdate(q1);
                c.statement.executeUpdate(q2);

                showCustomDialog("Success", "Patient Record Successfully Created!", PRIMARY_COLOR);
                setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                showCustomDialog("Error", "Database error occurred!", Color.BLACK);
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new New_patient();
    }
}