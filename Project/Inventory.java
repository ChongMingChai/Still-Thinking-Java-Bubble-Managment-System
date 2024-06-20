import java.io.Serializable;
import java.util.ArrayList;

public class Inventory implements Serializable {
    private ArrayList<String> flavors;

    public Inventory() {
        this.flavors = new ArrayList<>();
    }

    public void addFlavor(String flavor) {
        flavors.add(flavor);
    }

    public ArrayList<String> getFlavors() {
        return flavors;
    }
}
