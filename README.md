# PWKUtils Mod

**PWKUtils** is a serverside Fabric mod that adds various quality-of-life features for admins and players, allowing server owners to customize chat, tablist, prefixes, and more on Fabric servers.

Get it on modrinth: https://modrinth.com/mod/pwkutils

## Features

### 1. Player Prefixes
- Automatically assigns prefixes to players based on OP status
- Prefixes are configurable in the `config.json`
- Prefixes update live if a player is OPed or de-opped

### 2. Chat Formatting
- Customizable chat format
- Option to enable/disable formatting via config

### 3. Join/Leave Messages
- Custom join and leave messages
- Messages can include player names using `{player}`
- Can be toggled on/off in the config

### 4. Welcome Message
- Sends a configurable welcome message when a player joins
- Can be toggled on/off in the config
- Optionally supports multi-line messages using `\n`

### 5. Tablist Customization
- Custom header and footer for the tablist
- Shows online players if enabled in the config
- Updates every second

### 6. Commands
- `/gm <0-3>` – Quickly change your game mode:
  - `0` = Survival  
  - `1` = Creative  
  - `2` = Adventure  
  - `3` = Spectator  
- `/tps` – Displays the current server TPS
