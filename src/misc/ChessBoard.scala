package misc

/**
 * Immutable chess board
 * Composed of @code Cell objects
 * Contains Chess Dimensions and state of each of its Cells
 *  
 */
class ChessBoard(mC: Int, nC:Int, cellsC:Array[Cell]){
	val m = mC ; val n = nC
	val cells = cellsC

	def getCell(pos: Pos): Cell = cells(pos.x*n + pos.y)

	/**
	 * Updates a given cell on the chessboard immutably 
	 * There is room for optimizations here  as we simply clone the cells currently	  
	 */
	def updateCell(pos:Pos, cpc:ChessPiece) :ChessBoard = {
		val cellsCopy = cells.clone()
		cellsCopy.update(pos.x*n + pos.y, getCell(pos).fillCell(cpc))
		new ChessBoard(m, n, cellsCopy)		  
	}

	/**
	 * Updates a given cell on the chessboard immutably 
	 * There is room for optimizations here  as we simply clone the cells currently	   
	 */
	def updateCell(pos:Pos, isThreatened:Boolean) :ChessBoard = {
		val cellsCopy = cells.clone()
		cellsCopy.update(pos.x*n + pos.y, getCell(pos).markThreatened())
		new ChessBoard(m, n, cellsCopy)
	}
}
