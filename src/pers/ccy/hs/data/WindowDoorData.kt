package pers.ccy.hs.data

import javax.swing.DefaultListModel

class WindowDoorData {
    var id = 0
    var id_l = 0
    var type = 0
    var info: Double = .0
    var info2: Double = .0
    var info3: Double = .0
    var info4: Double = .0
    var next: WindowDoorData? = null
    var model: DefaultListModel<String>? = null
    var houseData: HouseData? = null
        set

    constructor() {}

    constructor(model: DefaultListModel<String>) {
        this.model = model
    }

    constructor(id: Int, id_l: Int, type: Int, info: Double, info2: Double, info3: Double) {
        this.id = id
        this.id_l = id_l
        this.type=type
        this.info = info
        this.info2 = info2
        this.info3 = info3
    }

    constructor(id: Int, id_l: Int, type:Int, info: Double, info2: Double, info3: Double, info4: Double) {
        this.id = id
        this.id_l = id_l
        this.type=type
        this.info = info
        this.info2 = info2
        this.info3 = info3
        this.info4 = info4
    }

    override fun toString(): String {
        return "$id,$id_l,$type,$info,$info2,$info3,$info4"
    }

    fun allToString(model: DefaultListModel<String>) {
        //if (type != 0)
        model.addElement(toString())
        next?.allToString(model)
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
        model?.clear()
        model?.let { allToString(it) }
    }
}