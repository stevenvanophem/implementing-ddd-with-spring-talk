package library.catalog;

public interface BookEventPublisher {

	void publish(Object event);

	default Book.Snapshot added(Book.Snapshot snapshot) {
		Book.Added added = new Book.Added(snapshot);
		this.publish(added);
		return snapshot;
	}

}
