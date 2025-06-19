package booking.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import booking.SpringConfig;
import booking.data.Booking;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class BookingServiceTest {

	@Autowired
	private BookingService bookSvc;


	@Before
	public void setup() {
	}

	@Test
	public void testBooking() {
		// TODO
		bookSvc.addBooking(new Booking("Gordan", 4, LocalDate.now(), LocalTime.of(18, 0)));
	}

}