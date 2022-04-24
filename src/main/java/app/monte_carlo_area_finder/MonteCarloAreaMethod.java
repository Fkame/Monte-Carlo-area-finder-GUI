package app.monte_carlo_area_finder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Point2D;

/**
 * Класс предоставляет функционал для выполнения некоторых алгоритмов Монте-Карло:
 * <ol>
 * <li>Численное интегрирование. Метод Монте-Карло можно использовать для поиска площади фигуры
 * ({@link MonteCarloAreaMethod#findAreaValue(IFigureWithCalculatedArea, IPointsGenerator, int, double)}).</li>
 * <li>Нахождение вероятности наступления события R за N повторов (менее точный аналог биномиального распределения) 
 * ({@link MonteCarloAreaMethod#findProbabilityOfSuccess(IFigureWithCalculatedArea, IPointsGenerator, int, int)}).</li>
 * </ol>
 * <p>Чтобы восстановить промежуточные вычисления, каждый из 2х основных методов заполняет список маркированных точек, получить который 
 * после выполнения метода можно с помощью {@link MonteCarloAreaMethod#getLastRunGeneratedPoints()}
 * <p>Для упрощённого создания кастомного генератора ({@link IPointsGenerator}) предоставляется функционал класса {@link MonteCarloSupport}.
 */
public class MonteCarloAreaMethod {
    
    /**
     * Маркированные точки, которые генерирует метод нахождения площади и метод поиска вероятности.
     */
    private final ArrayList<StatePoint2D> lastRunGeneratedPoints;

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

    /**
     * Метод позволяет найти площадь фигуры с помощью Метода Монте-Карло, т.е. произвести численное интегрирование. 
     * <p>В ходе работы он также заполняет список сгенерированных точек, 
     * получить которые можно с помощью {@link MonteCarloAreaMethod#getLastRunGeneratedPoints()}.
     * @param innerFigure объект, с помощью которого метод может определить, попадает ли
     * сгенерированная им точка внутрь фигуры, площадь которой нужно найти.
     * Определение попадания определяется с помощью реализации {@link IFigureWithCalculatedArea#isPointInArea(Point2D)}.
     * @param generator генератор случайных точек на плоскости с кастомной генерацией. То, как генерировать числа, определяет пользователь, 
     * метод лишь использует реализацию {@link IPointsGenerator#generatePoint()} для получения очередной сгенерированной точки
     * на 2d-плоскости.
     * @param amountOfPointsToGenerate количество точек, которое необходимо сгенерировать для поиска площади.
     * @param bigAreaValue площадь прямоугольника, в который вписана фигура, площадь которой необходимо найти.
     * @return площадь фигуры вписанной в прямоугольник.
     */
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
     * Метод позволяет определить вероятность осуществления события за N попыток.
     * @param innerFigure объект, с помощью которого метод может определить, попадает ли
     * сгенерированная им точка внутрь фигуры, площадь которой нужно найти.
     * Определение попадания определяется с помощью реализации {@link IFigureWithCalculatedArea#isPointInArea(Point2D)}.
     * @param generator генератор случайных точек на плоскости с кастомной генерацией. То, как генерировать числа, определяет пользователь, 
     * метод лишь использует реализацию {@link IPointsGenerator#generatePoint()} для получения очередной сгенерированной точки
     * на 2d-плоскости.
     * @param amountOfPointsToGenerate количество точек, которое необходимо сгенерировать для поиска площади.
     * @param amountOfExperiments колличнство сеансов моделирования. Их количество влияет на точность вычисления. Чем их больше - тем дольше
     * вычисления, но тем правдоподобнее результат.
     * @return вероятность осуществления события за N попыток
     */
    public BigDecimal findProbabilityOfSuccess(IFigureWithCalculatedArea innerFigure, IPointsGenerator generator, 
                                                int amountOfPointsToGenerate, int amountOfExperiments) 
    {
        lastRunGeneratedPoints.clear();
        int amountOfSuccess = 0;
        int experiment = amountOfExperiments;
        while (experiment > 0) {
            boolean atLeastOneSuccess = false;
            for (int i = 0; i < amountOfPointsToGenerate; i++) {
                Point2D generatedPoint = generator.generatePoint();
                boolean isPointInInnerArea = innerFigure.isPointInArea(generatedPoint);
                StatePoint2D stateRandPoint = new StatePoint2D(generatedPoint, isPointInInnerArea);
                lastRunGeneratedPoints.add(stateRandPoint);
                atLeastOneSuccess = atLeastOneSuccess | isPointInInnerArea;
            }
            if (atLeastOneSuccess) amountOfSuccess += 1;
            experiment -= 1;
        }

        BigDecimal success = BigDecimal.valueOf(amountOfSuccess);
        BigDecimal all = BigDecimal.valueOf(amountOfExperiments);
        return success.divide(all, this.scaleValue, RoundingMode.CEILING);  
    }

    /**
     * Метод считает, сколько точек попадают внутрь фигуры, то есть флаг {@link StatePoint2D#getState()} == true.
     * @param points список маркированных точек.
     * @return количество точек из общего числа, которые попадают внутрь фигуры, площадь которой ищется.
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

    /**
     * Получить список маркированных точек, генерируемых методами поиска площади и вычисления вероятности. 
     * <p>При запуске каждого из функциональных методов список очищается.
     * @return неизменяемый список маркированных точек.
     */
    public List<StatePoint2D> getLastRunGeneratedPoints() {
        return Collections.unmodifiableList(lastRunGeneratedPoints);
    }
}
