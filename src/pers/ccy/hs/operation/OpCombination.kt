package pers.ccy.hs.operation

import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.data.Point
import pers.ccy.hs.operation.OpFile.isHasDoor
import java.lang.Math.acos
import java.lang.Math.sqrt
import javax.swing.JFileChooser


object OpCombination {
    fun initOpen(combinationData: ArrayList<CombinationData>) {
        var jfc = JFileChooser() // 文件选择器
        while (combinationData.count() == 0) {
            jfc.dialogTitle = "请选择一个基础房屋"
            val state = jfc.showOpenDialog(null) // 此句是打开文件选择器界面的触发语句
            if (state != 1) {
                val f = jfc.selectedFile // f为选择到的文件
                combinationData.add(OpFile.import(f))
                combinationData[combinationData.count() - 1].id = combinationData.count()
            }
        }
    }

    fun importOpen(): CombinationData? {
        var jfc = JFileChooser() // 文件选择器
        jfc.dialogTitle = "请选择一个房屋"
        val state = jfc.showOpenDialog(null) // 此句是打开文件选择器界面的触发语句
        if (state != 1) {
            val f = jfc.selectedFile // f为选择到的文件
            var comb = OpFile.import(f)
            if (isHasDoor(comb))
                return comb
        }
        return null
    }

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
        return if(z>0)angle_ab else -angle_ab
    }

    //https://www.zybang.com/question/4670a077954a227bd43d3ea9d7246e5d.html
    fun isIpsilateral(p1:Point,p2:Point,p3:Point,p4:Point):Boolean{
        val a=p2.x-p1.x
        val b=p1.y-p2.y
        val c=(p2.y-p1.y)*p1.x-(p2.x-p1.x)*p1.y
        if((a*p3.x+b*p3.y+c)*(a*p4.x+b*p4.y+c)>0)
            return true
        return false
    }
}