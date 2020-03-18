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
    var model: DefaultListModel<String>? = null
    var modelCB: DefaultComboBoxModel<String>? = null
    var windowDoorData: WindowDoorData? = null

    constructor() {}

    constructor(
        model: DefaultListModel<String>,
        modelCB: DefaultComboBoxModel<String>,
        windowDoorData: WindowDoorData
    ) {
        this.model = model
        this.modelCB = modelCB
        this.windowDoorData = windowDoorData
    }

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

    fun allToString(model: DefaultListModel<String>) {
        //if (type != 0)
        model.addElement(toString())
        next?.allToString(model)
    }

    fun allToString(model: DefaultComboBoxModel<String>) {
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
        modelCB?.removeAllElements()
        modelCB?.let { allToString(it) }
    }
}