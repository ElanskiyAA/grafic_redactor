package Geometry;

public class Line extends ACurve {
    public Line(IPoint a, IPoint b) {
        super(a, b);
    }

    @Override
    public int GetCountPoint() {
        return 2;
    }

    @Override
    public IPoint GetPoint(double t) {
        double x = (1 - t) * a.getX() + t * b.getX();
        double y = (1 - t) * a.getY() + t * b.getY();
        return new Point(x, y);
    }
}
