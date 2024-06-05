package library.inventory;

import java.util.Optional;

public interface VolumeRepository {

	Optional<Volume.Snapshot> findById(Volume.Id id);

	Volume.Snapshot save(Volume.Snapshot volume);

}
