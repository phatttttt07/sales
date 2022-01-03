

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MyOrderFrame  extends JFrame implements ActionListener {
    Container container = getContentPane();
    JPanel infoPane = new JPanel();
    JLabel shopID;
    JLabel MyShopLabel = new JLabel("My Shop");
    JButton Back = new JButton("Back");
    JButton Update = new JButton("Update");
    JButton Approved = new JButton("Approved");
    JButton Details = new JButton("Order Detail");
    String[] header = {"Mã đơn hàng", "Mã khách hàng", "Ngày lập đơn", "PT Thanh toán", "PT Giao hàng", "Địa chỉ"};
    DefaultTableModel tableModel = new DefaultTableModel();
    JTable table = new JTable() {
        private static final long serialVersionUID = 1L;

        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JScrollPane jScrollPane = null;
    String URL = "jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
    String ma_gianhang = null;
    Connection conn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    getLIBRARY getLIB = new getLIBRARY();

    MyOrderFrame(String userText) throws SQLException {
        tableProcess(userText);
        PanelProcess();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    public void tableSearch() throws SQLException {
        conn = DriverManager.getConnection(URL);
        cs = conn.prepareCall("{call MY_ORDER(?)}");
        cs.setString(1, ma_gianhang);
        rs = cs.executeQuery();
        while (rs.next()) {
            String row[] = new String[6];
            for (int i = 0; i < 6; i++) {
                row[i] = rs.getString(i + 1);
            }
            tableModel.addRow(row);
        }

    }

    public void tableProcess(String userText) throws SQLException {
        tableModel.setColumnIdentifiers(header);
        table.setModel(tableModel);
        TableColumnModel tableColumnModel = table.getColumnModel();
        for (int i = 0; i < 6; i++) {
            tableColumnModel.getColumn(i).setPreferredWidth(100);
        }
        jScrollPane = new JScrollPane(table);
        ma_gianhang = getLIB.ma_GIANHANG(userText);
        tableSearch();
    }

    public void PanelProcess() {
        infoPane.setBackground(Color.CYAN.brighter());
        infoPane.setOpaque(true);
        shopID = new JLabel("ShopID: " + ma_gianhang);
        MyShopLabel.setFont(MyShopLabel.getFont().deriveFont(20.0f));
        shopID.setPreferredSize(new Dimension(140, 41));
        Back.setPreferredSize(new Dimension(140, 41));
        infoPane.add(MyShopLabel);
        infoPane.add(shopID);
        infoPane.add(Back);
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        jScrollPane.setBounds(200, 50, 550, 320);
        infoPane.setBounds(0, 0, 150, 500);
        Update.setBounds(200, 380, 145, 30);
        Approved.setBounds(605, 380, 145, 30);
        Details.setBounds(455, 380, 145, 30);
    }

    public void addComponentsToContainer() {
        container.add(infoPane);
        container.add(jScrollPane);
        container.add(Update);
        container.add(Approved);
        container.add(Details);
    }

    public void addActionEvent() {
        Back.addActionListener(this);
        Update.addActionListener(this);
        Approved.addActionListener(this);
        Details.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == Back) {
            this.setVisible(false);
            this.dispose();
        }
        if (actionEvent.getSource() == Details) {
            JFrame detailFrame = new JFrame();
            detailFrame.setTitle("Order Details");
            JTable tb = new JTable(){
                private static final long serialVersionUID = 1L;
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel tbdf = new DefaultTableModel();
            JScrollPane jScrollPaneDT = null;
            String[] hd = {"Mã đơn hàng", "Mã sản phẩm", "Số lượng", "Đơn giá"};
            tbdf.setColumnIdentifiers(hd);
            tb.setModel(tbdf);
            TableColumnModel tbColumnModel = tb.getColumnModel();
            for (int i=0;i<4;i++)
            {
                tbColumnModel.getColumn(i).setPreferredWidth(100);
            }
            jScrollPaneDT = new JScrollPane(tb);
            int selectedIndex = table.getSelectedRow();
            String ghID = tableModel.getValueAt(selectedIndex, 0).toString();
            try {
                conn = DriverManager.getConnection(URL);
                cs =conn.prepareCall("{call DETAILS_ORDER(?)}");
                cs.setString(1, ghID);
                rs = cs.executeQuery();
                while (rs.next())
                {
                    String row[] = new String[4];
                    for(int i= 0;i<4;i++)
                    {
                        row[i] = rs.getString(i+1);
                    }
                    tbdf.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            jScrollPaneDT.setBounds(10,10,410,250);
            detailFrame.add(jScrollPaneDT);
            detailFrame.setBounds(10,10,410,250);
            detailFrame.setVisible(true);
            detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
        if (actionEvent.getSource() == Update) {
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(header);
            table.setModel(tableModel);
            TableColumnModel tableColumnModel = table.getColumnModel();
            for (int i = 0; i < 6; i++) {
                tableColumnModel.getColumn(i).setPreferredWidth(100);
            }
            try {
                conn = DriverManager.getConnection(URL);
                cs = conn.prepareCall("{call MY_ORDER(?)}");
                cs.setString(1, ma_gianhang);
                rs = cs.executeQuery();
                while (rs.next()) {
                    String row[] = new String[6];
                    for (int i = 0; i < 6; i++) {
                        row[i] = rs.getString(i + 1);
                    }
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(actionEvent.getSource()==Approved)
        {
            int selectedIndex = table.getSelectedRow();
            String dhID = tableModel.getValueAt(selectedIndex, 0).toString();
            try {
                conn = DriverManager.getConnection(URL);
                cs = conn.prepareCall("{call APPROVED(?)}");
                cs.setString(1, dhID);
                cs.execute();
                JOptionPane.showMessageDialog(null, "Order Approved");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Order Approve Error","Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
