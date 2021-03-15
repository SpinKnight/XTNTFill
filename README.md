# What is this project? #
Since I couldn't find a decent TNTFill plugin for free on Spigot, so I decided to make one myself.
If you don't know what a "TNTFill plugin" is, it's basically just a plugin that lets you fill nearby dispensers with TNT with the TNT in your inventory or your factions "tnt bank".

**Disclaimer:** This plugin only supports TNT from a factions TNT bank, which means if you do not have a factions plugin, that has a tnt bank, like the original MassiveCraft Factions, it will not work.

If you need to come in contact with me, you can do so on my Discord `spin#0001`.

## Installation & Download ##
Head over to [this page](https://github.com/SpinKnight/XTNTFill/releases) to download the plugin.
Once downloaded you simply drag & drop it into the `plugins` folder like with every other plugin.

## Suggestions or ideas ##
If you have a suggestion, or an idea you think would be great for the plugin, feel free to contact me on Discord or request it on GitHub.

## Commands ##
- `/tntfill <radius> <amount>` - Fill nearby dispensers with <amount> tnt in each. You can set `strict-fill` in the config to `false` to divide <amount> into nearby dispensers instead.
- `/tntfill reload` - Reload the config.

### Aliases ###
- `/xtntfill`
- `/fill`

You are also able to configure other aliases in the config, if needed. Default configurable aliases are:
- `/f tntfill`
- `/f tnt fill`

## Permissions ##
- `tntfill.use` - Access to the /tntfill <radius> <amount> command.
- `tntfill.reload` - Access to /tntfill reload.
- `tntfill.admin` - Be able to bypass the tnt limit, if in gamemode creative.

### Bypass Mode ###
If you are in creative with the `tntfill.admin` permission, you can skip the TNT bank checks, and fill nearby dispensers without losing any tnt.

## Supported Factions Plugins ##
This plugin will only work, if you have one of the following plugins:
- `FactionsUUID (or fork)`
- `SaberFactions`
- `SavageFactions`
