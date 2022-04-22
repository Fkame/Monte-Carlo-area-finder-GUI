package app.monte_carlo_area_finder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;

public class MonteCarloAreaMethod {
    
    private ArrayList<StatePoint2D> lastRunGeneratedPoints;

    /**
     * Значение используется для округления вещественной части при подсчёте площади внутренней фигуры.
     */
    private int scaleValue = 3;

    public MonteCarloAreaMethod() {
        lastRunGeneratedPoints = new ArrayList<StatePoint2D>();
    }

    public MonteCarloAreaMethod(int scaleValue) {
        this.scaleValue = scaleValue;
        lastRunGeneratedPoints = new ArrayList<StatePoint2D>();
    }

    public BigDecimal findAreaValue(IFigureWithCalculatedArea innerFigure, IPointsGenerator generator, 
                                    int amountOfPointsToGenerate, double bigAreaValue)
    {       
        lastRunGeneratedPoints.clear();

        for (int i = 0; i < amountOfPointsToGenerate; i++) {
            Point2D generatedPoint = generator.generatePoint();
            boolean isPointInInnerArea = innerFigure.isPointInArea(generatedPoint);
            StatePoint2D stateRandPoint = new StatePoint2D(generatedPoint, isPointInInnerArea);

            lastRunGeneratedPoints.add(stateRandPoint);
        }
        int pointsInInnerArea = this.getAmountOfPointsInInnerArea(this.lastRunGeneratedPoints);
        BigDecimal areaValue = BigDecimal.valueOf(pointsInInnerArea)
                            .multiply(BigDecimal.valueOf(bigAreaValue))
                            .divide(BigDecimal.valueOf(amountOfPointsToGenerate), this.scaleValue, RoundingMode.CEILING);
        return areaValue;
    }

    /**
     * 
     * @param amountOfPointsToGenerate
     * @return
     */
    /*
    public BigDecimal findProbabilityOfSuccess(int amountOfPointsToGenerate, IPointsGenerator generator, ) {

    }
    */

    public int getAmountOfPointsInInnerArea(List<StatePoint2D> points) {
        return (int)points.stream().filter(point -> point.getState() == true).count();
    }

    /**
     * Метод устанавливает значение округления при вычислении площади.
     * Данное число используется методом {@link BigDecimal#divide(BigDecimal, int, RoundingMode)}
     * @param scaleValue до скольки знаков после запятой необходимо округлить значение площади.
     * @throws IllegalArgumentException если входное значение <= 0 будет выброшено исключение.
     */
    public void setScaleValue(int scaleValue) throws IllegalArgumentException {
        if (scaleValue <= 0) throw new IllegalArgumentException("Scale value cannot be <= 0");
        this.scaleValue = scaleValue;
    }

    public List<StatePoint2D> getLastRunGeneratedPoints() {
        return Collections.unmodifiableList(lastRunGeneratedPoints);
    }
}
