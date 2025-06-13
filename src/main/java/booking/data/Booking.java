package booking.data;

import java.time.*;

import javax.persistence.*;

@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String customerName;
	private int tableSize;
	private LocalDate date;
	private LocalTime time;

	public Booking() {
	}

	public Booking(String customerName, int tableSize, LocalDate date, LocalTime time) {
		this.customerName = customerName;
		this.tableSize = tableSize;
		this.date = date;
		this.time = time;
	}

	public Long getId() {
		return id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getTableSize() {
		return tableSize;
	}

	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public boolean isValid() {
		return !(getCustomerName() == null || getDate() == null || getTime() == null || getTableSize() <= 0);
	}
}