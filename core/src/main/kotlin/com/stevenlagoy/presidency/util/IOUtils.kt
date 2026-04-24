//@file:JvmName("IOUtils")
package com.stevenlagoy.presidency.util

import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.writeText

//enum class FileExtension(extension: String) {
//    ALL(""),
//    JSON(".json"),
//    CSV(".csv"),
//    TEXT(".txt"),
//}

@JvmOverloads
fun Path.listFiles(extension: IOUtils.FileExtension = IOUtils.FileExtension.ALL): Set<Path> {
    val pathSet = mutableSetOf<Path>()
    val stream = Files.newDirectoryStream(this)
    for (path in stream) {
        if (path == null || path.fileName == null) continue
        if (!Files.isDirectory(path) && extension.isType(path) && !FilePaths.IGNORED_PATHS.contains(path)) {
            pathSet.add(resolve(path.fileName))
        }
    }
    stream.close()
    return pathSet
}

