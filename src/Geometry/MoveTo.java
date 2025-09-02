package Geometry;

import java.util.ArrayList;

public class MoveTo extends ADecorator {
    private ArrayList<IPoint> pointsToMove;
    private IPoint p;

    public MoveTo(ArrayList<IPoint> pointsToMove, IPoint p) {
        this.pointsToMove = pointsToMove;
        this.p = p;
    }

    @Override
    public IPoint GetPoint(double t) {
        IPoint point;
        ArrayList<IPoint> movePoints = new ArrayList<>();
        double deltaX = p.getX() - pointsToMove.getFirst().getX();
        double deltaY = p.getY() - pointsToMove.getFirst().getY();
        for (IPoint p : pointsToMove) {
            movePoints.add(new Point(p.getX() + deltaX, p.getY() + deltaY));
        }
        int index = (int)Math.round(t * (movePoints.size() - 1));
        point = movePoints.get(index);
        return point;
    }

    @Override
    public ArrayList<IPoint> GetArrayPoints() {
        return pointsToMove;
    }

    public int GetSize() {
        return pointsToMove.size();
    }
}
