package misc

/**
 * Class denoting a cell on the chess board
 * @param Position
 * @param isThreatened If the position is safe or not
 * @param ChessPiece on it if any
 */
class Cell(posC :Pos, isThreatenedC:Boolean, chessPieceC:Option[ChessPiece]){
	val pos = posC
			val chesspiece = chessPieceC// avoiding null here
			val isThreatened :Boolean = isThreatenedC

			def isEmpty() :Boolean = chesspiece match{
			case None => true
			case _ => false
			}

			def fillCell(cpc:ChessPiece) :Cell = new Cell(pos, true, Some(cpc))	
			def markThreatened() :Cell = new Cell(pos, true, chesspiece)

			override def toString() : String = "Pos:" + pos  +  "Val " + {if (chesspiece == None) "X" else chesspiece.get} + " Threat:" + isThreatened 
}
