import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class CustomerPanel extends JPanel {
    private JTextField customerNameField;
    private JComboBox<String> teaFlavorBox;
    private JTextArea orderHistoryArea;
    private JButton orderButton;

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Customer Order Panel
        JPanel orderPanel = new JPanel(new GridLayout(3, 2));
        orderPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        orderPanel.add(customerNameField);

        orderPanel.add(new JLabel("Select Flavor:"));
        teaFlavorBox = new JComboBox<>(new String[]{"Original", "Matcha", "Taro", "Mango"});
        orderPanel.add(teaFlavorBox);

        orderButton = new JButton("Place Order");
        orderPanel.add(orderButton);

        add(orderPanel, BorderLayout.NORTH);

        // Order History Panel
        orderHistoryArea = new JTextArea();
        orderHistoryArea.setEditable(false);
        add(new JScrollPane(orderHistoryArea), BorderLayout.CENTER);

        // Add Action Listener
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerName = customerNameField.getText();
                String flavor = (String) teaFlavorBox.getSelectedItem();
                placeOrder(customerName, flavor);
            }
        });
    }

    private void placeOrder(String customerName, String flavor) {
        Order order = new Order(customerName, flavor);
        String orderDetails = "Customer: " + order.getCustomerName() + ", Flavor: " + order.getFlavor() + "\n";
        orderHistoryArea.append(orderDetails);
        // Save order details to file (binary file handling can be added here)
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Customer Order System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(new CustomerPanel());
        frame.setVisible(true);
    }
}

class Order implements Serializable {
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
