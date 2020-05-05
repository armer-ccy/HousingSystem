package pers.ccy.hs.UI

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.operation.OpPainting.DisplayHiding
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class StructureUI(houseData: HouseData) : JPanel(), ActionListener {

    var draw_w = 500
    var draw_h = 350

    val jp1 = JPanel_SelectTasks()
    private val jp2 = JPanel_SelectStraightLineORCurve()
    private val jp3 = JPanel_StraightLine(houseData)
    private val jp4 = JPanel_Curve(houseData)
    private val jp5 = JPanel_Door(houseData)
    private val jp6 = JPanel_Window(houseData)

    private val jp_info = JPanel_Info(houseData)
    private val jp_wd_info = JPanel_Info_WD( houseData)
    private val jp_draw = JPanel_Draw(houseData, draw_w - 10, draw_h - 20)

    override fun actionPerformed(e: ActionEvent) {
        /*if (jp1.jrba.get(0).isSelected) {
            Task1_init()
        } else {
            jp2.isVisible = false
            jp3.isVisible = false
            jp4.isVisible = false
            jp5.isVisible = false
            jp6.isVisible = false
            jp_info.isVisible = false
            jp_wd_info.isVisible = false
            jp_draw.isVisible = false
        }*/

        //if (jp1.jrba.get(0).isSelected) {
        when (jp2.jrba.filter { it.isSelected }.first().text) {
            "直线" -> DisplayHiding(0, jp3, jp4, jp5, jp6)
            "曲线" -> DisplayHiding(1, jp3, jp4, jp5, jp6)
            "门" -> DisplayHiding(2, jp3, jp4, jp5, jp6)
            "窗" -> DisplayHiding(3, jp3, jp4, jp5, jp6)
            else -> DisplayHiding(0, jp3, jp4, jp5, jp6)
        }
        //}

        this.repaint()
    }

    init {

        this.layout = XYLayout()
        this.add(jp1, XYConstraints(0, 0, 200, 50))
        this.add(jp2, XYConstraints(0, 50, 200, 50))
        this.add(jp3, XYConstraints(0, 100, 200, 250))
        this.add(jp4, XYConstraints(0, 100, 200, 250))
        this.add(jp5, XYConstraints(0, 100, 200, 250))
        this.add(jp6, XYConstraints(0, 100, 200, 250))

        Task1_init()

        jp1.jrba.forEach { it.addActionListener(this) }
        jp2.jrba.forEach { it.addActionListener(this) }
        jp3.jbtn.addActionListener(ActionListener {
            this.repaint()
        })
        jp4.jbtn.addActionListener(ActionListener {
            this.repaint()
        })

        HouseData.model.clear()
        HouseData.modelWD.clear()
        houseData.allToModel()

        this.add(jp_info, XYConstraints(205, 0, 150, 350))
        this.add(jp_wd_info, XYConstraints(355, 0, 105, 350))
        this.add(jp_draw, XYConstraints(460, 0, draw_w, draw_h))

        //this.setBounds(100, 100, 1000, 400)
        this.isVisible = true
        Task1_init()

        jp_info.scrollPane.setBounds(5, 15, 140, 255)
        jp_wd_info.scrollPane.setBounds(5, 15, 95, 255)
        //jp_info.scrollPane.setBounds(5, 15, jp_info.size.width - 10, jp_info.size.height - 95)
        //jp_wd_info.scrollPane.setBounds(5, 15, jp_wd_info.size.width - 10, jp_wd_info.size.height - 95)
    }

    fun Task1_init() {
        jp2.isVisible = true
        jp3.isVisible = true
        jp4.isVisible = false
        jp5.isVisible = false
        jp6.isVisible = false

        jp_info.isVisible = true
        jp_wd_info.isVisible = true
        jp_draw.isVisible = true
    }
}