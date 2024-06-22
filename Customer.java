import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Customer extends JPanel {
    private JComboBox<String> flavorComboBox;
    private JRadioButton smallButton, mediumButton, largeButton;
    private ButtonGroup sizeGroup;
    private JLabel totalLabel;
    private JButton placeOrderButton;

    public Customer(Inventory inventory) {
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Select Flavor:"));
        flavorComboBox = new JComboBox<>(inventory.getFlavors());
        add(flavorComboBox);

        add(new JLabel("Select Size:"));

        smallButton = new JRadioButton("Small");
        mediumButton = new JRadioButton("Medium");
        largeButton = new JRadioButton("Large");

        sizeGroup = new ButtonGroup();
        sizeGroup.add(smallButton);
        sizeGroup.add(mediumButton);
        sizeGroup.add(largeButton);

        JPanel sizePanel = new JPanel();
        sizePanel.add(smallButton);
        sizePanel.add(mediumButton);
        sizePanel.add(largeButton);

        add(sizePanel);

        totalLabel = new JLabel("Total Price: $0.00");
        add(totalLabel);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flavor = (String) flavorComboBox.getSelectedItem();
                if (flavor == null) {
                    JOptionPane.showMessageDialog(null, "Please select a flavor.");
                    return;
                }

                String size = getSelectedSize();
                if (size == null) {
                    JOptionPane.showMessageDialog(null, "Please select a size.");
                    return;
                }

                double basePrice = inventory.getPrice(flavor);
                double totalPrice = calculateTotalPrice(basePrice, size);
                totalLabel.setText("Total Price: $" + totalPrice);

                int confirmation = JOptionPane.showConfirmDialog(null, "Do you want to place the order?", "Confirm Order", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    inventory.addOrder("Customer", flavor, size, totalPrice);
                    JOptionPane.showMessageDialog(null, "Order placed successfully!");
                }
            }
        });

        add(placeOrderButton);

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
            case "small":
            default:
                return basePrice;
        }
    }

    private void updateTotalPrice(Inventory inventory) {
        String flavor = (String) flavorComboBox.getSelectedItem();
        if (flavor != null) {
            double basePrice = inventory.getPrice(flavor);
            String size = getSelectedSize();
            if (size != null) {
                double totalPrice = calculateTotalPrice(basePrice, size);
                totalLabel.setText("Total Price: $" + totalPrice);
            }
        }
    }

    public void updateFlavors(String[] flavors) {
        flavorComboBox.setModel(new DefaultComboBoxModel<>(flavors));
    }
}
