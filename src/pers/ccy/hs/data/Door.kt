package pers.ccy.hs.data

class Door {
    var point = arrayOfNulls<Point>(4)
    var isused = false
    var thick = .0
    var length = .0
    var angle = .0
    var id = 0

    constructor(id: Int, thick: Double, length: Double, angle: Double, point1: Point, point2: Point) {
        this.id = id
        this.thick = thick
        this.length = length
        this.angle = angle
        point[0] = point1
        point[1] = point2
    }

    fun addlast(point: Array<Point>) {
        this.point[2] = point[0]
        this.point[3] = point[1]
    }
}