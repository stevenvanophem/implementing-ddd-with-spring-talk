package library.lending;

import java.util.Optional;

import library.inventory.Volume;

public interface LendingRepository {

	boolean isAvailable(Volume.Id id);

	LendableBook.Snapshot save(LendableBook.Snapshot book);

	Optional<LendableBook.Snapshot> findById(LendableBook.Id id);

}

