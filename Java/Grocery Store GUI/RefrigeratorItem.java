public class RefrigeratorItem extends PerishableItem {
	/** RefrigeratorItem Constructor*/
    public RefrigeratorItem(String n, float p, float w) {
        super(n, p, w);
    }

    public String toString () {
        return super.toString() + "[keep refrigerated]";
    }
}
