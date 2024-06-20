import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JPanel {
    private JTextField newFlavorField;
    private JButton addFlavorButton;
    private JTextArea inventoryArea;
    private JButton viewOrdersButton;
    private JTextArea orderHistoryArea;

    public AdminPanel() {
        setLayout(new BorderLayout());

        // Add New Flavor Panel
        JPanel flavorPanel = new JPanel(new GridLayout(1, 3));
        flavorPanel.add(new JLabel("New Flavor:"));
        newFlavorField = new JTextField();
        flavorPanel.add(newFlavorField);
        addFlavorButton = new JButton("Add Flavor");
        flavorPanel.add(addFlavorButton);

        add(flavorPanel, BorderLayout.NORTH);

        // Inventory Panel
        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        add(new JScrollPane(inventoryArea), BorderLayout.CENTER);

        // View Orders Panel
        viewOrdersButton = new JButton("View Orders");
        orderHistoryArea = new JTextArea();
        orderHistoryArea.setEditable(false);
        add(viewOrdersButton, BorderLayout.SOUTH);
        add(new JScrollPane(orderHistoryArea), BorderLayout.SOUTH);

        // Add Action Listener
        addFlavorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newFlavor = newFlavorField.getText();
                addNewFlavor(newFlavor);
            }
        });

        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewOrders();
            }
        });
    }

    private void addNewFlavor(String flavor) {
        inventoryArea.append("New Flavor Added: " + flavor + "\n");
        // Save flavor to file (binary file handling can be added here)
    }

    private void viewOrders() {
        // Load order history from file (binary file handling can be added here)
        orderHistoryArea.append("Order History:\n");
    }
}
