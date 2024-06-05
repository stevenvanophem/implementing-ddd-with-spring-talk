package library.lending;

import java.util.Optional;

public interface LendingRepository {

	boolean isAvailable(LendableBook.CopyId id);

	LendableBook.Snapshot save(LendableBook.Snapshot book);

	Optional<LendableBook.Snapshot> findById(LendableBook.Id id);

}

