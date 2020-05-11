package pers.ccy.hs.test

import pers.ccy.hs.data.STLFile
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile

class DOM4JTest {
    var num = 12
    var stlf = STLFile(12)
    var vertices = stlf.vertices
    var normals = stlf.normals
    var stlPath = "C:\\Users\\ccy\\Desktop\\123.txt"

    init {
        try {
            val f: File = File(stlPath)
            f.delete()
            val stl = RandomAccessFile(f, "rw")
            stl.writeInt(-100)
            stl.seek(0)
            println(stl.readInt().toUInt())
            /*for (i in 0 until 80) {
                stl.writeByte(0x41)
            }
            stl.writeInt(num)
            for (i in 0 until num) {
                for (j in 0 until 3) {
                    stl.writeFloat(normals[3*i+j])
                }
                for (j in 0 until 9) {
                    stl.writeFloat(vertices[3*i+j])
                }
            }*/
            stl.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
fun main(){
    DOM4JTest()
}