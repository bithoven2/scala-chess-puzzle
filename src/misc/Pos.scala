package misc

/**
 * Position class holding the x and y co-ordinates
 * 
 */
class Pos (xc:Int, yc:Int){	    
		val x: Int = xc
				val y: Int = yc
				override def toString(): String = "(" + x + "," + y + ")"
				def >=(pos: Pos): Boolean = this.x > pos.x || (this.x == pos.x && this.y >= pos.y )
	}


/**
 * Companion Object for Pos class to simplify the usage of Pos
 */
object Pos{
	def apply(ax:Int, ay:Int) = new Pos(ax, ay)
}
