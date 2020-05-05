package pers.ccy.hs.test

import org.dom4j.Element
import org.dom4j.io.SAXReader
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.WindowDoorData
import java.io.File

object Demo1 {
    @Throws(java.lang.Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val saxReader = SAXReader()
        val document = saxReader.read(File("C:\\Users\\ccy\\Desktop\\xml.xml"))
        val root = document.rootElement
        val it = root.elementIterator()
        val houseData = HouseData()
        var hd = houseData
        while (it.hasNext()) {
            val element = it.next() as Element
            if (element.name == "WallNumber") {
                println("asdasdasdasdasdasdasdasdasdsadasdasd")
                houseData.id=element.text.toInt()
            } else if (element.name == "DoorOrWindowNumber") {
                WindowDoorData.number=element.text.toInt()
            }else {
                //id
                hd.next = HouseData()
                hd = hd.next!!
                hd.id = element.attributeValue("id").toInt()

                //其他
                val eleIt = element.elementIterator()
                while (eleIt.hasNext()) {
                    val e = eleIt.next() as Element

                    println(e.name)
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
        houseData.allToPrint()
        println("WallNumber:"+houseData.id)
        println("DoorOrWindowNumber:"+WindowDoorData.number)
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
}