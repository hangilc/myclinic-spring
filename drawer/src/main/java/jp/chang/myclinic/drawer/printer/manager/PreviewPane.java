package jp.chang.myclinic.drawer.printer.manager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

class PreviewPane extends JPanel {

    private static double displayScale = 3.78;   // dot per mm

    private double imageWidth;
    private double imageHeight;
    private double scale;

    PreviewPane(double imageWidth, double imageHeight, double scale){
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.scale = scale;
        int paneWidth = (int)(imageWidth * scale * displayScale);
        int paneHeight = (int)(imageHeight * scale * displayScale);
        setPreferredSize(new Dimension(paneWidth, paneHeight));
    }

    @Override
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        Dimension pref = getPreferredSize();
        Point2D fromPoint = new Point2D.Double(0, 0);
        Point2D toPoint = new Point2D.Double(pref.width, pref.height);
        g.draw(new Line2D.Double(fromPoint, toPoint));
    }

}
