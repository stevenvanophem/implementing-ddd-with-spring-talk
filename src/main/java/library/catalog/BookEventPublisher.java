package library.catalog;

public interface BookEventPublisher {

	void publish(Object event);

	default Book added(Book book) {
		final var added = new Book.Added(book);
		this.publish(added);
		return book;
	}

}
