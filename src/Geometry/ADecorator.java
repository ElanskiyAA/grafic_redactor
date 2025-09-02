package Geometry;

import java.util.ArrayList;

public abstract class ADecorator implements ICurve {
    @Override
    public abstract IPoint GetPoint(double t);
    public abstract ArrayList<IPoint> GetArrayPoints();

    public abstract int GetSize();
}
