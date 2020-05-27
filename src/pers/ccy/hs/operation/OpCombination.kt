package pers.ccy.hs.operation

import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.data.Door
import pers.ccy.hs.data.Point
import pers.ccy.hs.operation.OpFile.isHasDoor
import pers.ccy.hs.operation.OpFile.open
import java.lang.Math.acos
import java.lang.Math.sqrt
import javax.swing.JFileChooser
import kotlin.math.cos
import kotlin.math.sin


object OpCombination {
    fun UpdatModel(combinationData: ArrayList<CombinationData>) {
        CombinationData.model.clear()
        CombinationData.modelCB.removeAllElements()
        combinationData.forEach {
            it.UpdatModel()
        }
    }

    //https://blog.csdn.net/weixin_33681778/article/details/85738049
    fun VectorAngle(start: Point): Double {
        return VectorAngle(start, Point(.0, 1.0))
    }

    fun VectorAngle(start: Point, end: Point): Double {
        val a = sqrt(start.x * start.x + start.y * start.y)
        val b = sqrt(end.x * end.x + end.y * end.y)
        val aXb = start.x * end.x + start.y * end.y
        val cos_ab = aXb / (a * b)
        val angle_ab = acos(cos_ab)// * (180.0 / 3.141592654)
        //右手坐标系 v1×v2 得到的向量朝向屏幕外的话，说明就是角度就是逆时针方向，否则为顺时针方向。左手坐标系则相反。v1×v2 得到的向量朝向屏幕外的话，说明就是角度就是逆时针方向，否则为顺时针方向。左手坐标系则相反。
        val z = start.x * end.y - start.y * end.x
        return if (z > 0) angle_ab else -angle_ab
    }

    //https://www.zybang.com/question/4670a077954a227bd43d3ea9d7246e5d.html
    fun isIpsilateral(p1: Point, p2: Point, p3: Point, p4: Point): Boolean {
        val a = p2.x - p1.x
        val b = p1.y - p2.y
        val c = (p2.y - p1.y) * p1.x - (p2.x - p1.x) * p1.y
        if ((a * p3.x + b * p3.y + c) * (a * p4.x + b * p4.y + c) > 0)
            return true
        return false
    }

    fun connect(combinationData: ArrayList<CombinationData>, id: Int, a: Door, b: Door) {
        combinationData[combinationData.count() - 1].id = combinationData.count()
        a.isused = true
        b.isused = true
        //a.point[3]->b.point[0]
        //求角度
        combinationData[combinationData.count() - 1].angle =
            -OpCombination.VectorAngle(
                a.point[0]!!.sub(a.point[1]!!),
                b.point[1]!!.sub(b.point[0]!!)
            ) + combinationData[id - 1].angle
        //求起始
        //https://www.cnblogs.com/herd/p/11620760.html
        val angle = combinationData[combinationData.count() - 1].angle
        val angle1 = combinationData[id - 1].angle
        var real = a.point[3]!!
        real = Point(
            (real.x) * cos(angle1) - (real.y) * sin(angle1),
            (real.x) * sin(angle1) + (real.y) * cos(angle1)
        )
        real = Point(
            real.x + combinationData[id - 1].start.x,
            real.y + combinationData[id - 1].start.y
        )
        val m = real.sub(b.point[0]!!)
        val xx: Double =
            (m.x - real.x) * cos(angle) - (m.y - real.y) * sin(angle) + real.x
        val yy: Double =
            (m.x - real.x) * sin(angle) + (m.y - real.y) * cos(angle) + real.y
        combinationData[combinationData.count() - 1].start = Point(xx, yy)
    }
}