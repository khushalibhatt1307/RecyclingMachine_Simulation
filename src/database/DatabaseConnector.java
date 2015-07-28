package database;

import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import recycle.ItemType;
import recycle.RecycleMachine;

/*
 * Author - Khushali Bhatt
 * CLASS - class to connect to sql server database and fetch/store data
 */
public class DatabaseConnector
{
    private static final String DATABASEURL = "jdbc:sqlserver://Khushali-PC;databaseName=EcoReSystem;integratedSecurity=true";
    private static final String CLASSPATH = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static DatabaseConnector singletonDBConnector = null;
    private Connection conn = null;

    private DatabaseConnector() {
    }

    public static DatabaseConnector getInstance() {
        if (singletonDBConnector == null) {
            singletonDBConnector = new DatabaseConnector();
        }
        return singletonDBConnector;
    }

    public Connection getConnection()
        throws SQLException, ClassNotFoundException
    {
        Class.forName(CLASSPATH);
        this.conn = DriverManager.getConnection(DATABASEURL);
        return conn;
    }

    // get all statistics from now to specific past date 
    public HashMap<String, Double> getStatisticsFromNowToPastDatesByMachineId(int machineId, int daysBefore)
        throws SQLException, ClassNotFoundException
    {
        HashMap<String, Double> data = new HashMap<String, Double>();
        PreparedStatement pstmt = null;
        try {
            pstmt = getConnection().prepareStatement("{call dbo.getStatisticsFromNowToPastDatesByMachineId(?,?)}");
            pstmt.setInt(1, machineId);
            pstmt.setInt(2, daysBefore);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                data.put("totalWeightOfItemsEmptied", new Double(rs.getDouble("totalWeightOfItemsEmptied")));
                data.put("totalCouponAmountRedeemed", new Double(rs.getDouble("totalCouponAmountRedeemed")));
                data.put("totalCashAmountRedeemed", new Double(rs.getDouble("totalCashAmountRedeemed")));
                data.put("totalCashCouponAmountRedeemed", new Double(rs.getDouble("totalCashCouponAmountRedeemed")));
                data.put("numberOfTimesItemsEmptied", new Double(rs.getDouble("numberOfTimesItemsEmptied")));
            }
            rs.close();
            pstmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
        return data;
    }

