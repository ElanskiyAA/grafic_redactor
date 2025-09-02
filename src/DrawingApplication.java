import Comand.ACommand;
import Comand.CM;
import Comand.ICommand;
import Geometry.*;
import Geometry.Point;
import Visual.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class DrawingApplication extends JFrame {
    private JComboBox<String> Selector, SchemeSelector;
    private int SelectorSwitch = 0, SelectScheme = 0, SelectSchemeNow, sizeCurveNoew;
    private JButton Back, Clear, Save;
    private DrawingPanel drawingPanel;
    private ArrayList<VisualCurve> curves = new ArrayList<>();
    private ICommand Command;
    private IScheme Scheme;
    private ArrayList<IPoint> allPoints = new ArrayList<>();
    private ArrayList<IPoint> selectedPoints = new ArrayList<>();
    private ACurve curveNow;
    private boolean isDragging = false;
    private IPoint selectPointStart, selectPointFinish;

    public DrawingApplication() {
        // Иницилизируем список комманд
        new Initialization(curves).Execute();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        Back = new JButton("Назад");
        topPanel.add(Back);
        String[] shapes = {"Линия", "Кривая Безье"};
        Selector = new JComboBox<>(shapes);
        topPanel.add(Selector);
        String[] scheme = {"Схема 1", "Схема 2"};
        SchemeSelector = new JComboBox<>(scheme);
        topPanel.add(SchemeSelector);
        Clear = new JButton("Очистить");
        topPanel.add(Clear);
        Save = new JButton("Сохранить");
        topPanel.add(Save);

        drawingPanel = new DrawingPanel();

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);

        Back.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                curves.clear();
                allPoints.clear();
                CM.GetInstance().Undo();
                repaint();
            }
        });

        Selector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectorSwitch = Selector.getSelectedIndex();
            }
        });

        SchemeSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectScheme = SchemeSelector.getSelectedIndex();
            }
        });

        Clear.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //curves.clear();
                Command = new Remove(curves);
                CM.GetInstance().Registry(Command);
                Command.Execute();
                repaint();
            }
        });

        Save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SaveToSVG(curves);
            }
        });

        setTitle("Drawing Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrawingApplication app = new DrawingApplication();
            app.setVisible(true);
        });
    }

    private class DrawingPanel extends JPanel {
        private IPoint firstPoint;
        private IPoint secondPoint;
        Random random = new Random();

        public DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (firstPoint == null) {
                        firstPoint = new Point(e.getX(), e.getY());
                    } else {
                        secondPoint = new Point(e.getX(), e.getY());
                    }
                    if (isDragging) {
                        if (!selectedPoints.isEmpty()) {
                            ADecorator fragment = new Fragment(allPoints, selectedPoints.getFirst(), selectedPoints.getLast());
                            if (fragment.GetSize() == 0) {
                                return;
                            }
                            ArrayList<IPoint> fragmentPoints = fragment.GetArrayPoints();
                            IPoint movePoint = new Point(e.getX(), e.getY());
                            Command = new MoveToCurve(curves, curveNow, fragmentPoints, movePoint, SelectScheme);
                            CM.GetInstance().Registry(Command);
                            Command.Execute();
                            selectedPoints.clear();
                        }
                        firstPoint = null;
                        secondPoint = null;
                        isDragging = false;
                    } else if (secondPoint != null && firstPoint != null) {
                        curveNow = null;
                        allPoints.clear();
                        switch (SelectorSwitch) {
                            case 0:
                                Line line = new Line(firstPoint, secondPoint);
                                Command = new Add(curves, line, SelectScheme);
                                curveNow = line;
                                SelectSchemeNow = SelectScheme;
                                sizeCurveNoew = 2;
                                break;
                            case 1:
                                IPoint c = new Point(firstPoint.getX() + 25, firstPoint.getY() + 25);
                                IPoint d = new Point(secondPoint.getY() - 25, secondPoint.getY() - 25);
                                Bezier bezier = new Bezier(firstPoint, c, d, secondPoint);
                                Command = new Add(curves, bezier, SelectScheme);
                                curveNow = bezier;
                                SelectSchemeNow = SelectScheme;
                                sizeCurveNoew = 2;
                                break;
                        }
                        CM.GetInstance().Registry(Command);
                        Command.Execute();
                        firstPoint = null;
                        secondPoint = null;
                    }
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    selectPointStart = new Point(e.getX(), e.getY());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    selectPointStart = null;
                    selectPointFinish = null;
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    selectPointFinish = new Point(e.getX(), e.getY());
                    selectedPoints(selectPointStart, selectPointFinish);
                    //repaint(); // Перерисовка панели
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (curves != null) {
                for (VisualCurve curve : curves) {
                    switch (curve.GetScheme()) {
                        case 0:
                            Scheme = new GreenScheme(g, curve, 100);
                            break;
                        case 1:
                            Scheme = new DottedScheme(g, curve, 100);
                            break;
                    }
                    curve.draw(Scheme);
                }
            }
        }
    }

    private void selectedPoints(IPoint start, IPoint finish) {
        Rectangle selectRect = new Rectangle((int) Math.min(start.getX(), finish.getX()),
                (int) Math.min(start.getY(), finish.getY()),
                (int) Math.abs(start.getX() - finish.getX()),
                (int) Math.abs(start.getY() - finish.getY()));
        for (IPoint point : allPoints) {
            if (selectRect.contains((int)point.getX(), (int)point.getY())
                    && (!selectedPoints.contains(point))) {
                selectedPoints.add(point);
                System.out.print(point.getX());
                System.out.print(" ");
                System.out.println(point.getY());
            }
        }
        isDragging = true;
    }

    public void SaveToSVG(ArrayList<VisualCurve> curves) {
        String stringSVG;
        IScheme sc = null;
        stringSVG = "<svg width=\"1000\" height=\"1000\" xmlns=\"http://www.w3.org/2000/svg\">";
        for (VisualCurve curve : curves) {
            sc = switch (curve.GetScheme()) {
                case 0 -> new GreenScheme(curve, 100);
                case 1 -> new DottedScheme(curve, 100);
                default -> sc;
            };
            stringSVG += curve.saveToSVG(sc);
        }
        stringSVG += "</svg>";

        try (FileWriter writer = new FileWriter("SVGFile.svg")) {
            writer.write(stringSVG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Initialization extends ACommand {
        ArrayList<VisualCurve> Curves;
        public Initialization(ArrayList<VisualCurve> curves) {
            this.Curves = curves;
        }
        @Override
        public void doExecute() {
            Curves.clear();
        }
    }

    public class Add extends ACommand {
        ArrayList<VisualCurve> Curves;
        ACurve aCurve;
        int Scheme;
        public Add(ArrayList<VisualCurve> curves, ACurve aCurve, int scheme) {
            this.Curves = curves;
            this.aCurve = aCurve;
            this.Scheme = scheme;
        }
        @Override
        public void doExecute() {
            switch (SelectSchemeNow) {
                case 0:
                    Curves.add(new VisualLine(this.aCurve, this.Scheme));
                    allPoints.add(aCurve.GetPoint(0));
                    allPoints.add(aCurve.GetPoint(1));
                    break;
                case 1:
                    Curves.add(new VisualBezier(this.aCurve, this.Scheme));
                    allPoints.add(aCurve.GetPoint(0));
                    allPoints.add(aCurve.GetPoint(1));
                    break;
            }
        }
    }

    public class Remove extends ACommand {
        ArrayList<VisualCurve> Curves;
        public Remove(ArrayList<VisualCurve> curves) {
            this.Curves = curves;
        }
        @Override
        public void doExecute() {
            Curves.clear();
            allPoints.clear();
        }
    }

    public class MoveToCurve extends ACommand {
        ArrayList<VisualCurve> curves;
        ACurve aCurve;
        ArrayList<IPoint> fragmentPoints;
        IPoint movePoint;
        int scheme;
        public MoveToCurve(ArrayList<VisualCurve> curves, ACurve aCurve, ArrayList<IPoint> fragmentPoints, IPoint movePoint, int scheme) {
            this.curves = curves;
            this.aCurve = aCurve;
            this.fragmentPoints = fragmentPoints;
            this.movePoint = movePoint;
            this.scheme = scheme;
        }
        @Override
        public void doExecute() {
            ArrayList<IPoint> pointToDelete = new ArrayList<>();
            for (int i = 0; i < curves.size(); i++) {
                if (curves.get(i).GetCurve() == aCurve){
                    curves.remove(i);
                    for (IPoint p : allPoints) {
                        if((p.getX() == aCurve.GetPoint(0).getX() && p.getY() == aCurve.GetPoint(0).getY())
                                || (p.getX() == aCurve.GetPoint(1).getX() && p.getY() == aCurve.GetPoint(1).getY())){
                            pointToDelete.add(p);
                        }
                    }
                    break;
                }
            }
            allPoints.removeAll(pointToDelete);

            ArrayList<IPoint> sortList = new ArrayList<>();
            ArrayList<IPoint> addPoint = new ArrayList<>();
            ArrayList<Integer> index = new ArrayList<Integer>();
            for (double i = 0; i <= 1; i += (double) 1 / (sizeCurveNoew - 1)){
                boolean chek = false;
                IPoint point = curveNow.GetPoint(i);
                for (IPoint p : fragmentPoints) {
                    if (point.getX() == p.getX() && point.getY() == p.getY()) {
                        chek = true;
                        break;
                    }
                }
                if (chek) {
                    sortList.add(point);
                } else {
                    addPoint.add(point);
                    index.add((int)Math.round(i * (sizeCurveNoew - 1)));
                }
            }
            ArrayList<IPoint> MoveToPoints = new ArrayList<>();
            ADecorator movePoints = new MoveTo(sortList, movePoint);
            for (double i = 0; i <= 1; i += (double) 1 / (movePoints.GetSize() - 1)) {
                MoveToPoints.add(movePoints.GetPoint(i));
            }

            if (!addPoint.isEmpty()){
                for (int i = 0; i < addPoint.size(); i++) {
                    MoveToPoints.add(index.get(i), addPoint.get(i));
                }
            }

            switch (SelectorSwitch) {
                case 0:
                    Line line = new Line(MoveToPoints.get(0), MoveToPoints.get(1));
                    curves.add(new VisualLine(line, this.scheme));
                    allPoints.add(line.GetPoint(0));
                    allPoints.add(line.GetPoint(1));
                    curveNow = line;
                    break;
                case 1:
                    IPoint c = new Point(MoveToPoints.get(0).getX() + 25, MoveToPoints.get(0).getY() + 25);
                    IPoint d = new Point(MoveToPoints.get(1).getY() - 25, MoveToPoints.get(1).getY() - 25);
                    Bezier bezier = new Bezier(MoveToPoints.get(0), c, d, MoveToPoints.get(1));
                    curves.add(new VisualBezier(bezier, this.scheme));
                    allPoints.add(bezier.GetPoint(0));
                    allPoints.add(bezier.GetPoint(1));
                    curveNow = bezier;
                    break;
            }
        }
    }
}