package misc

import Array._
import scala.language.implicitConversions._
import scala.util.control.TailCalls._
import scala.annotation.tailrec
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global


object solution{  

	
	/**
	 * Gives all available Cells/positions for a given ChessBoard state
	 */
	def availablePositions(board:ChessBoard, pos:Pos): Array[Pos] = board.cells.filter(_.pos >= pos).filter(!_.isThreatened )map(_.pos)

	
	/**
	 * Checks whether or not a given chesspiece can be planted at a given position on the Chessboard
	 * 
	 */
	def checkChessPieceForPos(board: ChessBoard, cpc: ChessPiece, threatPositions:Array[Pos], pos: Pos): Boolean = 
			threatPositions.foldLeft(true)((r, c) => board.getCell(c).isEmpty() && r)

			
	/**
	 *   Places a chesspiece at a given unthreatened postion on the chess board
	 *   
	 */		
	def putChessPieceAtPos(board:ChessBoard, cpc:ChessPiece, threatPositions:Array[Pos], pos:Pos): ChessBoard ={	  
		threatPositions.foldLeft(board)((r,c) => r.updateCell(c, true)).updateCell(pos, cpc)
	}

	
	/**
	 * This is where the real magic and all the heavy lifting happens
	 * 
	 * This function iterates through a given permutation and establishes all possible valid Chessboard arrangements
	 * for the permutation.
	 * Note that there is recursion involved here. Given the algorithm, its not possible to establish tail call as
	 * because for every single invocation the method calls itself variable number of times via flatmap.
	 * Due to the nature of the problem the depth of the recursion is not a concern here as it can only grow with chessboard size
	 * OR with to Number of chess pieces involved. However with more number of chess pieces there will be less degrees of freedom 
	 * (free unthreatened positions) available which will automatically tone down the depth of recursion
	 * 
	 * However it would have been interesting to establish stack growth control for this algorithm.
	 * Standard tail recursion will not work. Neither will standard Trampoline.
	 * The best(somewhat complex) way to control recursion here would have been use of Free Monads in this case:
	 * http://blog.higher-order.com/assets/trampolines.pdf
	 * 
	 * Note that a chessboard if and once complete is flushed out immediately to the Standard IO synchronously
	 * Holding chessboard states can lead to Out of Memory error if the results are sufficiently large
	 * Thats the reason printout is done synchronously as asynchronous io would lead to OOO where the number of chessboard arrangements can go to Millions
	 * The affect of IO on computation thread can be bypassed by increasing the thread pool size Scala alots for Prallel computations
	 * Watermark of successful arrangement is retained via scala Options to aid calculation of total successful arrangements
	 *  
	 * Some commented out printlns have been left in the code - treat them as crude logger.debug code - <sorry :-)>
	 * should you be interested to see how exactly the algorithm works
	 * 
	 */
	def solve(board:ChessBoard, pos:Pos, cpcs:Array[ChessPiece]): Array[Option[ChessBoard]] ={
		cpcs match{ 
		case Array() => {//println("Board Close = " + board.cells.mkString(" "));						
						printChessBoard(board)
						Array(None)}
		case _ => {
		  //println("Chesspieces = " + cpcs.mkString(" ") )  
		  //println("Board = " + board.cells.mkString(" "))
		  
			def tryPutChessPieceAt(board:ChessBoard , cpc:ChessPiece, p:Pos): Array[Option[ChessBoard]] ={
					val threatenedPositions = cpc.threaten(board.m, board.n, p)
					//println("Trying Pos: " + p)
					//println("Threat Positions: " + threatenedPositions.mkString(" " ))
					if(checkChessPieceForPos(board, cpc, threatenedPositions, p)) 
						solve(putChessPieceAtPos(board, cpc, threatenedPositions, p), p, cpcs.tail)
					else{
					  //println("Not able to place piece. Board so far " + board.cells.mkString(" "))
					  Array()
					}		
			}
		  	//println("=========================")
		  	//println("Available positions: " + getAvailablePositionsAfter(board, pos).mkString(" "))
			availablePositions(board, pos).flatMap(tryPutChessPieceAt(board, cpcs.head, _))
			}
		
		}
	} 

	
	/**
	 * Creates all possible permutations of the Chess Pieces passed and invokes each of the permutation 
	 * to check for valid Chess Board arrangements. 
	 * Since each permutation can be processed independently of others - parallel map is used here
	 * to make use of multiple cpu cores
	 * 
	 */
	def permAndResolve(board:ChessBoard, chesspcs:Array[ChessPiece]): Array[Option[ChessBoard]]= {
		//Convert to String so get distinct perms based on piece type
		val distinctPerms = (for(cpc <- chesspcs) yield cpc.toString) .permutations.toArray
		println("Number of Distinct Permutations to process " + distinctPerms.length)
		
		def f(permStr: Array[String]) = {
			println("Processing perm with " + permStr.mkString(" "))  
			val permChessPiece = for( elem <- permStr) yield strToChessPiece(elem)
			solve(board, Pos(0,0), permChessPiece )
		}			  			
		distinctPerms.par.flatMap(elem => f(elem)) toArray	//Perms are processed in parallel 
	}
	