    /*
     * get all item types by machine id
     */
    public ArrayList<ItemType> getItemTypesByMachineId(int machineId)
        throws SQLException, ClassNotFoundException
    {
        ArrayList<ItemType> types = new ArrayList<ItemType>();
        PreparedStatement pstmt = null;
        try {
            pstmt = getConnection().prepareStatement("{call dbo.getItemTypesByMachineId(?)}");
            pstmt.setInt(1, machineId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ItemType type = new ItemType(rs.getString("itemTypeName"), rs.getDouble("itemTypePricePerLb"));
                types.add(type);
            }
            rs.close();
            pstmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
        return types;

    }

    public String getItemPrice(String itemType)

        throws SQLException, ClassNotFoundException
    {
        String price = "";
        Statement st = null;
        try {

            st = getConnection().createStatement();
            String sql = "select distinct itemTypePricePerLb from  RecycleMachineTypes where itemTypeName='" +
                    itemType + "'";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                price = rs.getString(1);

            }
            rs.close();
            st.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (st != null) {
                st.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
        return price;
    }

    public void addItemType(int macid, String itemtype, double price)

        throws SQLException, ClassNotFoundException
    {
        Statement st = null;
        try {

            st = getConnection().createStatement();
            String sql = "insert into RecycleMachineTypes (machineId, itemTypeName, itemTypePricePerLb) values ('" +
                    macid + "'" + ",'" + itemtype + "'" + ",'" + price + "'" + ")";
            int i = st.executeUpdate(sql);
            st.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (st != null) {
                st.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public void deleteItemType(int macid, String itemtype)

        throws SQLException, ClassNotFoundException
    {
        Statement st = null;
        try {

            st = getConnection().createStatement();
            String sql = "delete from  RecycleMachineTypes where machineId ='" + macid + "'" + " and itemTypeName='" +
                    itemtype + "'";
            int i = st.executeUpdate(sql);

            st.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (st != null) {
                st.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public void updatePrice(int macid, String itemtype, double price)

        throws SQLException, ClassNotFoundException
    {
        Statement st = null;
        try {

            st = getConnection().createStatement();
            String sql = "update RecycleMachineTypes  set itemTypePricePerLb ='" + price + "'" + " where machineId='" +
                    macid + "'" + " and " + "itemTypeName='" + itemtype + "'";
            int i = st.executeUpdate(sql);
            st.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (st != null) {
                st.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public void insertItemTypeByMachineId(int machineId,
                                          String itemTypeName,
                                          double itemTypePricePerLb)
        throws SQLException, ClassNotFoundException
    {
        CallableStatement cstmt = null;
        try {
            cstmt = getConnection().prepareCall("{call dbo.insertItemTypeByMachineId(?,?,?)}");
            cstmt.setInt(1, machineId);
            cstmt.setString(2, itemTypeName);
            cstmt.setDouble(3, itemTypePricePerLb);
            cstmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cstmt != null) {
                cstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public void insertRecycleMachine(int machineId,
                                     String location,
                                     double capacity,
                                     double totalMoneyRemaining,
                                     double totalWeight,
                                     boolean isOperational)
        throws SQLException, ClassNotFoundException
    {
        CallableStatement cstmt = null;
        try {
            cstmt = getConnection().prepareCall("{call dbo.insertRecycleMachine(?,?,?,?,?,?)}");
            cstmt.setInt(1, machineId);
            cstmt.setString(2, location);
            cstmt.setDouble(3, capacity);
            cstmt.setDouble(4, totalMoneyRemaining);
            cstmt.setDouble(5, totalWeight);
            cstmt.setBoolean(6, isOperational);
            cstmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cstmt != null) {
                cstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public void updateRecycleMachineItemType(int machineId,
                                             String itemTypeName,
                                             double itemTypePricePerLb)
        throws SQLException, ClassNotFoundException
    {
        CallableStatement cstmt = null;
        try {
            cstmt = getConnection().prepareCall("{call dbo.updateRecycleMachineItemType(?,?,?)}");
            cstmt.setInt(1, machineId);
            cstmt.setString(2, itemTypeName);
            cstmt.setDouble(3, itemTypePricePerLb);
            cstmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cstmt != null) {
                cstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public void deleteRecycleMachine(int machineId)
        throws SQLException, ClassNotFoundException
    {
        CallableStatement cstmt = null;
        try {
            cstmt = getConnection().prepareCall("{call dbo.deleteRecycleMachine(?)}");
            cstmt.setInt(1, machineId);
            cstmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cstmt != null) {
                cstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public void deleteRecycleMachineItemType(int machineId, String itemTypeName)
        throws SQLException, ClassNotFoundException
    {
        CallableStatement cstmt = null;
        try {
            cstmt = getConnection().prepareCall("{call dbo.deleteRecycleMachineItemType(?,?)}");
            cstmt.setInt(1, machineId);
            cstmt.setString(2, itemTypeName);
            cstmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cstmt != null) {
                cstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public void insertRecycleMachineStatistics(int id,
                                               int numberOfItems,
                                               double totalWeight,
                                               double totalCouponAmountRedeemed,
                                               double totalCashAmountRedeemed)
        throws SQLException, ClassNotFoundException
    {
        CallableStatement cstmt = null;
        try {
            cstmt = getConnection().prepareCall("{call dbo.insertRecycleMachineStatistics(?,?,?,?,?)}");
            cstmt.setInt(1, id);
            cstmt.setInt(2, numberOfItems);
            cstmt.setDouble(3, totalWeight);
            cstmt.setDouble(4, totalCouponAmountRedeemed);
            cstmt.setDouble(5, totalCashAmountRedeemed);
            cstmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cstmt != null) {
                cstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
    }

    public ArrayList<RecycleMachine> getAllRecycleMachines()
        throws SQLException, ClassNotFoundException
    {
        ArrayList<RecycleMachine> recycleMachines = new ArrayList<RecycleMachine>();
        PreparedStatement pstmt = null;
        try {
            pstmt = getConnection().prepareStatement("{call dbo.getAllRecycleMachines()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RecycleMachine recycleMachine =
                        new RecycleMachine(rs.getInt("machineId"),
                                           rs.getString("location"),
                                           rs.getDouble("capacity"),
                                           rs.getDouble("totalMoneyRemaining"),
                                           rs.getDouble("totalWeight"),
                                           rs.getBoolean("isOperational"));
                recycleMachines.add(recycleMachine);
            }
            rs.close();
            pstmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        }
        return recycleMachines;
    }

}
