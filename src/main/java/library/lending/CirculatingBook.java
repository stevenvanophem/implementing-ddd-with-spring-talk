package library.lending;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class CirculatingBook {

	private final Id id;
	private final CopyId copyId;
	private final UserId userId;
	private LocalDateTime createdAt;
	private LocalDate expectedReturnDate;
	private LocalDateTime returnedAt;

	private CirculatingBook(Id id, Checkout checkout) {
		Objects.requireNonNull(id, "circulating book id missing");
		Objects.requireNonNull(checkout, "checkout missing");

		this.id = id;
		this.copyId = checkout.copyId();
		this.userId = checkout.userId();
	}

	static CirculatingBook checkout(Id id, Checkout checkout) {
		return new CirculatingBook(id, checkout);
	}

	private CirculatingBook(Snapshot snapshot) {
		Objects.requireNonNull(snapshot);
		this.id = snapshot.id();
		this.copyId = snapshot.copyId();
		this.userId = snapshot.userId();
		this.createdAt = snapshot.createdAt();
		this.expectedReturnDate = snapshot.expectedReturnDate();
		this.returnedAt = snapshot.returnedAt();
	}

	static CirculatingBook load(Snapshot snapshot) {
		return new CirculatingBook(snapshot);
	}

	CirculatingBook checkin() {
		this.returnedAt = LocalDateTime.now();
		if (this.returnedAt.isAfter(expectedReturnDate.atStartOfDay())) {
			// calculate fee
		}
		return this;
	}

	public Snapshot snapshot() {
		return new Snapshot(id, copyId, userId, createdAt, expectedReturnDate, returnedAt);
	}

	public record Snapshot(
		Id id,
		CopyId copyId,
		UserId userId,
		LocalDateTime createdAt,
		LocalDate expectedReturnDate,
		LocalDateTime returnedAt
	) {

		public Snapshot {
			Objects.requireNonNull(id, "circulating book id missing");
			Objects.requireNonNull(copyId, "copy id missing");
			Objects.requireNonNull(userId, "user id missing");
			Objects.requireNonNull(createdAt, "created at missing");
		}

		public <T> T map(Function<Snapshot, T> function) {
			return function.apply(this);
		}

	}

	public record Id(UUID value) {

		public Id {
			Objects.requireNonNull(value, "circulating book id missing");
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

	public record CopyId(UUID value) {

		public CopyId {
			Objects.requireNonNull(value, "copy id missing");
		}

		public static CopyId generate() {
			return new CopyId(UUID.randomUUID());
		}

		public static CopyId fromString(String value) {
			return new CopyId(UUID.fromString(value));
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

	public record Checkout(CopyId copyId, UserId userId) {

		public Checkout {
			Objects.requireNonNull(copyId, "copy id missing");
			Objects.requireNonNull(userId, "user id missing");
		}

	}

	public record CheckedOut(CirculatingBook.Snapshot circulatingBook) {

		public CheckedOut {
			Objects.requireNonNull(circulatingBook, "circulating book missing");
		}

	}

	public record Checkin(Id id) {

		public Checkin {
			Objects.requireNonNull(id, "circulating book id missing");
		}

	}

	public record CheckedIn(CirculatingBook.Snapshot circulatingBook) {

		public CheckedIn {
			Objects.requireNonNull(circulatingBook, "circulating book missing");
		}

	}

	public static class Unavailable extends RuntimeException {

		public Unavailable(CopyId copyId) {
			super("Copy " + copyId + " is not available");
		}

	}

	public static class Unknown extends RuntimeException {

		public Unknown(Id id) {
			super("Circulating book " + id + " is unknown");
		}

	}

}
