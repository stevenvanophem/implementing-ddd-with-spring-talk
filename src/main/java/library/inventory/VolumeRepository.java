package library.inventory;

import java.util.Optional;

public interface VolumeRepository {

	Optional<Volume> findById(Volume.Id id);

	Volume save(Volume volume);

}
