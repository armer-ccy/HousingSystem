package pers.ccy.hs.operation

import org.dom4j.Element
import org.dom4j.io.SAXReader
import pers.ccy.hs.UI.combination.CombinationUI
import pers.ccy.hs.data.*
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import kotlin.math.cos
import kotlin.math.sin

object OpFile {
    fun open(file: File, houseData: HouseData) {
        val saxReader = SAXReader()
        val document = saxReader.read(file)
        val root = document.rootElement
        val it = root.elementIterator()
        var hd = houseData
        while (it.hasNext()) {
            val element = it.next() as Element
            when (element.name) {
                "WallNumber" -> {
                    houseData.id = element.text.toInt()
                }
                "DoorOrWindowNumber" -> {
                    WindowDoorData.number = element.text.toInt()
                }
                else -> {
                    //id
                    hd.next = HouseData()
                    hd = hd.next!!
                    hd.id = element.attributeValue("id").toInt()

                    //其他
                    val it2 = element.elementIterator()
                    while (it2.hasNext()) {
                        val element2 = it2.next() as Element

                        //门窗
                        if (element2.name == "DoorOrWindow") {
                            var wd: WindowDoorData
                            if (hd.windowDoorData != null) {
                                wd = hd.windowDoorData!!
                                while (wd.next != null) wd = wd.next!!
                                wd.next = WindowDoorData()
                                wd = wd.next!!
                            } else {
                                wd = WindowDoorData()
                                hd.windowDoorData = wd
                            }
                            val it3 = element2.elementIterator()
                            wd.id = element2.attributeValue("id").toInt()
                            while (it3.hasNext()) {
                                val element3 = it3.next() as Element
                                assignment(wd, element3.name, element3.text)
                            }
                        } else {//墙的数据
                            assignment(hd, element2.name, element2.text)
                        }
                    }
                }
            }
        }
    }
    //打印
    /*houseData.allToPrint()
    println("WallNumber:" + houseData.id)
    println("DoorOrWindowNumber:" + WindowDoorData.number)*/

