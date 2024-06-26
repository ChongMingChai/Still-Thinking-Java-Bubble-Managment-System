import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Inventory {
    private Map<String, Double> flavors;
    private Map<Integer, String> flavorIds;
    private Map<Integer, String> orders;
    private int nextFlavorId;
    private int nextOrderId;
    private Admin adminPanel;

    public Inventory() {
        flavors = new HashMap<>();
        flavorIds = new HashMap<>();
        orders = new HashMap<>();
        nextFlavorId = 1;
        nextOrderId = 1;

        // Load flavors from file
        loadFlavorsFromFile("flavors.txt");
    }

    private void loadFlavorsFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String flavor = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    if (!flavors.containsKey(flavor)) { // Check if flavor already exists
                        addFlavor(flavor, price, false); // Add flavor without saving to file
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Flavors file not found. Starting with an empty inventory.");
        }
    }

    public void setAdminPanel(Admin adminPanel) {
        this.adminPanel = adminPanel;
    }

    public void addFlavor(String flavor, double price) {
        addFlavor(flavor, price, true); // Add flavor and save to file
    }

    private void addFlavor(String flavor, double price, boolean saveToFile) {
        if (!flavors.containsKey(flavor)) { // Check if flavor already exists
            flavors.put(flavor, price);
            flavorIds.put(nextFlavorId++, flavor);
            if (saveToFile) {
                saveFlavorToFile(flavor, price);
            }
        }
    }

    private void saveFlavorToFile(String flavor, double price) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("flavors.txt", true))) {
            writer.write(flavor + "," + price);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Failed to save flavor to file.");
        }
    }

    public void updatePrice(int flavorId, double price) {
        String flavor = flavorIds.get(flavorId);
        if (flavor != null) {
            flavors.put(flavor, price);
            updateFlavorInFile(flavor, price);
        } else {
            System.out.println("Flavor not found.");
        }
    }

    private void updateFlavorInFile(String flavor, double price) {
        File inputFile = new File("flavors.txt");
        File tempFile = new File("temp_flavors.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(flavor)) {
                    writer.write(flavor + "," + price);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to update flavor in file.");
            return;
        }

        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Failed to rename temp file.");
            }
        } else {
            System.out.println("Failed to delete original file.");
        }
    }

    public double getPrice(String flavor) {
        return flavors.getOrDefault(flavor, 0.0);
    }

    public String getFlavorName(int flavorId) {
        return flavorIds.get(flavorId);
    }

    public String[] getFlavors() {
        return flavors.keySet().toArray(new String[0]);
    }

    public int getFlavorId(String flavor) {
        for (Map.Entry<Integer, String> entry : flavorIds.entrySet()) {
            if (entry.getValue().equals(flavor)) {
                return entry.getKey();
            }
        }
        return -1; // Return -1 if flavor not found
    }

    public void addOrder(String customerName, int flavorId, String size, double totalPrice) {
        String flavor = flavorIds.get(flavorId);
        String order = "Customer: " + customerName + ", Flavor: " + flavor + ", Size: " + size + ", Total Price: $" + totalPrice;
        orders.put(nextOrderId++, order);
        if (adminPanel != null) {
            adminPanel.updateOrders();
        }
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
        if (adminPanel != null) {
            adminPanel.updateOrders();
        }
    }

    public void editOrder(int orderId, String newCustomerName, int newFlavorId, String newSize) {
        if (orders.containsKey(orderId)) {
            String[] orderDetails = orders.get(orderId).split(", ");
            String flavor = flavorIds.get(newFlavorId);
            double newPrice = calculateTotalPrice(flavor, newSize);
            String updatedOrder = "Customer: " + newCustomerName + ", Flavor: " + flavor + ", Size: " + newSize + ", Total Price: $" + newPrice;
            orders.put(orderId, updatedOrder);
            if (adminPanel != null) {
                adminPanel.updateOrders();
            }
            recordDailySales(); // Update the DailySales.txt file
        } else {
            System.out.println("Order not found.");
        }
    }

    private void updateDailySalesFile() {
        File dailySalesFile = new File("DailySales.txt");
        if (dailySalesFile.exists()) {
            dailySalesFile.delete();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("DailySales.txt", true))) {
            for (Map.Entry<Integer, String> order : orders.entrySet()) {
                writer.write(order.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to update daily sales file.");
        }
    }

    private double calculateTotalPrice(String flavor, String size) {
        double basePrice = getPrice(flavor);
        switch (size.toLowerCase()) {
            case "medium":
                return basePrice + 1;
            case "large":
                return basePrice + 2;
            default:
                return basePrice;
        }
    }

    public void recordDailySales() {
        File dailySalesFile = new File("DailySales.txt");
        if (dailySalesFile.exists()) {
            dailySalesFile.delete();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("DailySales.txt", true))) {
            for (Map.Entry<Integer, String> order : orders.entrySet()) {
                writer.write(order.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to record daily sales.");
        }
    }

    public String viewSales() {
        StringBuilder sb = new StringBuilder();
        double totalSales = 0;
        int orderCount = 0;
        for (Map.Entry<Integer, String> order : orders.entrySet()) {
            sb.append(order.getValue()).append("\n");
            totalSales += extractPriceFromOrder(order.getValue());
            orderCount++;
        }
        sb.append("Total Orders: ").append(orderCount).append("\n");
        sb.append("Total Sales: $").append(totalSales).append("\n");
        return sb.toString();
    }

    private double extractPriceFromOrder(String order) {
        String[] parts = order.split(", ");
        for (String part : parts) {
            if (part.startsWith("Total Price: $")) {
                return Double.parseDouble(part.split("\\$")[1]);
            }
        }
        return 0.0;
    }
}