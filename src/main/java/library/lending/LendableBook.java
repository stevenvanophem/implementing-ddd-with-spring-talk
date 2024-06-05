package library.lending;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import library.inventory.Volume;

public class LendableBook {

	private final Id id;
	private final Volume.Id volumeId;
	private final UserId userId;
	private LocalDateTime createdAt;
	private LocalDate expectedReturnDate;
	private LocalDateTime returnedAt;

	private LendableBook(Id id, Rent rent) {
		Objects.requireNonNull(id, "lendable book id missing");
		Objects.requireNonNull(rent, "checkout missing");

		this.id = id;
		this.volumeId = rent.volumeId();
		this.userId = rent.userId();
	}

	static LendableBook rent(Id id, Rent rent) {
		return new LendableBook(id, rent);
	}

	private LendableBook(Snapshot snapshot) {
		Objects.requireNonNull(snapshot);
		this.id = snapshot.id();
		this.volumeId = snapshot.volumeId();
		this.userId = snapshot.userId();
		this.createdAt = snapshot.createdAt();
		this.expectedReturnDate = snapshot.expectedReturnDate();
		this.returnedAt = snapshot.returnedAt();
	}

	static LendableBook load(Snapshot snapshot) {
		return new LendableBook(snapshot);
	}

	LendableBook checkin() {
		this.returnedAt = LocalDateTime.now();
		if (this.returnedAt.isAfter(expectedReturnDate.atStartOfDay())) {
			// calculate fee
		}
		return this;
	}

	public Snapshot snapshot() {
		return new Snapshot(
			id,
			volumeId,
			userId,
			createdAt,
			expectedReturnDate,
			returnedAt
		);
	}

	public record Snapshot(
		Id id,
		Volume.Id volumeId,
		UserId userId,
		LocalDateTime createdAt,
		LocalDate expectedReturnDate,
		LocalDateTime returnedAt
	) {

		public Snapshot {
			Objects.requireNonNull(id, "book id missing");
			Objects.requireNonNull(volumeId, "volume id missing");
			Objects.requireNonNull(userId, "user id missing");
			Objects.requireNonNull(createdAt, "created at missing");
		}

		public <T> T map(Function<Snapshot, T> function) {
			return function.apply(this);
		}

	}

	public record Id(UUID value) {

		public Id {
			Objects.requireNonNull(value, "book id missing");
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

	public record UserId(UUID value) {

		public UserId {
			Objects.requireNonNull(value, "user id missing");
		}

		public static UserId generate() {
			return new UserId(UUID.randomUUID());
		}

		public static UserId fromString(String value) {
			return new UserId(UUID.fromString(value));
		}

		@Override
		public String toString() {
			return value.toString();
		}

	}

	public record Rent(Volume.Id volumeId, UserId userId) {

		public Rent {
			Objects.requireNonNull(volumeId, "volume id missing");
			Objects.requireNonNull(userId, "user id missing");
		}

	}

	public record Rented(LendableBook.Snapshot book) {

		public Rented {
			Objects.requireNonNull(book, "book missing");
		}

	}

	public record Return(Id id) {

		public Return {
			Objects.requireNonNull(id, "book id missing");
		}

	}

	public record Returned(LendableBook.Snapshot book) {

		public Returned {
			Objects.requireNonNull(book, "book missing");
		}

	}

	public static class Unavailable extends RuntimeException {

		public Unavailable(Volume.Id volumeId) {
			super("Copy " + volumeId + " is not available");
		}

	}

	public static class Unknown extends RuntimeException {

		public Unknown(Id id) {
			super("Circulating book " + id + " is unknown");
		}

	}

}
