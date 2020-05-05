package pers.ccy.hs.test

import org.dom4j.DocumentHelper
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileOutputStream


object Demo {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val doc = DocumentHelper.createDocument()

        //增加根节点
        val books = doc.addElement("books")

        //增加子元素
        val book1 = books.addElement("book")
        val title1 = book1.addElement("title")
        val author1 = book1.addElement("author")
        val book2 = books.addElement("book")
        val title2 = book2.addElement("title")
        val author2 = book2.addElement("author")


        //为子节点添加属性
        book1.addAttribute("id", "001")

        //为元素添加内容
        title1.text = "Harry Potter"
        author1.text = "J K. Rowling"
        book2.addAttribute("id", "002")
        title2.text = "Learning XML"
        author2.text = "Erik T. Ray"


        //实例化输出格式对象
        val format = OutputFormat.createPrettyPrint()

        //设置输出编码
        format.encoding = "UTF-8"

        //创建需要写入的File对象
        val file = File("C:" + File.separator + "Users\\ccy\\Desktop\\books.xml")

        //生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式
        val writer = XMLWriter(FileOutputStream(file), format)

        //开始写入，write方法中包含上面创建的Document对象
        writer.write(doc)
    }
}