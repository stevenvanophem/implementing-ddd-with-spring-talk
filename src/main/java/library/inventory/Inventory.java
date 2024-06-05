package library.inventory;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Inventory {

	private static final Logger logger = LoggerFactory.getLogger(Inventory.class);

	private final VolumeRepository repository;
	private final VolumeEventPublisher eventPublisher;

	public Inventory(VolumeRepository repository, VolumeEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
		this.repository = repository;
	}

	public Volume cataloging(Volume.Cataloging command) {
		Objects.requireNonNull(command);

		logger.debug("Cataloging a new volume");
		logger.trace("{}", command);

		return Volume.cataloging(command)
			.map(repository::save)
			.map(eventPublisher::cataloged);
	}

	public Volume loan(Volume.Loan command) {
		Objects.requireNonNull(command);

		final Volume.Id id = command.id();

		logger.debug("Loan volume {}", id);
		logger.trace("{}", command);

		return repository.findById(id).orElseThrow(() -> new Volume.NotFound(id))
			.loan()
			.map(repository::save)
			.map(eventPublisher::loaned);
	}

	public Volume checkIn(Volume.Checkin command) {
		Objects.requireNonNull(command);

		final Volume.Id id = command.id();

		logger.debug("Checkin volume {}", id);
		logger.trace("{}", command);

		return repository.findById(id).orElseThrow(() -> new Volume.NotFound(id))
			.checkIn()
			.map(repository::save)
			.map(eventPublisher::checkedIn);
	}

}
