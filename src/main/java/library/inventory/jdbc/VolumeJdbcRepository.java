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
	public Optional<Volume.Snapshot> findById(Volume.Id id) {
		return Optional.empty();
	}

	@Override
	public Volume.Snapshot save(Volume.Snapshot volume) {
		return null;
	}

}
