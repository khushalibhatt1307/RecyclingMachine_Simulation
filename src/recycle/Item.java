package recycle;

/*
 * Author - Khushali Bhatt
 *  Class to store item data
 */
public class Item
{
    private ItemType type;
    private String type1;
    private double weight;
	private int quantity;
	
	//constructor
	public Item(ItemType type,double weight, int quantity)
	{
		this.type = type;
		this.weight = weight;
		this.quantity = quantity;
	}
	public Item(String type,double weight, int quantity)
	{
		this.type1 = type;
		this.weight = weight;
		this.quantity = quantity;
	}
	
	//getter methods
	public ItemType getType()
	{
		return this.type;
	}

	public double getWeight()
	{
		return this.weight;
	}

	public double getWeightPerKg()
	{
		return this.weight * 0.453;
	}
	
	//setter methods
	public void setType(ItemType type) 
	{
		this.type = type;
	}
	
	public void setWeight(double weight)
	{
		this.weight = weight;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
    
}

