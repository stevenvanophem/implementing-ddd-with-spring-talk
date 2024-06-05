package library.lending.spring;

import javax.sql.DataSource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import library.lending.Circulation;
import library.lending.CirculationEventPublisher;
import library.lending.jdbc.CirculationJdbcRepository;

@Configuration
public class CirculationSpringConfiguration {

	@Bean
	public Circulation circulation(DataSource dataSource, ApplicationEventPublisher applicationEventPublisher) {
		final var jdbcClient = JdbcClient.create(dataSource);
		final var repository = new CirculationJdbcRepository(jdbcClient);
		final CirculationEventPublisher eventPublisher = applicationEventPublisher::publishEvent;
		return new Circulation(repository, eventPublisher);
	}

}
