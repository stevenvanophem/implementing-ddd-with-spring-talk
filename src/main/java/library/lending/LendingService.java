package library.lending;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import library.inventory.Volume;

public class LendingService {

	private static final Logger logger = LoggerFactory.getLogger(LendingService.class);

	private final LendingRepository repository;
	private final LendingEventPublisher eventPublisher;

	public LendingService(
		LendingRepository repository,
		LendingEventPublisher eventPublisher
	) {
		this.eventPublisher = eventPublisher;
		this.repository = repository;
	}

	public LendableBook rent(LendableBook.Rent command) {
		Objects.requireNonNull(command);

		final Volume.Id volumeId = command.volumeId();
		final LendableBook.Id id = LendableBook.Id.generate();

		logger.debug("Checkout volume {} as loan {}", volumeId, id);
		logger.trace("{}", command);

		if (!repository.isAvailable(volumeId))
			throw new LendableBook.Unavailable(volumeId);

		return LendableBook.rent(command)
			.map(repository::save)
			.map(eventPublisher::checkedOut);
	}

	public LendableBook checkin(LendableBook.Return command) {
		Objects.requireNonNull(command);

		final LendableBook.Id id = command.id();

		logger.debug("Checkin loan {}", id);
		logger.trace("{}", command);

		return repository.findById(id).orElseThrow(() -> new LendableBook.Unknown(id))
			.checkin()
			.map(repository::save)
			.map(eventPublisher::checkedIn);
	}

}
