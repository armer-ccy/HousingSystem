package pers.ccy.hs.main

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.UI.CombinationUI
import pers.ccy.hs.UI.StructureUI
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.WindowDoorData
import pers.ccy.hs.operation.MyFileFilter
import pers.ccy.hs.operation.OpFile.open
import pers.ccy.hs.operation.OpFile.save
import pers.ccy.hs.operation.OpFile.save_before
import pers.ccy.hs.operation.OpPainting.Add
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent.*
import java.io.File
import javax.swing.*


class Main : ActionListener {
    private var houseData = HouseData()

    private var select = 0
    var file: File? = null
    private var ends = ""
    var jfc = JFileChooser() // 文件选择器


    private val jf = JFrame()
    private val jpStructureUI = StructureUI(houseData)
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
            "新建（N）" -> {
                new()
            }
            "打开（O）" -> {
                val state = jfc.showOpenDialog(null) // 此句是打开文件选择器界面的触发语句

                if (state == 1) {
                    return  // 撤销则返回
                } else {
                    val f = jfc.selectedFile // f为选择到的文件
                    newFile(f)
                    new()
                    open(f, houseData)
                }
            }
            "保存（S）" -> {
                if (save_before(houseData, jf)) {
                    if (file == null) {
                        val state = jfc.showSaveDialog(null) // 此句是打开文件选择器界面的触发语句

                        if (state == 1) {
                            return  // 撤销则返回
                        } else {
                            val f = jfc.selectedFile // f为选择到的文件
                            newFile(f)
                            save(houseData,file!!)
                        }
                    } else {
                        save(houseData,file!!)
                    }
                }
            }
            "另存为" -> {
                if (save_before(houseData, jf)) {
                    jfc.setDialogTitle("另存为")
                    val state = jfc.showSaveDialog(null) // 此句是打开文件选择器界面的触发语句

                    if (state == 1) {
                        return  // 撤销则返回
                    } else {
                        val f = jfc.selectedFile // f为选择到的文件
                        newFile(f)
                        save(houseData,file!!)
                    }
                }
            }
        }

        houseData.UpdatModel()
        jf.repaint()
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

    fun new() {
        houseData.RemoveAll()
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