package library.lending;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public LendableBook.Snapshot rent(LendableBook.Rent command) {
		Objects.requireNonNull(command);

		final LendableBook.CopyId copyId = command.copyId();
		final LendableBook.Id id = LendableBook.Id.generate();

		logger.debug("Checkout copy {} as loan {}", copyId, id);
		logger.trace("{}", command);

		if (!repository.isAvailable(copyId))
			throw new LendableBook.Unavailable(copyId);

		return LendableBook.rent(id, command).snapshot()
			.map(repository::save)
			.map(eventPublisher::checkedOut);
	}

	public LendableBook.Snapshot checkin(LendableBook.Return command) {
		Objects.requireNonNull(command);

		final LendableBook.Id id = command.id();

		logger.debug("Checkin loan {}", id);
		logger.trace("{}", command);

		return repository.findById(id).orElseThrow(() -> new LendableBook.Unknown(id))
			.map(LendableBook::load)
			.checkin()
			.snapshot()
			.map(repository::save)
			.map(eventPublisher::checkedIn);
	}

}
