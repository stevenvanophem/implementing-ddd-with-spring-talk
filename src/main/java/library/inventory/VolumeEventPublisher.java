package library.inventory;

public interface VolumeEventPublisher {

	void publish(Object event);

	default Volume.Snapshot loaned(Volume.Snapshot volume) {
		final var event = new Volume.Loaned(volume);
		this.publish(event);
		return volume;
	}

	default Volume checkedIn(Volume.Snapshot volume) {
		final var event = new Volume.CheckedIn(volume);
		this.publish(event);
		return Volume.load(volume);
	}

	default Volume.Snapshot cataloged(Volume.Snapshot snapshot) {
		final var event = new Volume.Catalogued(snapshot);
		this.publish(event);
		return snapshot;
	}

}
