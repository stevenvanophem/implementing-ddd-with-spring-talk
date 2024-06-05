package library.lending.jdbc;

import java.util.Map;

import library.lending.LendableBook;

class LendingSqlParamsFactory {

	private LendingSqlParamsFactory() {
	}

	static Map<String, Object> create(LendableBook book) {
		return Map.of(
			"id", book.id().toString(),
			"volumeId", book.volumeId().toString(),
			"userId", book.userId().toString(),
			"expectedReturnDate", book.expectedReturnDate(),
			"returnedAt", book.returnedAt()
		);
	}

}
