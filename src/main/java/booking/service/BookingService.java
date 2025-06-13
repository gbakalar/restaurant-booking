package booking.service;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import booking.data.*;

@Service
public class BookingService {
	private final BookingRepository repository;

	// @formatter:off
	private final List<Table> tables = Arrays.asList(
			new Table(1, 4),
			new Table(2, 4),
			new Table(3, 5),
			new Table(4, 4),
			new Table(5, 6));
	// @formatter:on

	public BookingService(BookingRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void deleteBooking(Long id) {
		repository.deleteById(id);
	}

	@Transactional
	public Booking addBooking(Booking booking) {
		// check if booking is possible
		Optional<Table> table = findFreeTable(getBookingsForDate(booking.getDate()), booking);
		if (table.isPresent()) {
			booking.setTableId(table.get().getId());
			return repository.save(booking);
		}
		return null;
	}

	@Transactional
	public Booking modifyBooking(Booking booking) {
		// check if id is present and if yes, modify booking.
		// also check if modification is possible (e.g. similar to addBooking)
		Optional<Booking> oldBooking = repository.findById(booking.getId());
		if (!oldBooking.isPresent()) {
			// TODO - throw an RuntimeException to distinguish errors from no-free-table vs
			// not 'id' present or similar
			return null;
		}
		List<Booking> list = getBookingsForDate(oldBooking.get().getDate()).stream()
				.filter(b -> !Objects.equals(b.getId(), booking.getId())).collect(Collectors.toList());
		Optional<Table> table = findFreeTable(list, booking);
		if (table.isPresent()) {
			booking.setTableId(table.get().getId());
			return repository.save(booking);
		}
		return null;
	}

	public List<Booking> getBookingsForDate(LocalDate date) {
		return repository.findByDate(date);
	}

	private Optional<Table> findFreeTable(List<Booking> bookings, Booking booking) {
		LocalTime endTime = booking.getTime().plusHours(2);

		for (Table table : tables) {
			boolean isAvailable = bookings.stream()
					.filter(b -> b.getTableId() == table.getId() && b.getDate().equals(booking.getDate())).noneMatch(
							b -> intervalsOverlap(b.getTime(), b.getTime().plusHours(2), booking.getTime(), endTime));

			if (isAvailable && table.getSize() >= booking.getTableSize()) {
				return Optional.of(table);
			}
		}

		return Optional.empty();
	}

	private boolean intervalsOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
		return !end1.isBefore(start2) && !start1.isAfter(end2);
	}

}
