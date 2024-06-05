package library.inventory.spring;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import library.inventory.Inventory;
import library.inventory.Volume;
import library.lending.LendableBook;

@Component
public class InventoryEventListener {

	private static final Logger logger = LoggerFactory.getLogger(InventoryEventListener.class);

	private final Inventory inventory;

	public InventoryEventListener(Inventory inventory) {
		this.inventory = inventory;
	}

	@ApplicationModuleListener
	public void handle(LendableBook.Rented event) {
		Objects.requireNonNull(event);

		logger.debug("Handling book rented event in inventory");
		logger.trace("{}", event);

		final Volume.Id volumeId = event.book().volumeId();
		final var command = new Volume.Loan(volumeId);

		Volume volume = inventory.loan(command);
		logger.debug("Volume loaned: {}", volume);
	}

	@ApplicationModuleListener
	public void handle(LendableBook.Returned event) {
		Objects.requireNonNull(event);

		logger.debug("Handling book returned event in inventory");
		logger.trace("{}", event);

		final Volume.Id volumeId = event.book().volumeId();
		final var command = new Volume.Checkin(volumeId);

		Volume volume = inventory.checkIn(command);
		logger.debug("Volume checked in: {}", volume);
	}

}
