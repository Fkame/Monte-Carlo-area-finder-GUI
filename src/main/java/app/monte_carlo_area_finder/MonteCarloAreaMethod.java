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

    public int getAmountOfPointsInInnerArea(List<StatePoint2D> points) {
        return (int)points.stream().filter(point -> point.getState() == true).count();
    }

    /**
     * Метод генерирует целое число с равномерным распределением в заданном интервале.
     * @param startValue начало интервала для генерации числа.
     * @param endValue конец интервала для генерации числа.
     * @return сгенерированное целое число с равномерным распределением.
     * @throws IllegalArgumentException если {@code startValue} > {@code endValue} будет выброшено исключение.
     */
    public static int generateIntInInterval(int startValue, int endValue) throws IllegalArgumentException {
        if (startValue > endValue) throw new IllegalArgumentException("startValue > endValue in interval parameters");
        Random rand = new Random();
        //return rand.nextInt() * (endValue - startValue) + startValue;
        return rand.nextInt(endValue - startValue) + startValue;
    }

    /**
     * 
     * @param startValue
     * @param endValue
     * @return
     * @throws IllegalArgumentException
     */
    public static double generateDoubleInInterval(double startValue, double endValue) throws IllegalArgumentException {
        Random rand = new Random();
        BigDecimal maxValue = BigDecimal.valueOf(endValue);
        BigDecimal minValue = BigDecimal.valueOf(startValue);
        return BigDecimal.valueOf(rand.nextDouble())
            .multiply(maxValue.subtract(minValue))
            .add(minValue)
            .doubleValue();
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
