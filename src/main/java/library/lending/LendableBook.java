package library.lending;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
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

	private LendableBook(Rent rent) {
		Objects.requireNonNull(rent, "checkout missing");

		this.id = Id.generate();
		this.volumeId = rent.volumeId();
		this.userId = rent.userId();
	}

	static LendableBook rent(Rent rent) {
		return new LendableBook(rent);
	}

	private LendableBook(Load load) {
		Objects.requireNonNull(load);
		this.id = load.id();
		this.volumeId = load.volumeId();
		this.userId = load.userId();
		this.createdAt = load.createdAt();
		this.expectedReturnDate = load.expectedReturnDate();
		this.returnedAt = load.returnedAt();
	}

	public static LendableBook load(Load load) {
		return new LendableBook(load);
	}

	LendableBook checkin() {
		this.returnedAt = LocalDateTime.now();
		if (this.returnedAt.isAfter(expectedReturnDate.atStartOfDay())) {
			// calculate fee
		}
		return this;
	}

	public <T> T map(Function<LendableBook, T> function) {
		return function.apply(this);
	}

	public Id id() {
		return id;
	}

	public Volume.Id volumeId() {
		return volumeId;
	}

	public UserId userId() {
		return userId;
	}

	public Optional<LocalDateTime> createdAt() {
		return Optional.ofNullable(createdAt);
	}

	public Optional<LocalDate> expectedReturnDate() {
		return Optional.ofNullable(expectedReturnDate);
	}

	public Optional<LocalDateTime> returnedAt() {
		return Optional.ofNullable(returnedAt);
	}

	public record Load(
		Id id,
		Volume.Id volumeId,
		UserId userId,
		LocalDateTime createdAt,
		LocalDate expectedReturnDate,
		LocalDateTime returnedAt
	) {

		public Load {
			Objects.requireNonNull(id, "book id missing");
			Objects.requireNonNull(volumeId, "volume id missing");
			Objects.requireNonNull(userId, "user id missing");
			Objects.requireNonNull(createdAt, "created at missing");
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

	public record Rented(LendableBook book) {

		public Rented {
			Objects.requireNonNull(book, "book missing");
		}

	}

	public record Return(Id id) {

		public Return {
			Objects.requireNonNull(id, "book id missing");
		}

	}

	public record Returned(LendableBook book) {

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
