package pers.ccy.hs.test

object GetCharAscii {
    /*0-9对应Ascii 48-57
     *A-Z 65-90
     *a-z 97-122
     *第33～126号(共94个)是字符，其中第48～57号为0～9十个阿拉伯数字
     */
    @JvmStatic
    fun main(args: Array<String>) {
// System.out.println(charToByteAscii('9'));
// System.out.println(byteAsciiToChar(57));
        println(SumStrAscii("="))
        println(SumStrAscii(">"))
        println(SumStrAscii("abc"))
    }

    /**
     * 方法一：将char 强制转换为byte
     *
     * @param ch
     * @return
     */
    fun charToByteAscii(ch: Char): Byte {
        return ch.toByte()
    }

    /**
     * 方法二：将char直接转化为int，其值就是字符的ascii
     *
     * @param ch
     * @return
     */
    fun charToByteAscii2(ch: Char): Byte {
        return ch.toByte()
    }

    /**
     * 同理，ascii转换为char 直接int强制转换为char
     *
     * @param ascii
     * @return
     */
    fun byteAsciiToChar(ascii: Int): Char {
        return ascii.toChar()
    }

    /**
     * 求出字符串的ASCII值和
     * 注意，如果有中文的话，会把一个汉字用两个byte来表示，其值是负数
     */
    fun SumStrAscii(str: String): Int {
        val bytestr = str.toByteArray()
        var sum = 0
        for (i in bytestr.indices) {
            sum += bytestr[i]
        }
        return sum
    }
}