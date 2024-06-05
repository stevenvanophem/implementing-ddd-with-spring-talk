package library.catalog;

public interface RemoteKnowledgeBase {

	Book.Title search(Book.Isbn isbn);

}
