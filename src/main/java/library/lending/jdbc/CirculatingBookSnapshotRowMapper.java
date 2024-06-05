package library.lending.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import library.lending.CirculatingBook;

class CirculatingBookSnapshotRowMapper implements RowMapper<CirculatingBook.Snapshot> {

	@Override
	public CirculatingBook.Snapshot mapRow(@NonNull ResultSet resultSet, int rowNum) throws SQLException {
		final LocalDateTime createdAt = resultSet.getObject("createdAt", LocalDateTime.class);
		final LocalDate expectedReturnDate = resultSet.getObject("expectedReturnDate", LocalDate.class);
		final LocalDateTime returnedAt = resultSet.getObject("returnedAt", LocalDateTime.class);

		return new CirculatingBook.Snapshot(
			CirculatingBook.Id.fromString(resultSet.getString("id")),
			CirculatingBook.CopyId.fromString(resultSet.getString("copyId")),
			CirculatingBook.UserId.fromString(resultSet.getString("userId")),
			createdAt,
			expectedReturnDate,
			returnedAt
		);
	}

}
