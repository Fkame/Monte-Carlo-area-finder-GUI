package app;

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
        showDataOnPointsChart();
        Alert alert = new Alert(AlertType.INFORMATION, "Button is in development proccess");
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
        scatterSeries1 = new XYChart.Series<Number, Number>();
        points_chart.getData().add(scatterSeries1);
        for (int i = 1; i < 10; i++) {
            scatterSeries1.getData().add(new XYChart.Data<Number, Number>(i, i + 1));
        }

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
            String[] points = toParse.split("-");
            int x1 = Integer.parseInt(points[0].split(";")[0]);
            int y1 = Integer.parseInt(points[0].split(";")[1]);
            int x2 = Integer.parseInt(points[1].split(";")[0]);
            int y2 = Integer.parseInt(points[1].split(";")[1]);
            return new Rectangle2D(x1, y1, x2, y2);

        } catch (Exception ex) {
            return null;
        }
        
    }
}
