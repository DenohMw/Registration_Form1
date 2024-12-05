import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegistrationForm extends JFrame {
    JTextField nameField, mobileField, addressField;
    JRadioButton maleButton, femaleButton;
    JComboBox<String> dayComboBox, monthComboBox, yearComboBox;
    JTextArea displayArea;
    JButton submitButton, resetButton;
    ButtonGroup genderGroup;

    public RegistrationForm() {
        setTitle("Registration Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(100, 40));
        headerPanel.setBackground(Color.ORANGE);
        JLabel headerLabel = new JLabel("Registration Form");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 25));
        headerPanel.add(headerLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setPreferredSize(new Dimension(400, 800));
        formPanel.setBackground(Color.CYAN);

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridBagLayout());
        displayPanel.setBackground(Color.CYAN);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 40, 100, 25);
        formPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 40, 200, 25);
        formPanel.add(nameField);

        JLabel mobileLabel = new JLabel("Mobile:");
        mobileLabel.setBounds(20, 100, 100, 25);
        formPanel.add(mobileLabel);

        mobileField = new JTextField();
        mobileField.setBounds(130, 100, 200, 25);
        formPanel.add(mobileField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(20, 140, 100, 25);
        formPanel.add(genderLabel);

        maleButton = new JRadioButton("Male");
        maleButton.setBackground(Color.CYAN);
        maleButton.setBounds(130, 140, 70, 25);
        formPanel.add(maleButton);

        femaleButton = new JRadioButton("Female");
        femaleButton.setBackground(Color.CYAN);
        femaleButton.setBounds(210, 140, 70, 25);
        formPanel.add(femaleButton);

        genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        JLabel dobLabel = new JLabel("DOB:");
        dobLabel.setBounds(20, 180, 100, 25);
        formPanel.add(dobLabel);

        String[] days = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        dayComboBox = new JComboBox<>(days);
        dayComboBox.setBounds(130, 180, 50, 25);
        formPanel.add(dayComboBox);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setBounds(190, 180, 60, 25);
        formPanel.add(monthComboBox);

        String[] years = {"2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010"};
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setBounds(260, 180, 70, 25);
        formPanel.add(yearComboBox);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 220, 100, 25);
        formPanel.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(130, 220, 200, 25);
        formPanel.add(addressField);

        JCheckBox termsCheckBox = new JCheckBox("Accept Terms and Conditions");
        termsCheckBox.setBounds(20, 260, 250, 25);
        termsCheckBox.setBackground(Color.CYAN);
        formPanel.add(termsCheckBox);

        submitButton = new JButton("Submit");
        submitButton.setBounds(50, 300, 100, 25);
        submitButton.setBackground(Color.GREEN);
        formPanel.add(submitButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(200, 300, 100, 25);
        resetButton.setBackground(Color.ORANGE);
        formPanel.add(resetButton);

        displayArea = new JTextArea(25, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.CENTER;
        displayPanel.add(scrollPane, g);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.WEST);
        add(displayPanel, BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty() || mobileField.getText().isEmpty() || genderGroup.getSelection() == null || dayComboBox.getSelectedItem() == null || monthComboBox.getSelectedItem() == null || yearComboBox.getSelectedItem() == null || addressField.getText().isEmpty() || !termsCheckBox.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields and accept the terms and conditions.");
                } else {
                    String name = nameField.getText();
                    String mobile = mobileField.getText();
                    String gender = maleButton.isSelected() ? "Male" : "Female";
                    String dob = yearComboBox.getSelectedItem() + "-" + (monthComboBox.getSelectedIndex() + 1) + "-" + dayComboBox.getSelectedItem();
                    String address = addressField.getText();

                    saveUser(name, mobile, gender, dob, address);
                    displayUserDetails();
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                mobileField.setText("");
                genderGroup.clearSelection();
                dayComboBox.setSelectedIndex(0);
                monthComboBox.setSelectedIndex(0);
                yearComboBox.setSelectedIndex(0);
                addressField.setText("");
                termsCheckBox.setSelected(false);
            }
        });

        setVisible(true);
    }

    private void saveUser(String name, String mobile, String gender, String dob, String address) {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "INSERT INTO users (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    stmt.setString(2, mobile);
                    stmt.setString(3, gender);
                    stmt.setString(4, dob);
                    stmt.setString(5, address);
                    stmt.executeUpdate();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayUserDetails() {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String query = "SELECT * FROM users";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {
                    displayArea.setText("");
                    while (rs.next()) {
                        String name = rs.getString("name");
                        String mobile = rs.getString("mobile");
                        String gender = rs.getString("gender");
                        String dob = rs.getString("dob");
                        String address = rs.getString("address");

                        displayArea.append("Name: " + name + "\n");
                        displayArea.append("Mobile: " + mobile + "\n");
                        displayArea.append("Gender: " + gender + "\n");
                        displayArea.append("DOB: " + dob + "\n");
                        displayArea.append("Address: " + address + "\n\n");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}
