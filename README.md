# cellsociety
##Names
Mike Ma  
Karen Li  
Cameron Givler  

##Dates
###Start Date
September 10  

###Completion Date
Spetember 27  

##Roles
Cameron - GUI  
Mike - XML parser, Models  
Karen - Grid, Cells  

##Main Class
gui/CellSociety.java  
XMLGenerator can be used to generate XML files - It does not have a GUI component  

##Resource File
None: You can generate an xml file in the program

##Known Bugs
Ant model partially works

##Instructions
+Run CellSociety.java

+Create a new animation using "new model" button or load an animation from a XML file

+Click "Start" to run the animation

+Click "Pause" to pause the animation

+Click "Step" to run the animation step by step

+Click file->"save" to save current animation to an xml file

+Click "settings" to change global settings of the animation(such as shapes, grid, etc.)

+Also, you can generate more XMLs using xmlFactory/XMLGenerator.java!

##Features

###Simulation

* Allow a variety of grid location shapes:  
square (with 8 neighbors max, 3 on top and bottom and 1 on each side)   
triangular (with 8 neighbors max, 3 on top and bottom and 1 on each side)   
hexagonal (with 6 neighbors max, 1 on top and bottom and 2 on each "side") 

* Allow a variety of grid edge types:  
finite (bounded by the initial size, with locations on the edges having smaller neighborhoods)  
toroidal (bounded by the initial size, with locations on the edges having full size neighborhoods such that the neighbors past the edge are taken from the opposite side of the grid)  
NOTE: this simulates an infinite periodic tiling because each side is connected to its opposite side

* Implement the rules for six simulations (all simulations should work on all kinds of grid types):  
Game of live   
Segregation  
Predator-prey   
Fire spreading   
foraging ants  
sugarscape  

###Configuration  
* Implement error checking for incorrect file data:  
no simulation type given  
default values when parameter values are not given  
invalid cell state values/location given  

* Allow simulations initial configuration to be set by:  
list of specific locations and states (load from an XML)  
randomly based on probability/upper limit  

* Allow simulations to be "styled", using the settings menu:  
kind of grid to use, both by shapes and by edges  
whether or not grid locations should be outlined  
neighbors to consider (i.e., cardinal directions, or all directions) 

###Visualization  
* Display a graph of the populations of all of the "kinds" of cells over the time of the simulation  
* Allow users to interact with the simulation dynamically to create, kill, or change a state at a grid location (by left click on the cell)  
* Allow users change simulation speed  
* Allow users to interactively set the initial states of the grid locations and save them in an XML file so that it can be reloaded later   

###Impressions
We need some sleep.
