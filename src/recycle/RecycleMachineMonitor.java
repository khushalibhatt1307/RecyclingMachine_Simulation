package recycle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import database.DatabaseConnector;

/*
 * Author - Khushali Bhatt
 * Class that will represent Recycle Machine Monitor or Admin
 */
public class RecycleMachineMonitor
{

    // this will store all RCM that RecylcingMachineMonitor manages
    HashMap<Integer, RecycleMachine> recycleMachinesMap;

    public RecycleMachineMonitor()
    {
        this.recycleMachinesMap = new HashMap<Integer, RecycleMachine>();
    }

    public void AddRecycleMachine(int id,
                                  String location,
                                  int capacity,
                                  double totalBeginningMoney,
                                  double totalWeight,
                                  boolean isOperational)
    {
        RecycleMachine recycleMachine = new RecycleMachine(id, location, capacity, totalBeginningMoney, totalWeight,
                                                           false);
        recycleMachinesMap.put(new Integer(id), recycleMachine);

        try {
            DatabaseConnector.getInstance().insertRecycleMachine(id, location, capacity, totalBeginningMoney,
                                                                 totalWeight, isOperational);
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
    }

    public RecycleMachine RemoveRecycleMachine(int id)
    {
        try {
            DatabaseConnector.getInstance().deleteRecycleMachine(id);
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
        return recycleMachinesMap.remove(new Integer(id));
    }

    public OperationalStatus isRecycleMachineOperational(int id)
    {
        RecycleMachine recyleMachine = recycleMachinesMap.get(new Integer(id));
        return recyleMachine.getOperational();
    }

    public void addRecycleItemType(int id, ItemType type)
    { 
        RecycleMachine recyleMachine = recycleMachinesMap.get(new Integer(id));
        recyleMachine.addSupportedItemType(type);

        try {
            DatabaseConnector.getInstance().insertItemTypeByMachineId(id, type.getName(), type.getPricePerLb());
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
    }

    public void addnewItem(int id, String type, double d)
    {

        try {
            DatabaseConnector.getInstance().addItemType(id, type, d);
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
    }

    public void updatePriceNew(int id, String type, double d)
    {

        try {
            DatabaseConnector.getInstance().updatePrice(id, type, d);
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
    }

    public void removeRecycleItemType(int id, ItemType type)
    {
        Integer machineId = new Integer(id);
        RecycleMachine recyleMachine = recycleMachinesMap.get(machineId);
        recyleMachine.removeSupportedItemType(type);

        try {
            DatabaseConnector.getInstance().deleteRecycleMachineItemType(id, type.getName());
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
    }

    public void deleteItemType(int id, String type)
    {

        try {
            DatabaseConnector.getInstance().deleteItemType(id, type);
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
    }

    public double checkTotalMoneyRemaining(int id)
    {
        RecycleMachine recyleMachine = recycleMachinesMap.get(new Integer(id));
        return recyleMachine.getTotalMoneyRemaining();
    }

    // insert stats when recycle machine is emptied
    public void emptyRecycleMachine(int id)
    {
        RecycleMachine recyleMachine = recycleMachinesMap.get(new Integer(id));
        try {
            DatabaseConnector.getInstance()
                    .insertRecycleMachineStatistics(id,
                                                    recyleMachine.getNumberOfItems(),
                                                    recyleMachine.getTotalWeight(),
                                                    recyleMachine.getTotalCouponAmountRedeemed(),
                                                    recyleMachine.getTotalCashAmountRedeemed());
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
        recyleMachine.emptyItems();

    }

    public void changeItemTypePrice(int id, String typeName, double price)
    {
        RecycleMachine recyleMachine = recycleMachinesMap.get(new Integer(id));
        ItemType type = recyleMachine.getSupportedItemTypeByName(typeName);
        type.setPricePerLb(price);

        try {
            DatabaseConnector.getInstance().updateRecycleMachineItemType(id, typeName, price);
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
    }

    public double getCurrentCapacity(int id)
    {
        RecycleMachine recyleMachine = recycleMachinesMap.get(new Integer(id));
        return recyleMachine.capacity();
    }

    public double getAvailableCapacity(int id)
    {
        RecycleMachine recyleMachine = recycleMachinesMap.get(new Integer(id));
        return recyleMachine.getAvailableCapacity();
    }

    public ArrayList<ItemType> getItemTypesByMachineId(int machineId)
    {
        ArrayList<ItemType> itemTypes = null;

        try {
            itemTypes = DatabaseConnector.getInstance().getItemTypesByMachineId(machineId);
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
        return itemTypes;
    }

    public ArrayList<RecycleMachine> getAllRecycleMachines()
    {
        ArrayList<RecycleMachine> recyclemachines = null;

        try {
            recyclemachines = DatabaseConnector.getInstance().getAllRecycleMachines();
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
        return recyclemachines;
    }

    public String getPrice(String name)
    {
        
        String price = "";
        try {
            price = DatabaseConnector.getInstance().getItemPrice(name);
        }
        catch (SQLException e) {

        }
        catch (ClassNotFoundException ex) {

        }
        return price;
    }

}
