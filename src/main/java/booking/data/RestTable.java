package booking.data;

import javax.persistence.*;


@Entity
@Table(name = "rest_table")
public class RestTable {
	@Id
	private Long id;
	private int size;

	public RestTable() {
	}

	public RestTable(Long id, int size) {
		this.id = id;
		this.size = size;
	}

	public Long getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

}