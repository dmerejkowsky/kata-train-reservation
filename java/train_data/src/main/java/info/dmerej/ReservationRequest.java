package info.dmerej;

import java.util.List;

public record ReservationRequest(String train_id, String booking_reference, List<String> seats) {
}
