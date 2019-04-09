ScotlandYard manual
===================

Welcome to the ScotlandYard manual. 

The is a minimalistic turn-based implementation of the ScotlandYard 
Game with the features including:

 * Mr.X travel log
 * Ticket counter
 * Visual ticket picker
 * Extensible AI 
 
**Exclusions**
 
As this is a turn-based implementation, players are playing
a strict subset of the game, in other words, not all possible 
game space can be explored in this implementation.

Police are also excluded for simplicity. 

### Game setup

When the game starts, a setup screen will appear that allows you to 
configure the game. 
There are two tabs, in the `Player settings` tab, you may configure 
the following:

 * Name
 * Starting location
 * AI
 * Tickets
 
If no name is specified, the colour of the player is used.
The starting location defaults to random, locations are also 
mutually exclusive between all players. AI defaults to none, 
which means a human player will have to pick a move on the board.

The second tab call `Round settings` allows you to modify game 
configurations listed below

 * Timeout
 * Round count
 * Reveal rounds
 
Timeout controls how long a player can decide when selecting a move.
If the player fails to select a move then the opposite side wins.

Round count controls how many rounds the game have, the default is 24 
rounds. Reveal rounds control whether Mr.X's location will be shown on
Mr.X's travel log and the board.

### Game play

Mr.X is always the first to play. When a player is required to make a move, 
a small notification box that contains a timer will appear on the top, the 
board will then circle locations that are available to the player. Players
should pick a location to move to by clicking on the circles and then select
which ticket to use to complete the move.

### Options

The menu on top provides options for you to change during the game, 
you may do the following inthe:

**Game**

 * New game - creates a new game window
 * Close window - exits the game

**View**

 * Reset viewport - centers the board
 * Focus player - scrolls to player when moves are requred
 * Move history - shows move trails
 * Mr'X travel log - toggles visibility for the log
 * Tickets - toggles visibility for the ticket counter
 
**Help**

 * Find node - a tool that helps with finding locations on the map
 * Manual - this manual
 * Debug - starts the ScenicView JavaFX inspector
 * About - displays information about this app
 * License - displays licensing information for the app

### Debugging

Debugger is provided by ScenicView which can
be downloaded and installed automatically in case
`scenicView.jar` is not present in the current directory.

To start the debugger , go to `Help>Debug`