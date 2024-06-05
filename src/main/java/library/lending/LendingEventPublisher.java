package library.lending;

import java.util.Objects;

public interface LendingEventPublisher {

	void publish(Object event);

	default LendableBook.Snapshot checkedOut(LendableBook.Snapshot book) {
		Objects.requireNonNull(book);
		final var event = new LendableBook.Rented(book);
		this.publish(event);
		return book;
	}

	default LendableBook.Snapshot checkedIn(LendableBook.Snapshot book) {
		Objects.requireNonNull(book);
		final var event = new LendableBook.Returned(book);
		this.publish(event);
		return book;
	}

}
