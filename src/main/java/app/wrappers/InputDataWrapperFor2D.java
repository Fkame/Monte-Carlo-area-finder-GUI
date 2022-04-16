package app.wrappers;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;

/**
 * Обёртка для вводимых пользователем данных. Чтобы было удобно их вместе передавать между методами
 */
public class InputDataWrapperFor2D {
    /**
     * Количество точек, которые нужно сгенерировать для поиска площади внутренней фигуры.
     */
    private int amountOfPointsToGenerate;

    /**
     * Количество циклов (экспериментов), которые нужно провести. Это просто количество повторов генераций точек.
     */
    private int amountOfExperiments;

    /**
     * Область внешнего прямоугольника, внутри которого проводится генерация точек, и внутри которого располагается фигура, площадь
     * которой нужно найти.
     */
    private Rectangle2D bigArea;

    /**
     * Сообщения об ошибках. Если какие-то данные не удалось достать из полей, это поле не будет пустым, в противном случае
     * оно останется пустое.
     */
    private ArrayList<String> errorMessages;

    public InputDataWrapperFor2D() { 
        errorMessages = new ArrayList<String>();
    }

    public InputDataWrapperFor2D(int amountOfPointsToGenerate, int amountOfExperiments, Rectangle2D bigArea) {
        this.amountOfExperiments = amountOfExperiments;
        this.amountOfPointsToGenerate = amountOfPointsToGenerate;
        this.bigArea = bigArea;
    }

    public InputDataWrapperFor2D(String errorMessage) {
        this.errorMessages = new ArrayList<String>();
        this.errorMessages.add(errorMessage);
    }

    public InputDataWrapperFor2D(ArrayList<String> errorMessages) {
        this.errorMessages = new ArrayList<String>(errorMessages);
    }

    public void setAmountOfPoints(int amountOfPointsToGenerate) {
        this.amountOfPointsToGenerate = amountOfPointsToGenerate;
    }

    public void setAmountOfExperiments(int amountOfExperiments) {
        this.amountOfExperiments = amountOfExperiments;
    }

    public void setBigArea(Rectangle2D bigArea) {
        this.bigArea = bigArea;
    } 

    public void addErrorMessage(String errorMessage) {
        this.errorMessages.add(errorMessage);
    }

    public int getAmountOfPoints() {
        return this.amountOfPointsToGenerate;
    }

    public int getAmountOfExperiments() {
        return this.amountOfExperiments;
    }

    public Rectangle2D getBigArea() {
        return this.bigArea;
    }

    public ArrayList<String> getErrorMessages() {
        return this.errorMessages;
    }

    public String toString() {
        return new StringBuilder().append("Points=[").append(this.amountOfPointsToGenerate).append("]\n")
                    .append("Experiments=[").append(this.amountOfExperiments).append("]\n")
                    .append("OuterRectange:{")
                        .append("\n    minX=[").append(this.bigArea.getMinX())
                        .append("]\n    minY=[").append(this.bigArea.getMinY())
                        .append("]\n    maxX=[").append(this.bigArea.getMaxX())
                        .append("]\n    maxY=[").append(this.bigArea.getMaxY())
                    .append("]}")
                    .toString();
    }
}
