package pers.ccy.hs.data

/**
 * A point representing a location in `(x,y)` coordinate space,
 * specified in integer precision.
 *
 * @author      Sami Shaio
 * @since       1.0
 */
class Point(var x: Double, var y: Double) {

    override fun equals(obj: Any?): Boolean {
        if (obj is Point) {
            val pt = obj
            return x == pt.x && y == pt.y
        }
        return super.equals(obj)
    }

    override fun toString(): String {
        return javaClass.name + "[x=" + x + ",y=" + y + "]"
    }

    fun sub(p: Point): Point {
        return Point(x - p.x, y - p.y)
    }

    fun toPrint() {
        println("x:$x,y:$y")
    }
}