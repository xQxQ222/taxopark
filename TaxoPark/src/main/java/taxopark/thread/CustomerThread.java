package taxopark.thread;


import taxopark.model.Location;
import taxopark.model.Order;
import taxopark.ui.TaxiParkUI;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class CustomerThread extends Thread {
    private final BlockingQueue<Order> orderQueue;
    private final List<Location> locations;
    private final TaxiParkUI ui;
    private int idCounter = 1;
    private final Random random = new Random();

    public CustomerThread(BlockingQueue<Order> orderQueue, List<Location> locations, TaxiParkUI ui) {
        this.orderQueue = orderQueue;
        this.locations = locations;
        this.ui = ui;
    }

    public void run() {
        while (true) {
            try {
                Location from = locations.get(random.nextInt(locations.size()));
                Location to;
                do {
                    to = locations.get(random.nextInt(locations.size()));
                } while (from.equals(to));

                String id = "Заказчик-" + idCounter++;
                Order order = new Order(id, from, to, false);
                orderQueue.put(order);
                ui.log("Создан заказ: " + order);

                new Thread(() -> {
                    try {
                        Thread.sleep(8000);
                        if (!order.isCancelled()) {
                            order.setCancelled(true);
                            ui.log("⛔ " + order.getCustomerId() + " отменил заказ (слишком долго)");
                        }
                    } catch (InterruptedException ignored) {}
                }).start();

                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
        }
    }

}



