//*PHillip Amanya 
public class Shopper{
    private static final int MAX_CART_ITEMS=100;
    private GroceryItem cart[];
    private int numitems=0;
    private GroceryBag[] bag,bag2;

    public Shopper(){//Shopper constructor, initalizes the cart.
        cart=new GroceryItem[MAX_CART_ITEMS];
    }
	
	
	/**
	* adds Grocery Item to the Shoppers Cart.
	* @param  g the GroceryItem to be added to the Cart of this shopper.
	*/
    public void addItem(GroceryItem g){
        for(int i=0;i<cart.length;i++){
            if(cart[i]==null){
                cart[i]=g;
                numitems++;
                break;
            }
        }
    }
	
	/**
	* Removes An Item from the Shoppers Cart.
	*@param item the Grocery Item to be removed from the shoppers Cart
	*/
    public void removeItem(GroceryItem item){
        for(int i=0;i<cart.length;i++){
            if ((cart[i]==item)){
                cart[i]=cart[((numitems)-1)];
                cart[(numitems-1)]=null;
                numitems=numitems-1;
                break;
            }
        }
    }
	
	/** 
	* Pack Bags Method available in the Shopper class.
	* Fills as many bags possible one at a time below each bages weight limit MAXWEIGHT in GroceryBag class.
	* Some items may be too big to fit in any bags therefore they will be left. 
	* @return Returns an array of GroceryBags all packed below their weight limit. 
	*/
    public GroceryBag[] packBags() {
        bag = new GroceryBag[100];
        GroceryBag b1=new GroceryBag();
        int j=0;
        for (int counter = 0; counter < numitems; counter++) {
            if(cart[counter].getWeight()<=(5-(b1.getWeight()))){
                b1.addItem(cart[counter]);
                removeItem(cart[counter]);
                counter--;
            }else if((cart[counter].getWeight())>5) {}
            else if (cart[counter].getWeight()<5){
                bag[j]=b1;
                j++;
                b1=new GroceryBag();
                counter--;
            }
        }
        bag[j]=b1;
        j++;
        bag2=new GroceryBag[j];
        for (int i=0;i<j;i++){bag2[i]=bag[i];}
        return bag2;
    }
	/**
	* Returns number of items the Shopper has
	*@Return returns the number of Items 
	*/
    public int getNumItems(){return numitems;}
	
	/**
	* Returns Shoppers Cart 
	*@Return array of GroceryItems
	*/
    public GroceryItem[] getCart(){return cart;}
	
	/**
	* Returns String of Shopper items to specifications.
	*@Return String of the Current shoppers cart. 
	*/
    public String toString(){return ("Shopper with shopping cart containing "+numitems+" items");}
}
