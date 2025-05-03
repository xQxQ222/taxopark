package taxopark;

import taxopark.model.Driver;
import taxopark.model.Location;
import taxopark.model.Order;
import taxopark.thread.CustomerThread;
import taxopark.thread.DispatcherThread;
import taxopark.ui.TaxiParkUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaxiParkUI ui = new TaxiParkUI();

            List<Location> locations = List.of(
                    new Location("Площадь", 0, 0),
                    new Location("Кинотеатр", 2, 3),
                    new Location("Больница", 5, 2),
                    new Location("Школа", 3, 5),
                    new Location("Магазин", 1, 4)
            );

            List<Driver> drivers = new ArrayList<>(List.of(
                    new Driver("Водитель-1", locations.get(0), true),
                    new Driver("Водитель-2", locations.get(1), true),
                    new Driver("Водитель-3", locations.get(2), true)
            ));

            BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

            ui.setLocations(locations);
            ui.setOrderQueue(orderQueue);

            new DispatcherThread(orderQueue, drivers, ui).start();
            new CustomerThread(orderQueue, locations, ui).start();
        });
    }
}




