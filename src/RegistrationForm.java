import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;

    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent) {
        super (parent);
        setTitle("Create a new account ");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //Button of Register
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        // Button of Cancel
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });
        setVisible(true);
    }

    private void registerUser() {
//        String name = tfName.getName();
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
               JOptionPane.showMessageDialog(this,
                    "Please enter all fields ",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm the password",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //add new user to the database
        user = addUserToDatabase(name, email, phone, address, password);
        if (user != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to Register New user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public User user;
    private User addUserToDatabase(String name, String email, String phone , String address , String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/ayushdb/Peoples?autoReconnect=true&useSSL=false";

        final String USERNAME = "root";
        final String PASSWORD= "Pavilion@19";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            //connection to the database successfully:
            Statement stml = conn.createStatement();

            String sql = "INSERT INTO users (name , email, phone , address, password)"+
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            //Insert row to the table
            int addedRows = preparedStatement.executeUpdate();
            if ( addedRows > 0){
                user = new User();
                user.name =name;
                user.email = email;
                user.phone = phone ;
                user.address = address;
                user.Password = password;
            }
            stml.close();
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return user;

    }

    public static void main (String args[]){
        RegistrationForm myForm = new RegistrationForm(null);
        User user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of : " +user.name);

        }
        else{
            System.out.println("Registration cancelled: ");
        }
    }
}
