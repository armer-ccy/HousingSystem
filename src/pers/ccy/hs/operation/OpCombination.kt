package pers.ccy.hs.operation

import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.WindowDoorData
import pers.ccy.hs.operation.OpFile.isHasDoor
import java.lang.Math.*
import javax.swing.JFileChooser
import javax.swing.JPanel
import kotlin.math.pow

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
            if(isHasDoor(comb))
                return  comb
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
}