package booking.service;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import booking.data.*;

@Service
public class BookingService {
	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private RestTableRepository tableRepo;

	@Transactional
	public void deleteBooking(Long id) {
		bookingRepo.deleteById(id);
	}

	@Transactional
	public Booking addBooking(Booking booking) {
		// check if booking is possible
		Optional<RestTable> table = findFreeTable(bookingRepo.findByDate(booking.getDate()), booking);
		if (table.isPresent()) {
			booking.setTableId(table.get().getId());
			return bookingRepo.save(booking);
		}
		return null;
	}

	@Transactional
	public Booking modifyBooking(Booking booking) {
		// check if id is present and if yes, modify booking.
		// also check if modification is possible (e.g. similar to addBooking)
		Optional<Booking> oldBooking = bookingRepo.findById(booking.getId());
		if (!oldBooking.isPresent()) {
			// TODO - throw an RuntimeException to distinguish errors from no-free-table vs
			// not 'id' present or similar
			return null;
		}
		List<Booking> list = bookingRepo.findByDate(oldBooking.get().getDate()).stream()
				.filter(b -> !Objects.equals(b.getId(), booking.getId())).collect(Collectors.toList());
		Optional<RestTable> table = findFreeTable(list, booking);
		if (table.isPresent()) {
			booking.setTableId(table.get().getId());
			return bookingRepo.save(booking);
		}
		return null;
	}

	public List<Booking> getBookingsForDate(LocalDate date) {
		return bookingRepo.findByDate(date);
	}

	// this is the key point
	private Optional<RestTable> findFreeTable(List<Booking> bookings, Booking booking) {
		// select only tables that have capacity for the request
		// sort them in ascending order so that the first one is
		// the already min capacity that satisfies request 
		List<RestTable> tables = tableRepo.findAllBySize(booking.getTableSize());

		for (RestTable table : tables) {

			// Lock to prevent double booking in case when multiple
			// bookings are concurrently trying to book a slot.
			// Unlock will happen when transaction ends (commit or roll-back)
			tableRepo.findByIdForUpdate(table.getId());

			// @formatter:off
			boolean isAvailable = bookings.stream()
					.filter(b -> b.getTableId() == table.getId() && b.getDate().equals(booking.getDate()))
					.noneMatch(b -> intervalsOverlap(
							b.getTime(), b.getTime().plusHours(1).plusMinutes(59),
							booking.getTime(), booking.getTime().plusHours(1).plusMinutes(59))
					);
			// @formatter:on

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
