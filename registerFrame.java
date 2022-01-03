import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class registerFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel Name = new JLabel("Name: ");
    JLabel Birth = new JLabel("Birth: ");
    JLabel Address = new JLabel("Address: ");
    JLabel phoneNumber = new JLabel("Phone Number: ");
    JLabel eMail = new JLabel("Email: ");
    JLabel UserName = new JLabel("User Name: ");
    JLabel PassWord = new JLabel("Password: ");
    JTextField NameField = new JTextField();
    JTextField BirthField = new JTextField();
    JTextField AddField = new JTextField();
    JTextField pNumberField = new JTextField();
    JTextField eMailField = new JTextField();
    JTextField UserNameField = new JTextField();
    JPasswordField pwdField = new JPasswordField();
    JCheckBox showPassword = new JCheckBox("Show Password");
    JButton submit = new JButton("Submit");
    JButton reset = new JButton("Reset");
    registerFrame()
    {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    public void setLayoutManager()
    {
        container.setLayout(null);
    }

    public void setLocationAndSize()
    {
        Name.setBounds(50, 100,100,30);
        Birth.setBounds(50, 130,100,30);
        Address.setBounds(50, 160,100,30);
        phoneNumber.setBounds(50, 190,100,30);
        eMail.setBounds(50, 220,100,30);
        UserName.setBounds(50, 250,100,30);
        PassWord.setBounds(50, 280,100,30);
        NameField.setBounds(150, 100,150,30);
        BirthField.setBounds(150, 130,150,30);
        AddField.setBounds(150, 160,150,30);
        pNumberField.setBounds(150, 190,150,30);
        eMailField.setBounds(150, 220,150,30);
        UserNameField.setBounds(150, 250,150,30);
        pwdField.setBounds(150, 280,150,30);
        showPassword.setBounds(150, 310,150,30);
        submit.setBounds(80,350,100,30);
        reset.setBounds(190,350,100,30);
    }
    public void addActionEvent()
    {
        submit.addActionListener(this);
        reset.addActionListener(this);
        showPassword.addActionListener(this);
    }
    public void addComponentsToContainer()
    {
        container.add(Name);
        container.add(Birth);
        container.add(Address);
        container.add(phoneNumber);
        container.add(eMail);
        container.add(UserName);
        container.add(PassWord);
        container.add(NameField);
        container.add(BirthField);
        container.add(AddField);
        container.add(pNumberField);
        container.add(eMailField);
        container.add(UserNameField);
        container.add(pwdField);
        container.add(showPassword);
        container.add(submit);
        container.add(reset);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Connection conn = null;
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        CallableStatement cs = null;
        String userText;

        String tF[] = new String[7];
        tF[0] = NameField.getText();
        tF[1] = BirthField.getText();
        tF[2] = AddField.getText();
        tF[3] = pNumberField.getText();
        tF[4] = eMailField.getText();
        tF[5] = UserNameField.getText();
        tF[6] = pwdField.getText();
        if(actionEvent.getSource() == submit)
        {
            for(int i=0;i<7;i++)
            {
                if(tF[i].isEmpty())
                {
                    JOptionPane.showMessageDialog(this, "Data Missing", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            try {
                conn = java.sql.DriverManager.getConnection(URL);
                cs = conn.prepareCall("{call REGISTER(?,?,?,?,?,?,?,?)}");
                for(int i=0;i<7;i++)
                {
                    cs.setString(i+1,tF[i]);
                }
                cs.registerOutParameter(8, Types.INTEGER);
                cs.execute();
                int result = cs.getInt(8);
                switch(result)
                {
                    case(0): JOptionPane.showMessageDialog(this, "This UserName has been used", "ERROR",JOptionPane.ERROR_MESSAGE);
                    break;
                    case(1): JOptionPane.showMessageDialog(this, "Register Successful");
                        this.setVisible(false);
                        this.dispose();
                    break;
                    case(2): JOptionPane.showMessageDialog(this, "This phone number has been used", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                try {
                    cs.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (actionEvent.getSource() == reset)
        {
            for(int i=0;i<7;i++)
            {
                NameField.setText("");
                BirthField.setText("");
                AddField.setText("");
                pNumberField.setText("");
                eMailField.setText("");
                UserNameField.setText("");
                pwdField.setText("");
            }
        }

        if(actionEvent.getSource() == showPassword)
        {
            if(showPassword.isSelected())
            {
                pwdField.setEchoChar((char) 0);
            }
            else
            {
                pwdField.setEchoChar('*');
            }
        }
    }
}
