import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("Bubble Tea Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Customer", new CustomerPanel());
        tabbedPane.addTab("Admin", new AdminPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
