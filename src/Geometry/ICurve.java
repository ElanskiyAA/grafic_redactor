package Geometry;

public interface ICurve {
    IPoint GetPoint(double t);
}

class A implements ICurve {

    @Override
    public IPoint GetPoint(double t) {
        return null;
    }
}
