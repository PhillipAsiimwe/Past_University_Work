//*Phillip Amanya 
public class GroceryItem 
    private String name// string represting the name of the Item. 
    private float   price,weight;// Price of the Item, Weight of the Item 
    private boolean perishable;// True if item is perishable.

    public GroceryItem(){
        name="Unknown";//
        price=0;
        weight=0;
        perishable=false;
    }
    public GroceryItem(String nm,float p,float w){
        name=nm;
        price=p;
        weight=w;
        perishable=false;
    }
    public GroceryItem(String nm,float p,float w,boolean per) {
        name = nm;
        price = p;
        weight = w;
        perishable = per;
    }

    public String getName(){return name;}
    public boolean getperishable(){return perishable;}// returns boolean on perishable items.
    public float getPrice(){return price;}
    public float getWeight(){return this.weight;}
    public String toString(){return(name+" weighing "+weight+"kg with price $"+price);}//A toString() method that returns a string representation of the item like this:"Cascade weighing 2.4kg with price $4.79"
}
