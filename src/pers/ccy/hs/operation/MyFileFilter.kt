package pers.ccy.hs.operation

import java.io.File
import javax.swing.filechooser.FileFilter

class MyFileFilter(
    private var ends: String, // 文件后缀
    private var desc: String // 文件描述文字
) : FileFilter() {

    // 只显示符合扩展名的文件，目录全部显示
    override fun accept(file: File): Boolean {
        if (file.isDirectory) return true
        val fileName = file.name
        return fileName.toUpperCase().endsWith(ends.toUpperCase())
    }

    override fun getDescription()=desc

    fun getEnds()=ends
}