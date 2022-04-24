package app.monte_carlo_area_finder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import javafx.geometry.Point2D;

/**
 * Из-за попытки сделать класс {@link MonteCarloAreaMethod} гибче, его использование усложнилось. 
 * Данный класс предоставляет ряд методов для упрощения работы по созданию генераторов случайных чисел, реализующих интерфейс
 * {@link IPointsGenerator}.
 */
public class MonteCarloSupport {
    
    /**
     * Создаёт генератор точек на плоскости с равномерным распределением в указанных границах c вещественными координатами. 
     * <p>Если необходимо создать точку в одномерном пространстве, 
     * достаточно {@code start} и {@code end} параметры ненужного измерения указать 0.
     * @param startX левая граница генерации по оси ОХ
     * @param endX правая граница генерации по оси ОХ
     * @param startY левая граница генерации по оси ОУ
     * @param endY правая граница генерации по оси ОУ
     * @param scaleValue до скольки знаков после запятой округлять вещественные числа.
     * @return самая простая реализация функционального интерфейса {@link IPointsGenerator} по генерации точек с вещественными координатами
     * на плоскости.
     */
    public static IPointsGenerator createSimpleDoubleGenerator(double startX, double endX, double startY, double endY, int scaleValue) {
        return (() ->  {
            double xCoord = MonteCarloSupport.generateDoubleInInterval(startX, endX, scaleValue);
            double yCoord = MonteCarloSupport.generateDoubleInInterval(startY, endY, scaleValue);
            return new Point2D(xCoord, yCoord);
        });
    }

    /**
     * Создаёт генератор точек на плоскости с равномерным распределением в указанных границах c целыми координатами. 
     * <p>Если необходимо создать точку в одномерном пространстве, 
     * достаточно {@code start} и {@code end} параметры ненужного измерения указать 0.
     * @param startX левая граница генерации по оси ОХ
     * @param endX правая граница генерации по оси ОХ
     * @param startY левая граница генерации по оси ОУ
     * @param endY правая граница генерации по оси ОУ
     * @return самая простая реализация функционального интерфейса {@link IPointsGenerator} по генерации точек с целыми координатами
     * на плоскости.
     */
    public static IPointsGenerator createSimpleIntGenerator(int startX, int endX, int startY, int endY) {
        return (() -> {
            int xCoord = MonteCarloSupport.generateIntInInterval(startX, endX);
            int yCoord = MonteCarloSupport.generateIntInInterval(startY, endY);
            return new Point2D(xCoord, yCoord);
        });
    }

    /**
     * Метод генерирует вещественное число с равномерным распределением в заданном интервале.
     * @param startValue начало интервала для генерации числа.
     * @param endValue конец интервала для генерации числа.
     * @param scaleValue до скольки знаков после запятой округлять вещественный результат.
     * @return сгенерированное целое число с равномерным распределением.
     * @throws IllegalArgumentException если {@code startValue} > {@code endValue} будет выброшено исключение.
     */
    public static double generateDoubleInInterval(double startValue, double endValue, int scaleValue) throws IllegalArgumentException {
        if (startValue > endValue) throw new IllegalArgumentException("startValue > endValue in interval parameters");
        if (startValue == endValue) return startValue;
        Random rand = new Random();
        BigDecimal maxValue = BigDecimal.valueOf(endValue);
        BigDecimal minValue = BigDecimal.valueOf(startValue);
        return BigDecimal.valueOf(rand.nextDouble())
            .multiply(maxValue.subtract(minValue))
            .add(minValue)
            .setScale(scaleValue, RoundingMode.CEILING)
            .doubleValue();
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
        if (startValue == endValue) return startValue;
        Random rand = new Random();
        return rand.nextInt(endValue - startValue) + startValue;
    }
}
