package library.inventory;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import library.catalog.Book;

public class Volume {

	private final Id id;
	private final Book.Id bookId;
	private final BarCode barCode;
	private boolean available;

	private Volume(Load load) {
		Objects.requireNonNull(load, "volume snapshot missing");
		this.id = load.id();
		this.bookId = load.bookId();
		this.barCode = load.barCode();
		this.available = load.available();
	}

	public static Volume load(Load load) {
		return new Volume(load);
	}

	private Volume(Cataloging command) {
		this.id = Id.generate();
		this.bookId = command.bookId();
		this.barCode = command.barCode();
		this.available = true;
	}

	public static Volume cataloging(Cataloging command) {
		return new Volume(command);
	}

	public Volume loan() {
		this.available = false;
		return this;
	}

	public Volume checkIn() {
		this.available = true;
		return this;
	}

	public <T> T map(Function<Volume, T> function) {
		return function.apply(this);
	}

	public Id id() {
		return id;
	}

	public Book.Id bookId() {
		return bookId;
	}

	public BarCode barCode() {
		return barCode;
	}

	public boolean available() {
		return available;
	}

	public record Id(UUID value) {

		public Id {
			Objects.requireNonNull(value, "volume id missing");
		}

		public static Id generate() {
			return new Id(UUID.randomUUID());
		}

		public static Id fromString(String value) {
			return new Id(UUID.fromString(value));
		}

		@Override
		public String toString() {
			return value.toString();
		}

	}

	public record BarCode(String code) {

		public BarCode {
			Objects.requireNonNull(code, "Volume bar code missing");
			if (code.isBlank())
				throw new IllegalArgumentException("Volume bar code must not be blank");
		}

	}

	public record Load(
		Id id,
		Book.Id bookId,
		BarCode barCode,
		boolean available
	) {

		public Load {
			Objects.requireNonNull(id, "volume id missing");
			Objects.requireNonNull(bookId, "book id missing");
			Objects.requireNonNull(barCode, "bar code missing");
		}

	}

	public record Loan(Id id) {

		public Loan {
			Objects.requireNonNull(id, "volume id missing");
		}

	}

	public record Loaned(Volume volume) {

		public Loaned {
			Objects.requireNonNull(volume, "volume missing");
		}

	}

	public record Checkin(Id id) {

		public Checkin {
			Objects.requireNonNull(id, "volume id missing");
		}

	}

	public record CheckedIn(Volume volume) {

		public CheckedIn {
			Objects.requireNonNull(volume, "volume missing");
		}

	}

	public record Cataloging(Book.Id bookId, BarCode barCode) {

		public Cataloging {
			Objects.requireNonNull(bookId, "book id missing");
			Objects.requireNonNull(barCode, "bar code missing");
		}

	}

	public record Catalogued(Volume volume) {

		public Catalogued {
			Objects.requireNonNull(volume, "volume missing");
		}

	}

	public static class NotFound extends RuntimeException {

		public NotFound(Id id) {
			super("Volume not found: " + id);
		}

	}

}
