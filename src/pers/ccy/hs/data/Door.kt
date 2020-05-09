package pers.ccy.hs.data

class Door(var id: Int, var thick: Double, var length: Double, var angle: Double, point1: Point, point2: Point) {
    var point = arrayOfNulls<Point>(4)
    var isused = false

    init {
        point[0] = point1
        point[1] = point2
    }

    fun addlast(p1: Point, p2: Point) {
        this.point[2] = p1
        this.point[3] = p2
    }

    fun printPoint() {
        print("0:")
        point[0]?.toPrint()
        print("1:")
        point[1]?.toPrint()
        print("2:")
        point[2]?.toPrint()
        print("3:")
        point[3]?.toPrint()
    }

    fun toSave(): String {
        var str = "<Door id=\"$id\">\n" +
                "<point>${point[0]!!.x},${point[0]!!.y},${point[1]!!.x},${point[1]!!.y},${point[2]!!.x},${point[2]!!.y},${point[3]!!.x},${point[3]!!.y}</point>\n" +
                "<isused>$isused</isused>\n" +
                "<thick>$thick</thick>\n" +
                "<length>$length</length>\n" +
                "<angle>$angle</angle>\n" +
                "</Door>\n\n"
        return str
    }
}