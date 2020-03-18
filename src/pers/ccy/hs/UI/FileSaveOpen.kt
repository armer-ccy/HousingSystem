package pers.ccy.hs.UI

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.filechooser.FileFilter
import javax.swing.filechooser.FileNameExtensionFilter

class t3 : JFrame() {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) { // TODO Auto-generated method stub
            val test = t3()
        }
    }

    init {
        title = "选项卡面板"
        setBounds(400, 400, 400, 400)
        defaultCloseOperation = EXIT_ON_CLOSE
        val label = JLabel("双击面板任意一处选择照片(JPG/GIF)", SwingConstants.CENTER) //设置标签文字居中显示
        label.addMouseListener(object : MouseAdapter() {
            var fileChooser: JFileChooser? = null
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    val i = fileChooser!!.showOpenDialog(contentPane) // 显示文件选择对话框
                    // 判断用户单击的是否为“打开”按钮
                    if (i == JFileChooser.APPROVE_OPTION) {
                        val selectedFile = fileChooser!!.selectedFile // 获得选中的图片对象
                        label.icon = ImageIcon(selectedFile.absolutePath) // 将图片显示到标签上
                        label.text = null
                    }
                }
            }

            init {
                fileChooser = JFileChooser() // 创建文件选择对话框
                val filter: FileFilter =
                    FileNameExtensionFilter("图像文件（JPG/GIF）", "HSPRT", "HSASM") // 设置文件过滤器，只列出JPG或GIF格式的图片
                fileChooser!!.fileFilter = filter
            }
        })
        add(label)
        isVisible = true
    }
}