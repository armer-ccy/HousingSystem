package pers.ccy.hs.main

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.UI.combination.CombinationUI
import pers.ccy.hs.UI.output.OutputUI
import pers.ccy.hs.UI.structure.StructureUI
import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.operation.MyFileFilter
import pers.ccy.hs.operation.OpCombination.UpdatModel
import pers.ccy.hs.operation.OpFile.initOpen
import pers.ccy.hs.operation.OpFile.open
import pers.ccy.hs.operation.OpFile.save
import pers.ccy.hs.operation.OpFile.save_before
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent.*
import java.io.File
import javax.swing.*


class Main : ActionListener {
    private var houseData = HouseData()
    var combinationData: ArrayList<CombinationData> = ArrayList()

    private var select = 0
    var fileStructure: File? = null
    var fileComb: File? = null
    private var ends = ""
    var jfc = JFileChooser() // 文件选择器
    var jfc1 = JFileChooser() // 文件选择器
    var jfc2 = JFileChooser() // 文件选择器


    private val jf = JFrame()
    private val jpStructureUI = StructureUI(houseData)//房屋构造
    private val jpCombinationUI = CombinationUI(combinationData)//选择方式
    private val menuBar = JMenuBar()
    private val fileMenu = JMenu("文件")

    private val newMenuItem = JMenuItem("新建（N）")
    private val openMenuItem = JMenuItem("打开（O）")
    private val saveMenuItem = JMenuItem("保存（S）")
    private val saveAsMenuItem = JMenuItem("另存为")
    private val outputMenuItem = JMenuItem("导出")

    override fun actionPerformed(e: ActionEvent?) {
        val menuItem = e?.source as JMenuItem
        when (menuItem.text) {
            "新建（N）" -> {
                if (jpStructureUI.isVisible) {
                    newStructure()
                    fileStructure = null
                } else {
                    newComb()
                    fileComb = null
                    UpdatModel(combinationData)
                    jf.repaint()
                    initOpen(combinationData)
                    UpdatModel(combinationData)
                    jf.repaint()
                }
            }
            "打开（O）" -> {
                val state = jfc.showOpenDialog(null) // 此句是打开文件选择器界面的触发语句

                if (state == 1) {
                    return  // 撤销则返回
                } else {
                    val f = jfc.selectedFile // f为选择到的文件
                    newFile(f, jfc)
                    if (ends == ".HSPRT") {
                        newStructure()
                        open(f, houseData)
                        jpCombinationUI.jp1.jrba[0].doClick()
                    } else {
                        newComb()
                        open(f, combinationData)
                        jpStructureUI.jp1.jrba[1].doClick()
                        UpdatModel(combinationData)
                        CombinationData.modelNew.removeAllElements()
                        CombinationUI.comData?.UpdatCBModel()
                    }
                    jf.repaint()
                }
            }
            "保存（S）" -> {
                if (jpStructureUI.isVisible) {
                    if (save_before(houseData, jf)) {
                        if (fileStructure == null) {
                            jfc1.dialogTitle = "保存"
                            val state = jfc1.showSaveDialog(null) // 此句是打开文件选择器界面的触发语句

                            if (state == 1) {
                                return  // 撤销则返回
                            } else {
                                val f = jfc1.selectedFile // f为选择到的文件
                                newFile(f, jfc1)
                                save(houseData, fileStructure!!)
                            }
                        } else {
                            save(houseData, fileStructure!!)
                        }
                    }
                } else {
                    if (save_before(combinationData, jf)) {
                        if (fileComb == null) {
                            jfc2.dialogTitle = "保存"
                            val state = jfc2.showSaveDialog(null) // 此句是打开文件选择器界面的触发语句

                            if (state == 1) {
                                return  // 撤销则返回
                            } else {
                                val f = jfc2.selectedFile // f为选择到的文件
                                newFile(f, jfc2)
                                save(combinationData, fileComb!!)
                            }
                        } else {
                            save(combinationData, fileComb!!)
                        }
                    }
                }
            }
            "另存为" -> {
                if (jpStructureUI.isVisible) {
                    if (save_before(houseData, jf)) {
                        val state = jfc1.showSaveDialog(null) // 此句是打开文件选择器界面的触发语句

                        if (state == 1) {
                            return  // 撤销则返回
                        } else {
                            val f = jfc1.selectedFile // f为选择到的文件
                            newFile(f, jfc1)
                            save(houseData, fileStructure!!)
                        }
                    }
                } else {
                    if (save_before(combinationData, jf)) {
                        jfc2.dialogTitle = "另存为"
                        val state = jfc2.showSaveDialog(null) // 此句是打开文件选择器界面的触发语句

                        if (state == 1) {
                            return  // 撤销则返回
                        } else {
                            val f = jfc2.selectedFile // f为选择到的文件
                            newFile(f, jfc2)
                            save(combinationData, fileComb!!)
                        }
                    }
                }
            }
            "导出" -> {
                OutputUI().show()
            }
        }

        houseData.UpdatModel()
        jf.repaint()
    }

