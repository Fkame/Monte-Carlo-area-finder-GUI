package app.monte_carlo_method;

import javafx.geometry.Point2D;

@FunctionalInterface
public interface IFigureWithCalculatedArea {
    public boolean isPointInArea(Point2D point);
}