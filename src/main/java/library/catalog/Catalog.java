package library.catalog;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Catalog {

	private static final Logger logger = LoggerFactory.getLogger(Catalog.class);

	private final BookRepository repository;
	private final BookEventPublisher eventPublisher;
	private final RemoteKnowledgeBase remoteKnowledgeBase;

	public Catalog(
		BookRepository repository,
		BookEventPublisher eventPublisher,
		RemoteKnowledgeBase remoteKnowledgeBase
	) {
		this.remoteKnowledgeBase = remoteKnowledgeBase;
		this.eventPublisher = eventPublisher;
		this.repository = repository;
	}

	public Book add(Book.Add command) {
		Objects.requireNonNull(command);

		final Book.Isbn isbn = command.isbn();
		final Book.Title title = remoteKnowledgeBase.search(isbn);

		logger.debug("Adding new book to catalog");
		logger.trace("{} - {}", command, title);

		return Book.add(command, title)
			.map(repository::save)
			.map(eventPublisher::added);
	}

}
