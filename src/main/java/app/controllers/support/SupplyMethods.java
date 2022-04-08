package app.controllers.support;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SupplyMethods {
    
    public static XYChart.Series<Number, Number> copySeriesData(XYChart.Series<Number, Number> seriesToCopy) {
        XYChart.Series<Number, Number> copy = new XYChart.Series<>();
        for (XYChart.Data<Number, Number> dataToCopy : seriesToCopy.getData()) {
            copy.getData().add(new XYChart.Data<Number, Number>(dataToCopy.getXValue(), dataToCopy.getYValue()));
        }
        return copy;
    }

    public static Alert getErrorAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, text);
        alert.setHeaderText(null);
        return alert;
    }

    public static double calcAvgValueInList(int limitNumber, List<BigDecimal> values, int accuracy) throws IllegalArgumentException {
        BigDecimal numLimit = BigDecimal.valueOf(limitNumber);
        if (limitNumber <= 0) throw new IllegalArgumentException("limitNumber <= 0!!!");
        if (limitNumber > values.size()) throw new IllegalArgumentException("limit number > size of values list!!!");
        BigDecimal sumValue = BigDecimal.ZERO;
        for (int i = 0; i < numLimit.intValue(); i++) {
            sumValue = sumValue.add(values.get(i));
        }
        double currAvgAreaValue = sumValue.divide(numLimit, accuracy, RoundingMode.CEILING).doubleValue(); 
        return currAvgAreaValue;  
    }
}
