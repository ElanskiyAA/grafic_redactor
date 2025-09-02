package Visual;

import Geometry.IPoint;

import java.awt.*;
import java.awt.geom.Point2D;

public class DottedScheme implements IScheme {
    VisualCurve curve;
    int level;
    int size = 6;
    Graphics g;
    public DottedScheme(Graphics g, VisualCurve curve, int level) {
        this.curve = curve;
        this.level = level;
        this.g = g;
    }
    public DottedScheme(VisualCurve curve, int level) {
        this.curve = curve;
        this.level = level;
    }
    @Override
    public void drawScheme() {
        g.setColor(Color.BLACK);
        drawDotIt();
        IPoint first = curve.GetPoint(0);
        IPoint second = curve.GetPoint((double) 1 / level);
        double angel = Math.atan2(second.getY() - first.getY(), second.getX() - first.getX());
        drawSquare(first, angel, g);

        first = curve.GetPoint(1 - (double) 1 / level);
        second = curve.GetPoint(1);
        angel = Math.atan2(second.getY() - first.getY(), second.getX() - first.getX());
        drawSquare(second, angel, g);
    }
    private void drawDotIt(){
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < level; i += 2) {
            double t1 = (double) i / level;
            double t2 = (double) (i + 1) / level;
            IPoint first = curve.GetPoint(t1);
            IPoint second = curve.GetPoint(t2);
            //g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine((int) first.getX(), (int) first.getY(), (int) second.getX(), (int) second.getY());
            //g.drawLine((int) first.getX(), (int) first.getY(), (int) second.getX(), (int) second.getY());
        }
    }
    public void drawSquare(IPoint centr, double angel, Graphics g){
        int halfSize = size / 2;
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        for (int i = 0; i < 4; i++) {
            double x = (i % 2 == 0) ? centr.getX() - halfSize : centr.getX() + halfSize;
            double y = (i < 2) ? centr.getY() - halfSize : centr.getY() + halfSize;
            xPoints[i] = (int) (Math.cos(angel) * (x - centr.getX()) - Math.sin(angel) * (y - centr.getY()) + centr.getX());
            yPoints[i] = (int) (Math.sin(angel) * (x - centr.getX()) + Math.cos(angel) * (y - centr.getY()) + centr.getY());
        }
        int swap = xPoints[0];
        xPoints[0] = xPoints[1];
        xPoints[1] = swap;

        swap = yPoints[0];
        yPoints[0] = yPoints[1];
        yPoints[1] = swap;

        g.fillPolygon(xPoints, yPoints, 4);
    }
    @Override
    public String saveToSVG() {
        int halfSize = size / 2;
        String result = "";
        for (int i = 0; i < level; i+=2) {
            double t1 = (double) i / level;
            double t2 = (double) (i + 1) / level;
            IPoint f = curve.GetPoint(t1);
            IPoint s = curve.GetPoint(t2);
            result += STR."<line x1=\"\{f.getX()}\" y1=\"\{f.getY()}\" x2=\"\{s.getX()}\" y2=\"\{s.getY()}\" style=\"stroke:black; stroke-width:2\"/>";
        }

        IPoint first = curve.GetPoint(0);
        IPoint second = curve.GetPoint((double) 1 / level);
        double angel = Math.atan2(second.getY() - first.getY(), second.getX() - first.getX());
        result += drawSquareSVG(result, first, angel);

        first = curve.GetPoint(1 - (double) 1 / level);
        second = curve.GetPoint(1);
        angel = Math.atan2(second.getY() - first.getY(), second.getX() - first.getX());
        result += drawSquareSVG(result, second, angel);
        return result;
    }

    public String drawSquareSVG(String result, IPoint centr, double angel){
        int halfSize = size / 2;
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        for (int i = 0; i < 4; i++) {
            double x = (i % 2 == 0) ? centr.getX() - halfSize : centr.getX() + halfSize;
            double y = (i < 2) ? centr.getY() - halfSize : centr.getY() + halfSize;
            xPoints[i] = (int) (Math.cos(angel) * (x - centr.getX()) - Math.sin(angel) * (y - centr.getY()) + centr.getX());
            yPoints[i] = (int) (Math.sin(angel) * (x - centr.getX()) + Math.cos(angel) * (y - centr.getY()) + centr.getY());
        }
        int swap = xPoints[0];
        xPoints[0] = xPoints[1];
        xPoints[1] = swap;

        swap = yPoints[0];
        yPoints[0] = yPoints[1];
        yPoints[1] = swap;

        return result + STR."<polygon points=\"\{xPoints[0]},\{yPoints[0]} \{xPoints[1]},\{yPoints[1]} \{xPoints[2]},\{yPoints[2]} \{xPoints[3]},\{yPoints[3]}\" fill=\"black\"/>";
    }
}
