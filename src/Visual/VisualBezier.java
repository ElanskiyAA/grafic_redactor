package Visual;

import Geometry.ACurve;
import Geometry.IPoint;

public class VisualBezier extends VisualCurve {
    private ACurve curve;
    private int Scheme;

    public VisualBezier(ACurve curve, int scheme) {
        this.curve = curve;
        this.Scheme = scheme;
    }

    @Override
    public int GetSize() {
        return curve.GetCountPoint();
    }

    @Override
    public int GetScheme(){
        return this.Scheme;
    }
    @Override
    public String saveToSVG(IScheme g) {
        return g.saveToSVG();
    }

    @Override
    public ACurve GetCurve() {
        return this.curve;
    }

    @Override
    public IPoint GetPoint(double t) {
        return curve.GetPoint(t);
    }
    @Override
    public void draw(IScheme g) {
        g.drawScheme();
    }
}
