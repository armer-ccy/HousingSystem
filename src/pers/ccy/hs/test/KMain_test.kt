package pers.ccy.hs.test

import kotlin.math.PI
import kotlin.math.atan2

fun main() {
    while(true) {
        var a: Int = readLine()?.toInt() ?: 0
        var b: Int = readLine()?.toInt() ?: 0
        var angle = PI/2 - atan2(b.toFloat(), a.toFloat()).toDouble()
        println(angle)
        println(angle * 180 / PI)
    }
}