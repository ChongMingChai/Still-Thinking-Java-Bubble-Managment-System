import javax.swing.*;
import java.awt.*;

public class Mainframe extends JFrame {
    public Mainframe() {
        setTitle("Bubble Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Inventory inventory = new Inventory();
        Customer customerPanel = new Customer(inventory);
        Admin adminPanel = new Admin(inventory, customerPanel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Customer", customerPanel);
        tabbedPane.addTab("Admin", adminPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Mainframe().setVisible(true);
            }
        });
    }
}
