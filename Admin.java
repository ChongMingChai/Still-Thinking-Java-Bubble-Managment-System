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

        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 5, 5));

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
        buttonPanel.add(addFlavorButton);

        JButton editFlavorButton = new JButton("Edit Flavor Price");
        editFlavorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flavor = JOptionPane.showInputDialog("Enter flavor name to edit:");
                String priceStr = JOptionPane.showInputDialog("Enter new price for " + flavor + ":");
                double price = Double.parseDouble(priceStr);
                inventory.updatePrice(flavor, price);
                customerPanel.updateFlavors(inventory.getFlavors());
                JOptionPane.showMessageDialog(null, "Price updated.");
            }
        });
        buttonPanel.add(editFlavorButton);

        JButton viewOrdersButton = new JButton("View Orders");
        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea textArea = new JTextArea(inventory.viewOrders());
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Orders", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(viewOrdersButton);

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
        buttonPanel.add(deleteOrderButton);

        JButton editOrderButton = new JButton("Edit an Order");
        editOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderIdStr = JOptionPane.showInputDialog("Enter order ID to edit:");
                int orderId = Integer.parseInt(orderIdStr);
                String newFlavor = JOptionPane.showInputDialog("Enter new flavor:");
                String newSize = JOptionPane.showInputDialog("Enter new size (Small/Medium/Large):");
                String newPriceStr = JOptionPane.showInputDialog("Enter new total price:");
                double newPrice = Double.parseDouble(newPriceStr);
                inventory.editOrder(orderId, newFlavor, newSize, newPrice);
                JOptionPane.showMessageDialog(null, "Order edited.");
            }
        });
        buttonPanel.add(editOrderButton);

        add(buttonPanel, BorderLayout.EAST);

        JTextArea orderTextArea = new JTextArea();
        orderTextArea.setEditable(false);
        JScrollPane orderScrollPane = new JScrollPane(orderTextArea);
        add(orderScrollPane, BorderLayout.CENTER);

        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderTextArea.setText(inventory.viewOrders());
            }
        });
    }
}
