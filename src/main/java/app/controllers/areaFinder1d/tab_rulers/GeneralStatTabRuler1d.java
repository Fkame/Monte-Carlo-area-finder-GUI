package app.controllers.areaFinder1d.tab_rulers;

import java.util.List;

import app.controllers.areaFinder1d.wrappers.ExperimentsWrapper1d;
import app.monte_carlo_area_finder.StatePoint2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

public class GeneralStatTabRuler1d {

    public static int SCALE_VALUE = 5;

    private ScatterChart<Number, Number> all_points_chart;

    private XYChart.Series<Number, Number> innerPointsSeries;

    private XYChart.Series<Number, Number> outerPointsSeries;

    private LineChart<Number, Number> success_amount_chart;

    private  XYChart.Series<Number, Number> successSeries;

    private TreeTableView<ExperimentsWrapper1d> data_table_inGeneral;

    public GeneralStatTabRuler1d(ScatterChart<Number, Number> all_points_chart, 
                                XYChart.Series<Number, Number> innerPointsSeries, XYChart.Series<Number, Number> outerPointsSeries, 
                                LineChart<Number, Number> success_amount_chart, XYChart.Series<Number, Number> successSeries, 
                                TreeTableView<ExperimentsWrapper1d> data_table_inGeneral) 
    {
        this.all_points_chart = all_points_chart;
        this.innerPointsSeries = innerPointsSeries;
        this.outerPointsSeries = outerPointsSeries;
        this.success_amount_chart = success_amount_chart;
        this.successSeries = successSeries;
        this.data_table_inGeneral = data_table_inGeneral;
    }

    public void showDataOnPointsChart(List<StatePoint2D> points) {
        for (StatePoint2D point : points) {
            XYChart.Data<Number, Number> pointToDraw = 
                    new XYChart.Data<Number, Number>(point.getPoint().getX(), point.getPoint().getY());
            if (point.getState() != true) {
                outerPointsSeries.getData().add(pointToDraw);
            }
            else {
                innerPointsSeries.getData().add(pointToDraw);
            }
        }
    }

    public void showDataOnLinesChart(int numOfExperiment, int amountOfSuccess) {       
        XYChart.Data<Number, Number> successValuePoint = new XYChart.Data<Number, Number>(numOfExperiment, amountOfSuccess);
        this.successSeries.getData().add(successValuePoint);
    }

    public void showDataOnLinesChart(List<Integer> amountOfSuccessList) {
        for (int i = 0; i < amountOfSuccessList.size(); i++) {
            XYChart.Data<Number, Number> successValuePoint = new XYChart.Data<Number, Number>(i + 1, amountOfSuccessList.get(i));
            this.successSeries.getData().add(successValuePoint);
        }
    }

    public void showDataOnTable(List<ExperimentsWrapper1d> exps, double possibilityValue, int successExpsNum, String addInfo) {
        if (exps.size() == 0 ) throw new IllegalArgumentException("Empty expsList!");

        ExperimentsWrapper1d rootValue = new ExperimentsWrapper1d(null, "Список экспериментов", exps.size(), possibilityValue, successExpsNum, addInfo);
        TreeItem<ExperimentsWrapper1d> generalTable_itemRoot = new TreeItem<ExperimentsWrapper1d>(rootValue);
        data_table_inGeneral.setRoot(generalTable_itemRoot);
        
        for (int i = 0; i < exps.size(); i++) {
            TreeItem<ExperimentsWrapper1d> eItem = new TreeItem<>(exps.get(i));
            generalTable_itemRoot.getChildren().add(eItem);
        }
    }

    public void clearAllPointsChart() {
        innerPointsSeries.getData().clear();
        outerPointsSeries.getData().clear();
    }

    public void clearAccuracyChart() {
        successSeries.getData().clear();
    }

}
