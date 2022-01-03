import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminFrame extends JFrame implements ActionListener {
    Container container = getContentPane();
    JPanel infoPanel = new JPanel();
    JLabel shopID;
    JLabel AdminLabel = new JLabel("Admin");
    JButton AddProduct = new JButton("+Add Product");
    JButton DelProduct = new JButton("-Delele Product");
    JButton OrderList = new JButton("Order List");
    JButton Back = new JButton("Back");
    JButton Update = new JButton("Update");
    String []header = {"Mã sản phẩm", "Tên sản phẩm", "Giá sản phẩm", "SL", "Danh mục", "Mô tả"};
    DefaultTableModel tblModel = new DefaultTableModel();
    JTable table = new JTable() {
        private static final long serialVersionUID = 1L;
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JScrollPane jScrollPane = null;
    String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
    String usertxt = null;
    Connection conn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    String ma_gh = new String();
    getLIBRARY get = new getLIBRARY();
    AdminFrame(String userText) throws SQLException {
        tableSearch(userText);
        PanelProcess(userText);
        setLayOutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    public void setLayOutManager()
    {
        container.setLayout(null);
    }

    public void tableSearch(String userText) throws SQLException {
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        TableColumnModel tableColumnModel = table.getColumnModel();
        for(int i=0;i<6;i++)
        {
            tableColumnModel.getColumn(i).setPreferredWidth(150);
        }
        jScrollPane = new JScrollPane(table);
        ma_gh = get.ma_GIANHANG(userText);
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        try {
            conn = DriverManager.getConnection(URL);
            cs = conn.prepareCall("{call MYSHOP(?)}");
            cs.setString(1, ma_gh);
            rs = cs.executeQuery();
            while (rs.next())
            {
                String row[] = new String[6];
                for(int i=0;i<6;i++)
                {
                    row[i] = rs.getString(i+1);
                }
                tblModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        usertxt = userText;
    }

    public void PanelProcess(String userText)
    {
        shopID = new JLabel("ShopID: "+ma_gh);
        infoPanel.setBackground(Color.CYAN.brighter());
        infoPanel.setOpaque(true);
        AdminLabel.setFont(AdminLabel.getFont().deriveFont(20.0f));
        OrderList.setPreferredSize(new Dimension(140, 41));
        Back.setPreferredSize(new Dimension(140,41));
        infoPanel.add(AdminLabel);
        infoPanel.add(shopID);
        infoPanel.add(OrderList);
        infoPanel.add(Back);
    }
    public void addComponentsToContainer()
    {
        container.add(infoPanel);
        container.add(jScrollPane);
        container.add(AddProduct);
        container.add(DelProduct);
        container.add(Update);
    }
    public void setLocationAndSize()
    {
        jScrollPane.setBounds(200,50,550,320);
        infoPanel.setBounds(0,0,150,500);
        AddProduct.setBounds(455,380,145,30);
        DelProduct.setBounds(605,380,145,30);
        Update.setBounds(200,380,145,30);
    }
    public void addActionEvent(){
        AddProduct.addActionListener(this);
        DelProduct.addActionListener(this);
        OrderList.addActionListener(this);
        DelProduct.addActionListener(this);
        Back.addActionListener(this);
        Update.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == AddProduct)
        {
            AddProductFrame addProductFrame = new AddProductFrame(ma_gh);
            addProductFrame.setTitle("Add Product");
            addProductFrame.setVisible(true);
            addProductFrame.setBounds(10,10,300,300);
            addProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addProductFrame.setLocationRelativeTo(this);
        }
        if(actionEvent.getSource() == Update)
        {
            tblModel.setRowCount(0);
            tblModel.setColumnIdentifiers(header);
            table.setModel(tblModel);
            TableColumnModel tableColumnModel = table.getColumnModel();
            for(int i=0;i<6;i++)
            {
                tableColumnModel.getColumn(i).setPreferredWidth(150);
            }
            try {
                conn = DriverManager.getConnection(URL);
                cs = conn.prepareCall("{call MYSHOP(?)}");
                cs.setString(1, ma_gh);
                rs = cs.executeQuery();
                while (rs.next())
                {
                    String row[] = new String[6];
                    for(int i=0;i<6;i++)
                    {
                        row[i] = rs.getString(i+1);
                    }
                    tblModel.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(actionEvent.getSource() == DelProduct) {
            int selectedIndex = table.getSelectedRow();
            String pID = tblModel.getValueAt(selectedIndex, 0).toString();
            try {
                conn = DriverManager.getConnection(URL);
                cs = conn.prepareCall("{call DELETE_PRODUCT(?)}");
                cs.setString(1, pID);
                cs.execute();
                JOptionPane.showMessageDialog(this, "Delete product successful, pleasse update");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Delete product failed, try again");
            }
        }
        if(actionEvent.getSource() == OrderList)
        {
            try {
                MyOrderFrame myOrderFrame = new MyOrderFrame(usertxt);
                myOrderFrame.setTitle("Order List");
                myOrderFrame.setVisible(true);
                myOrderFrame.setBounds(10,10,800,500);
                myOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(actionEvent.getSource() == Back)
        {
            this.setVisible(false);
            this.dispose();
        }
    }
}