    fun newFile(f: File, jfc: JFileChooser) {
        if ((jfc.fileFilter as MyFileFilter).ends.size == 1) ends = (jfc.fileFilter as MyFileFilter).gets()
        else ends = f.name.substring(f.name.lastIndexOf("."))
        val f1 = if (f.absolutePath.toUpperCase().endsWith(ends.toUpperCase())) {
            // 如果文件是以选定扩展名结束的，则使用原名
            f
        } else {
            // 否则加上选定的扩展名
            File(f.absolutePath + ends)
        }
        if (ends == ".HSPRT") {
            fileStructure = f1
        } else {
            fileComb = f1
        }
    }

    fun newStructure() {
        houseData.RemoveAll()
        houseData.UpdatModel()
    }

    fun newComb() {
        combinationData.clear()
        UpdatModel(combinationData)
        CombinationUI.comData = null
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main()
        }
    }

    init {
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
        fileMenu.add(outputMenuItem)

        menuBar.add(fileMenu)

        newMenuItem.addActionListener(this)
        openMenuItem.addActionListener(this)
        saveMenuItem.addActionListener(this)
        saveAsMenuItem.addActionListener(this)
        outputMenuItem.addActionListener(this)

        jf.jMenuBar = menuBar
        //jf.jMenuBar.setBounds(0,0,100,30)

        //jf.add(menuBar,XYConstraints(0,0,1000,100))

        jf.add(jpStructureUI, XYConstraints(0, 0, 10000, 10000))
        jf.add(jpCombinationUI, XYConstraints(0, 0, 10000, 10000))

        jpStructureUI.jp1.jrba[1].addActionListener {
            jpStructureUI.isVisible = false
            jpCombinationUI.isVisible = true
            select = 1
            jpCombinationUI.jp1.jrba[1].doClick()
        }
        jpCombinationUI.jp1.jrba[0].addActionListener {
            jpStructureUI.isVisible = true
            jpCombinationUI.isVisible = false
            select = 0
            jpStructureUI.jp1.jrba[0].doClick()
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
        jfc.fileFilter = MyFileFilter("房间构造文件(*.HSPRT)", ".HSPRT")
        jfc.fileFilter = MyFileFilter("房间组合文件(*.HSASM)", ".HSASM")
        jfc.fileFilter = MyFileFilter("房间构造文件(*.HSPRT)或房间组合文件(*.HSASM)", ".HSPRT", ".HSASM")
        jfc1.fileSelectionMode = 0 // 设定只能选择到文件
        jfc1.removeChoosableFileFilter(jfc.acceptAllFileFilter) // 不显示“所有文件”
        jfc1.fileFilter = MyFileFilter("房间构造文件(*.HSPRT)", ".HSPRT")

        jfc2.fileSelectionMode = 0 // 设定只能选择到文件
        jfc2.removeChoosableFileFilter(jfc.acceptAllFileFilter) // 不显示“所有文件”
        jfc2.fileFilter = MyFileFilter("房间组合文件(*.HSASM)", ".HSASM")
    }
}