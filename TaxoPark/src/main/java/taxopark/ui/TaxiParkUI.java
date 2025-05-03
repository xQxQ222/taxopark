package taxopark.ui;

import org.jdesktop.swingx.JXFrame;
import taxopark.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.List;

public class TaxiParkUI extends JXFrame {
    private final JTextArea logArea;
    private final JComboBox<String> fromBox;
    private final JComboBox<String> toBox;
    private final JTextField customerIdField;
    private final JButton createButton;

    private List<Location> locations;
    private BlockingQueue<Order> orderQueue;

    public TaxiParkUI() {
        super("Таксопарк", false);
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        // Панель управления заказами
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel(new FlowLayout());

        fromBox = new JComboBox<>();
        toBox = new JComboBox<>();
        customerIdField = new JTextField(10);
        createButton = new JButton("Создать заказ");

        inputPanel.add(new JLabel("Клиент:"));
        inputPanel.add(customerIdField);
        inputPanel.add(new JLabel("Откуда:"));
        inputPanel.add(fromBox);
        inputPanel.add(new JLabel("Куда:"));
        inputPanel.add(toBox);
        inputPanel.add(createButton);

        controlPanel.add(inputPanel);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);

        createButton.addActionListener(e -> createOrder());
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
        fromBox.removeAllItems();
        toBox.removeAllItems();
        for (Location loc : locations) {
            fromBox.addItem(loc.getName());
            toBox.addItem(loc.getName());
        }
    }

    public void setOrderQueue(BlockingQueue<Order> queue) {
        this.orderQueue = queue;
    }

    private void createOrder() {
        String customerId = customerIdField.getText().trim();
        if (customerId.isEmpty()) {
            log("Введите ID клиента!");
            return;
        }

        String fromName = (String) fromBox.getSelectedItem();
        String toName = (String) toBox.getSelectedItem();

        if (fromName == null || toName == null || fromName.equals(toName)) {
            log("Проверьте локации: откуда и куда должны быть разными.");
            return;
        }

        Location from = locations.stream().filter(l -> l.getName().equals(fromName)).findFirst().orElse(null);
        Location to = locations.stream().filter(l -> l.getName().equals(toName)).findFirst().orElse(null);

        try {
            Order order = new Order(customerId, from, to, false);
            orderQueue.put(order);
            log("🔧 Создан заказ вручную: " + order);

            new Thread(() -> {
                try {
                    Thread.sleep(8000); // 8 секунд ожидания
                    if (!order.isCancelled()) {
                        order.setCancelled(true);
                        log("⛔ " + order.getCustomerId() + " отменил заказ (слишком долго)");
                    }
                } catch (InterruptedException ignored) {}
            }).start();

        } catch (InterruptedException ex) {
            log("Ошибка при создании заказа.");
        }
    }


    public void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}



