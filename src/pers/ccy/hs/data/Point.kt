package pers.ccy.hs.data

/**
 * A point representing a location in `(x,y)` coordinate space,
 * specified in integer precision.
 *
 * @author      Sami Shaio
 * @since       1.0
 */
class Point(x: Double, y: Double) {

    var x = .0
    var y = .0

    init {
        this.x = x
        this.y = y
    }

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
}