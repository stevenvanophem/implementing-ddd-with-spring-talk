package library.lending;

import java.util.Objects;

public interface CirculationEventPublisher {

	void publish(Object event);

	default CirculatingBook.Snapshot checkedOut(CirculatingBook.Snapshot circulatingBook) {
		Objects.requireNonNull(circulatingBook);
		final var event = new CirculatingBook.CheckedOut(circulatingBook);
		this.publish(event);
		return circulatingBook;
	}

	default CirculatingBook.Snapshot checkedIn(CirculatingBook.Snapshot circulatingBook) {
		Objects.requireNonNull(circulatingBook);
		final var event = new CirculatingBook.CheckedIn(circulatingBook);
		this.publish(event);
		return circulatingBook;
	}

}
