package library.inventory.spring;

import javax.sql.DataSource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import library.inventory.Inventory;
import library.inventory.VolumeEventPublisher;
import library.inventory.jdbc.VolumeJdbcRepository;

@Configuration
public class InventorySpringConfiguration {

	@Bean
	public Inventory inventory(DataSource dataSource, ApplicationEventPublisher applicationEventPublisher) {
		final var jdbcClient = JdbcClient.create(dataSource);
		final var repository = new VolumeJdbcRepository(jdbcClient);
		VolumeEventPublisher eventPublisher = applicationEventPublisher::publishEvent;
		return new Inventory(repository, eventPublisher);
	}

}
