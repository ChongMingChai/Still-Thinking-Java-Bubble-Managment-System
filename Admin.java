import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Admin extends JPanel {
    private Inventory inventory;
    private Customer customerPanel;

    public Admin(Inventory inventory, Customer customerPanel) {
        this.inventory = inventory;
        this.customerPanel = customerPanel;

        setLayout(new GridLayout(5, 1));

        JButton addFlavorButton = new JButton("Add New Flavor");
        addFlavorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flavor = JOptionPane.showInputDialog("Enter new flavor name:");
                String priceStr = JOptionPane.showInputDialog("Enter price for " + flavor + ":");
                double price = Double.parseDouble(priceStr);
                inventory.addFlavor(flavor, price);
                customerPanel.updateFlavors(inventory.getFlavors());
                JOptionPane.showMessageDialog(null, "Flavor added.");
            }
        });
        add(addFlavorButton);

        JButton editFlavorButton = new JButton("Edit Flavor Price");
        editFlavorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flavor = JOptionPane.showInputDialog("Enter flavor name to edit:");
                String priceStr = JOptionPane.showInputDialog("Enter new price for " + flavor + ":");
                double price = Double.parseDouble(priceStr);
                inventory.updatePrice(flavor, price);
                JOptionPane.showMessageDialog(null, "Price updated.");
            }
        });
        add(editFlavorButton);

        JButton viewOrdersButton = new JButton("View Orders");
        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea textArea = new JTextArea(inventory.viewOrders());
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Orders", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(viewOrdersButton);

        JButton deleteOrderButton = new JButton("Delete an Order");
        deleteOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderIdStr = JOptionPane.showInputDialog("Enter order ID to delete:");
                int orderId = Integer.parseInt(orderIdStr);
                inventory.deleteOrder(orderId);
                JOptionPane.showMessageDialog(null, "Order deleted.");
            }
        });
        add(deleteOrderButton);
    }
}
