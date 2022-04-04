package app;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {

    private boolean isFirstLaunch = true;

    @FXML
    private TextField enter_num_points;

    @FXML
    private TextField enter_num_experiments;

    @FXML 
    private TextField enter_outershape;

    @FXML 
    private Button start_btn;
    
    @FXML 
    private ScatterChart<Number, Number> points_chart;
    private XYChart.Series<Number, Number> scatterSeries1;
    private XYChart.Series<Number, Number> scatterSeries2;

    @FXML
    private LineChart<Number, Number> accuracy_chart;
    private  XYChart.Series<Number, Number> lineSeries;

    @FXML
    private TableView<TableColumn<String, String>> data_table;

    // JavaFX он нужен пустой
    public MainController() {}

    @FXML
    private void startMethod(ActionEvent event) {
        if (isFirstLaunch) {
            isFirstLaunch = false;
            bindSeriesWithCharts();
        }
        else {
            scatterSeries1.getData().clear();
            scatterSeries2.getData().clear();
            lineSeries.getData().clear();
        }

        int amountOfPointsToGenerate = 0;
        try {
            amountOfPointsToGenerate = Integer.parseInt(this.enter_num_points.getText());
            if (amountOfPointsToGenerate <= 0) throw new Exception();
        } catch (Exception e) {
            this.showErrorAlert("Ошибка в поле ввода количества точек для генерации!");
            return;
        }

        int amountOfExperiments = 0;
        try {
            amountOfExperiments = Integer.parseInt(this.enter_num_experiments.getText());
            if (amountOfExperiments <= 0) throw new Exception();
        } catch (Exception e) {
            this.showErrorAlert("Ошибка в поле ввода количества экспериментов!");
            return;
        }

        Rectangle2D rect = this.parseRectBy2Points(this.enter_outershape.getText());
        if (rect == null) {
            this.showErrorAlert("Ошибка в поле ввода диагональных точек внешнего прямоугольника!");
            return;
        }    

        MonteCarloAreaMethod areaFinder = 
                    new MonteCarloAreaMethod(rect.getMinX(), rect.getMaxX(), rect.getMinY(), rect.getMaxY(), new Random());

        ArrayList<BigDecimal> areaValues = new ArrayList<>();
        for (int i = 0; i < amountOfExperiments; i++) {
            BigDecimal areaValue = areaFinder.findAreaValue(amountOfPointsToGenerate);
            areaValues.add(areaValue);

            // Вывод сгенерированных точек
            List<StatePoint2D> generatedData = areaFinder.getLastRunGeneratedPoints();
            for (StatePoint2D point : generatedData) {
                XYChart.Data<Number, Number> pointToDraw = 
                        new XYChart.Data<Number, Number>(point.getPoint().getX(), point.getPoint().getY());
                if (point.getState() != true) {
                    scatterSeries2.getData().add(pointToDraw);
                }
                else {
                    scatterSeries1.getData().add(pointToDraw);
                }
            }

            // Отображение повышения точности в виде среднего значения результатов экспериментов
            BigDecimal numOfExperiment = BigDecimal.valueOf(i + 1);
            double currAvgAreaValue = areaValues.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                                        .divide(numOfExperiment, 3, RoundingMode.CEILING)
                                        .doubleValue();            
            XYChart.Data<Number, Number> currAvgValuePoint = 
                    new XYChart.Data<Number, Number>(numOfExperiment.intValue(), currAvgAreaValue);
            lineSeries.getData().add(currAvgValuePoint);
        }
    }

    private void showErrorAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, text);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void showHelp(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION, "Button is in development proccess");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showDataOnPointsChart() {

    }

    private void showDataOnLinesChart() {

    }

    private void showDataOnDataTable() {

    }

    public void bindSeriesWithCharts() {
        scatterSeries1 = new XYChart.Series<Number, Number>();
        scatterSeries2 = new XYChart.Series<Number, Number>();
        lineSeries = new XYChart.Series<Number, Number>();

        points_chart.getData().clear();
        points_chart.getData().add(scatterSeries1);
        points_chart.getData().add(scatterSeries2);

        accuracy_chart.getData().clear();
        accuracy_chart.getData().add(lineSeries);
    }

    //**TODO: сделать верификацию строки с указанием ошибки в знаках*/
    private Rectangle2D parseRectBy2Points(String toParse) {
        try {
            String[] points = toParse.split(";");
            double x1 = Double.parseDouble(points[0].split(",")[0]);
            double y1 = Double.parseDouble(points[0].split(",")[1]);
            double x2 = Double.parseDouble(points[1].split(",")[0]);
            double y2 = Double.parseDouble(points[1].split(",")[1]);
            double width = BigDecimal.valueOf(x2).subtract(BigDecimal.valueOf(x1)).doubleValue();
            double height = BigDecimal.valueOf(y2).subtract(BigDecimal.valueOf(y1)).doubleValue();
            return new Rectangle2D(x1, y1, width, height);
        } catch (Exception ex) {
            return null;
        }
        
    }
}
