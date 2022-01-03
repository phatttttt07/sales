import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.*;
import static javax.swing.JTable.*;
import java.math.*;

public class MyCart extends JFrame implements ActionListener {
    Container container = getContentPane();
    JPanel infoPanel = new JPanel();
    JLabel CartId;
    JButton Back = new JButton("Back");
    JButton Purchase = new JButton("Purchase");
    JButton DeleteFromCart = new JButton(" Delete ");
    JButton InputMethod = new JButton("Method");
    JLabel TotalMoney = new JLabel("Total Money: ");
    JTextField TotalMoneyField;
    String []header ={"Mã sản phẩm", "Số lượng", "Đơn giá"};
    DefaultTableModel tableModel = new DefaultTableModel();
    JTable table = new JTable() {
        private static final long serialVersionUID = 1L;
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JScrollPane jScrollPane = null;
    getLIBRARY getLIB = new getLIBRARY();
    String ma_gioHang = null;
    String ma_khachhang = null;
    String ma_gianhang = null;
    JButton QtyFix = new JButton("Qty Fix");
    JButton addDiscount = new JButton("Discount");
    double totalmn;
    String DS = null;
    String purchase = null;
    String shipping = null;
    String address = null;

    public void tableProcess(String userText) throws SQLException {
        tableModel.setColumnIdentifiers(header);
        table.setModel(tableModel);
        TableColumnModel tableColumnModel = table.getColumnModel();
        for(int i=0;i<3;i++)
        {
            tableColumnModel.getColumn(i).setPreferredWidth(100);
        }
        jScrollPane = new JScrollPane(table);
        ma_gioHang = getLIB.ma_GH(userText);
        ma_khachhang = getLIB.MA_KH(userText);
        ma_gianhang = getLIB.ma_GIANHANG(userText);
        tableSearch();
    }

    public void tableSearch() throws SQLException {
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        Connection conn = null;
        ResultSet rs;
        CallableStatement cs;
        conn= DriverManager.getConnection(URL);
        cs = conn.prepareCall("{call VIEW_GIOHANG(?)}");
        cs.setString(1, ma_gioHang);
        rs = cs.executeQuery();
        while(rs.next())
        {
            String row[] = new String[3];
            for(int i=0;i<3;i++)
            {
                row[i] = rs.getString(i+1);
            }
            tableModel.addRow(row);
        }
    }

    public void PanelProcess() throws SQLException {
        getLIBRARY get = new getLIBRARY();
        CartId = new JLabel("CartID: " + ma_gioHang);
        infoPanel.setBackground(Color.CYAN.brighter());
        infoPanel.setOpaque(true);
        CartId.setPreferredSize(new Dimension(100,41));
        Back.setPreferredSize(new Dimension(100,41));
        Purchase.setPreferredSize(new Dimension(100,41));
        InputMethod.setPreferredSize(new Dimension(100,41));
        infoPanel.add(CartId);
        infoPanel.add(Back);
        infoPanel.add(Purchase);
        infoPanel.add(InputMethod);
    }

    MyCart(String userText) throws SQLException{
        tableProcess(userText);
        PanelProcess();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    public void setLayoutManager()
    {
        container.setLayout(null);
    }

    public void setLocationAndSize() throws SQLException {
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        Connection conn = null;
        conn = DriverManager.getConnection(URL);
        CallableStatement cs;
        cs = conn.prepareCall("{call TOTAL_MONEY(?,?)}");
        cs.setString(1, ma_gioHang);
        cs.registerOutParameter(2, Types.FLOAT);
        cs.execute();
        totalmn = cs.getFloat(2);
        TotalMoneyField = new JTextField();
        TotalMoneyField.setText(String.valueOf(totalmn));
        TotalMoneyField.setEditable(false);
        infoPanel.setBounds(0,0,150,400);
        jScrollPane.setBounds(160,30,410,250);
        TotalMoney.setBounds(380,280, 80,30);
        TotalMoneyField.setBounds(460,280,110,30);
        addDiscount.setBounds(460,315,110,30);
        DeleteFromCart.setBounds(160, 280,130,30);
        QtyFix.setBounds(160,315,130,30);
    }

    public void addComponentsToContainer()
    {
        container.add(infoPanel);
        container.add(jScrollPane);
        container.add(TotalMoney);
        container.add(TotalMoneyField);
        container.add(DeleteFromCart);
        container.add(QtyFix);
        container.add(addDiscount);
    }
    public void addActionEvent()
    {
        DeleteFromCart.addActionListener(this);
        Back.addActionListener(this);
        Purchase.addActionListener(this);
        QtyFix.addActionListener(this);
        InputMethod.addActionListener(this);
        addDiscount.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        Connection conn = null;
        ResultSet rs;
        CallableStatement cs;
        int discount;
        int status = 0;
        if (actionEvent.getSource() == DeleteFromCart) {
            if (DS == null) {
                int selectedIndex = table.getSelectedRow();
                String pID = tableModel.getValueAt(selectedIndex, 0).toString();
                try {
                    conn = DriverManager.getConnection(URL);
                    cs = conn.prepareCall("{call DEL_PRODUCT(?,?)}");
                    cs.setString(1, ma_gioHang);
                    cs.setString(2, pID);
                    cs.execute();
                    cs.close();
                    JOptionPane.showMessageDialog(null, "Product Deleted");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    cs = conn.prepareCall("{call TOTAL_MONEY(?,?)}");
                    cs.setString(1, ma_gioHang);
                    cs.registerOutParameter(2, Types.INTEGER);
                    cs.execute();
                    int totalmn = cs.getInt(2);
                    TotalMoneyField.setText(String.valueOf(totalmn));
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                tableModel.setRowCount(0);
                tableModel.setColumnIdentifiers(header);
                table.setModel(tableModel);
                TableColumnModel tableColumnModel = table.getColumnModel();
                for (int i = 0; i < 3; i++) {
                    tableColumnModel.getColumn(i).setPreferredWidth(100);
                }
                try {
                    conn = DriverManager.getConnection(URL);
                    cs = conn.prepareCall("{call VIEW_GIOHANG(?)}");
                    cs.setString(1, ma_gioHang);
                    rs = cs.executeQuery();
                    while (rs.next()) {
                        String row[] = new String[3];
                        for (int i = 0; i < 3; i++) {
                            row[i] = rs.getString(i + 1);
                        }
                        tableModel.addRow(row);
                    }
                    cs = conn.prepareCall("{call TOTAL_MONEY(?,?)}");
                    cs.setString(1, ma_gioHang);
                    cs.registerOutParameter(2, Types.INTEGER);
                    cs.execute();
                    int totalmn = cs.getInt(2);
                    TotalMoneyField.setText(String.valueOf(totalmn));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "You cannot delete product after using Discount Code, please exit form cart and try again!", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        if(actionEvent.getSource() == Back)
        {
            this.setVisible(false);
            this.dispose();
        }
        if(actionEvent.getSource() == QtyFix) {
            if (DS == null) {
                int selectedIndex = table.getSelectedRow();
                String pID = tableModel.getValueAt(selectedIndex, 0).toString();
                String price = tableModel.getValueAt(selectedIndex, 2).toString();
                JPanel qtypanel = new JPanel();
                JLabel qtylb = new JLabel("Qty: ");
                JTextField qtytf = new JTextField(10);
                qtypanel.add(qtylb);
                qtypanel.add(qtytf);
                int result = JOptionPane.showConfirmDialog(null, qtypanel, "Input your product's Qty", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (Integer.parseInt(qtytf.getText()) <= 0) {
                        JOptionPane.showMessageDialog(null, "Invalid Qty, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            conn = DriverManager.getConnection(URL);
                            cs = conn.prepareCall("{call QTY(?,?,?,?,?)}");
                            cs.setString(1, ma_gioHang);
                            cs.setString(2, pID);
                            cs.setString(3, qtytf.getText());
                            cs.setString(4, price);
                            cs.registerOutParameter(5, Types.INTEGER);
                            cs.execute();
                            status = cs.getInt(5);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            cs = conn.prepareCall("{call TOTAL_MONEY(?,?)}");
                            cs.setString(1, ma_gioHang);
                            cs.registerOutParameter(2, Types.INTEGER);
                            cs.execute();
                            int totalmn = cs.getInt(2);
                            TotalMoneyField.setText(String.valueOf(totalmn));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        tableModel.setRowCount(0);
                        tableModel.setColumnIdentifiers(header);
                        table.setModel(tableModel);
                        TableColumnModel tableColumnModel = table.getColumnModel();
                        for (int i = 0; i < 3; i++) {
                            tableColumnModel.getColumn(i).setPreferredWidth(100);
                        }
                        try {
                            conn = DriverManager.getConnection(URL);
                            cs = conn.prepareCall("{call VIEW_GIOHANG(?)}");
                            cs.setString(1, ma_gioHang);
                            rs = cs.executeQuery();
                            while (rs.next()) {
                                String row[] = new String[3];
                                for (int i = 0; i < 3; i++) {
                                    row[i] = rs.getString(i + 1);
                                }
                                tableModel.addRow(row);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (status == 0) {
                            JOptionPane.showMessageDialog(null, "Fix qty failed, please try again!!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else if (status == 1) {
                            JOptionPane.showMessageDialog(null, "Fix qty successful");
                        }
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "You cannot fix product's qty after using Discount Code, please exit form cart and try again!", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        if(actionEvent.getSource()==InputMethod)
        {
            JTextField purchaseField = new JTextField(10);
            JTextField shippingField = new JTextField(10);
            JTextField addressField = new JTextField(10);
            JPanel myPanel = new JPanel();
            myPanel.add(Box.createVerticalStrut(15));
            myPanel.add(new JLabel("Purchase Method: "));
            myPanel.add(purchaseField);
            myPanel.add(new JLabel("Shipping Method: "));
            myPanel.add(shippingField);
            myPanel.add(new JLabel("Address: "));
            myPanel.add(addressField);

            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Please enter purchase method, shipping method and address", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                address = addressField.getText();
                purchase = purchaseField.getText();
                shipping = purchaseField.getText();
            }
        }
        if(actionEvent.getSource() == Purchase)
        {
            if(purchase == null || shipping ==null || address == null) {
                JOptionPane.showMessageDialog(null, "Please input method purchase, shipping, address", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    conn = DriverManager.getConnection(URL);
                    cs = conn.prepareCall("{call THANHTOAN(?,?,?,?,?,?)}");
                    cs.setString(1, ma_gioHang);
                    cs.setString(2, DS);
                    cs.setString(3, purchase);
                    cs.setString(4, shipping);
                    cs.setString(5, String.valueOf(totalmn));
                    cs.registerOutParameter(6, Types.VARCHAR);
                    cs.execute();
                    String HD_ID = cs.getString(6);
                    cs = conn.prepareCall("{call SALES_REQUEST(?,?,?,?,?)}");
                    cs.setString(1, ma_gioHang);
                    cs.setString(2, purchase);
                    cs.setString(3, shipping);
                    cs.setString(4, address);
                    cs.setString(5, ma_khachhang);
                    cs.execute();
                    if (HD_ID == "NULL") {
                        JOptionPane.showMessageDialog(null, "Purchase Failed", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Purchase Successful, Your Order Code is '" + HD_ID+"'");
                        try {
                            cs = conn.prepareCall("{call DEL_ALLPRODUCT(?)}");
                            cs.setString(1, ma_gioHang);
                            cs.execute();
                            tableModel.setRowCount(0);
                            tableModel.setColumnIdentifiers(header);
                            table.setModel(tableModel);
                            TableColumnModel tableColumnModel = table.getColumnModel();
                            for (int i = 0; i < 3; i++) {
                                tableColumnModel.getColumn(i).setPreferredWidth(100);
                            }
                            try {
                                conn = DriverManager.getConnection(URL);
                                cs = conn.prepareCall("{call VIEW_GIOHANG(?)}");
                                cs.setString(1, ma_gioHang);
                                rs = cs.executeQuery();
                                while (rs.next()) {
                                    String row[] = new String[3];
                                    for (int i = 0; i < 3; i++) {
                                        row[i] = rs.getString(i + 1);
                                    }
                                    tableModel.addRow(row);
                                }
                                cs = conn.prepareCall("{call TOTAL_MONEY(?,?)}");
                                cs.setString(1, ma_gioHang);
                                cs.registerOutParameter(2, Types.INTEGER);
                                cs.execute();
                                int totalmn = cs.getInt(2);
                                TotalMoneyField.setText(String.valueOf(totalmn));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(actionEvent.getSource() == addDiscount)
        {
            if(DS != null)
            {
                JOptionPane.showMessageDialog(null, "Already Discounted!!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            else
                if(DS == null) {
                    JPanel panel = new JPanel();
                    JTextField discountTF = new JTextField(8);
                    panel.add(new JLabel("Input Discount Code: "));
                    panel.add(discountTF);
                    int result = JOptionPane.showConfirmDialog(null, panel, "Discount Code", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        DS = discountTF.getText();
                        try {
                            conn = DriverManager.getConnection(URL);
                            cs = conn.prepareCall("{call DISCOUNT(?,?)}");
                            cs.setString(1, DS);
                            cs.registerOutParameter(2, Types.INTEGER);
                            cs.execute();
                            discount = cs.getInt(2);
                            if (discount == 0) {
                                JOptionPane.showMessageDialog(null, "Discount code not available or expire", "ERROR", JOptionPane.ERROR_MESSAGE);
                                DS = null;
                            } else {
                                totalmn -= (totalmn * discount) / 100;
                                TotalMoneyField.setText(String.valueOf(totalmn));
                                JOptionPane.showMessageDialog(null, "Discount code approved");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
}
