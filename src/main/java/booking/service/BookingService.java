package booking.service;

import java.time.*;
import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import booking.data.*;

@Service
public class BookingService {
	private final BookingRepository repository;

	public BookingService(BookingRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void deleteBooking(Long id) {
		repository.deleteById(id);
	}

	@Transactional
	public Booking addBooking(Booking booking) {
		// check if booking is valid
		if (isValid(booking)) {
			return repository.save(booking);
		}
		return null;
	}

	@Transactional
	public Booking modifyBooking(Booking booking) {
		// check if id is present and if yes, modify booking.
		// also check if modification is valid (e.g. similar to validation of
		// addBooking)
		Optional<Booking> oldBooking = repository.findById(booking.getId());
		if (!oldBooking.isPresent()) {
			return null;
		}
		if (isValid(booking)) {
			return repository.save(booking);
		}
		return null;
	}

	public List<Booking> getBookingsForDate(LocalDate date) {
		return repository.findByDate(date);
	}

	private boolean isValid(Booking booking) {
		// do all kind of checks here - is there a table for required number of people
		// and given date and time
		// return false if no valid
		return true;
	}
}