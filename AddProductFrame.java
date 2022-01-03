import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddProductFrame extends JFrame implements ActionListener {
    Container container = getContentPane();
    JLabel pName = new JLabel("Name: ");
    JLabel pPrice = new JLabel("Price: ");
    JLabel Qty = new JLabel("Qty: ");
    JLabel Cate = new JLabel("Category: ");
    JLabel Des = new JLabel("Description: ");
    JTextField pNameTF = new JTextField();
    JTextField pPriceTF = new JTextField();
    JTextField QtyTF = new JTextField();
    JTextField CateTF = new JTextField();
    JTextField DesTF = new JTextField();
    JButton Submit = new JButton("Submit");
    JButton Reset = new JButton("Reset");
    String GH_id = new String();
    AddProductFrame(String GHID)
    {
        setLayOutManager();
        setLocationAndSize(GHID);
        addComponentsToContainer();
        addActionEvent();
    }
    public void setLayOutManager()
    {
        container.setLayout(null);
    }
    public void setLocationAndSize(String GHID)
    {
        GH_id = GHID;
        pName.setBounds(30,30, 100,30);
        pPrice.setBounds(30,60,100,30);
        Qty.setBounds(30,90,100,30);
        Cate.setBounds(30,120,100,30);
        Des.setBounds(30,150,100,30);
        pNameTF.setBounds(100,30,150,30);
        pPriceTF.setBounds(100,60,150,30);
        QtyTF.setBounds(100,90,150,30);
        CateTF.setBounds(100,120,150,30);
        DesTF.setBounds(100,150,150,30);
        Submit.setBounds(50,200,100,30);
        Reset.setBounds(160,200,100,30);
    }
    public void addComponentsToContainer()
    {
        container.add(pName);
        container.add(pPrice);
        container.add(Qty);
        container.add(Cate);
        container.add(pNameTF);
        container.add(pPriceTF);
        container.add(QtyTF);
        container.add(CateTF);
        container.add(Submit);
        container.add(Reset);
        container.add(Des);
        container.add(DesTF);
    }

    public void addActionEvent()
    {
        Submit.addActionListener(this);
        Reset.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == Reset)
        {
            pNameTF.setText("");
            pPriceTF.setText("");
            QtyTF.setText("");
            CateTF.setText("");
            DesTF.setText("");
        }
        if(actionEvent.getSource()==Submit)
        {
            if(pNameTF.getText().isEmpty() || pPriceTF.getText().isEmpty()||QtyTF.getText().isEmpty()||CateTF.getText().isEmpty()||DesTF.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Data Missing", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                Connection conn = null;
                String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
                CallableStatement cs = null;
                try {
                    conn = DriverManager.getConnection(URL);
                    cs = conn.prepareCall("{call PRODUCT_REGISTER(?,?,?,?,?,?,?)}");
                    cs.setString(1,GH_id);
                    cs.setString(2,pNameTF.getText());
                    cs.setString(3, pPriceTF.getText());
                    cs.setString(4,QtyTF.getText());
                    cs.setString(5,CateTF.getText());
                    cs.setString(6,DesTF.getText());
                    cs.registerOutParameter(7, Types.INTEGER);
                    cs.execute();
                    int status = cs.getInt(7);
                    if(status == 1)
                    {
                        JOptionPane.showMessageDialog(null, "Add Product Successful");
                        this.setVisible(false);
                        this.dispose();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Add Product Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
