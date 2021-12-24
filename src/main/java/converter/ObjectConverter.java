package converter;

import com.displee.cache.CacheLibrary;
import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.Archive317;
import com.displee.cache.index.archive.file.File;
import definitions.ObjectDefinition;
import utils.Buffer;

import java.util.Objects;

public class ObjectConverter {

    public static Archive getArchive(CacheLibrary cacheLibrary, int index, int archive) {
        return cacheLibrary.index(index).archive(archive);
    }

    public void convertObjectDefinitions(CacheLibrary cache317, CacheLibrary osrsCache) {
        Archive317 archive = (Archive317) getArchive(cache317, 0, 2);

        Objects.requireNonNull(archive, "Archive cannot be null");
        File dataFile = archive.file("loc.dat");
        File indexFile = archive.file("loc.idx");
        Buffer dataBuffer = new Buffer(Objects.requireNonNull(dataFile, "Data file cannot be null")
                .getData());
        Buffer indexBuffer = new Buffer(Objects.requireNonNull(indexFile, "Index file cannot be null")
                .getData());

        int offsetPosition = 2;

        int totalObjects = indexBuffer.readUnsignedShort();

        ObjectDefinition[] objectDefinitions = new ObjectDefinition[totalObjects];

        int[] offsets = new int[totalObjects];

        for (int id = 0; id < totalObjects; id++) {
            offsets[id] = offsetPosition;
            offsetPosition += indexBuffer.readUnsignedShort();
        }

        for (int id = 0; id < totalObjects; id++) {
            ObjectDefinition objectDefinition = new ObjectDefinition(id);

            int offset = offsets[id];
            dataBuffer.setPosition(offset);
            objectDefinition.decode(dataBuffer);

            objectDefinitions[id] = objectDefinition;

            objectDefinition.save(osrsCache);
        }

        osrsCache.index(2).update();
    }
}
