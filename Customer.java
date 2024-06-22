import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Customer extends JPanel {
    private JList<String> flavorList;
    private JScrollPane flavorScrollPane;
    private JRadioButton smallButton, mediumButton, largeButton;
    private ButtonGroup sizeGroup;
    private JLabel totalLabel;
    private JButton placeOrderButton;
    private JTextField customerNameField;

    public Customer(Inventory inventory) {
        setLayout(new BorderLayout());

        // Customer name input
        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Info"));

        JLabel nameLabel = new JLabel("Name: ");
        customerNameField = new JTextField(15);
        customerPanel.add(nameLabel);
        customerPanel.add(customerNameField);

        add(customerPanel, BorderLayout.NORTH);

        // Flavor selection with prices
        flavorList = new JList<>(getFlavorWithPrices(inventory));
        flavorList.setCellRenderer(new FlavorListRenderer());
        flavorScrollPane = new JScrollPane(flavorList);
        flavorScrollPane.setPreferredSize(new Dimension(200, 0));
        add(flavorScrollPane, BorderLayout.WEST);

        // Size selection
        JPanel sizePanel = new JPanel(new GridLayout(4, 1));
        sizePanel.setBorder(BorderFactory.createTitledBorder("Select Size"));

        // Price reminder
        JLabel priceReminder = new JLabel("<html>Price:<br>Small: +$0<br>Medium: +$1<br>Large: +$2</html>");
        sizePanel.add(priceReminder);

        smallButton = new JRadioButton("Small");
        mediumButton = new JRadioButton("Medium");
        largeButton = new JRadioButton("Large");

        sizeGroup = new ButtonGroup();
        sizeGroup.add(smallButton);
        sizeGroup.add(mediumButton);
        sizeGroup.add(largeButton);

        sizePanel.add(smallButton);
        sizePanel.add(mediumButton);
        sizePanel.add(largeButton);

        add(sizePanel, BorderLayout.CENTER);

        // Place Order button and Total Price label
        JPanel orderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        totalLabel = new JLabel("Total Price: $0.00");
        orderPanel.add(totalLabel);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerName = customerNameField.getText();
                if (customerName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter your name.");
                    return;
                }

                String flavor = flavorList.getSelectedValue();
                if (flavor == null) {
                    JOptionPane.showMessageDialog(null, "Please select a flavor.");
                    return;
                }

                String size = getSelectedSize();
                if (size == null) {
                    JOptionPane.showMessageDialog(null, "Please select a size.");
                    return;
                }

                double basePrice = inventory.getPrice(flavor.split(" - ")[0]);
                double totalPrice = calculateTotalPrice(basePrice, size);
                totalLabel.setText("Total Price: $" + totalPrice);

                int confirmation = JOptionPane.showConfirmDialog(null, "Do you want to place the order?", "Confirm Order", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    inventory.addOrder(customerName, flavor, size, totalPrice);
                    JOptionPane.showMessageDialog(null, "Order placed successfully!");
                }
            }
        });
        orderPanel.add(placeOrderButton);

        add(orderPanel, BorderLayout.SOUTH);

        // Size selection listener
        ActionListener sizeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTotalPrice(inventory);
            }
        };

        smallButton.addActionListener(sizeListener);
        mediumButton.addActionListener(sizeListener);
        largeButton.addActionListener(sizeListener);
    }

    private String getSelectedSize() {
        if (smallButton.isSelected()) return "Small";
        if (mediumButton.isSelected()) return "Medium";
        if (largeButton.isSelected()) return "Large";
        return null;
    }

    private double calculateTotalPrice(double basePrice, String size) {
        switch (size.toLowerCase()) {
            case "medium":
                return basePrice + 1;
            case "large":
                return basePrice + 2;
            default:
                return basePrice;
        }
    }

    private void updateTotalPrice(Inventory inventory) {
        String selectedFlavor = flavorList.getSelectedValue();
        if (selectedFlavor != null) {
            double basePrice = inventory.getPrice(selectedFlavor.split(" - ")[0]);
            String selectedSize = getSelectedSize();
            if (selectedSize != null) {
                double totalPrice = calculateTotalPrice(basePrice, selectedSize);
                totalLabel.setText("Total Price: $" + totalPrice);
            }
        }
    }

    private String[] getFlavorWithPrices(Inventory inventory) {
        String[] flavors = inventory.getFlavors();
        String[] flavorWithPrices = new String[flavors.length];
        for (int i = 0; i < flavors.length; i++) {
            flavorWithPrices[i] = flavors[i] + " - $" + inventory.getPrice(flavors[i]);
        }
        return flavorWithPrices;
    }

    // Add this method to update the flavor list when admin makes changes
    public void updateFlavors(String[] flavors) {
        flavorList.setListData(flavors);
    }

    // Custom list cell renderer to display flavor with price
    private static class FlavorListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setText(value.toString());
            return component;
        }
    }
}
