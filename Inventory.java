import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Double> flavors;
    private Map<Integer, String> orders;
    private int nextOrderId;

    public Inventory() {
        flavors = new HashMap<>();
        orders = new HashMap<>();
        nextOrderId = 1;
    }

    public void addFlavor(String flavor, double price) {
        flavors.put(flavor, price);
    }

    public void updatePrice(String flavor, double price) {
        if (flavors.containsKey(flavor)) {
            flavors.put(flavor, price);
        } else {
            System.out.println("Flavor not found.");
        }
    }

    public double getPrice(String flavor) {
        return flavors.getOrDefault(flavor, 0.0);
    }

    public String[] getFlavors() {
        return flavors.keySet().toArray(new String[0]);
    }

    public void addOrder(String customerName, String flavor, String size, double totalPrice) {
        String order = "Customer: " + customerName + ", Flavor: " + flavor + ", Size: " + size + ", Total Price: $" + totalPrice;
        orders.put(nextOrderId++, order);
    }

    public String viewOrders() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> order : orders.entrySet()) {
            sb.append("Order ID: ").append(order.getKey()).append(", ").append(order.getValue()).append("\n");
        }
        return sb.toString();
    }

    public void deleteOrder(int orderId) {
        orders.remove(orderId);
    }

    public void editOrder(int orderId, String newFlavor, String newSize, double newPrice) {
        if (orders.containsKey(orderId)) {
            String[] orderDetails = orders.get(orderId).split(", ");
            String customerName = orderDetails[0].split(": ")[1];
            String updatedOrder = "Customer: " + customerName + ", Flavor: " + newFlavor + ", Size: " + newSize + ", Total Price: $" + newPrice;
            orders.put(orderId, updatedOrder);
        } else {
            System.out.println("Order not found.");
        }
    }
}
