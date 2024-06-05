package library.lending;

import java.util.Optional;

import library.inventory.Volume;

public interface LendingRepository {

	boolean isAvailable(Volume.Id id);

	LendableBook save(LendableBook book);

	Optional<LendableBook> findById(LendableBook.Id id);

}

