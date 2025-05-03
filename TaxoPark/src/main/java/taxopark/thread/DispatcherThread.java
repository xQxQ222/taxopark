package taxopark.thread;

import taxopark.model.Driver;
import taxopark.model.Location;
import taxopark.model.Order;
import taxopark.ui.TaxiParkUI;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DispatcherThread extends Thread {
    private final BlockingQueue<Order> orderQueue;
    private final List<Driver> drivers;
    private final TaxiParkUI ui;

    public DispatcherThread(BlockingQueue<Order> orderQueue, List<Driver> drivers, TaxiParkUI ui) {
        this.orderQueue = orderQueue;
        this.drivers = drivers;
        this.ui = ui;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = orderQueue.take();
                if (order.isCancelled()) {
                    ui.log("🚫 Заказ отменён до обработки: " + order);
                    continue;
                }

                Driver nearestDriver = null;
                double minDistance = Double.MAX_VALUE;

                for (Driver driver : drivers) {
                    if (!driver.isAvailable()) continue;

                    double dist = getDistance(driver.getLocation(), order.getFrom());
                    if (dist < minDistance) {
                        minDistance = dist * 100;
                        nearestDriver = driver;
                    }
                }

                if (nearestDriver != null) {
                    nearestDriver.setAvailable(false);
                    nearestDriver.setLocation(order.getTo());

                    ui.log("🚕 " + nearestDriver.getName() + " отправлен на заказ: " + order);
                    Thread.sleep(5000);

                    nearestDriver.setAvailable(true);
                    ui.log("✅ " + nearestDriver.getName() + " завершил заказ: " + order);
                } else {
                    ui.log("❗Нет доступных водителей для: " + order);
                }

            } catch (InterruptedException ignored) {
            }
        }
    }

    private double getDistance(Location a, Location b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}