	def solvePuzzle(rows:Int, cols:Int, cpcs:Array[ChessPiece]): Array[Option[ChessBoard]] =  
			permAndResolve( new ChessBoard(rows, cols, for(i <- 0 until rows toArray; j <- 0 until cols) yield new Cell(Pos(i,j), false, None)), cpcs)


	/**
	 * Prints the total number of possible ChessBoard arrangements to standard out
	 */
	def printResults(boards: Array[Option[ChessBoard]]): Unit ={
	    println("========================================================================")
		println("Total No of possible Chess Board arrangements: " + boards.length)	  
		println("========================================================================")			  
	}
	
		
	
	/**
	 * Prints ChessBoard to standard out
	 * Accumulates data in a string buffer and flushes in a single go to println to 
	 * avoid interleaved print results
	 * @param ChessBoard
	 */
	def printChessBoard(board: ChessBoard): Unit={		  
		var sb = board.cells.foldLeft(new StringBuilder)(buildCells(_, _))
		sb.append("\n====================================================================================")
		//val f: Future[Unit] = Future{ println(sb.toString)}//submit to future for io
		println(sb.toString)
		
	}
	
	def buildCells(sb:StringBuilder, cell:Cell) :StringBuilder={
		if(cell.pos.y  == 0) sb.append("\n----------------------------------------------------------------------\n")		  
		if (cell.isEmpty()) sb.append("X \t") else sb.append(cell.chesspiece.get + "\t")	     
	}
	
	
	/**
	 * Converts a String to actual Chesspiece
	 * Note that there is no catch-all pattern match as the intention is to error if an invalid chess piece is passed
	 */
	def strToChessPiece(name :String) :ChessPiece = name match{
	case "King" => new King
	case "Knight" => new Knight
	case "Rook" => new Rook
	case "Bishop" => new Bishop
	case "Queen" => new Queen
	}

	
	/**
	 * Main Method for the application
	 * USAGE: <X-DIMENSION>:Integer <Y-DIMENSION>:Integer <ChessPiece1>:String <ChessPiece2>:String <Chesspiece3>:String ...
	 * Valid ChessPiece Types => [King, Queen, Bishop, Rook, Knight]
	 * Ok to have more than one chesspiece for the same ChessPiece Type
	 * 
	 * e.g 5 5 King Queen Rook Rook Knight Bishop
	 * 
	 */
	def main (args: Array[String]){
		var rows = None: Option[Int]
		var cols = None: Option[Int]
		var chessPieces = None: Option[Array[ChessPiece]]
		try{
			rows = Some(args(0).toInt)
			cols = Some(args(1).toInt)		
			chessPieces = Some(for(i <- 2 until args.length toArray ) yield strToChessPiece(args(i)))

		}catch{
		case ex: Exception =>{ 
			println("USAGE: <X-DIMENSION> <Y-DIMENSION> <ChessPiece1> <ChessPiece2> <Chesspiece3> ...") 
			println("e.g: 5 5 King Queen Rook Rook Knight Bishop")
			ex.printStackTrace()
		}					
		}		
		printResults(solvePuzzle(rows.get, cols.get, chessPieces.get))
	}

}