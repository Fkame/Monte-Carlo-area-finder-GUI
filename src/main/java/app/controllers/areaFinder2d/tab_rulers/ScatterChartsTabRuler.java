package app.controllers.areaFinder2d.tab_rulers;

import java.util.List;

import app.controllers.support.SupplyMethods;
import app.monte_carlo_area_finder.StatePoint2D;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ScatterChartsTabRuler {
    
    private FlowPane pointCharts_container;

    public ScatterChartsTabRuler (FlowPane pointCharts_container) {
        this.pointCharts_container = pointCharts_container;
    }

    public void clearChartsContainer() {
        pointCharts_container.getChildren().clear();
    }

    public void drawExperimentPointsChart(int number, List<StatePoint2D> statedPoints) {
        XYChart.Series<Number,Number> inOuterFigurePoints = new XYChart.Series<>();
        XYChart.Series<Number,Number> inInnerFigurePoints = new XYChart.Series<>();

        for (StatePoint2D statePoint : statedPoints) {
            XYChart.Data<Number, Number> point = new XYChart.Data<>(statePoint.getPoint().getX(), statePoint.getPoint().getY());
            if (statePoint.getState() == true) {
                inInnerFigurePoints.getData().add(point);
            } else {
                inOuterFigurePoints.getData().add(point);
            }
        }
        
        ScatterChart<Number,Number> scatChart = new ScatterChart<>(new NumberAxis(), new NumberAxis());
        scatChart.setPrefSize(300, 300);
        scatChart.legendVisibleProperty().set(false);
        scatChart.getData().addAll(inInnerFigurePoints, inOuterFigurePoints);
        scatChart.setTitle("Эксперимент №" + number);
        scatChart.setOnMouseClicked(event -> {
            XYChart.Series<Number, Number> ser1 = SupplyMethods.copySeriesData(inInnerFigurePoints);
            XYChart.Series<Number, Number> ser2 = SupplyMethods.copySeriesData(inOuterFigurePoints);
            double widthAndHeight = scatChart.getWidth() * 2.5;
            this.openChartInOwnWindow(scatChart.getTitle(), widthAndHeight, widthAndHeight, ser1, ser2);
        });

        pointCharts_container.getChildren().add(scatChart);
    }
    
    /**
     * TODO: сделать создание копии графика. Добавить ограничитель на создание окон через события и UserData свойство класса
     */
    private void openChartInOwnWindow(String title, double width, double height, XYChart.Series<Number, Number> ... series) {
        ScatterChart<Number, Number> scatChart = new ScatterChart<>(new NumberAxis(), new NumberAxis());
        scatChart.setTitle(title);
        scatChart.legendVisibleProperty().set(false);
        scatChart.getData().addAll(series);

        BorderPane container = new BorderPane(scatChart);
        Scene chartScene = new Scene(container, width, height);
        Stage chartStage = new Stage();
        chartStage.setScene(chartScene);
        chartStage.setTitle(title);
        chartStage.centerOnScreen();
        chartStage.show();
    }
}
