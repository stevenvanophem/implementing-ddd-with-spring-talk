package library.lending.spring;

import javax.sql.DataSource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import library.lending.LendingService;
import library.lending.LendingEventPublisher;
import library.lending.jdbc.LendingJdbcRepository;

@Configuration
public class LendingSpringConfiguration {

	@Bean
	public LendingService lendingService(DataSource dataSource, ApplicationEventPublisher applicationEventPublisher) {
		final var jdbcClient = JdbcClient.create(dataSource);
		final var repository = new LendingJdbcRepository(jdbcClient);
		final LendingEventPublisher eventPublisher = applicationEventPublisher::publishEvent;
		return new LendingService(repository, eventPublisher);
	}

}
