package com.stevenlagoy.presidency.tools.mapcompiler

import com.stevenlagoy.presidency.tools.mapcompiler.model.LayerType
import java.io.File
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

/**
 * Main entry point for the map asset compiler.
 *
 * Usage: MapCompiled <inputDir> <outputDir>
 *
 * Images must be pre-padded to 5224x28672 (512x102 tiles wide, 512x56 tiles tall).
 * Use scripts/pad_maps.sh or ImageMagick to pad before running this tool.
 */
fun main(args: Array<String>) {
    if (args.size < 2) {
        System.err.println("Usage: MapCompiler <inputDir> <outputDir> [changedFileName ...]")
        System.err.println("  inputDir:  directory containing base map files")
        System.err.println("  outputDir: directory to write compiled .bin files")
        System.err.println("  changedFileName: optional filenames to restrict processing to")
        System.err.println("Got: ${args.joinToString(" ")}")
        exitProcess(1)
    }

    val inputDir = File(args[0]).also {
        require(it.exists() && it.isDirectory) { "Input directory does not exist: ${it.absolutePath}" }
    }
    val outputDir = File(args[1]).also {
        it.mkdirs()
        require(it.isDirectory) { "Could not create output directory: ${it.absolutePath}" }
    }

    val requestedFileNames = args.drop(2).toSet()

    val layersToProcess = if (requestedFileNames.isEmpty()) {
        LayerType.entries
    } else {
        LayerType.entries.filter { it.filename in requestedFileNames }
    }

    if (requestedFileNames.isNotEmpty() && layersToProcess.size < requestedFileNames.size) {
        val matchedNames = layersToProcess.map { it.filename }.toSet()
        val unmatched = requestedFileNames - matchedNames
        System.err.println("WARNING: no LayerType matches these requested files, skipping: $unmatched")
    }

    println("Mandate Map Compiler")
    println("Input:  ${inputDir.absolutePath}")
    println("Output: ${outputDir.absolutePath}")
    println("Layers: ${layersToProcess.joinToString(", ") { it.filename }}")
    println()

    val config = CompilerConfig(
        tileSize    = 512,
        imageWidth  = 52224,
        imageHeight = 28672,
        tilesWide   = 102,
        tilesHigh   = 56,
    )

    val totalTime = measureTimeMillis {
        for (layer in layersToProcess) {
            val inputFile  = inputDir.resolve(layer.filename)
            val outputFile = outputDir.resolve(layer.outputFilename)

            if (!inputFile.exists()) {
                System.err.println("WARNING: Missing input file, skipping: ${inputFile.absolutePath}")
                continue
            }

            println("Processing ${layer.filename}...")
            val elapsed = measureTimeMillis {
                processLayer(layer, inputFile, outputFile, config)
            }
            println("  Done in ${elapsed}ms -> ${outputFile.name}")
            println()
        }
    }

    println("Compilation complete in ${totalTime}ms")
}

private fun processLayer(
    layer: LayerType,
    inputFile: File,
    outputFile: File,
    config: CompilerConfig,
) {
    val accumulator = RegionAccumulator(layer)
    val reader = TiledImageReader(inputFile, config)

    var tilesProcessed = 0
    val totalTiles = config.tilesWide * config.tilesHigh

    reader.forEachTile { tileX, tileY, pixels ->
        LayerProcessor.processTile(
            tileX = tileX,
            tileY = tileY,
            pixels = pixels,
            tileSize = config.tileSize,
            imageWidth = config.imageWidth,
            emptyColor = layer.emptyColor,
            accumulator = accumulator,
        )
        tilesProcessed++
        if (tilesProcessed % config.tilesWide == 0 || tilesProcessed == totalTiles) {
            val pct = tilesProcessed * 100 / totalTiles
            print("\r  Tiles: $tilesProcessed / $totalTiles ($pct%)")
        }
    }
    println()

    val compiledMap = accumulator.build()
    println("  Regions found: ${compiledMap.regions.size}")

    BinaryWriter.write(compiledMap, outputFile)
}
