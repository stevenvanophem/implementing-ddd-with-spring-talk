package library.lending.jdbc;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;

import library.lending.CirculatingBook;
import library.lending.CirculationRepository;

public class CirculationJdbcRepository implements CirculationRepository {

	private static final Logger logger = LoggerFactory.getLogger(CirculationJdbcRepository.class);

	private final JdbcClient jdbcClient;

	public CirculationJdbcRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	@Override
	public boolean isAvailable(CirculatingBook.CopyId id) {
		Objects.requireNonNull(id);

		final String sql = """
			select count(*) = 0 
			from Loan 
			where copyId = :id 
			and returnedAt is null
			""";

		logger.debug("Check if copy {} is available", id);
		logger.trace("{} - {}", sql, id);

		Integer result = jdbcClient.sql(sql)
			.param("id", id)
			.query(Integer.class)
			.single();

		logger.trace("Got {} rows", result);

		return result > 0;
	}

	@Override
	public CirculatingBook.Snapshot save(CirculatingBook.Snapshot circulatingBook) {
		Objects.requireNonNull(circulatingBook);

		logger.debug("Save circulating book");

		if (this.alreadyExists(circulatingBook.id())) {
			return this.update(circulatingBook);
		}
		return this.insert(circulatingBook);
	}

	@Override
	public Optional<CirculatingBook.Snapshot> findById(CirculatingBook.Id id) {
		Objects.requireNonNull(id);

		final String sql = """
			select * from Loan
			where id = :id
			""";

		logger.debug("Find circulating book by id {}", id);
		logger.trace("{} - {}", sql, id);

		return jdbcClient.sql(sql)
			.param("id", id)
			.query(new CirculatingBookSnapshotRowMapper())
			.optional();
	}

	private boolean alreadyExists(CirculatingBook.Id id) {
		String sql = """
			select count(*) > 0 
			from Loan 
			where id = :id
			""";

		Integer result = jdbcClient.sql(sql)
			.param("id", id)
			.query(Integer.class)
			.single();

		return result > 0;
	}

	private CirculatingBook.Snapshot update(CirculatingBook.Snapshot circulatingBook) {
		Objects.requireNonNull(circulatingBook);

		final var params = CirculationSqlParamsFactory.create(circulatingBook);
		final String sql = """
			UPDATE Loan
			SET copyId = :copyId,
				userId = :userId,
				expectedReturnDate = :expectedReturnDate,
				returnedAt = :returnedAt
			WHERE id = :id
			""";

		logger.debug("Update circulating book");
		logger.trace("{} - {}", sql, params);

		jdbcClient.sql(sql)
			.params(params)
			.update();

		return circulatingBook;
	}

	private CirculatingBook.Snapshot insert(CirculatingBook.Snapshot circulatingBook) {
		Objects.requireNonNull(circulatingBook);

		final var params = CirculationSqlParamsFactory.create(circulatingBook);
		final String sql = """
			INSERT INTO Loan (id, copyId, userId, expectedReturnDate, returnedAt)
			VALUES (:id, :copyId, :userId, :expectedReturnDate, :returnedAt)
			""";

		logger.debug("Insert circulating book");
		logger.trace("{} - {}", sql, params);

		jdbcClient.sql(sql)
			.params(params)
			.update();

		return circulatingBook;
	}

}
