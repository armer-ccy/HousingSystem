package pers.ccy.hs.main

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.UI.CombinationUI
import pers.ccy.hs.UI.StructureUI
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.WindowDoorData
import pers.ccy.hs.operation.MyFileFilter
import pers.ccy.hs.operation.Op.Add
import pers.ccy.hs.operation.Op.RemoveAll
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent.*
import java.io.File
import javax.swing.*


class Main : ActionListener {

    private val modelWD = DefaultListModel<String>()
    private var windowDoorData = WindowDoorData(modelWD)

    private val model = DefaultListModel<String>()
    private val modelCB = DefaultComboBoxModel<String>()
    private var houseData = HouseData(model, modelCB, windowDoorData)

    private var select = 0
    var file: File? = null
    private var ends = ""
    var jfc = JFileChooser() // 文件选择器


    private val jf = JFrame()
    private val jpStructureUI = StructureUI(houseData, windowDoorData, model, modelCB, modelWD)
    private val jpCombinationUI = CombinationUI()
    private val menuBar = JMenuBar()
    private val fileMenu = JMenu("文件")

    private val newMenuItem = JMenuItem("新建（N）")
    private val openMenuItem = JMenuItem("打开（O）")
    private val saveMenuItem = JMenuItem("保存（S）")
    private val saveAsMenuItem = JMenuItem("另存为")

    override fun actionPerformed(e: ActionEvent?) {
        val menuItem = e?.source as JMenuItem
        when (menuItem.text) {
            "新建（N）" ->{}
            "打开（O）" -> {
                val state = jfc.showOpenDialog(null) // 此句是打开文件选择器界面的触发语句

                if (state == 1) {
                    return  // 撤销则返回
                } else {
                    val f = jfc.selectedFile // f为选择到的文件
                    newFile(f)
                    open()
                }
            }
            "保存（S）" -> {
                if (save_before()) {
                    if (file == null) {
                        val state = jfc.showSaveDialog(null) // 此句是打开文件选择器界面的触发语句

                        if (state == 1) {
                            return  // 撤销则返回
                        } else {
                            val f = jfc.selectedFile // f为选择到的文件
                            newFile(f)
                            save()
                        }
                    } else {
                        save()
                    }
                }
            }
            "另存为" -> {
                if (save_before()) {
                    jfc.setDialogTitle("另存为")
                    val state = jfc.showSaveDialog(null) // 此句是打开文件选择器界面的触发语句

                    if (state == 1) {
                        return  // 撤销则返回
                    } else {
                        val f = jfc.selectedFile // f为选择到的文件
                        newFile(f)
                        save()
                    }
                }
            }
        }
    }

    fun newFile(f: File) {
        ends = (jfc.fileFilter as MyFileFilter).getEnds()
        if (f.absolutePath.toUpperCase().endsWith(ends.toUpperCase())) {
            // 如果文件是以选定扩展名结束的，则使用原名
            file = f

        } else {
            // 否则加上选定的扩展名
            file = File(f.absolutePath + ends);
        }
        if (ends == ".HSPRT")
            jpStructureUI.jp1.jrba[1].isSelected = true
        else
            jpCombinationUI.jp1.jrba[1].isSelected = true
    }

    fun open() {
        val readText = file!!.readLines()
        var i = 0
        RemoveAll(houseData)
        RemoveAll(windowDoorData)
        for (element in 0 until readText.size) {
            val strArr = readText[element].split(",")
            if (strArr[1] == "0" && element > 0) {
                i = element
                break
            } else {
                if (element != 0)
                    Add(
                        houseData, HouseData(
                            strArr[0].toInt(),
                            strArr[1].toInt(),
                            strArr[2].toInt(),
                            strArr[3].toDouble(),
                            strArr[4].toDouble(),
                            strArr[5].toDouble(),
                            strArr[6].toDouble()
                        )
                    )
                else
                    houseData.id = strArr[0].toInt()
            }
        }
        houseData.UpdatModel()

        for (element in i until readText.size) {
            val strArr = readText[element].split(",")
            if (element == i)
                windowDoorData.id = strArr[0].toInt()
            else
                Add(
                    windowDoorData, WindowDoorData(
                        strArr[0].toInt(),
                        strArr[1].toInt(),
                        strArr[2].toInt(),
                        strArr[3].toDouble(),
                        strArr[4].toDouble(),
                        strArr[5].toDouble(),
                        strArr[6].toDouble()
                    )
                )
        }
        windowDoorData.UpdatModel()
        jf.repaint()
    }

    fun save() {
        file!!.writeText("")
        var str = houseData.allToString()
        for (i in str)
            file!!.appendText("$i\n")
        str = windowDoorData.allToString()
        for (i in str)
            file!!.appendText("$i\n")
    }

    fun save_before(): Boolean {
        var now = windowDoorData
        while (now.next != null) {
            if (now.next!!.type == 0)
                return true
            now = now.next!!
        }
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

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main()
        }
    }

    init {
        windowDoorData.houseData = houseData

        jf.title = "房屋拼接系统"
        jf.layout = XYLayout()
        jf.setBounds(100, 100, 1000, 410)

        fileMenu.add(newMenuItem)
        newMenuItem.setMnemonic('N')
        newMenuItem.accelerator = KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK)
        fileMenu.add(openMenuItem)
        openMenuItem.setMnemonic('O')
        openMenuItem.accelerator = KeyStroke.getKeyStroke(VK_O, CTRL_DOWN_MASK)
        fileMenu.addSeparator() //添加一条分割线
        fileMenu.add(saveMenuItem)
        saveMenuItem.setMnemonic('S')
        saveMenuItem.accelerator = KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK)
        fileMenu.add(saveAsMenuItem)
        saveAsMenuItem.accelerator = KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK + SHIFT_DOWN_MASK)

        menuBar.add(fileMenu)

        newMenuItem.addActionListener(this)
        openMenuItem.addActionListener(this)
        saveMenuItem.addActionListener(this)
        saveAsMenuItem.addActionListener(this)

        jf.jMenuBar = menuBar
        //jf.jMenuBar.setBounds(0,0,100,30)

        //jf.add(menuBar,XYConstraints(0,0,1000,100))

        jf.add(jpStructureUI, XYConstraints(0, 0, 1000, 400))
        jf.add(jpCombinationUI, XYConstraints(0, 0, 1000, 400))

        jpStructureUI.jp1.jrba[1].addActionListener {
            jpCombinationUI.jp1.jrba[1].isSelected = true
            jpStructureUI.isVisible = false
            jpCombinationUI.isVisible = true
            select = 1

        }
        jpCombinationUI.jp1.jrba[0].addActionListener {
            jpStructureUI.jp1.jrba[0].isSelected = true
            jpStructureUI.isVisible = true
            jpCombinationUI.isVisible = false
            select = 0

        }

        jpStructureUI.isVisible = true
        jpCombinationUI.isVisible = false

        jf.isVisible = true
        jf.isResizable = true
        jf.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    init {
        jfc.fileSelectionMode = 0 // 设定只能选择到文件
        jfc.removeChoosableFileFilter(jfc.acceptAllFileFilter) // 不显示“所有文件”
        jfc.fileFilter = MyFileFilter(".HSPRT", "房间构造文件(*.HSPRT)")
        jfc.fileFilter = MyFileFilter(".HSASM", "房间组合文件(*.HSASM)")
        //jfc.fileFilter = FileNameExtensionFilter("房间构造文件(*.HSPRT)", "HSPRT") // 设置文件过滤器
        //jfc.fileFilter = FileNameExtensionFilter("房间组合文件(*.HSASM)", "HSASM") // 设置文件过滤器
    }
}