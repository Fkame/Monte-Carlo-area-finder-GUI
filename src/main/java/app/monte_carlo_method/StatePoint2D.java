package app.monte_carlo_method;

import javafx.geometry.Point2D;

/**
 * Обёртка для точки с добавлением информации о том, попадает ли она во внутренню фигуру (которая внутри большого прямоугольника), 
 * площадь которой нужно найти.
 */
public class StatePoint2D {

    /**
     * Точка на плоскости
     */
    private Point2D point;

    /**
     * Попадает ли она в пределы фигуры, площадь которой нужно найти.
     */
    private boolean isInInnerArea;

    public StatePoint2D(Point2D point, boolean isInInnerArea) {
        this.point = point;
        this.isInInnerArea = isInInnerArea;
    }

    public Point2D getPoint() {
        return point;
    }

    public boolean getState() {
        return isInInnerArea;
    }
    
}
