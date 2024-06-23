import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Admin extends JPanel {
    private Inventory inventory;
    private Customer customerPanel;
    private JList<String> orderList;
    private DefaultListModel<String> orderListModel;

    public Admin(Inventory inventory, Customer customerPanel) {
        this.inventory = inventory;
        this.customerPanel = customerPanel;

        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 5, 5));

        JButton addFlavorButton = new JButton("Add New Flavor");
        addFlavorButton.setIcon(resizeIcon(new ImageIcon("Add.png"), 20, 20)); // 添加图标
        addFlavorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flavor = JOptionPane.showInputDialog("Enter new flavor name:");
                if (inventory.getFlavorId(flavor) != -1) {
                    JOptionPane.showMessageDialog(null, "Flavor already exists.");
                    return;
                }
                String priceStr = JOptionPane.showInputDialog("Enter price for " + flavor + ":");
                double price = Double.parseDouble(priceStr);
                inventory.addFlavor(flavor, price);
                customerPanel.updateFlavors(inventory.getFlavors());
                JOptionPane.showMessageDialog(null, "Flavor added.");
            }
        });
        buttonPanel.add(addFlavorButton);

        JButton editFlavorButton = new JButton("Edit Flavor Price");
        editFlavorButton.setIcon(resizeIcon(new ImageIcon("Edit.png"),20,20)); // 添加图标
        editFlavorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flavorIdStr = JOptionPane.showInputDialog("Enter flavor ID to edit:");
                int flavorId = Integer.parseInt(flavorIdStr);
                String priceStr = JOptionPane.showInputDialog("Enter new price for flavor ID " + flavorId + ":");
                double price = Double.parseDouble(priceStr);
                inventory.updatePrice(flavorId, price);
                customerPanel.updateFlavors(inventory.getFlavors());
                JOptionPane.showMessageDialog(null, "Price updated.");
            }
        });
        buttonPanel.add(editFlavorButton);

        JButton deleteOrderButton = new JButton("Delete an Order");
        deleteOrderButton.setIcon(resizeIcon(new ImageIcon("Delete.png"),20,20)); // 添加图标
        deleteOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = orderList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedOrder = orderList.getSelectedValue();
                    int orderId = Integer.parseInt(selectedOrder.split(", ")[0].split(": ")[1]);
                    inventory.deleteOrder(orderId);
                    JOptionPane.showMessageDialog(null, "Order deleted.");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an order to delete.");
                }
            }
        });
        buttonPanel.add(deleteOrderButton);

        JButton editOrderButton = new JButton("Edit an Order");
        editOrderButton.setIcon(resizeIcon(new ImageIcon("Edit.png"),20,20)); // 添加图标
        editOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = orderList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedOrder = orderList.getSelectedValue();
                    int orderId = Integer.parseInt(selectedOrder.split(", ")[0].split(": ")[1]);
        
                    // Create a dialog for editing the order
                    JDialog editDialog = new JDialog();
                    editDialog.setTitle("Edit Order");
                    editDialog.setModal(true);
                    editDialog.setLayout(new BorderLayout());
        
                    // Customer name input
                    JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel nameLabel = new JLabel("New Customer Name: ");
                    JTextField customerNameField = new JTextField(15);
                    customerPanel.add(nameLabel);
                    customerPanel.add(customerNameField);
                    editDialog.add(customerPanel, BorderLayout.NORTH);
        
                    // Flavor selection
                    JPanel flavorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel flavorLabel = new JLabel("Select New Flavor: ");
                    JComboBox<String> flavorComboBox = new JComboBox<>(inventory.getFlavors());
                    flavorPanel.add(flavorLabel);
                    flavorPanel.add(flavorComboBox);
                    editDialog.add(flavorPanel, BorderLayout.CENTER);
        
                    // Size selection
                    JPanel sizePanel = new JPanel(new GridLayout(3, 1));
                    sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
        
                    JRadioButton smallButton = new JRadioButton("Small");
                    JRadioButton mediumButton = new JRadioButton("Medium");
                    JRadioButton largeButton = new JRadioButton("Large");
        
                    ButtonGroup sizeGroup = new ButtonGroup();
                    sizeGroup.add(smallButton);
                    sizeGroup.add(mediumButton);
                    sizeGroup.add(largeButton);
        
                    sizePanel.add(smallButton);
                    sizePanel.add(mediumButton);
                    sizePanel.add(largeButton);
                    editDialog.add(sizePanel, BorderLayout.WEST); 

                    // OK and Cancel buttons
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    JButton okButton = new JButton("OK");
                    okButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String newCustomerName = customerNameField.getText();
                            String newFlavor = (String) flavorComboBox.getSelectedItem();
                            int newFlavorId = inventory.getFlavorId(newFlavor);
                            String newSize = smallButton.isSelected() ? "Small" : mediumButton.isSelected() ? "Medium" : "Large";
                            inventory.editOrder(orderId, newCustomerName, newFlavorId, newSize);
                            JOptionPane.showMessageDialog(null, "Order edited.");
                            editDialog.dispose();
                        }
                    });
                    JButton cancelButton = new JButton("Cancel");
                    cancelButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            editDialog.dispose();
                        }
                    });
                    buttonPanel.add(okButton);
                    buttonPanel.add(cancelButton);
                    editDialog.add(buttonPanel, BorderLayout.SOUTH);
        
                    editDialog.pack();
                    editDialog.setLocationRelativeTo(null);
                    editDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an order to edit.");
                }
            }
        });
        buttonPanel.add(editOrderButton);

        JButton viewSalesButton = new JButton("View Sales");
        viewSalesButton.setIcon(resizeIcon(new ImageIcon("View.png"), 20, 20)); // 添加图标
        viewSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventory.recordDailySales();
                String salesReport = inventory.viewSales();
                JOptionPane.showMessageDialog(null, salesReport, "Daily Sales Report", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(viewSalesButton);

        add(buttonPanel, BorderLayout.EAST);

        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);
        JScrollPane orderScrollPane = new JScrollPane(orderList);
        add(orderScrollPane, BorderLayout.CENTER);

        inventory.setAdminPanel(this);
    }

    public void updateOrders() {
        orderListModel.clear();
        String orders = inventory.viewOrders();
        if (!orders.isEmpty()) {
            String[] orderArray = orders.split("\n");
            for (String order : orderArray) {
                orderListModel.addElement(order);
            }
        }
    }
    
    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
