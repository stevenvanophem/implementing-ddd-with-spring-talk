package library.inventory.jdbc;

import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;

import library.inventory.Volume;
import library.inventory.VolumeRepository;

public class VolumeJdbcRepository implements VolumeRepository {

	private final JdbcClient jdbcClient;

	public VolumeJdbcRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	@Override
	public Optional<Volume> findById(Volume.Id id) {
		return Optional.empty();
	}

	@Override
	public Volume save(Volume volume) {
		return null;
	}

}
