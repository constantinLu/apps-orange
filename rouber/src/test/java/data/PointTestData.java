package data;

import com.orange.rouber.model.Point;

import java.math.BigDecimal;
import java.util.Random;

public interface PointTestData {

    Random random = new Random();

    default Point.PointBuilder aPoint() {
        return Point.builder()
                .x(BigDecimal.valueOf(random.nextDouble()))
                .y(BigDecimal.valueOf(random.nextDouble()));
    }
}
