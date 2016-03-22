package misc

/**
 * A utility singleton object holding re usable functions related to moves that can be carried out
 * by different pieces on the chess board
 * 
 */
object ChessMoves {
	
    /**
     * Filters invalid(out of bounds) positions based on current position and Chess Board Dimensions
     *  
     */
	def filter_pos(pos :Pos, m :Int, n :Int) :Boolean = 0 <= pos.x && pos.x < m  && 0 <= pos.y && pos.y< n

	/**
	 * Lists out all possible horizontal moves/positions based on current position
	 * Does not do strict bounds checks @see filter_pos for this
	 * 
	 */
	def horizontalMove(m :Int, pos :Pos) :Array[Pos]= for(i <- 0 until m toArray; if i != pos.x) yield Pos(i, pos.y)

	/**
	 * Lists out all possible vertical moves based on current position
	 * Does not do strict bounds checks @see filter_pos for this
	 */
	def verticalMove(n :Int, pos :Pos) :Array[Pos]= for(i <- 0 until n toArray ; if i != pos.y) yield Pos(pos.x, i)

	/**
	 * Lists out all possible diagnonal moves based on current position
	 * Does not do strict bounds checks @see filter_pos for this
	 */	
	def diagonalMove(m: Int, n :Int, pos :Pos) :Array[Pos] = {
			val istart = pos.x - m ; val iend = m - pos.x 
			val jstart = pos.y -n ; val jend = n - pos.y
			val moves = 
			  for(i<- istart until iend toArray ;j <- jstart until jend; if i.abs == j.abs && i!=0 && j!=0 ) yield Pos(pos.x + i , pos.y + j)
			moves filter(filter_pos(_, m, n))
	}

}