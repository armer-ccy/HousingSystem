package pers.ccy.hs.test;
import java.applet.Applet;
import java.awt.Graphics;

public class Main3 extends Applet
{
    public void paint(Graphics g)
    {
        int x1=50,y1=20;
        int width1=50,height1=50;
        int width2=20,height2=20;

        g.drawRect(x1, y1, width1, height1);
        g.drawRoundRect(x1+50, y1+50, width1, height1, width2, height2);
    }
}