package library.catalog;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.validator.routines.ISBNValidator;

import library.inventory.Volume;

public class Book {

	private final Id id;
	private final Title title;
	private final Isbn isbn;

	private Book(Snapshot command) {
		Objects.requireNonNull(command);
		this.id = command.id();
		this.title = command.title();
		this.isbn = command.isbn();
	}

	public static Book load(Snapshot snapshot) {
		return new Book(snapshot);
	}

	private Book(Add command, Title title) {
		Objects.requireNonNull(command);
		Objects.requireNonNull(title);
		this.id = Id.generate();
		this.title = title;
		this.isbn = command.isbn();
	}

	public static Book add(Add command, Title title) {
		return new Book(command, title);
	}

	public Snapshot snapshot() {
		return new Snapshot(
			id,
			title,
			isbn
		);
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

	public record Title(String value) {

		public Title {
			Objects.requireNonNull(value, "book title missing");
			if (value.isBlank())
				throw new IllegalArgumentException("book title must not be blank");
		}

		public static Title fromString(String value) {
			return new Title(value);
		}

		@Override
		public String toString() {
			return value;
		}

	}

	public record Isbn(String value) {

		private static final ISBNValidator VALIDATOR = new ISBNValidator();

		public Isbn {
			Objects.requireNonNull(value, "book isbn missing");
			if (value.isBlank())
				throw new IllegalArgumentException("book isbn must not be blank");
			if (!VALIDATOR.isValid(value))
				throw new IllegalArgumentException("invalid isbn: " + value);
		}

	}

	public record Snapshot(
		Id id,
		Title title,
		Isbn isbn
	) {

		public Snapshot {
			Objects.requireNonNull(id, "book id missing");
			Objects.requireNonNull(title, "book title missing");
			Objects.requireNonNull(isbn, "book isbn missing");
		}

		public <T> T map(Function<Snapshot, T> function) {
			return function.apply(this);
		}

	}

	public record Add(Isbn isbn) {

		public Add {
			Objects.requireNonNull(isbn, "book isbn missing");
		}

	}

	public record Added(Snapshot snapshot) {

		public Added {
			Objects.requireNonNull(snapshot, "book missing");
		}

	}

}
