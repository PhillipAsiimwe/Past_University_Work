//*Phillip Amanya
public class GroceryBag  {// Holds grocery items in a class allowing easy access and editing of grocery bag.
    private GroceryItem items[];
    private int numitems=0;
    private float weight;
    private static final float MAXWEIGHT=5;
    private static final int MAXITEM=25;

    public GroceryBag() {
        this.items = new GroceryItem[MAXITEM];
        weight=0;
    }
    public void addItem(GroceryItem g) {// Helps add Grocery item to current bag
        if ((g.getWeight() + weight) <= MAXWEIGHT) {
            for (int i=0;i<items.length;i++){
                if(items[i]==null){
                    items[i]=g;
                    numitems++;
                    weight=weight+g.getWeight();
                    break;
                }
            }
        }
    }
    public GroceryItem heaviestItem(){//calculates the heaviestItem.
        float hi=0;
        for (int j=0;j<2;j++) {
            if (numitems > 0 && j == 0) {
                for (int i = 0; i < numitems; i++) {
                    if (items[i].getWeight() > hi) {
                        hi = items[i].getWeight();
                    }
                }
            } else if(numitems > 0 && j == 1){
                for (int i = 0; i < numitems; i++) {
                    if (items[i].getWeight() == hi) {
                        return items[i];

                    }
                }
            }
        }return null;
    }
    public void removeItem(GroceryItem item){// removes item from bag
        for (int i=0;i<numitems;i++){
            if (items[i]==item){
                items[i]=null;
                weight=weight-item.getWeight();
                break;
            }
        }
    }

    public String toString(){// to string provides the information accurately descripted.
        if(numitems==0){
            return "An empty grocery bag.";
        }else{
            return ("A "+weight+"Kg grocery bag with "+numitems+" items");
        }
    }
    public boolean has(GroceryItem item){//Basic contains function.
        for (GroceryItem a:items){
            if(item==a){
                return true;
            }
        }
        return false;
    }
    public GroceryItem[] unpackPerishables(){// Goes through Grocery Bag and removes all the Perishable items butting them into an array, Could be put into another bag.
        GroceryBag pers=new GroceryBag();
        GroceryBag nonpers=new GroceryBag();
        for (int i=0;i<numitems;i++){
            if (items[i].getperishable()){pers.addItem(items[i]);}
            else if (!(items[i].getperishable())){nonpers.addItem(items[i]);}
        }
        GroceryItem[] Persh=new GroceryItem[pers.getNumItems()];
        for (int i=0;i<pers.getNumItems();i++){Persh[i]=pers.getItems()[i];}
        items=new GroceryItem[MAXITEM];
        for (int i=0;i<nonpers.getNumItems();i++){items[i]=nonpers.getItems()[i];}
        numitems=nonpers.getNumItems();
        weight=nonpers.getWeight();
        return Persh;
    }

    public int getNumItems(){return numitems;}
    public GroceryItem[] getItems(){return items;}
    public float getWeight(){return weight;}

}
