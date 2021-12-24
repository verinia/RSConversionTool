package converter

import com.displee.cache.CacheLibrary
import utils.CompressionUtils
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

class ModelConverter {

    companion object {
        fun convertModels(originalCache: CacheLibrary, modifiedCache: CacheLibrary, osrsCache: CacheLibrary) {
            val originalIndex = originalCache.index(1)
            val modifiedIndex = modifiedCache.index(1)

            for (index in originalIndex.archives().indices) {
                var originalModelFile = originalIndex.readArchiveSector(index)
                var modifiedModelFile = modifiedIndex.readArchiveSector(index)

                if (originalModelFile != null && modifiedModelFile != null) {
                    var originalModelData = originalModelFile.data
                    var modifiedModelData = modifiedModelFile.data

                    var originalSize = originalModelData.size
                    var modifiedSize = modifiedModelData.size

                    if (originalSize != modifiedSize) {
                        println("Exporting Index ($index).")
                        var exportData = originalModelData

                        if (CompressionUtils.isGZipped(ByteArrayInputStream(exportData))) {
                            exportData = CompressionUtils.degzip(ByteBuffer.wrap(exportData))
                        }

                        osrsCache.put(7, index, exportData)
                    }
                }
            }

            osrsCache.update()
        }
    }
}