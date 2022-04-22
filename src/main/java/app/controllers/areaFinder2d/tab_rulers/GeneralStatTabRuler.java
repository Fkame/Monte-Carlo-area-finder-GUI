package app.controllers.areaFinder2d.tab_rulers;

import java.math.BigDecimal;
import java.util.List;

import app.monte_carlo_area_finder.StatePoint2D;
import app.wrappers.ExperimentsWrapper;
import app.wrappers.InputDataWrapperFor2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

public class GeneralStatTabRuler {

    public static int SCALE_VALUE = 5;

    private ScatterChart<Number, Number> all_points_chart;

    private XYChart.Series<Number, Number> innerPointsSeries;

    private XYChart.Series<Number, Number> outerPointsSeries;

    private LineChart<Number, Number> accuracy_chart;

    private  XYChart.Series<Number, Number> accuracySeries;

    private TreeTableView<ExperimentsWrapper> data_table_inGeneral;

    public GeneralStatTabRuler(ScatterChart<Number, Number> all_points_chart, 
                                XYChart.Series<Number, Number> innerPointsSeries, XYChart.Series<Number, Number> outerPointsSeries, 
                                LineChart<Number, Number> accuracy_chart, XYChart.Series<Number, Number> accuracySeries, 
                                TreeTableView<ExperimentsWrapper> data_table_inGeneral) 
    {
        this.all_points_chart = all_points_chart;
        this.innerPointsSeries = innerPointsSeries;
        this.outerPointsSeries = outerPointsSeries;
        this.accuracy_chart = accuracy_chart;
        this.accuracySeries = accuracySeries;
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

    public void showDataOnLinesChart(int numOfExperiment, double currAvgAreaValue) {       
        XYChart.Data<Number, Number> currAvgValuePoint = new XYChart.Data<Number, Number>(numOfExperiment, currAvgAreaValue);
        this.accuracySeries.getData().add(currAvgValuePoint);
    }

    public void showDataOnDataTable(int numOfPoints, String addInfo, List<BigDecimal> areasList, List<Double> avgValuesList) {
        if (areasList.size() == 0 | avgValuesList.size() == 0) throw new IllegalArgumentException("На отрисовку таблицы подан какой-то пустой массив");
        if (areasList.size() != avgValuesList.size()) throw new IllegalArgumentException("У массива значений должен быть тот же размер, что и у массива средних значений");
        Double lastAvgValue = avgValuesList.get(avgValuesList.size() - 1);
        int amountOfExperiments = areasList.size();

        ExperimentsWrapper rootValue = new ExperimentsWrapper("Список экспериментов", amountOfExperiments, lastAvgValue, lastAvgValue, addInfo);
        TreeItem<ExperimentsWrapper> generalTable_itemRoot = new TreeItem<ExperimentsWrapper>(rootValue);
        data_table_inGeneral.setRoot(generalTable_itemRoot);

        for (int i = 0; i < amountOfExperiments; i++) {
            double areaValue = areasList.get(i).setScale(SCALE_VALUE).doubleValue();
            Double currAvgAreaValue = avgValuesList.get(i);
            ExperimentsWrapper eInfo = 
                        new ExperimentsWrapper("Эксперимент №" + (i + 1), numOfPoints, areaValue, currAvgAreaValue, "");
            TreeItem<ExperimentsWrapper> eItem = new TreeItem<>(eInfo);
            generalTable_itemRoot.getChildren().add(eItem);
        }
    }

    public void clearAllPointsChart() {
        innerPointsSeries.getData().clear();
        outerPointsSeries.getData().clear();
    }

    public void clearAccuracyChart() {
        accuracySeries.getData().clear();
    }

}
