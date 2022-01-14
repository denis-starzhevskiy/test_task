package task.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class CurrencyLimit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String symbol1;

    @Column
    String symbol2;

    @Column
    Character pricePrecision;

    @Column
    Double minLotSize;

    @Column
    Double minLotSizeS2;

    @Column
    Double maxLotSize;

    @Column
    Double minPrice;

    @Column
    Double maxPrice;

    @Override
    public String toString() {
        return "name=" + symbol1 + "/" + symbol2 +
                ",minPrice=" + minPrice +
                ",maxPrice=" + maxPrice;
    }
}
