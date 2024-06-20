import java.io.Serializable;

public class Order implements Serializable {
    private String customerName;
    private String flavor;

    public Order(String customerName, String flavor) {
        this.customerName = customerName;
        this.flavor = flavor;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getFlavor() {
        return flavor;
    }
}
