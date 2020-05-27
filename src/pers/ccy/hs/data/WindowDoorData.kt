package pers.ccy.hs.data

import javax.swing.DefaultComboBoxModel
import javax.swing.DefaultListModel

class WindowDoorData() {
    var id = 0              //门窗标识号
    var type = 0            //门窗类别
    var thick: Double = .0  //厚度
    var info: Double = .0   //信息1
    var info2: Double = .0  //信息2
    var info3: Double = .0  //信息3
    var info4: Double = .0  //信息4
    var next: WindowDoorData? = null

    companion object {
        var number = 0
    }

    constructor(id: Int, type: Int, thick: Double, info: Double, info2: Double, info3: Double) : this() {
        this.id = id
        this.type = type
        this.thick = thick
        this.info = info
        this.info2 = info2
        this.info3 = info3
    }


    constructor(id: Int, type: Int, thick: Double, info: Double, info2: Double, info3: Double, info4: Double) : this(
        id,
        type,
        thick,
        info,
        info2,
        info3
    ) {
        this.info4 = info4
    }

    constructor(v: WindowDoorData) : this(
        v.id,
        v.type,
        v.thick,
        v.info,
        v.info2,
        v.info3,
        v.info4
    ) {
    }

    constructor(str: String) : this() {
        val strArr = str.split(",")
        this.id = strArr[0].toInt()
        this.type = strArr[1].toInt()
        this.thick = strArr[2].toDouble()
        this.info = strArr[3].toDouble()
        this.info2 = strArr[4].toDouble()
        this.info3 = strArr[5].toDouble()
        this.info4 = strArr[6].toDouble()
    }

    override fun toString(): String {
        return "$id,$type,$thick,$info,$info2,$info3,$info4"
    }

    fun toString(Wid: Int): String {
        return "$id,$Wid,$type,$thick,$info,$info2,$info3,$info4"
    }

    fun allToString(modelWD: DefaultListModel<String>, Wid: Int) {
        //if (type != 0)
        modelWD.addElement(toString(Wid))
        next?.allToString(modelWD, Wid)
    }

    fun allToString(): ArrayList<String> {
        val str: ArrayList<String>
        str = //if (type == 0) arrayListOf("") else
            arrayListOf(toString())
        if (next != null)
            str.addAll(next!!.allToString())
        return str
    }

    fun RemoveAll() {
        next?.let { it.RemoveAll() }
        next = null
    }

    fun toSave(): String {
        return "<DoorOrWindow id=\"$id\">\n" +
                "<type>$type</type>\n" +
                "<thick>$thick</thick>\n" +
                "<info>$info</info>\n" +
                "<info2>$info2</info2>\n" +
                "<info3>$info3</info3>\n" +
                "<info4>$info4</info4>\n" +
                "</DoorOrWindow>\n"
    }

    fun allToSave(): String {
        var str = toSave()
        str += if (next != null) next!!.allToSave() else ""
        return str
    }

    fun allToPrint() {
        println("DoorOrWindow:" + toString())
        if (next != null) next?.allToPrint()
    }

    fun exchange(v: WindowDoorData) {
        id = v.id
        type = v.type
        thick = v.thick
        info = v.info
        info2 = v.info2
        info3 = v.info3
        info4 = v.info4
    }
}