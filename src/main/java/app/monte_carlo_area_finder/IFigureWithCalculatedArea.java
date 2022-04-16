package app.monte_carlo_area_finder;

import javafx.geometry.Point2D;

/**
 * Реализация интерфейса позволяет задать пользовательские фигуры, для которых методом Монте-Карло ищется
 * площадь.
 */
@FunctionalInterface
public interface IFigureWithCalculatedArea {

    /**
     * Метод проверяет, попадает ли точка внутрь фигуры, для которой ищется площадь.
     * @param point сгенерированная случайная точка.
     * @return попадает ли точка в пределы фигуры, для которой ищется площадь.
     */
    public boolean isPointInArea(Point2D point);
}