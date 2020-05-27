package pers.ccy.hs.data

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class STLFile() {
    var facetNum = 0 // 三角片数
    var vertices = ArrayList<Float>()// 点 length=[faces * 3 * 3]//3顶点*3xyz
    var normals = ArrayList<Float>()// 三角面片法向量的3个分量值数据length=[faces * 3]

    constructor(facetNum: Int) : this() {
        this.facetNum = facetNum
        for (i in 1..facetNum) {
            normals.addAll(listOf(0f, 0f, 0f))
        }
        //实验正方体
        vertices.addAll(
            listOf(
                0f, 0f, 0f, 0f, 100f, 0f, 0f, 0f, 100f,
                0f, 100f, 0f, 0f, 0f, 100f, 0f, 100f, 100f,
                0f, 100f, 0f, 0f, 100f, 100f, 100f, 100f, 0f,
                100f, 100f, 100f, 0f, 100f, 100f, 100f, 100f, 0f,
                0f, 0f, 100f, 0f, 100f, 100f, 100f, 0f, 100f,
                0f, 100f, 100f, 100f, 100f, 100f, 100f, 0f, 100f,
                100f, 100f, 0f, 100f, 0f, 100f, 100f, 0f, 0f,
                100f, 100f, 0f, 100f, 100f, 100f, 100f, 0f, 100f,
                0f, 0f, 0f, 0f, 0f, 100f, 100f, 0f, 0f,
                100f, 0f, 100f, 0f, 0f, 100f, 100f, 0f, 0f,
                0f, 0f, 0f, 0f, 100f, 0f, 100f, 0f, 0f,
                0f, 100f, 0f, 100f, 0f, 0f, 100f, 100f, 0f
            )
        )
    }

    fun add(
        p1x: Double, p1y: Double,
        p2x: Double, p2y: Double,
        low: Double, high: Double
    ) {
        normals.addAll(listOf(0f, 0f, 0f))
        vertices.addAll(
            listOf(
                p1x.toFloat(),
                p1y.toFloat(),
                low.toFloat(),
                p2x.toFloat(),
                p2y.toFloat(),
                low.toFloat(),
                p1x.toFloat(),
                p1y.toFloat(),
                high.toFloat()
            )
        )
        normals.addAll(listOf(0f, 0f, 0f))
        vertices.addAll(
            listOf(
                p1x.toFloat(),
                p1y.toFloat(),
                high.toFloat(),
                p2x.toFloat(),
                p2y.toFloat(),
                high.toFloat(),
                p2x.toFloat(),
                p2y.toFloat(),
                low.toFloat()
            )
        )
        facetNum += 2
    }

    fun addStyle(
        style: Int,
        p1x: Double, p1y: Double,
        p2x: Double, p2y: Double,
        low: Double, high: Double
    ) {
        val thick = 1
        when (style) {
            0 -> {
            }
            1 -> {
                add(p1x, p1y, p2x, p2y, (low + high) / 2.5 - thick, (low + high) / 2.5 + thick)
            }
            2 -> {
                add(p1x, p1y, p2x, p2y, (low + high) / 2 - thick, (low + high) / 2 + thick)
                val angle= atan2(p1y-p2y,p1x-p2x)
                add(
                    (p1x + p2x) / 2 - thick * cos(angle),
                    (p1y + p2y) / 2 - thick * sin(angle),
                    (p1x + p2x) / 2 + thick * cos(angle),
                    (p1y + p2y) / 2 + thick * sin(angle),
                    low,
                    high
                )
            }
            else -> {
            }
        }

    }
}