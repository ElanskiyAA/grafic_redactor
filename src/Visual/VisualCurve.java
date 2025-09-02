package Visual;

import Geometry.ACurve;
import Geometry.ICurve;
import Geometry.IPoint;

import java.awt.*;

public abstract class VisualCurve implements ICurve, IDrawable {
    public abstract int GetSize();
    public abstract int GetScheme();
    public abstract String saveToSVG(IScheme g);
    public abstract ACurve GetCurve();
    @Override
    public abstract IPoint GetPoint(double t);
    @Override
    public abstract void draw(IScheme g);
}
