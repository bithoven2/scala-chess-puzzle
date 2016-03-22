package misc

import misc.ChessMoves._

/**
 * Chess piece with abstract threaten method - 
 * Threaten method lists all positions the Chesspiece can threaten based on its current position on the board  
 * 
 */
abstract class ChessPiece(){	  
	def threaten(m: Int, n: Int, pos: Pos): Array[Pos]
}


/**
 * King 
 */
class King() extends ChessPiece(){
	override def threaten(m: Int, n: Int, pos: Pos):  Array[Pos] = 
			Array(Pos(pos.x + 1, pos.y +1), Pos(pos.x - 1, pos.y +1), Pos(pos.x + 1, pos.y -1), 
					Pos(pos.x - 1, pos.y -1), Pos(pos.x, pos.y+1),  Pos(pos.x, pos.y -1), 
					Pos(pos.x-1, pos.y), Pos(pos.x +1, pos.y)
					) filter(filter_pos(_, m, n))	

	override def toString(): String = "King"
}

/**
 * Knight
 */
class Knight() extends ChessPiece(){
	override def threaten(m: Int, n: Int, pos: Pos):  Array[Pos] = {
			val knightMoves = for(i <- Array(-2, -1, 1, 2); j <- Array(-2, -1, 1, 2) ; if i.abs != j.abs) yield Pos(pos.x + i, pos.y +j) 
			knightMoves filter(filter_pos(_, m, n))
	}		
	override def toString(): String = "Knight"
}


/**
 * Rook
 */
class Rook() extends ChessPiece(){
	override def threaten(m: Int, n: Int, pos: Pos):  Array[Pos] = 	horizontalMove(m, pos) ++ verticalMove(n, pos)							
	override def toString(): String = "Rook"
}

/**
 * Bishop
 * 
 */
class Bishop() extends ChessPiece(){
	override def threaten(m: Int, n: Int, pos: Pos):  Array[Pos] = 	diagonalMove(m, n, pos)
	override def toString(): String = "Bishop"
}

/**
 * Queen
 */
class Queen() extends ChessPiece(){
	override def threaten(m: Int, n: Int, pos: Pos):  Array[Pos] = horizontalMove(m, pos) ++ verticalMove(n, pos) ++ diagonalMove(m, n, pos)
	override def toString(): String = "Queen"
}

