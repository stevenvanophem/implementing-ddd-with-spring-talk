package library.lending;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Circulation {

	private static final Logger logger = LoggerFactory.getLogger(Circulation.class);

	private final CirculationRepository repository;
	private final CirculationEventPublisher eventPublisher;

	public Circulation(
		CirculationRepository repository,
		CirculationEventPublisher eventPublisher
	) {
		this.eventPublisher = eventPublisher;
		this.repository = repository;
	}

	public CirculatingBook.Snapshot checkout(CirculatingBook.Checkout command) {
		Objects.requireNonNull(command);

		final CirculatingBook.CopyId copyId = command.copyId();
		final CirculatingBook.Id id = CirculatingBook.Id.generate();

		logger.debug("Checkout copy {} as loan {}", copyId, id);
		logger.trace("{}", command);

		if (!repository.isAvailable(copyId))
			throw new CirculatingBook.Unavailable(copyId);

		return CirculatingBook.checkout(id, command).snapshot()
			.map(repository::save)
			.map(eventPublisher::checkedOut);
	}

	public CirculatingBook.Snapshot checkin(CirculatingBook.Checkin command) {
		Objects.requireNonNull(command);

		final CirculatingBook.Id id = command.id();

		logger.debug("Checkin loan {}", id);
		logger.trace("{}", command);

		return repository.findById(id).orElseThrow(() -> new CirculatingBook.Unknown(id))
			.map(CirculatingBook::load)
			.checkin()
			.snapshot()
			.map(repository::save)
			.map(eventPublisher::checkedIn);
	}

}
