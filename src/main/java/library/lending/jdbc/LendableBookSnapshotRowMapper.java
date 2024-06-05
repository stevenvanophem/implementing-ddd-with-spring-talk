package library.lending.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import library.inventory.Volume;
import library.lending.LendableBook;

class LendableBookSnapshotRowMapper implements RowMapper<LendableBook> {

	@Override
	public LendableBook mapRow(@NonNull ResultSet resultSet, int rowNum) throws SQLException {
		final LocalDateTime createdAt = resultSet.getObject("createdAt", LocalDateTime.class);
		final LocalDate expectedReturnDate = resultSet.getObject("expectedReturnDate", LocalDate.class);
		final LocalDateTime returnedAt = resultSet.getObject("returnedAt", LocalDateTime.class);

		LendableBook.Load command = new LendableBook.Load(
			LendableBook.Id.fromString(resultSet.getString("id")),
			Volume.Id.fromString(resultSet.getString("volumeId")),
			LendableBook.UserId.fromString(resultSet.getString("userId")),
			createdAt,
			expectedReturnDate,
			returnedAt
		);
		return LendableBook.load(command);
	}

}
