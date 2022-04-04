package app;

import javafx.geometry.Point2D;

/**
 * Обёртка для точки с добавлением информации о том, попадает ли она во внутренню фигуру (которая внутри большого прямоугольника), 
 * площадь которой нужно найти.
 */
public class StatePoint2D {
    private Point2D point;
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
