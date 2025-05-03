package taxopark.thread;


import taxopark.model.*;
import taxopark.ui.TaxiParkUI;

public class DriverThread extends Thread {
    private final Driver driver;
    private final Order order;
    private final TaxiParkUI ui;

    public DriverThread(Driver driver, Order order, TaxiParkUI ui) {
        this.driver = driver;
        this.order = order;
        this.ui = ui;
    }

    public void run() {
        try {
            ui.log(driver.getName() + " направляется к " + order.getCustomerId() + " из " + order.getFrom().getName());
            Thread.sleep(driver.getLocation().distanceTo(order.getFrom()) * 500);

            driver.setLocation(order.getFrom());
            ui.log(driver.getName() + " везет " + order.getCustomerId() + " в " + order.getTo().getName());
            Thread.sleep(driver.getLocation().distanceTo(order.getTo()) * 500);

            driver.setLocation(order.getTo());
            driver.setAvailable(true);
            ui.log(driver.getName() + " завершил заказ для " + order.getCustomerId());

        } catch (InterruptedException ignored) {
        }
    }
}



