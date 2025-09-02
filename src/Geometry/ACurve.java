package Geometry;

public abstract class ACurve implements ICurve {
    protected IPoint a;
    protected IPoint b;
    public ACurve(IPoint a, IPoint b) {
        this.a = a;
        this.b = b;
    }
    public abstract int GetCountPoint();
    @Override
    public abstract IPoint GetPoint(double t);
}
