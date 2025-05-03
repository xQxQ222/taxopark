package taxopark.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private String name;
    private int x;
    private int y;

    public int distanceTo(Location other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}

