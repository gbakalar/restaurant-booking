package booking.service;

// this should be persisted, but for simplicity we can keep it in memory
public class Table {
	private final int id;
	private final int size;

	public Table(int id, int size) {
		this.id = id;
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public int getSize() {
		return size;
	}
}