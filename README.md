# ScotlandYard Board Game
==============

Callum Ke

This is the full coursework for my first year Object Orientated Programming course in Java. It simulates the Scotland Yard board game.
- A GUI skeleton was given. To pass the coursework(50%) you needed to implement methods to pass all the tests.
- To obtain the rest of the marks you had to implement an AI to play as the MrX Player to find the next best move. 
	- The AI I implemented uses Dijkstras algorithm and a location weighting factor to choose the best move.

How to Run
------------
To run the program you will need to have Maven installed
To check the tests pass run:

	mvn clean test
  
To play the game/open the GUI run:

	mvn clean compile exec:java


How to Play
------------
The game can be played in three ways:
1. Player vs Player 
	- This is the default setting
2. Player vs MrX AI
	- This is done by choosing 'MrXAI' as the player for MrX in the dropdown menu.
3. Random Detective AI vs MrX AI
	- This is to quickly show how the MrX AI works without any user playing
	- This is done by choosing 'DetectiveAI' and 'MrXAI' in the dropdown menus.


