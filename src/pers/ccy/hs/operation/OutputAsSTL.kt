package pers.ccy.hs.operation

import pers.ccy.hs.data.STLFile
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer

class OutputAsSTL(var stlf:STLFile,var stlPath:String) {
    var num = stlf.facetNum
    var vertices = stlf.vertices
    var normals = stlf.normals

    init {
        try {
            val f: File = File(stlPath)
            f.delete()
            val stl = RandomAccessFile(f, "rw")
            for (i in 0 until 80) {
                stl.writeByte(0x41)
            }
            stl.writeInt(writeInt(num))
            for (i in 0 until num) {
                for (j in 0 until 3) {
                    val b=float2ByteArray(normals[3 * i + j])
                    stl.writeByte(b!![3].toInt())
                    stl.writeByte(b[2].toInt())
                    stl.writeByte(b[1].toInt())
                    stl.writeByte(b[0].toInt())
                }
                for (j in 0 until 9) {
                    val b=float2ByteArray(vertices[9 * i + j])
                    stl.writeByte(b!![3].toInt())
                    stl.writeByte(b[2].toInt())
                    stl.writeByte(b[1].toInt())
                    stl.writeByte(b[0].toInt())
                }
                stl.writeByte(0)
                stl.writeByte(0)
            }
            stl.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun float2ByteArray(value: Float): ByteArray? {
        return ByteBuffer.allocate(4).putFloat(value).array()
    }

    fun writeInt(n: Int): Int {
        val a: Int = (n/0x1000000)
        val b: Int = ((n/0x10000)%100) shl 8
        val c: Int = ((n/0x100)%100) shl 16
        val d: Int = (n%0x100) shl 24
        return a+b+c+d
    }
}