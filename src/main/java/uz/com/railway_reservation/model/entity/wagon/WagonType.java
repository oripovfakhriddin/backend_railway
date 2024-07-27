package uz.com.railway_reservation.model.entity.wagon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WagonType {

    BOXCAR(7.0),
    FLATCAR(3.0),
    GONDOLACAR(4.0),
    HOPPERCAR(5.0),
    REEFER(6.0),
    AUTORACK(8.0);

    private final double amount;
}
