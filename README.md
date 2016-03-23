# scala-chess-puzzle


Inroduction:
----------------
This project is a scala based solution to interesting chess problem as mentioned in Chess Problem 2-v2.docx.
Demonstrates the conciseness and power of expression that functional programming brings when it comes to solving 
algorithmically complex  problems.


AIM:
---
To demonstrate following development abilities:
1. Solving non-trivial algorithmically complex business problems
2. Knowledge and practical application of functional programming paradigms and tools
3. Scala skills


Instructions:
--------------
1. The solution is packaged as an eclipse scala project.
2. It should be straight forward to compile and run on commandline or an ide.
3. The 6 source files need to be compiled using scala compiler and run as scala application.
4. This can be easily done on the cli or on eclipse.
5. The easiest way is to download scala-eclipse bundle and point to the src folder
http://scala-ide.org/download/sdk.html.

6. Instructions for running the program is described in the main function in Main.scala file
7. A sample result is pasted below for  3x3 board with 2 Kings & 1 Rook
8. Result for the Puzzle mentioned in Chess Problem 2-v2.docx :
	
	i.e. for problem: 6x9 board with 2Kings 1Rook 1Bishop 1Queen 1Knight

	ANSWER: 30854803 possible arrangments

	Detailed Results and instructions can be found in results.txt

9.To disable printout  for results having Millions of possible arrangement comment out line 66 in Main.scala/solve



============================================================================================
SAMPLE RESULT 
==============

Command
=========
misc.solution 3 3 King King Rook

Output:
=======
Number of Distinct Permutations to process 3
Processing perm with King Rook King
Processing perm with King King Rook
Processing perm with Rook King King

----------------------------------------------------------------------
King	X 	X 	
----------------------------------------------------------------------
X 	X 	Rook	
----------------------------------------------------------------------
King	X 	X 	
====================================================================================

----------------------------------------------------------------------
King	X 	King	
----------------------------------------------------------------------
X 	X 	X 	
----------------------------------------------------------------------
X 	Rook	X 	
====================================================================================

----------------------------------------------------------------------
X 	Rook	X 	
----------------------------------------------------------------------
X 	X 	X 	
----------------------------------------------------------------------
King	X 	King	
====================================================================================

----------------------------------------------------------------------
X 	X 	King	
----------------------------------------------------------------------
Rook	X 	X 	
----------------------------------------------------------------------
X 	X 	King	
====================================================================================
========================================================================
Total No of possible Chess Board arrangements: 4
========================================================================