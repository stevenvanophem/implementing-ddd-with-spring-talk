package library.catalog.jdbc;

import org.springframework.jdbc.core.simple.JdbcClient;

import library.catalog.Book;
import library.catalog.BookRepository;

public class BookJdbcRepository implements BookRepository {

	private final JdbcClient jdbcClient;

	public BookJdbcRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	@Override
	public Book.Snapshot save(Book.Snapshot snapshot) {
		return null;
	}

}
