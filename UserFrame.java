import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.*;
import static javax.swing.JTable.*;

public class UserFrame extends JFrame implements  ActionListener{
    Container container = getContentPane();
    JPanel infoPanel = new JPanel();
    JLabel Name ;
    JLabel ID ;
    JLabel searchLb = new JLabel("SEARCH:");
    String cbb[] = {"Name", "ProductID", "Category"};
    JComboBox cbbSearchOption = new JComboBox(cbb);
    JButton logOUT = new JButton("Log Out");
    JButton exit = new JButton("Exit");
    JButton Search = new JButton("Search");
    JButton Reset = new JButton("Reset");
    JButton AddToCart = new JButton(" + Add To Cart");
    JButton MyCart = new JButton("My Cart");
    JButton pHistory = new JButton("Purchase History");
    JButton sHistory = new JButton("Search History");
    JButton AdminView = new JButton("Admin View");
    JTextField searchField = new JTextField();
    String []header = {"Mã sản phẩm", "Tên sản phẩm", "Giá sản phẩm", "SL", "Danh mục", "Mô tả"};
    DefaultTableModel tblModel = new DefaultTableModel();
    JTable table = new JTable() {
        private static final long serialVersionUID = 1L;
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JScrollPane jScrollPane = null;
    Connection conn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    String userTxt = new String();
    String ma_gh = new String();
    String kh_ID = new String();
    public void tableProcess()
    {
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        TableColumnModel tableColumnModel = table.getColumnModel();
        for(int i=0;i<6;i++)
        {
            tableColumnModel.getColumn(i).setPreferredWidth(150);
        }
        jScrollPane = new JScrollPane(table);
        tableSearch();
    }

    public void tableSearch()
    {
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        try {
            conn = DriverManager.getConnection(URL);
            cs = conn.prepareCall("{call SEARCH()}");
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

    UserFrame(String userText) throws SQLException {
    PanelProcess(userText);
    tableProcess();
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();
    }

    public void PanelProcess(String userText) throws SQLException {
        getLIBRARY get = new getLIBRARY();
        userTxt = userText;
        String nameLB = get.TEN_KH(userText);
        kh_ID = get.MA_KH(userText);
        ma_gh = get.ma_GH(userText);
        Name = new JLabel("Name: "+nameLB);
        ID = new JLabel("UserID: "+ kh_ID);
        infoPanel.add(Name);
        infoPanel.add(ID);
        infoPanel.setBackground(Color.CYAN.brighter());
        infoPanel.setOpaque(true);
        MyCart.setPreferredSize(new Dimension(140,41));
        pHistory.setPreferredSize(new Dimension(140,41));
        sHistory.setPreferredSize(new Dimension(140,41));
        exit.setPreferredSize(new Dimension(140,41));
        logOUT.setPreferredSize(new Dimension(140,41));
        infoPanel.add(MyCart, BorderLayout.CENTER);
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        conn = DriverManager.getConnection(URL);
        cs = conn.prepareCall("{call CHECKSHOP(?,?)}");
        cs.setString(1, userText);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.execute();
        int status = cs.getInt(2);
        if(status == 0)
        {
        }
        else if(status ==1)
        {
            infoPanel.add(AdminView);
            AdminView.setPreferredSize(new Dimension(140,41));

        }
        infoPanel.add(pHistory, BorderLayout.CENTER);
        infoPanel.add(sHistory, BorderLayout.CENTER);
        infoPanel.add(exit, BorderLayout.CENTER);
        infoPanel.add(logOUT, BorderLayout.LINE_END);
    }

    public void setLayoutManager()
    {
        container.setLayout(null);
    }

    public void setLocationAndSize()
    {
        jScrollPane.setBounds(200,80,550,320);
        infoPanel.setBounds(0,0,150,500);
        searchField.setBounds(250, 10,500,30);
        Search.setBounds(550, 40, 100,30);
        Reset.setBounds(650,40,100,30);
        AddToCart.setBounds(630 , 410, 120,30);
        searchLb.setBounds(195,3,80,50);
        cbbSearchOption.setBounds(450,40,100,30);
    }
    public void addComponentsToContainer()
    {
        container.add(jScrollPane);
        container.add(infoPanel);
        container.add(Search);
        container.add(Reset);
        container.add(AddToCart);
        container.add(searchField);
        container.add(searchLb);
        container.add(cbbSearchOption);
    }
    public void addActionEvent()
    {
        cbbSearchOption.addActionListener(this);
        Search.addActionListener(this);
        Reset.addActionListener(this);
        AddToCart.addActionListener(this);
        cbbSearchOption.addActionListener(this);
        pHistory.addActionListener(this);
        sHistory.addActionListener(this);
        MyCart.addActionListener(this);
        logOUT.addActionListener(this);
        exit.addActionListener(this);
        AdminView.addActionListener(this);

    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Connection conn = null;
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        CallableStatement cs = null;
        if(actionEvent.getSource() == exit)
        {
            int i = JOptionPane.showConfirmDialog(null, "Would you like to Exit?","Exit", JOptionPane.YES_NO_OPTION);
            if(i == JOptionPane.YES_OPTION)
            {
                this.setVisible(false);
                this.dispose();
            }
        }
        if(actionEvent.getSource() == logOUT)
        {
            int i = JOptionPane.showConfirmDialog(null, "Would you like to Log out?","Exit", JOptionPane.YES_NO_OPTION);
            if(i == JOptionPane.YES_OPTION)
            {
                this.setVisible(false);
                this.dispose();
                loginFrame lg = new loginFrame();
                lg.setTitle("Sales Management");
                lg.setVisible(true);
                lg.setBounds(10,10,370,600);
                lg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                lg.setResizable(false);
            }
        }
        if(actionEvent.getSource() == Reset)
        {
            searchField.setText("");
        }
        if(actionEvent.getSource() == Search)
        {
            String searchText = searchField.getText();
            String danhmuc = new String();
            if(searchText.isEmpty())
            {
                tableSearch();
            }
            else
            {
                String call = new String();
                Integer k = (Integer)cbbSearchOption.getSelectedIndex();
                switch(k)
                {
                    case 0: call = "{call FULLTEXT_INDEX_NAMESEARCH(?)}";
                    danhmuc = "NAME_SEARCH";
                    break;
                    case 1: call = "{call SEARCH_ID(?)}";
                    danhmuc = "PRODUCT_ID_SEARCH";
                    break;
                    case 2: call = "{call SEARCH_DANHMUC(?)}";
                    danhmuc = "CATEGORY_SEARCH";
                    break;
                }
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
                    cs = conn.prepareCall(call);
                    cs.setString(1, searchText);
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
            try {
                cs = conn.prepareCall("{call SEARCH_HISTORY(?, ?, ?)}");
                cs.setString(1, kh_ID);
                cs.setString(2, searchText);
                cs.setString(3, danhmuc);
                cs.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(actionEvent.getSource()==MyCart)
        {
            try {
                MyCart myCart = new MyCart(userTxt);
                myCart.setTitle("My Cart");
                myCart.setVisible(true);
                myCart.setBounds(10,10,600,400);
                myCart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(actionEvent.getSource() == AddToCart)
        {
            int selectedIndex = table.getSelectedRow();
            String pID = tblModel.getValueAt(selectedIndex, 0).toString();
            String SL = JOptionPane.showInputDialog("Qty?");
            if(Integer.parseInt(SL)<=0)
            {
                JOptionPane.showMessageDialog(null, "Invalid Qty", "ERRPR", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    conn = DriverManager.getConnection(URL);
                    cs = conn.prepareCall("{call ADD_PRODUCT(?,?,?)}");
                    cs.setString(1, ma_gh);
                    cs.setString(2, pID);
                    cs.setString(3, SL);
                    cs.execute();
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(actionEvent.getSource() == AdminView)
        {
            try {
                AdminFrame myShopFrame = new AdminFrame(userTxt);
                myShopFrame.setTitle("My Shop");
                myShopFrame.setVisible(true);
                myShopFrame.setBounds(10,10,800,500);
                myShopFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(actionEvent.getSource() == pHistory)
        {
            JFrame detailFrame = new JFrame();
            detailFrame.setTitle("Purchase History");
            JTable tb = new JTable(){
                private static final long serialVersionUID = 1L;
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel tbdf = new DefaultTableModel();
            JScrollPane jScrollPaneDT = null;
            String[] hd = {"Mã sản phẩm", "Đơn giá", "Ngày mua", "Số lượng"};
            tbdf.setColumnIdentifiers(hd);
            tb.setModel(tbdf);
            TableColumnModel tbColumnModel = tb.getColumnModel();
            for (int i=0;i<4;i++)
            {
                tbColumnModel.getColumn(i).setPreferredWidth(100);
            }
            jScrollPaneDT = new JScrollPane(tb);
            try {
                conn = DriverManager.getConnection(URL);
                cs =conn.prepareCall("{call PURCHASE_HISTORY(?)}");
                cs.setString(1, kh_ID);
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
        if(actionEvent.getSource()== sHistory)
        {

            JFrame detailFrame = new JFrame();
            detailFrame.setTitle("Search History");
            JTable tb = new JTable(){
                private static final long serialVersionUID = 1L;
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel tbdf = new DefaultTableModel();
            JScrollPane jScrollPaneDT = null;
            String[] hd = {"Từ khóa", "Thời gian", "Danh mục"};
            tbdf.setColumnIdentifiers(hd);
            tb.setModel(tbdf);
            TableColumnModel tbColumnModel = tb.getColumnModel();
            for (int i=0;i<3;i++)
            {
                tbColumnModel.getColumn(i).setPreferredWidth(100);
            }
            jScrollPaneDT = new JScrollPane(tb);
            try {
                conn = DriverManager.getConnection(URL);
                cs =conn.prepareCall("{call SHOW_SEARCH_HISTORY(?)}");
                cs.setString(1, kh_ID);
                rs = cs.executeQuery();
                while (rs.next())
                {
                    String row[] = new String[3];
                    for(int i= 0;i<3;i++)
                    {
                        row[i] = rs.getString(i+1);
                    }
                    tbdf.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            jScrollPaneDT.setBounds(10,10,450,250);
            detailFrame.add(jScrollPaneDT);
            detailFrame.setBounds(10,10,450,250);
            detailFrame.setVisible(true);
            detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }
}

