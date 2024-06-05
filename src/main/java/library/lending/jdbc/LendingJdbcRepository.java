package library.lending.jdbc;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;

import library.lending.LendableBook;
import library.lending.LendingRepository;

public class LendingJdbcRepository implements LendingRepository {

	private static final Logger logger = LoggerFactory.getLogger(LendingJdbcRepository.class);

	private final JdbcClient jdbcClient;

	public LendingJdbcRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	@Override
	public boolean isAvailable(LendableBook.CopyId id) {
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
	public LendableBook.Snapshot save(LendableBook.Snapshot book) {
		Objects.requireNonNull(book);

		logger.debug("Save circulating book");

		if (this.alreadyExists(book.id())) {
			return this.update(book);
		}
		return this.insert(book);
	}

	@Override
	public Optional<LendableBook.Snapshot> findById(LendableBook.Id id) {
		Objects.requireNonNull(id);

		final String sql = """
			select * from Loan
			where id = :id
			""";

		logger.debug("Find circulating book by id {}", id);
		logger.trace("{} - {}", sql, id);

		return jdbcClient.sql(sql)
			.param("id", id)
			.query(new LendableBookSnapshotRowMapper())
			.optional();
	}

	private boolean alreadyExists(LendableBook.Id id) {
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

	private LendableBook.Snapshot update(LendableBook.Snapshot circulatingBook) {
		Objects.requireNonNull(circulatingBook);

		final var params = LendingSqlParamsFactory.create(circulatingBook);
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

	private LendableBook.Snapshot insert(LendableBook.Snapshot circulatingBook) {
		Objects.requireNonNull(circulatingBook);

		final var params = LendingSqlParamsFactory.create(circulatingBook);
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
