public class FreezerItem extends PerishableItem {// describes a freezer Item
	/**
	* Constructer to create Freezer item
	*@Param n String Name of Frezzer Item
	*@Param p Float describing the price 
	*@Param w Float describing the weight of the frozzen Item.
	*/
    public FreezerItem(String n, float p, float w) {
        super(n, p, w);
    }

    public String toString () {
        return super.toString() + "[keep frozen]";
    }
}
