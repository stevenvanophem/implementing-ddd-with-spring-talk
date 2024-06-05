package library.lending;

import java.util.Optional;

public interface CirculationRepository {

	boolean isAvailable(CirculatingBook.CopyId id);

	CirculatingBook.Snapshot save(CirculatingBook.Snapshot circulatingBook);

	Optional<CirculatingBook.Snapshot> findById(CirculatingBook.Id id);

}

