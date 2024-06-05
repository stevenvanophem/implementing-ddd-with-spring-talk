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

	private Volume(Snapshot snapshot) {
		Objects.requireNonNull(snapshot, "volume snapshot missing");
		this.id = snapshot.id();
		this.bookId = snapshot.bookId();
		this.barCode = snapshot.barCode();
		this.available = snapshot.available();
	}

	public static Volume load(Snapshot snapshot) {
		return new Volume(snapshot);
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

	public Snapshot snapshot() {
		return new Snapshot(
			id,
			bookId,
			barCode,
			available
		);
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

	public record Snapshot(
		Id id,
		Book.Id bookId,
		BarCode barCode,
		boolean available
	) {

		public Snapshot {
			Objects.requireNonNull(id, "volume id missing");
			Objects.requireNonNull(bookId, "book id missing");
			Objects.requireNonNull(barCode, "bar code missing");
		}

		public <T> T map(Function<Snapshot, T> function) {
			return function.apply(this);
		}

	}

	public record Loan(Id id) {

		public Loan {
			Objects.requireNonNull(id, "volume id missing");
		}

	}

	public record Loaned(Volume.Snapshot volume) {

		public Loaned {
			Objects.requireNonNull(volume, "volume missing");
		}

	}

	public record Checkin(Id id) {

		public Checkin {
			Objects.requireNonNull(id, "volume id missing");
		}

	}

	public record CheckedIn(Volume.Snapshot volume) {

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

	public record Catalogued(Volume.Snapshot volume) {

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
