/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myexam.studentregistrationform;

/**
 *
 * @author VU-STUDENT
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.text.DateFormatSymbols;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;

public class StudentRegistrationForm extends JFrame {
    // Form components
    private final JTextField tfFirstName = new JTextField(20);
    private final JTextField tfLastName = new JTextField(20);
    private final JTextField tfEmail = new JTextField(20);
    private final JTextField tfEmailConfirm = new JTextField(20);
    private final JPasswordField pfPassword = new JPasswordField(20);
    private final JPasswordField pfPasswordConfirm = new JPasswordField(20);

    private final JComboBox<Integer> cbYear = new JComboBox<>();
    private final JComboBox<String> cbMonth = new JComboBox<>();
    private final JComboBox<Integer> cbDay = new JComboBox<>();

    private final JRadioButton rbMale = new JRadioButton("Male");
    private final JRadioButton rbFemale = new JRadioButton("Female");
    private final ButtonGroup genderGroup = new ButtonGroup();

    private final JRadioButton rbCivil = new JRadioButton("Civil");
    private final JRadioButton rbCSE = new JRadioButton("Computer Science and Engineering");
    private final JRadioButton rbElectrical = new JRadioButton("Electrical");
    private final JRadioButton rbElectronics = new JRadioButton("Electronics and Communication");
    private final JRadioButton rbMechanical = new JRadioButton("Mechanical");
    private final ButtonGroup deptGroup = new ButtonGroup();

    private final JTextArea taOutput = new JTextArea(12, 28);

    // inline error labels
    private final JLabel errName = new JLabel(" ");
    private final JLabel errEmail = new JLabel(" ");
    private final JLabel errEmailConf = new JLabel(" ");
    private final JLabel errPassword = new JLabel(" ");
    private final JLabel errPasswordConf = new JLabel(" ");
    private final JLabel errDOB = new JLabel(" ");
    private final JLabel errGender = new JLabel(" ");
    private final JLabel errDept = new JLabel(" ");

    // files
    private final Path csvPath = Paths.get("students.csv");

    // background color (light grey)
    private final Color BG_COLOR = new Color(0xC0, 0xC0, 0xC0);

    public StudentRegistrationForm() {
        super("New Student Registration Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(10,10));
        main.setBorder(new EmptyBorder(10,10,10,10));
        // Apply background color
        main.setBackground(BG_COLOR);
        add(main);

        // Title at top
        JLabel title = new JLabel("New Student Registration Form", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        main.add(title, BorderLayout.NORTH);

        // Left: form; Right: output
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG_COLOR);
        main.add(center, BorderLayout.CENTER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // First Name
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Student First Name : "), gbc);
        gbc.gridx = 1;
        center.add(tfFirstName, gbc);
        gbc.gridx = 2;
        errName.setForeground(Color.RED);
        center.add(errName, gbc);
        row++;

        // Last Name
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Student Last Name : "), gbc);
        gbc.gridx = 1;
        center.add(tfLastName, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Email Address : "), gbc);
        gbc.gridx = 1;
        center.add(tfEmail, gbc);
        gbc.gridx = 2;
        errEmail.setForeground(Color.RED);
        center.add(errEmail, gbc);
        row++;

        // Confirm Email
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Confirm Email Address : "), gbc);
        gbc.gridx = 1;
        center.add(tfEmailConfirm, gbc);
        gbc.gridx = 2;
        errEmailConf.setForeground(Color.RED);
        center.add(errEmailConf, gbc);
        row++;

        // Password
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Password : "), gbc);
        gbc.gridx = 1;
        center.add(pfPassword, gbc);
        gbc.gridx = 2;
        errPassword.setForeground(Color.RED);
        center.add(errPassword, gbc);
        row++;

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Confirm Password : "), gbc);
        gbc.gridx = 1;
        center.add(pfPasswordConfirm, gbc);
        gbc.gridx = 2;
        errPasswordConf.setForeground(Color.RED);
        center.add(errPasswordConf, gbc);
        row++;

        // DOB label and combos
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Date of Birth : "), gbc);
        gbc.gridx = 1;
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        dobPanel.setBackground(BG_COLOR);
        populateDOBCombos();
        dobPanel.add(cbYear);
        dobPanel.add(cbMonth);
        dobPanel.add(cbDay);
        center.add(dobPanel, gbc);
        gbc.gridx = 2;
        errDOB.setForeground(Color.RED);
        center.add(errDOB, gbc);
        row++;

        // Add listeners to year/month to update day count
        cbYear.addActionListener(e -> updateDays());
        cbMonth.addActionListener(e -> updateDays());

        // Gender
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Gender : "), gbc);
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        genderPanel.setBackground(BG_COLOR);
        genderGroup.add(rbMale); genderGroup.add(rbFemale);
        genderPanel.add(rbMale); genderPanel.add(rbFemale);
        center.add(genderPanel, gbc);
        gbc.gridx = 2;
        errGender.setForeground(Color.RED);
        center.add(errGender, gbc);
        row++;

        // Department
        gbc.gridx = 0; gbc.gridy = row;
        center.add(new JLabel("Department : "), gbc);
        gbc.gridx = 1;
        JPanel deptPanel = new JPanel(new GridLayout(0,1));
        deptPanel.setBackground(BG_COLOR);
        deptGroup.add(rbCivil); deptGroup.add(rbCSE); deptGroup.add(rbElectrical);
        deptGroup.add(rbElectronics); deptGroup.add(rbMechanical);
        deptPanel.add(rbCivil); deptPanel.add(rbCSE); deptPanel.add(rbElectrical);
        deptPanel.add(rbElectronics); deptPanel.add(rbMechanical);
        center.add(deptPanel, gbc);
        gbc.gridx = 2;
        errDept.setForeground(Color.RED);
        center.add(errDept, gbc);
        row++;

        // Buttons Submit / Cancel
        gbc.gridx = 0; gbc.gridy = row;
        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnPanel.setBackground(BG_COLOR);
        btnPanel.add(btnSubmit); btnPanel.add(btnCancel);
        center.add(btnPanel, gbc);
        row++;

        // Right-hand output area
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(new EmptyBorder(0,10,0,0));
        right.setBackground(BG_COLOR);
        JLabel outLabel = new JLabel("Your Data is Below:");
        taOutput.setEditable(false);
        taOutput.setLineWrap(true);
        taOutput.setWrapStyleWord(true);
        // keep output text area white for readability
        taOutput.setBackground(Color.WHITE);
        JScrollPane sp = new JScrollPane(taOutput);
        sp.setPreferredSize(new Dimension(320, 120));
        right.add(outLabel, BorderLayout.NORTH);
        right.add(sp, BorderLayout.CENTER);

        // put center and right together
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_COLOR);
        wrapper.add(center, BorderLayout.WEST);
        wrapper.add(right, BorderLayout.EAST);
        main.add(wrapper, BorderLayout.CENTER);

        // Action listeners
        btnCancel.addActionListener(e -> clearForm());
        btnSubmit.addActionListener(e -> handleSubmit());
    }

    private void populateDOBCombos() {
        int nowYear = Year.now().getValue();
        for (int y = nowYear; y >= 1900; y--) {
            cbYear.addItem(y);
        }
        String[] months = new DateFormatSymbols().getMonths();
        for (int m = 1; m <= 12; m++) {
            cbMonth.addItem(String.format("%02d - %s", m, months[m-1]));
        }
        updateDays();
    }

    private void updateDays() {
        Integer year = (Integer) cbYear.getSelectedItem();
        int monthIndex = cbMonth.getSelectedIndex() + 1;
        if (year == null) year = Year.now().getValue();
        int daysInMonth = YearMonth.of(year, monthIndex).lengthOfMonth();

        int selectedDay = cbDay.getItemCount() > 0 ? (Integer) cbDay.getSelectedItem() : 1;
        cbDay.removeAllItems();
        for (int d = 1; d <= daysInMonth; d++) {
            cbDay.addItem(d);
        }
        // restore if possible
        if (selectedDay <= daysInMonth) {
            cbDay.setSelectedItem(selectedDay);
        } else {
            cbDay.setSelectedItem(1);
        }
    }

    private void clearForm() {
        tfFirstName.setText("");
        tfLastName.setText("");
        tfEmail.setText("");
        tfEmailConfirm.setText("");
        pfPassword.setText("");
        pfPasswordConfirm.setText("");
        cbYear.setSelectedIndex(0);
        cbMonth.setSelectedIndex(0);
        updateDays();
        genderGroup.clearSelection();
        deptGroup.clearSelection();
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        errName.setText(" ");
        errEmail.setText(" ");
        errEmailConf.setText(" ");
        errPassword.setText(" ");
        errPasswordConf.setText(" ");
        errDOB.setText(" ");
        errGender.setText(" ");
        errDept.setText(" ");
    }

    private void handleSubmit() {
        clearErrorLabels();
        boolean ok = true;

        String first = tfFirstName.getText().trim();
        String last = tfLastName.getText().trim();
        String email = tfEmail.getText().trim();
        String emailConf = tfEmailConfirm.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        String passwordConf = new String(pfPasswordConfirm.getPassword()).trim();

        // Name required
        if (first.isEmpty() || last.isEmpty()) {
            errName.setText("First and Last required");
            ok = false;
        }

        // Email validations
        if (!isValidEmail(email)) {
            errEmail.setText("Invalid email");
            ok = false;
        }
        if (!email.equals(emailConf)) {
            errEmailConf.setText("Emails do not match");
            ok = false;
        }

        // Password validations: 8-20 chars, at least 1 letter and 1 digit
        if (!isValidPassword(password)) {
            errPassword.setText("8-20 chars, at least 1 letter & 1 digit");
            ok = false;
        }
        if (!password.equals(passwordConf)) {
            errPasswordConf.setText("Passwords do not match");
            ok = false;
        }

        // DOB & age validation
        int y = (Integer) cbYear.getSelectedItem();
        int m = cbMonth.getSelectedIndex() + 1;
        int d = (Integer) cbDay.getSelectedItem();
        LocalDate dob;
        try {
            dob = LocalDate.of(y, m, d);
            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 16 || age > 60) {
                errDOB.setText("Age must be between 16 and 60 (current: " + age + ")");
                ok = false;
            }
        } catch (Exception ex) {
            errDOB.setText("Invalid date");
            ok = false;
            dob = null;
        }

        // Gender single select
        String gender = null;
        if (rbMale.isSelected()) gender = "Male";
        else if (rbFemale.isSelected()) gender = "Female";
        if (gender == null) {
            errGender.setText("Select gender");
            ok = false;
        }

        // Department single select
        String dept = null;
        if (rbCivil.isSelected()) dept = "Civil";
        else if (rbCSE.isSelected()) dept = "Computer Science and Engineering";
        else if (rbElectrical.isSelected()) dept = "Electrical";
        else if (rbElectronics.isSelected()) dept = "Electronics and Communication";
        else if (rbMechanical.isSelected()) dept = "Mechanical";
        if (dept == null) {
            errDept.setText("Select department");
            ok = false;
        }

        // If invalid, show summary dialog listing errors
        if (!ok) {
            StringBuilder sb = new StringBuilder();
            sb.append("Please fix the inline errors and try again.\n\nInline errors shown in red next to fields.");
            JOptionPane.showMessageDialog(this, sb.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // All valid — prepare record and write to file
        LocalDate dobFinal = LocalDate.of(y,m,d);
        String id = generateStudentID(dobFinal.getYear());
        String record = formatRecord(id, first, last, email, dept, dobFinal, gender);

        // Display in right-hand textarea
        taOutput.setText(record);

        // Append to CSV
        try {
            appendRecordCSV(id, first, last, email, dept, dobFinal, gender);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save record: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }

        // Show dialog with formatted record & id
        JOptionPane.showMessageDialog(this, "Registration successful!\n\n" + record,
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private static boolean isValidEmail(String email) {
        if (email.isEmpty()) return false;
        // simple RFC-ish email regex (not perfect but fine for assignment)
        Pattern p = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return p.matcher(email).matches();
    }

    private static boolean isValidPassword(String pwd) {
        if (pwd.length() < 8 || pwd.length() > 20) return false;
        boolean hasLetter = false, hasDigit = false;
        for (char c : pwd.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }

    private String generateStudentID(int year) {
        // Format: YYYY-xxxx where xxxx increments per year (zero-padded 4 digits)
        // Read existing students.csv and count how many have this year prefix to get next number
        int next = 1;
        if (Files.exists(csvPath)) {
            try {
                List<String> lines = Files.readAllLines(csvPath);
                int max = 0;
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    // CSV format: id,first,last,email,department,dob,gender
                    String[] parts = line.split(",", -1);
                    if (parts.length > 0) {
                        String id = parts[0].trim();
                        if (id.startsWith(year + "-")) {
                            String suffix = id.substring((year + "-").length());
                            try {
                                int num = Integer.parseInt(suffix);
                                if (num > max) max = num;
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
                next = max + 1;
            } catch (IOException ex) {
                // If reading fails, fall back to 1
                next = 1;
            }
        }
        return String.format("%04d-%04d", year, next);
    }

    private String formatRecord(String id, String first, String last, String email,
                                String dept, LocalDate dob, String gender) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Name: ").append(first).append(" ").append(last).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Department: ").append(dept).append("\n");
        sb.append("DOB: ").append(dob.format(fmt)).append("\n");
        sb.append("Gender: ").append(gender).append("\n");
        return sb.toString();
    }

    private void appendRecordCSV(String id, String first, String last, String email,
                                 String dept, LocalDate dob, String gender) throws IOException {
        // CSV: id,first,last,email,department,dob,gender
        boolean createHeader = !Files.exists(csvPath);
        try (BufferedWriter bw = Files.newBufferedWriter(csvPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            if (createHeader) {
                bw.write("id,first,last,email,department,dob,gender");
                bw.newLine();
            }
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String line = String.join(",",
                    id,
                    escapeCsv(first),
                    escapeCsv(last),
                    escapeCsv(email),
                    escapeCsv(dept),
                    dob.format(fmt),
                    escapeCsv(gender)
            );
            bw.write(line);
            bw.newLine();
        }
    }

    private String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentRegistrationForm app = new StudentRegistrationForm();
            app.setVisible(true);
        });
    }
}