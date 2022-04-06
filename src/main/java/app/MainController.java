package app;

import java.math.BigDecimal;
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

    @FXML
    /**
     * Поле ввода количества точек для генерации.
     */
    private TextField enter_num_points;

    @FXML
    /**
     * Поле воода количества экспериментов
     */
    private TextField enter_num_experiments;

    @FXML
    private TextField enter_outershapeX1;

    @FXML
    private TextField enter_outershapeX2;

    @FXML
    private TextField enter_outershapeY1;

    @FXML
    private TextField enter_outershapeY2;

    @FXML 
    /**
     * Кнопка запуска экспериментов
     */
    private Button start_btn;
    
    @FXML 
    /**
     * График из точек, на котором отображаются точки во внешней фигуре и попавшие во внутреннюю фигуру
     */
    private ScatterChart<Number, Number> points_chart;

    /**
     * Набор точек, которые попали во внутреннюю фигуру
     */
    private XYChart.Series<Number, Number> scatterSeries1;
    /**
     * Набор точек, которые не попали во внутреннюю фигуру
     */
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
        scatterSeries1.getData().clear();
        scatterSeries2.getData().clear();
        lineSeries.getData().clear();

        InputDataWrapper dataWrap = validateAndParseDataFromFields();
        if (dataWrap.getErrorMessages().size() > 0) {
            this.showErrorAlert(String.join("\n", dataWrap.getErrorMessages()));
            return;
        }

        MonteCarloAreaMethod areaFinder = new MonteCarloAreaMethod(dataWrap.getBigArea(), new Random());

        ArrayList<BigDecimal> areasList = new ArrayList<>();
        for (int i = 0; i < dataWrap.getAmountOfExperiments(); i++) {
            BigDecimal areaValue = areaFinder.findAreaValue(dataWrap.getAmountOfPoints());
            areasList.add(areaValue);
            this.showDataOnPointsChart(areaFinder.getLastRunGeneratedPoints());
            this.showDataOnLinesChart(i + 1, areasList);
            this.showDataOnDataTable();
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

    private void showDataOnPointsChart(List<StatePoint2D> points) {
        for (StatePoint2D point : points) {
            XYChart.Data<Number, Number> pointToDraw = 
                    new XYChart.Data<Number, Number>(point.getPoint().getX(), point.getPoint().getY());
            if (point.getState() != true) {
                scatterSeries2.getData().add(pointToDraw);
            }
            else {
                scatterSeries1.getData().add(pointToDraw);
            }
        }
    }

    private void showDataOnLinesChart(int numOfExperiment, List<BigDecimal> values) {
        BigDecimal numLimit = BigDecimal.valueOf(numOfExperiment);
        BigDecimal sumValue = BigDecimal.ZERO;
        for (int i = 0; i < numLimit.intValue(); i++) {
            sumValue = sumValue.add(values.get(i));
        }
        double currAvgAreaValue = sumValue.divide(numLimit, 3, RoundingMode.CEILING).doubleValue();            
        XYChart.Data<Number, Number> currAvgValuePoint = new XYChart.Data<Number, Number>(numLimit.intValue(), currAvgAreaValue);
        this.lineSeries.getData().add(currAvgValuePoint);
    }

    private void showDataOnDataTable() {

    }

    private InputDataWrapper validateAndParseDataFromFields() {
        InputDataWrapper dataWrap = new InputDataWrapper();
        int amountOfPointsToGenerate = 0;
        try {
            amountOfPointsToGenerate = Integer.parseInt(this.enter_num_points.getText());
            if (amountOfPointsToGenerate <= 0) throw new Exception();
            dataWrap.setAmountOfPoints(amountOfPointsToGenerate);
        } catch (Exception e) {
            dataWrap.addErrorMessage("Ошибка в поле ввода количества точек для генерации!");
        }

        int amountOfExperiments = 0;
        try {
            amountOfExperiments = Integer.parseInt(this.enter_num_experiments.getText());
            if (amountOfExperiments <= 0) throw new Exception();
            dataWrap.setAmountOfExperiments(amountOfExperiments);
        } catch (Exception e) {
            dataWrap.addErrorMessage("Ошибка в поле ввода количества экспериментов!");
        }

        double x1, x2, y1, y2 = 0;
        try {
            x1 = Double.parseDouble(this.enter_outershapeX1.getText());
            x2 = Double.parseDouble(this.enter_outershapeX2.getText());
            y1 = Double.parseDouble(this.enter_outershapeY1.getText());
            y2 = Double.parseDouble(this.enter_outershapeY2.getText());
            if (x1 > x2 | y1 > y2) throw new Exception();
            double width = BigDecimal.valueOf(x2).subtract(BigDecimal.valueOf(x1)).doubleValue();
            double height = BigDecimal.valueOf(y2).subtract(BigDecimal.valueOf(y1)).doubleValue();
            dataWrap.setBigArea(new Rectangle2D(x1, y1, width, height));
        } catch (Exception e) {
            dataWrap.addErrorMessage("Ошибка при вводе диагональных точек описывающего прямоугольника!");
        }

        return dataWrap;
    }

    public void prepareSeriesAndCharts() {
        scatterSeries1 = new XYChart.Series<Number, Number>();
        scatterSeries2 = new XYChart.Series<Number, Number>();
        lineSeries = new XYChart.Series<Number, Number>();

        points_chart.getData().clear();
        points_chart.getData().addAll(scatterSeries1, scatterSeries2);

        accuracy_chart.getData().clear();
        accuracy_chart.getData().add(lineSeries);

        points_chart.setTitle("Сгенерированные точки для поиска площади внутренней фигуры");
        accuracy_chart.setTitle("Среднее значение площади экспериментов от 1 до текущего");

        accuracy_chart.getXAxis().setLabel("Номер эксперимента");
        accuracy_chart.getYAxis().setLabel("Значение площади фигуры");

        scatterSeries1.setName("Точки во внутренней фигуре");
        scatterSeries2.setName("Точки во внешней фигуре");
        lineSeries.setName("AVG площади с учётом предыдущих экспериментов");
    }
}
