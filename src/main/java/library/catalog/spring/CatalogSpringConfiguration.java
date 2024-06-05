package library.catalog.spring;

import javax.sql.DataSource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.client.RestClient;

import library.catalog.BookEventPublisher;
import library.catalog.Catalog;
import library.catalog.jdbc.BookJdbcRepository;
import library.catalog.openlibrary.OpenLibraryRemoteKnowledgeBase;

@Configuration
public class CatalogSpringConfiguration {

	@Bean
	public Catalog catalog(
		DataSource dataSource,
		RestClient.Builder restClientBuilder,
		ApplicationEventPublisher applicationEventPublisher
	) {
		final var jdbcClient = JdbcClient.create(dataSource);
		final var repository = new BookJdbcRepository(jdbcClient);
		final var knowledgeBase = new OpenLibraryRemoteKnowledgeBase(restClientBuilder);
		BookEventPublisher eventPublisher = applicationEventPublisher::publishEvent;
		return new Catalog(repository, eventPublisher, knowledgeBase);
	}

}
