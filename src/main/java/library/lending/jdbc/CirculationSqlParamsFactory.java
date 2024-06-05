package library.lending.jdbc;

import java.util.Map;

import library.lending.CirculatingBook;

class CirculationSqlParamsFactory {

	private CirculationSqlParamsFactory() {
	}

	static Map<String, Object> create(CirculatingBook.Snapshot circulatingBook) {
		return Map.of(
			"id", circulatingBook.id(),
			"copyId", circulatingBook.copyId(),
			"userId", circulatingBook.userId(),
			"expectedReturnDate", circulatingBook.expectedReturnDate(),
			"returnedAt", circulatingBook.returnedAt()
		);
	}

}
