package taxopark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Order {
    private String customerId;
    private Location from;
    private Location to;

    private volatile boolean cancelled;

    @Override
    public String toString() {
        return customerId + " [" + from.getName() + " → " + to.getName() + "]";
    }
}


