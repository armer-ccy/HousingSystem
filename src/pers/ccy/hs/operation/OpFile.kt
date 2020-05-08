package pers.ccy.hs.operation

import org.dom4j.Element
import org.dom4j.io.SAXReader
import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.WindowDoorData
import java.io.File
import javax.swing.JFrame
import javax.swing.JOptionPane

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
        //打印
        /*houseData.allToPrint()
        println("WallNumber:" + houseData.id)
        println("DoorOrWindowNumber:" + WindowDoorData.number)*/
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

    fun save_before(houseData: HouseData, jf: JFrame): Boolean {
        isHasDoor(houseData)
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
}