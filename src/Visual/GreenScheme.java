package Visual;

import Geometry.ACurve;
import Geometry.IPoint;

import java.awt.*;
import java.awt.geom.Point2D;

public class GreenScheme implements IScheme {
    VisualCurve curve;
    int level;
    Graphics g;
    public GreenScheme(Graphics g, VisualCurve curve, int level){
        this.curve = curve;
        this.level = level;
        this.g = g;
    }

    public GreenScheme(VisualCurve curve, int level){
        this.curve = curve;
        this.level = level;
    }

    @Override
    public void drawScheme() {
        g.setColor(Color.GREEN);
        drawGreenLine();
        IPoint point = curve.GetPoint(0);
        g.drawOval((int)point.getX() - 2, (int)point.getY() - 2, 4,4);

        point = curve.GetPoint(1 - (double) 1 / level);
        IPoint second = curve.GetPoint(1);
        double angel = Math.atan2(second.getY() - point.getY(), second.getX() - point.getX());

        int arrowLenght = 10;
        double arrowAngel = Math.PI / 8;
        Point2D.Double p1 = new Point2D.Double(
                second.getX() - arrowLenght * Math.cos(angel - arrowAngel),
                second.getY() - arrowLenght * Math.sin(angel - arrowAngel)
        );
        Point2D.Double p2 = new Point2D.Double(
                second.getX() - arrowLenght * Math.cos(angel + arrowAngel),
                second.getY() - arrowLenght * Math.sin(angel + arrowAngel)
        );
        g.drawLine((int)second.getX(), (int)second.getY(), (int)p1.x, (int)p1.y);
        g.drawLine((int)second.getX(), (int)second.getY(), (int)p2.x, (int)p2.y);
    }
    private void drawGreenLine (){
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < level; i++){
            double t1 = (double) i / level;
            double t2 = (double) (i + 1) / level;
            IPoint first = curve.GetPoint(t1);
            IPoint second = curve.GetPoint(t2);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine((int) first.getX(), (int) first.getY(), (int) second.getX(), (int) second.getY());
            //g.drawLine((int) first.getX(), (int) first.getY(), (int) second.getX(), (int) second.getY());
        }
    }
    @Override
    public String saveToSVG(){
        String result = "";
        for (int i = 0; i < level; i++) {
            double t1 = (double) i / level;
            double t2 = (double) (i + 1) / level;
            IPoint f = curve.GetPoint(t1);
            IPoint s = curve.GetPoint(t2);
            result += STR."<line x1=\"\{f.getX()}\" y1=\"\{f.getY()}\" x2=\"\{s.getX()}\" y2=\"\{s.getY()}\" style=\"stroke:green; stroke-width:2\"/>";
        }
        IPoint p = curve.GetPoint(0);
        result += STR."<circle cx=\"\{p.getX()}\" cy=\"\{p.getY()}\" r=\"4\" style=\"stroke:green; fill:green; stroke-width:3\"/>";

        IPoint p1 = curve.GetPoint(1 - (double) 1 / level);
        IPoint p2 = curve.GetPoint(1);

        double angel = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
        int arrowLenght = 10;
        double arrowAngel = Math.PI / 8;
        Point2D.Double pp1 = new Point2D.Double(
                p2.getX() - arrowLenght * Math.cos(angel - arrowAngel),
                p2.getY() - arrowLenght * Math.sin(angel - arrowAngel)
        );
        Point2D.Double pp2 = new Point2D.Double(
                p2.getX() - arrowLenght * Math.cos(angel + arrowAngel),
                p2.getY() - arrowLenght * Math.sin(angel + arrowAngel)
        );
        result += STR."<line x1=\"\{p2.getX()}\" y1=\"\{p2.getY()}\" x2=\"\{pp1.x}\" y2=\"\{pp1.y}\" style=\"stroke:green; stroke-width:2\"/>";
        result += STR."<line x1=\"\{p2.getX()}\" y1=\"\{p2.getY()}\" x2=\"\{pp2.x}\" y2=\"\{pp2.y}\" style=\"stroke:green; stroke-width:2\"/>";
        return result;
    }
}
