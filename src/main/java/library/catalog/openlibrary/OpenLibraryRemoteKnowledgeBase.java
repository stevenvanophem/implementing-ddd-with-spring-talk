package library.catalog.openlibrary;

import java.util.List;
import java.util.Objects;

import org.springframework.web.client.RestClient;

import library.catalog.Book;
import library.catalog.RemoteKnowledgeBase;

public class OpenLibraryRemoteKnowledgeBase implements RemoteKnowledgeBase {

	private final RestClient restClient;

	public OpenLibraryRemoteKnowledgeBase(RestClient.Builder builder) {
		this.restClient = builder
			.baseUrl("https://openlibrary.org/")
			.build();
	}

	@Override
	public Book.Title search(Book.Isbn isbn) {
		Objects.requireNonNull(isbn);

		SearchResult result = restClient.get()
			.uri("isbn/{isbn}.json", isbn.value())
			.retrieve()
			.body(SearchResult.class);

		return new Book.Title(result.title());
	}

	private record SearchResult(
		List<String> publishers,
		String title,
		List<String> isbn_13,
		int revisions
	) {

		private SearchResult {
			Objects.requireNonNull(title, "title missing");
		}

	}

}
