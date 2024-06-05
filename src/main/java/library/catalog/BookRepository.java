package library.catalog;

public interface BookRepository {

	Book.Snapshot save(Book.Snapshot snapshot);

}
