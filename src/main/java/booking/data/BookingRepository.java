package booking.data;

import java.time.*;
import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByDate(LocalDate date);
}