
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import java.sql.*;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import static java.sql.DriverManager.getConnection;
import static java.sql.Types.*;

public class loginFrame extends JFrame implements ActionListener
{
    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");
    JButton register = new JButton("Register");
    //ImageIcon icon = new ImageIcon("C:\\Users\\nkang\\IdeaProjects\\tiki\\src\\com\\tiki\\salesiconRS.png");
   // JLabel iconLabel = new JLabel(icon);
    loginFrame()
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
        userLabel.setBounds(50,210,100,30);
        passwordLabel.setBounds(50,240,100,30);
        userTextField.setBounds(150,210,150,30);
        passwordField.setBounds(150,240,150,30);
        showPassword.setBounds(150,280,150,30);
        loginButton.setBounds(80,320,100,30);
        resetButton.setBounds(190,320,100,30);
        register.setForeground(Color.BLUE.darker());
        register.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        register.setBounds(80,360,100,30);
       //iconLabel.setBounds(110,40, 150,150);
    }

    public void addComponentsToContainer()
    {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
        container.add(register);
       // container.add(iconLabel);
    }

    public void addActionEvent()
    {
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
        passwordField.addActionListener(this);
        userTextField.addActionListener(this);
        register.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Connection conn = null;
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        CallableStatement cs = null;
        if((actionEvent.getSource() == loginButton)||(actionEvent.getSource() == passwordField)||(actionEvent.getSource() == userTextField)) {
            String userText;
            String pwdText;
            userText = userTextField.getText();
            pwdText = passwordField.getText();
            if (userText.isEmpty() || pwdText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data Missing", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
              //  String sql = "select * from"
                try {
                    conn = DriverManager.getConnection(URL);
                    cs = conn.prepareCall("{call LOG_IN(?,?,?)}");
                    cs.setString(1, userText);
                    cs.setString(2, pwdText);
                    cs.registerOutParameter(3, Types.INTEGER);
                    cs.execute();
                    int result = cs.getInt(3);
                    switch(result)
                    {
                        case(0): JOptionPane.showMessageDialog(this, "Wrong UserName or PassWord", "ERROR",JOptionPane.ERROR_MESSAGE);
                        break;
                        case(2): JOptionPane.showMessageDialog(this, "Your Account Has Been Banned", "ERROR",JOptionPane.ERROR_MESSAGE);
                        break;
                        case(1): {
                            JOptionPane.showMessageDialog(this, "Log In Successful");
                            this.setVisible(false);
                            this.dispose();
                            UserFrame userFrame = new UserFrame(userText);
                            userFrame.setVisible(true);
                            userFrame.setTitle("Sales");
                            userFrame.setSize(800,500);
                            userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            userFrame.setResizable(false);
                        }
                    }
                } catch (SQLException   e) {
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
        }
        if (actionEvent.getSource() == resetButton)
        {
            userTextField.setText("");
            passwordField.setText("");
        }

        if(actionEvent.getSource() == showPassword)
        {
            if(showPassword.isSelected())
            {
                passwordField.setEchoChar((char) 0);
            }
            else
            {
                passwordField.setEchoChar('*');
            }
        }
        if(actionEvent.getSource() == register)
        {
            registerFrame rF = new registerFrame();
            rF.setTitle("Register");
            rF.setVisible(true);
            rF.setBounds(10,10,370,600);
            rF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            rF.setResizable(false);
        }
    }
}

