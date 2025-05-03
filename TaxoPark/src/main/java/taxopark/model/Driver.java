package taxopark.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Driver {
    private String name;
    private Location location;
    private boolean available;

    public synchronized void setAvailable(boolean available) {
        this.available = available;
    }

    public synchronized boolean isAvailable() {
        return available;
    }

    public synchronized void setLocation(Location location) {
        this.location = location;
    }
}
