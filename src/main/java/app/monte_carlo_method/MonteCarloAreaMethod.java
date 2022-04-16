package app.monte_carlo_method;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Line;

public class MonteCarloAreaMethod {
    
    private Point2D leftBottomPoint;
    private Point2D leftUpperPoint;
    private Point2D rightUpperPoint;
    private Point2D rightBottomPoint;
    private Random generator;

    private ArrayList<ArrayList<StatePoint2D>> allGeneratedPoints;
    private ArrayList<StatePoint2D> lastRunGeneratedPoints;

    /**
     * Конструктор с параметрами области генерации точек.
     * @param x1BigArea левая граница прямоугольника генерации точек по оси X;
     * @param x2BigArea правая граница прямоугольника генерации точек по оси X;
     * @param y1BigArea нижняя граница прямоугольника генерации точек по оси Y;
     * @param y2BigArea верхняя граница прямоугольника генерации точек по оси Y;
     * @param generator генератор случайных чисел с равномерным распределением.
     * @exception IllegalArgumentException если x1 > x2 или y1 > y2.
     */
    public MonteCarloAreaMethod(double x1BigArea, double x2BigArea, double y1BigArea, double y2BigArea, Random generator) {
        if (x1BigArea > x2BigArea | y1BigArea > y2BigArea) throw new IllegalArgumentException("x1 must be < x2, y1 must be < y2!");
        if (generator == null) throw new IllegalArgumentException("Generator must not be null!");
        this.leftBottomPoint =  new Point2D(x1BigArea, y1BigArea);
        this.leftUpperPoint =   new Point2D(x1BigArea, y2BigArea);
        this.rightUpperPoint =  new Point2D(x2BigArea, y2BigArea);
        this.rightBottomPoint = new Point2D(x2BigArea, y1BigArea);
        this.generator = generator;

        allGeneratedPoints = new ArrayList<ArrayList<StatePoint2D>>();
        lastRunGeneratedPoints = new ArrayList<StatePoint2D>();
    }

    public MonteCarloAreaMethod(Line area, Random generator) {
        if (area == null) throw new IllegalArgumentException("input area must not be null!"); 
        if (generator == null) throw new IllegalArgumentException("Generator must not be null!");
        this.leftBottomPoint = new Point2D(area.getStartX(), area.getStartY());
        this.leftUpperPoint = this.leftBottomPoint;
        this.rightUpperPoint = new Point2D(area.getEndX(), area.getEndY());
        this.rightBottomPoint = rightUpperPoint;
        this.generator = generator;

        allGeneratedPoints = new ArrayList<ArrayList<StatePoint2D>>();
        lastRunGeneratedPoints = new ArrayList<StatePoint2D>();
    }

    public MonteCarloAreaMethod(Rectangle2D rectangeArea, Random generator) {
        if (rectangeArea == null) throw new IllegalArgumentException("input area must not be null!");
        if (generator == null) throw new IllegalArgumentException("Generator must not be null!");
        this.leftBottomPoint =  new Point2D(rectangeArea.getMinX(), rectangeArea.getMinY());
        this.leftUpperPoint =   new Point2D(rectangeArea.getMinX(), rectangeArea.getMaxY());
        this.rightUpperPoint =  new Point2D(rectangeArea.getMaxX(), rectangeArea.getMaxY());
        this.rightBottomPoint = new Point2D(rectangeArea.getMaxX(), rectangeArea.getMinY());
        this.generator = generator;

        allGeneratedPoints = new ArrayList<ArrayList<StatePoint2D>>();
        lastRunGeneratedPoints = new ArrayList<StatePoint2D>();
    }

    public BigDecimal findAreaValue(IFigureWithCalculatedArea innerFigure, int amountOfPointsToGenerate) {
        lastRunGeneratedPoints.clear();
        for (int i = 0; i < amountOfPointsToGenerate; i++) {
            Point2D randPoint = generateRandomPointInBigArea();

            //StatePoint2D stateRandPoint = new StatePoint2D(randPoint, isPointInSearchArea(randPoint));
            boolean isPointInInnerArea = innerFigure.isPointInArea(randPoint);
            StatePoint2D stateRandPoint = new StatePoint2D(randPoint, isPointInInnerArea);

            lastRunGeneratedPoints.add(stateRandPoint);
        }
        allGeneratedPoints.add(lastRunGeneratedPoints);
        int pointsInInnerArea = getAmountOfPointsInInnerArea(this.lastRunGeneratedPoints);
        BigDecimal areaValue = BigDecimal.valueOf(pointsInInnerArea)
                            .multiply(this.getBigAreaValue())
                            .divide(BigDecimal.valueOf(amountOfPointsToGenerate), 3, RoundingMode.CEILING);
        return areaValue;

    }

    public int getAmountOfPointsInInnerArea(List<StatePoint2D> points) {
        return (int)points.stream().filter(point -> point.getState() == true).count();
    }

    public BigDecimal getBigAreaValue() {
        // Поправка для работоспособности метода в 1д пространствах.
        if (this.leftBottomPoint.distance(leftUpperPoint) == 0)
            return BigDecimal.valueOf(leftBottomPoint.distance(rightBottomPoint));
            
        return BigDecimal.valueOf(leftBottomPoint.distance(rightBottomPoint))
                .multiply(BigDecimal.valueOf(leftBottomPoint.distance(leftUpperPoint)));
    }

    public Point2D generateRandomPointInBigArea() {
        BigDecimal minXValue = BigDecimal.valueOf(this.leftBottomPoint.getX());
        BigDecimal maxXValue = BigDecimal.valueOf(this.rightBottomPoint.getX());
        BigDecimal minYValue = BigDecimal.valueOf(this.leftBottomPoint.getY());
        BigDecimal maxYValue = BigDecimal.valueOf(this.rightUpperPoint.getY());
        double randX = BigDecimal.valueOf(generator.nextDouble())
                    .multiply(maxXValue.subtract(minXValue))
                    .add(minXValue)
                    .doubleValue();
        double randY = BigDecimal.valueOf(generator.nextDouble())
                    .multiply(maxYValue.subtract(minYValue))
                    .add(minYValue)
                    .doubleValue();
        Point2D randomPoint = new Point2D(randX, randY);
        return randomPoint;
    }

    /**
     * TODO: Удалить метод.
     * @param point
     * @return
     */
    public boolean isPointInSearchArea(Point2D point) {
        double upperY = Math.log(point.getX());
        double bottomY = BigDecimal.valueOf(point.getX())
                        .subtract(BigDecimal.valueOf(3.0))
                        .doubleValue();
        if (point.getY() < upperY & point.getY() > bottomY) return true;
        return false;
    }

    public List<List<StatePoint2D>> getAllGeneratedPoints() {
        return Collections.unmodifiableList(allGeneratedPoints);
    }

    public List<StatePoint2D> getLastRunGeneratedPoints() {
        return Collections.unmodifiableList(lastRunGeneratedPoints);
    }

    public void cleanAllSavedData() {
        this.lastRunGeneratedPoints.clear();
        for (int i = 0; i < this.allGeneratedPoints.size(); i++) {
            this.allGeneratedPoints.get(i).clear();
        }
        this.allGeneratedPoints.clear();
    }
}
