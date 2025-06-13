package booking.service;

import org.junit.*;
import org.mockito.*;

import booking.data.*;

public class BookingServiceTest {

	private BookingRepository repo;
	private BookingService service;

	@Before
	public void setup() {
		repo = Mockito.mock(BookingRepository.class);
		service = new BookingService(repo);
	}

	@Test
	public void testBooking() {
		// TODO
	}

}