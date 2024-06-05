package library.lending.jdbc;

import java.util.Map;

import library.lending.LendableBook;

class LendingSqlParamsFactory {

	private LendingSqlParamsFactory() {
	}

	static Map<String, Object> create(LendableBook.Snapshot book) {
		return Map.of(
			"id", book.id(),
			"volumeId", book.copyId(),
			"userId", book.userId(),
			"expectedReturnDate", book.expectedReturnDate(),
			"returnedAt", book.returnedAt()
		);
	}

}
