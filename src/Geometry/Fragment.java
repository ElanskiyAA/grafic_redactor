package Geometry;

import java.util.ArrayList;

public class Fragment extends ADecorator {
    ArrayList<IPoint> selectedPoints = new ArrayList<>();
    private IPoint tStart;
    private IPoint tFinish;

    public Fragment(ArrayList<IPoint> allPoints, IPoint tStart, IPoint tFinish) {
        this.tStart = tStart;
        this.tFinish = tFinish;

        for (int i = 0; i < allPoints.size(); i++) {
            if (allPoints.indexOf(tStart) <= i && i <= allPoints.indexOf(tFinish)){
                selectedPoints.add(allPoints.get(i));
            }
        }
    }

    @Override
    public IPoint GetPoint(double t) {
        IPoint point;
        int index = (int)Math.round(t * (selectedPoints.size() - 1));
        point = selectedPoints.get(index);
        return point;
    }

    @Override
    public ArrayList<IPoint> GetArrayPoints() {
        return selectedPoints;
    }

    public int GetSize() {
        return selectedPoints.size();
    }
}
