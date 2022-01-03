import java.awt.*;
import java.sql.*;

public class getLIBRARY{

    public String MA_KH(String ma_TK) throws SQLException {
        String ma_KH = new String();
        Connection conn = null;
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        conn = DriverManager.getConnection(URL);
        CallableStatement cs = null;
        cs = conn.prepareCall("{call GET_MAKH(?,?)}");
        cs.setString(1, ma_TK);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.execute();
        ma_KH = cs.getString(2);
        return ma_KH;
    }
    public String TEN_KH(String ma_TK) throws  SQLException{
        String ten_KH;
        Connection conn = null;
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        CallableStatement cs = null;
        conn = DriverManager.getConnection(URL);
        cs = conn.prepareCall("{call GET_TENKH(?,?)}");
        cs.setString(1, ma_TK);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.execute();
        ten_KH = cs.getString(2);
        return ten_KH;
    }
    public String ma_GH(String ma_TK) throws SQLException{
        String ma_GH = new String();
        Connection conn = null;
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        conn = DriverManager.getConnection(URL);
        CallableStatement cs = null;
        cs = conn.prepareCall("{call GET_MAGIOHANG(?,?)}");
        cs.setString(1, ma_TK);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.execute();
        ma_GH = cs.getString(2);
        return ma_GH;
    }
    public String ma_GIANHANG(String ma_TK) throws SQLException{
        String ma_GIANHANG = new String();
        Connection conn = null;
        String URL ="jdbc:sqlserver://localhost\\MSSQLSERVER01:1433;database=SALES;integratedSecurity=true;";
        conn = DriverManager.getConnection(URL);
        CallableStatement cs = null;
        cs = conn.prepareCall("{call MA_GIANHANG(?,?)}");
        cs.setString(1, ma_TK);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.execute();
        ma_GIANHANG = cs.getString(2);
        return ma_GIANHANG;
    }
}
