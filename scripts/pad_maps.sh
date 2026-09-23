#!/usr/bin/env bash
# pad_maps.sh
# Pads the three source map PNGs to 52224x28672 (512x102 tiles × 512x56 tiles).
#
# Original size: 51968 x 28346
# Padded size:   52224 x 28672  (+256 right, +326 bottom)
#
# Padding color:
#   counties.png  → black (0x000000), fully opaque  — "no county" sentinel
#   terrain.png   → black (0x000000), fully opaque  — padding reads as ocean
#   urban.png     → black (0x000000), fully opaque  — "no urban area"
#
# Requires: ImageMagick (magick / convert)
#
# Usage:
#   chmod +x scripts/pad_maps.sh
#   ./scripts/pad_maps.sh
# Or from the project root:
#   bash scripts/pad_maps.sh
#
# Input:  assets_raw/maps/source/{counties,terrain,urban}.png
# Output: assets_raw/maps/source/{counties,terrain,urban}.png  (in-place)
#         (originals are backed up to assets_raw/maps/source/backup/)

set -euo pipefail

INPUT_DIR="assets_raw/maps/source"
BACKUP_DIR="$INPUT_DIR/backup"
TARGET_W=52224
TARGET_H=28672

# Use 'magick' (ImageMagick 7) with 'convert' (ImageMagick 6) as fallback
if command -v magick &>/dev/null; then
    IM="magick"
elif command -v convert &>/dev/null; then
    IM="convert"
else
    echo "ERROR: ImageMagick not found. Install it from https://imagemagick.org/" >&2
    exit 1
fi

echo "Using ImageMagick: $($IM --version | head -1)"
echo "Target size: ${TARGET_W}x${TARGET_H}"
echo ""

mkdir -p "$BACKUP_DIR"

for LAYER in counties terrain urban; do
    SRC="$INPUT_DIR/${LAYER}.png"

    if [ ! -f "$SRC" ]; then
        echo "SKIP: $SRC not found"
        continue
    fi

    # Read actual size
    ACTUAL=$($IM identify -format "%wx%h" "$SRC")
    if [ "$ACTUAL" = "${TARGET_W}x${TARGET_H}" ]; then
        echo "SKIP: $LAYER.png is already ${TARGET_W}x${TARGET_H}"
        continue
    fi

    echo "Padding $LAYER.png  ($ACTUAL → ${TARGET_W}x${TARGET_H})..."

    # Back up original
    cp "$SRC" "$BACKUP_DIR/${LAYER}_original.png"

    # Pad: place image at top-left (gravity NorthWest), fill remainder with black.
    # -extent expands the canvas; new pixels default to the background color.
    $IM "$SRC" \
        -background black \
        -gravity NorthWest \
        -extent "${TARGET_W}x${TARGET_H}" \
        "$SRC"

    echo "  Done → $SRC"
done

echo ""
echo "Padding complete. Originals backed up to $BACKUP_DIR/"
