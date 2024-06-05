package library.catalog;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.validator.routines.ISBNValidator;

public class Book {

	private final Id id;
	private final Title title;
	private final Isbn isbn;

	private Book(Load command) {
		Objects.requireNonNull(command);
		this.id = command.id();
		this.title = command.title();
		this.isbn = command.isbn();
	}

	public static Book load(Load load) {
		return new Book(load);
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

	public <T> T map(Function<Book, T> function) {
		return function.apply(this);
	}

	public Id id() {
		return id;
	}

	public Title title() {
		return title;
	}

	public Isbn isbn() {
		return isbn;
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

	public record Load(
		Id id,
		Title title,
		Isbn isbn
	) {

		public Load {
			Objects.requireNonNull(id, "book id missing");
			Objects.requireNonNull(title, "book title missing");
			Objects.requireNonNull(isbn, "book isbn missing");
		}

	}

	public record Add(Isbn isbn) {

		public Add {
			Objects.requireNonNull(isbn, "book isbn missing");
		}

	}

	public record Added(Book book) {

		public Added {
			Objects.requireNonNull(book, "book missing");
		}

	}

}
