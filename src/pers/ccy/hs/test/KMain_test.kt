package pers.ccy.hs.test

import com.google.gson.GsonBuilder
import org.dom4j.Attribute
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.junit.Test
import java.io.File

/**
 * @author futao
 * Created on 2017/11/3 - 14:37.
 */
class DOM4JTest {

    init {
        println("aaaaa")
        val list = ArrayList<Book>()
        var book = Book()

        val saxReader = SAXReader()
        val document = saxReader.read(File("C:\\Users\\ccy\\Desktop\\books.xml"))
        println("aaaaa")
        //根节点
        val root = document.rootElement
        //通过Element的elementIterator()方法获取迭代器
        val elementIterator = root.elementIterator()
        //遍历迭代器，获取根节点的子节点信息
        for ((index, i) in elementIterator.withIndex()) {
            val element = i as Element
//            println(element.name)
            println("===============开始解析第${index + 1}本书===============")
            book = Book()
            //遍历子节点的属性
            i.attributes()
                .map { it as Attribute }
                .forEach {
                    println(it.name + "    :   " + it.value)
                    assignment(book, it.name, it.value)
                }
            println("aaaaa")
            //遍历子节点的子节点和内容
            for (k in i.elementIterator()) {
                k as Element
                println(k.name + "    :   " + k.textTrim)
                assignment(book, k.name, k.textTrim)
            }
            list.add(book)
        }
        println(GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(list))
    }

    private fun assignment(book: Book, property: String, value: String): Book {
        when (property) {
            "id" -> book.id = value.toInt()
            "name" -> book.name = value
            "author" -> book.author = value
            "year" -> book.year = value
            "price" -> book.price = value
        }
        return book
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("aaaaa")
            DOM4JTest()
        }
    }
}