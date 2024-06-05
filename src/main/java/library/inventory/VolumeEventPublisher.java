package library.inventory;

public interface VolumeEventPublisher {

	void publish(Object event);

	default Volume loaned(Volume volume) {
		final var event = new Volume.Loaned(volume);
		this.publish(event);
		return volume;
	}

	default Volume checkedIn(Volume volume) {
		final var event = new Volume.CheckedIn(volume);
		this.publish(event);
		return volume;
	}

	default Volume cataloged(Volume volume) {
		final var event = new Volume.Catalogued(volume);
		this.publish(event);
		return volume;
	}

}
