# MolehuntSpeedrun
Official Minecraft plugin used in the Molehunt Speedrun youtube videos

Molehunt Speedrun is one of Piffin380's game series, and is better explained
[in the videos themselves](https://www.youtube.com/watch?v=4I6DrKDet7w). This readme will be more focused on plugin
setup.

To install, just plop the file into a bukkit fork (spigot, paper, etc.) server and start the server. This will generate
the configuration file to edit values for the plugin. The configurable values go as follows:
- The `moles` value changes the amount of moles that the game starts with.
- The `borderSize` value changes the size of the world border upon starting. The border always centers around 0, 0
- The `spreadDistance` value determines how far players will be teleported from spawn.
- The `irisMode` boolean defaults to false and should be set to true if your players are adamant about using Iris or
  some other shader mod that replaces Minecraft's default rendering system. If this value is false and players use Iris,
  the locator bar looks like it does in the video linked above.

Be sure to reload your server after modifying any of these values!

This plugin requires the use of a resource pack for the locator bar. The below values can be set in the
`server.properties` file to have players automatically download the pack upon joining the game. If you prefer to
download the pack manually, it is `molehuntSpeedrun.zip` in the github repo.

````
require-resource-pack=true
resource-pack=https://raw.githubusercontent.com/Pm7-dev/MolehuntSpeedrun/refs/heads/main/MolehuntSpeedrun.zip
resource-pack-id=
resource-pack-prompt=
resource-pack-sha1=c64b781855d7011af72210535a317453e941db72
````

Before starting, Have every player switch to the same skin (or use a skin swapper plugin), and unbind player list keys so nobody accidentally views the tab list. The moles, once selected, can rebind their tab key. Also remember to add the empty end portal at the center of the world you are using.  

As of update 1.1, this plugin disables joining and leaving messages, so make sure to coordinate once all your players are online lol

To start the game, run `/startmolehunt` from an operator account.  
To reset the game when finished, run `/resetmolehunt`.