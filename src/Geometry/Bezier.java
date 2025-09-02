package Geometry;

public class Bezier extends ACurve {
    private IPoint c;
    private IPoint d;

    public Bezier(IPoint a, IPoint c, IPoint d, IPoint b) {
        super(a, b);
        this.c = c;
        this.d = d;
    }

    @Override
    public int GetCountPoint() {
        return 4;
    }

    @Override
    public IPoint GetPoint(double t) {
        double x = Math.pow(1 - t, 3) * a.getX() + 3 * t * Math.pow(1 - t, 2) * c.getX() +
                3 * Math.pow(t, 2) * (1 - t) * d.getX() + Math.pow(t, 3) * b.getX();
        double y = Math.pow(1 - t, 3) * a.getY() + 3 * t * Math.pow(1 - t, 2) * c.getY() +
                3 * Math.pow(t, 2) * (1 - t) * d.getY() + Math.pow(t, 3) * b.getY();
        return new Point(x, y);
    }
}
