# StopPlantGrowth

Plugin for [PaperMC](https://papermc.io/) that **prevents plant growth/spread** while aiming to **minimize impact on MSPT** (server tick time).

> Built against the Paper **1.21 API** and **Java 21** (tested with Minecraft **1.21.8**).

---

## Features

- Right-click a plant with a configured tool (default: `SHEARS`) to toggle whether it can grow or spread.

- Configurable via Bukkit material names in `prevented-blocks` (wheat, carrots, kelp, vines, etc.).

- Toggled plants are stored in a small SQLite database:  
  `plugins/StopPlantGrowth/block_data.db`

- On server start, entries that no longer point to plants are automatically removed.

- Player messages use [Adventure MiniMessage](https://docs.advntr.dev/minimessage/) for rich text formatting.

---

## Requirements

- **Server:** [PaperMC](https://papermc.io/) **1.21.x**  
  - Built against: `io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT`
- **Java:** **21+**

Running on older Paper or Java versions is **not guaranteed** to work.

---

## Installation

1. Stop your Paper server (recommended).
2. Download the plugin JAR and place it in your `plugins/` folder:
   - `StopPlantGrowth-*.*-SNAPSHOT.jar`
3. Start (or restart) your server.
4. Confirm that:
   - `StopPlantGrowth` shows up in `/plugins`
   - A database file was created at  
     `plugins/StopPlantGrowth/block_data.db`

---

## Usage

1. Hold a configured **tool item** in your **main hand** (see `preventing-items` in the config; default: `SHEARS`).
2. **Right-click** a supported plant block (any material listed under `prevented-blocks`).
3. The plugin will:
   - Toggle that block’s “growth state” between **allowed** and **prevented**
   - Store/remove it in the SQLite database
   - Send you a message based on `prevention-message`, where `%s` becomes `"Prevented"` or `"Allowed"`

Internally, the plugin listens to:

- `BlockGrowEvent`
- `BlockSpreadEvent`

If the block (or its new state/source) has the `stopGrowth` metadata, the event is cancelled and the plant will no longer grow or spread.

---

## Configuration

Default `config.yml`:

```yml
# List of blocks that can be prevented from growing.
# Use Bukkit Material names. Examples:
# WHEAT, CARROTS, POTATOES, BEETROOTS, NETHER_WART, SUGAR_CANE, CACTUS,
# MELON_STEM, PUMPKIN_STEM, BAMBOO,
# KELP, SEA_PICKLE, VINE, TWISTING_VINES_PLANT, WEEPING_VINES_PLANT, KELP_PLANT
prevented-blocks:
  - WHEAT
  - CARROTS
  - POTATOES
  - BEETROOTS
  - NETHER_WART
  - SUGAR_CANE
  - CACTUS
  - MELON_STEM
  - PUMPKIN_STEM
  - BAMBOO
  - KELP
  - SEA_PICKLE
  - VINE
  - TWISTING_VINES_PLANT
  - WEEPING_VINES_PLANT
  - KELP_PLANT

# List of items that can be used to prevent growth when held in the player's main hand.
# Again, use Bukkit Material names.
# Example: SHEARS
preventing-items:
  - SHEARS

# Message sent to the player when growth is toggled.
# The first %s is replaced with "Prevented" or "Allowed".
# Supports Adventure MiniMessage formatting.
prevention-message: "<white>This plant's growth has been %s</white>"
