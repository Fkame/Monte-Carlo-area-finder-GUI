package app.wrappers;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Line;

/**
 * Обёртка для вводимых пользователем данных. Чтобы было удобно их вместе передавать между методами
 */
public class InputDataWrapperFor1D {
    /**
     * Количество точек, которые нужно сгенерировать для поиска площади внутренней фигуры.
     */
    private int amountOfPointsToGenerate;

    /**
     * Количество циклов (экспериментов), которые нужно провести. Это просто количество повторов генераций точек.
     */
    private int amountOfExperiments;

   
    private Line bigInterval;

    private Line innerInterval;

    /**
     * Сообщения об ошибках. Если какие-то данные не удалось достать из полей, это поле не будет пустым, в противном случае
     * оно останется пустое.
     */
    private ArrayList<String> errorMessages;

    public InputDataWrapperFor1D() { 
        errorMessages = new ArrayList<String>();
    }

    public InputDataWrapperFor1D(int amountOfPointsToGenerate, int amountOfExperiments, Line innerInterval, Line bigInterval) {
        this.amountOfExperiments = amountOfExperiments;
        this.amountOfPointsToGenerate = amountOfPointsToGenerate;
        this.innerInterval = innerInterval;
        this.bigInterval = bigInterval;
    }

    public InputDataWrapperFor1D(String errorMessage) {
        this.errorMessages = new ArrayList<String>();
        this.errorMessages.add(errorMessage);
    }

    public InputDataWrapperFor1D(ArrayList<String> errorMessages) {
        this.errorMessages = new ArrayList<String>(errorMessages);
    }

    public void setAmountOfPoints(int amountOfPointsToGenerate) {
        this.amountOfPointsToGenerate = amountOfPointsToGenerate;
    }

    public void setAmountOfExperiments(int amountOfExperiments) {
        this.amountOfExperiments = amountOfExperiments;
    }

    public void setBigInterval(Line bigInterval) {
        this.bigInterval = bigInterval;
    } 

    public void setInnerInterval(Line innerInterval) {
        this.innerInterval = innerInterval;
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

    public Line getBigInterval() {
        return this.bigInterval;
    }

    public ArrayList<String> getErrorMessages() {
        return this.errorMessages;
    }

    public Line getInnerInterval() {
        return this.innerInterval;
    }
}
