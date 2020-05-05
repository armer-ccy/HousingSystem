package pers.ccy.hs.data

import javax.swing.DefaultComboBoxModel
import javax.swing.DefaultListModel

class HouseData {
    var id = 0
    var type = 0
    var type2 = 0
    var info = .0
    var info2 = .0
    var info3 = .0
    var info4 = .0
    var next: HouseData? = null
    var windowDoorData: WindowDoorData? = null

    companion object {
        val model = DefaultListModel<String>()
        val modelCB = DefaultComboBoxModel<String>()
        val modelWD = DefaultListModel<String>()
    }

    constructor() {}

    constructor(id: Int, type: Int, type2: Int, info: Double, info2: Double) {
        this.id = id
        this.type = type
        this.type2 = type2
        this.info = info
        this.info2 = info2
    }

    constructor(id: Int, type: Int, type2: Int, info: Double, info2: Double, info3: Double) {
        this.id = id
        this.type = type
        this.type2 = type2
        this.info = info
        this.info2 = info2
        this.info3 = info3
    }

    constructor(id: Int, type: Int, type2: Int, info: Double, info2: Double, info3: Double, info4: Double) {
        this.id = id
        this.type = type
        this.type2 = type2
        this.info = info
        this.info2 = info2
        this.info3 = info3
        this.info4 = info4
    }

    override fun toString(): String {
        return "$id,$type,$type2,$info,$info2,$info3,$info4"
    }

    fun allToModel() {
        //if (type != 0)
        model.addElement(toString())
        windowDoorData?.allToString(modelWD, id)
        if (type == 1) modelCB.addElement(toString())
        next?.allToModel()
    }

    fun allToString(): ArrayList<String> {
        val str: ArrayList<String>
        str = //if (type == 0) arrayListOf("") else
            arrayListOf(toString())
        if (next != null)
            str.addAll(next!!.allToString())
        return str
    }

    fun UpdatModel() {
        model.clear()
        modelWD.clear()
        modelCB.removeAllElements()
        allToModel()
    }

    fun RemoveAll() {
        id = 0
        WindowDoorData.number = 0
        next?.let {
            it.RemoveAll()
            it.windowDoorData?.let { it.RemoveAll() }
        }
        next = null
    }

    fun Search(a: Int): HouseData {
        if (id == a && type != 0)
            return this
        else
            return next!!.Search(a)
    }

    fun Add(wdd: WindowDoorData) {
        if (windowDoorData == null) {
            windowDoorData = wdd
            return
        }

        var now = windowDoorData
        while (now!!.next != null) now = now.next
        now.next = wdd
    }

    fun toSave(): String {
        var str = "<Wall id=\"$id\">\n" +
                "<type>$type</type>\n" +
                "<type2>$type2</type2>\n" +
                "<info>$info</info>\n" +
                "<info2>$info2</info2>\n" +
                "<info3>$info3</info3>\n" +
                "<info4>$info4</info4>\n"
        str += if(windowDoorData!=null) windowDoorData!!.allToSave() else ""
        str += "</Wall>\n\n"
        return str
    }

    fun allToSave(): String {
        var str = toSave()
        str += if(next!=null) next!!.allToSave() else ""
        return str
    }

    fun allToPrint() {
        println("Wall:"+toString())
        windowDoorData?.allToPrint()
        next?.allToPrint()
    }
}