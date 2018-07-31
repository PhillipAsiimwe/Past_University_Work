public class GroceryItem implements Carryable{
    private String      name;
    private float       price;
    private float       weight;
	/** zero parameter Contructor for the GroceryItem class
	*/
    public GroceryItem() {
        name = "?";
        price = 0;
        weight = 0;
    }
	/** GroceryItem Constructor 
	*@Param n String name of the GroceryItem
	*@Param p Float price of the GroceryItem 
	*@Param w Float weight of the GroceryItem
	*/
    public GroceryItem(String n, float p, float w) {
        name = n;
        price = p;
        weight = w;
    }
	/**@Return String the name of the item*/
    public String getName() { return name; }
	/**@Return Float of the price of the item*/
    public float getPrice() { return price; }
	/**@Return Float of the weight of the item*/
    public float getWeight() { return weight; }

    public String toString () {
        return name + " weighing " + weight + "kg with price $" + price;
    }
	/** @Returns the name of the item*/
    public String getDescription() { return name; }
}