    fun open(file: File, combinationData: ArrayList<CombinationData>) {
        val saxReader = SAXReader()
        val document = saxReader.read(file)
        val root = document.rootElement

        val it = root.elementIterator()
        while (it.hasNext()) {
            val element = it.next() as Element
            //房间

            val it2 = element.elementIterator()
            while (it2.hasNext()) {
                val element2 = it2.next() as Element
                when (element2.name) {
                    "configure" -> {
                        val it3 = element2.elementIterator()
                        val houseData = HouseData()
                        var hd = houseData
                        while (it3.hasNext()) {
                            val element3 = it3.next() as Element
                            when (element3.name) {
                                "path" -> {
                                    combinationData[combinationData.count() - 1].path = element3.text
                                }
                                "name" -> {
                                    combinationData[combinationData.count() - 1].name = element3.text
                                }
                                "connect" -> {
                                    combinationData[combinationData.count() - 1].connect = element3.text
                                    if (element3.text != "") {
                                        val arr = element3.text.split(",")
                                        val a = combinationData[arr[0].toInt() - 1].selectDoor(arr[1].toInt())
                                        val b = combinationData[combinationData.count() - 1].selectDoor(arr[2].toInt())
                                        connect(combinationData, arr[0].toInt(), a, b)
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        val it3 = element2.elementIterator()
                        val houseData = HouseData()
                        var hd = houseData
                        //墙
                        while (it3.hasNext()) {
                            val element3 = it3.next() as Element
                            //id
                            hd.next = HouseData()
                            hd = hd.next!!
                            hd.id = element3.attributeValue("id").toInt()

                            val it4 = element3.elementIterator()
                            while (it4.hasNext()) {
                                val element4 = it4.next() as Element
                                //门窗
                                if (element4.name == "DoorOrWindow") {
                                    var wd: WindowDoorData
                                    if (hd.windowDoorData != null) {
                                        wd = hd.windowDoorData!!
                                        while (wd.next != null) wd = wd.next!!
                                        wd.next = WindowDoorData()
                                        wd = wd.next!!
                                    } else {
                                        wd = WindowDoorData()
                                        hd.windowDoorData = wd
                                    }
                                    val it5 = element4.elementIterator()
                                    wd.id = element4.attributeValue("id").toInt()
                                    while (it5.hasNext()) {
                                        val element5 = it5.next() as Element
                                        assignment(wd, element5.name, element5.text)
                                    }
                                } else {//墙的数据
                                    assignment(hd, element4.name, element4.text)

                                }
                            }
                        }
                        combinationData.add(CombinationData(houseData))
                        combinationData[combinationData.count() - 1].id = combinationData.count()
                    }
                }
            }
            //打印
            /*houseData.allToPrint()
            println("WallNumber:" + houseData.id)
            println("DoorOrWindowNumber:" + WindowDoorData.number)*/
        }
    }

    private fun assignment(houseData: HouseData, property: String, value: String) {
        when (property) {
            "type" -> houseData.type = value.toInt()
            "type2" -> houseData.type2 = value.toInt()
            "info" -> houseData.info = value.toDouble()
            "info2" -> houseData.info2 = value.toDouble()
            "info3" -> houseData.info3 = value.toDouble()
            "info4" -> houseData.info4 = value.toDouble()
        }
    }

    private fun assignment(windowDoorData: WindowDoorData, property: String, value: String) {
        when (property) {
            "type" -> windowDoorData.type = value.toInt()
            "thick" -> windowDoorData.thick = value.toDouble()
            "info" -> windowDoorData.info = value.toDouble()
            "info2" -> windowDoorData.info2 = value.toDouble()
            "info3" -> windowDoorData.info3 = value.toDouble()
            "info4" -> windowDoorData.info4 = value.toDouble()
        }
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

    fun save_before(houseData: HouseData, jf: JFrame): Boolean {
        if (isHasDoor(houseData)) return true
        val worr = JOptionPane.showOptionDialog(
            jf,
            "房间没有门",
            "房间没有门",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null, arrayOf("取消保存", "继续保存"), "取消保存"
        )
        return worr == 1
    }

    fun save_before(combinationData: ArrayList<CombinationData>, jf: JFrame): Boolean {
        return true
    }

    fun isHasDoor(houseData: HouseData): Boolean {
        var hd = houseData
        while (hd.next != null) {
            var now = hd.next!!.windowDoorData
            while (now != null) {
                if (now.type == 0)
                    return true
                now = now.next
            }
            hd = hd.next!!
        }
        return false
    }

    fun isHasDoor(combinationData: CombinationData): Boolean {
        var hd = combinationData.houseData
        while (hd.next != null) {
            var now = hd.next!!.windowDoorData
            while (now != null) {
                if (now.type == 0)
                    return true
                now = now.next
            }
            hd = hd.next!!
        }
        return false
    }

    fun save(houseData: HouseData, file: File) {
        var str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<painting>\n"
        str += if (houseData.next != null) houseData.next!!.allToSave() else ""
        str += "<WallNumber>${houseData.id}</WallNumber>\n<DoorOrWindowNumber>${WindowDoorData.number}</DoorOrWindowNumber>\n"
        str += "</painting>"
        file.writeText(str)
    }

    fun save(combinationData: ArrayList<CombinationData>, file: File) {
        var str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<combination>\n"
        combinationData.forEach { str += it.toSave() }
        str += "</combination>"
        file.writeText(str)
    }

    fun import(file: File): CombinationData {
        val saxReader = SAXReader()
        val document = saxReader.read(file)
        val root = document.rootElement
        val it = root.elementIterator()
        var houseData = HouseData()
        var hd = houseData
        while (it.hasNext()) {
            val element = it.next() as Element
            when (element.name) {
                "WallNumber" -> {
                    houseData.id = element.text.toInt()
                }
                "DoorOrWindowNumber" -> {
                    WindowDoorData.number = element.text.toInt()
                }
                else -> {
                    //id
                    hd.next = HouseData()
                    hd = hd.next!!
                    hd.id = element.attributeValue("id").toInt()

                    //其他
                    val eleIt = element.elementIterator()
                    while (eleIt.hasNext()) {
                        val e = eleIt.next() as Element

                        //门窗
                        if (e.name == "DoorOrWindow") {
                            var wd: WindowDoorData
                            if (hd.windowDoorData != null) {
                                wd = hd.windowDoorData!!
                                while (wd.next != null) wd = wd.next!!
                                wd.next = WindowDoorData()
                                wd = wd.next!!
                            } else {
                                wd = WindowDoorData()
                                hd.windowDoorData = wd
                            }
                            val it1 = e.elementIterator()
                            wd.id = e.attributeValue("id").toInt()
                            while (it1.hasNext()) {
                                val element2 = it1.next() as Element
                                assignment(wd, element2.name, element2.text)
                            }
                        } else {//墙的数据
                            assignment(hd, e.name, e.text)
                        }
                    }
                }
            }
        }
        return CombinationData(houseData, file)
        //打印
        /*houseData.allToPrint()
        println("WallNumber:" + houseData.id)
        println("DoorOrWindowNumber:" + WindowDoorData.number)*/
    }

    fun initOpen(combinationData: ArrayList<CombinationData>) {
        val jfc = JFileChooser() // 文件选择器
        jfc.fileSelectionMode = 0 // 设定只能选择到文件
        jfc.removeChoosableFileFilter(jfc.acceptAllFileFilter) // 不显示“所有文件”
        jfc.fileFilter = MyFileFilter( "房间构造文件(*.HSPRT)或房间组合文件(*.HSASM)",".HSPRT",".HSASM")
        while (combinationData.count() == 0) {
            jfc.dialogTitle = "请选择一个基础房屋"
            val state = jfc.showOpenDialog(null) // 此句是打开文件选择器界面的触发语句
            if (state != 1) {
                val f = jfc.selectedFile // f为选择到的文件
                val ends = f.name.substring(f.name.lastIndexOf("."))
                when (ends) {
                    ".HSPRT" -> {
                        combinationData.add(OpFile.import(f))
                        combinationData[combinationData.count() - 1].id = combinationData.count()
                    }
                    ".HSASM" -> {
                        open(f, combinationData)
                    }
                    else ->{

                    }
                }
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

}