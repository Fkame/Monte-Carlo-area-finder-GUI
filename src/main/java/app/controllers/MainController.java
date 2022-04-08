package app.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.controllers.support.SupplyMethods;
import app.controllers.tab_rulers.FunctionDrawingTabRuler;
import app.controllers.tab_rulers.GeneralStatTabRuler;
import app.controllers.tab_rulers.ScatterChartsTabRuler;
import app.monte_carlo_method.MonteCarloAreaMethod;
import app.monte_carlo_method.StatePoint2D;
import app.wrappers.ExperimentsWrapper;
import app.wrappers.InputDataWrapper;
import app.controllers.support.SupplyMethods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/** 
 * TODO: Сделать разбиение на 1 класс = 1 вкладка, сделать многопоточность, реализовать отрисовку функций перед моделированием, добавить чтение любых функций
 */
public class MainController {

    private GeneralStatTabRuler generalStatTabRuler;
    private ScatterChartsTabRuler scatterChartsTabRuler;
    private FunctionDrawingTabRuler functionDrawingTabRuler;

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
    private ScatterChart<Number, Number> all_points_chart;

    /**
     * Набор точек, которые попали во внутреннюю фигуру на графике {@link MainController#all_points_chart}
     */
    private XYChart.Series<Number, Number> innerPointsSeries;
    /**
     * Набор точек, которые НЕ попали во внутреннюю фигуру на графике {@link MainController#all_points_chart}
     */
    private XYChart.Series<Number, Number> outerPointsSeries;

    @FXML
    /**
     * Среднее значение площади в зависимости от числа экспериментов
     */
    private LineChart<Number, Number> accuracy_chart;

    /**
     * Набор точек 
     */
    private  XYChart.Series<Number, Number> accuracySeries;

    @FXML
    private TreeTableView<ExperimentsWrapper> data_table_inGeneral;

    @FXML 
    private LineChart<Number, Number> functions_chart;
    
    @FXML
    private TextField minX_field;
    @FXML
    private TextField maxX_field;
    @FXML
    private TextField minY_field;
    @FXML
    private TextField maxY_field;
    @FXML
    private TextField stepValue_field;

    @FXML
    private FlowPane pointCharts_container;

    // JavaFX он нужен пустой
    public MainController() {}

    @FXML
    private void startMethod(ActionEvent event) {
        this.generalStatTabRuler.clearAllPointsChart();
        this.generalStatTabRuler.clearAccuracyChart();
        this.scatterChartsTabRuler.clearChartsContainer();;

        InputDataWrapper dataWrap = validateAndParseDataFromFields();
        if (dataWrap.getErrorMessages().size() > 0) {
            SupplyMethods.getErrorAlert(String.join("\n", dataWrap.getErrorMessages())).showAndWait();
            return;
        }

        MonteCarloAreaMethod areaFinder = new MonteCarloAreaMethod(dataWrap.getBigArea(), new Random());

        ArrayList<BigDecimal> areasList = new ArrayList<>();
        ArrayList<Double> currAvgAreasValuesList = new ArrayList<>();

        for (int i = 0; i < dataWrap.getAmountOfExperiments(); i++) {
            BigDecimal areaValue = areaFinder.findAreaValue(dataWrap.getAmountOfPoints());
            areasList.add(areaValue);

            List<StatePoint2D> unmodifPointsList = areaFinder.getLastRunGeneratedPoints();
            this.generalStatTabRuler.showDataOnPointsChart(unmodifPointsList);
            this.scatterChartsTabRuler.drawExperimentPointsChart(i + 1, unmodifPointsList);

            double currAvgAreaValue = SupplyMethods.calcAvgValueInList(i + 1, areasList, 3);
            this.generalStatTabRuler.showDataOnLinesChart(i + 1, currAvgAreaValue);

            currAvgAreasValuesList.add(currAvgAreaValue);
        }
        this.generalStatTabRuler.showDataOnDataTable(dataWrap, areasList, currAvgAreasValuesList);
    }

    @FXML
    public void draw_functions_method(ActionEvent event) {
        
    }

    @FXML
    private void showHelp(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION, "Button is in development proccess");
        alert.setHeaderText(null);
        alert.showAndWait();
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

    public void prepareAllComponents() {
        this.prepareCharts();
        this.prepareTables();
        this.prepareTabRulers();
    }

    private void prepareTabRulers() {
        this.generalStatTabRuler = new GeneralStatTabRuler(all_points_chart, innerPointsSeries, outerPointsSeries, 
                                            accuracy_chart, accuracySeries, data_table_inGeneral);
        this.scatterChartsTabRuler = new ScatterChartsTabRuler(this.pointCharts_container);
        this.functionDrawingTabRuler = null;
    }

    /**
     * TODO: добавить создание осей и графика точек с 0, чтобы нормально отображалась легенда
     */
    private void prepareCharts() {

        // Создание наборов значений для графиков
        innerPointsSeries = new XYChart.Series<Number, Number>();
        outerPointsSeries = new XYChart.Series<Number, Number>();
        accuracySeries = new XYChart.Series<Number, Number>();

        all_points_chart.getData().clear();
        all_points_chart.getData().addAll(innerPointsSeries, outerPointsSeries);

        accuracy_chart.getData().clear();
        accuracy_chart.getData().add(accuracySeries);

        // Названия графиков
        all_points_chart.setTitle("Сгенерированные точки для поиска площади внутренней фигуры");
        accuracy_chart.setTitle("Среднее значение площади экспериментов от 1 до текущего");

        // Подписи осей
        accuracy_chart.getXAxis().setLabel("Номер эксперимента");
        accuracy_chart.getYAxis().setLabel("Значение площади фигуры");

        // Наименования наборов значений для подписей в легенде графика
        innerPointsSeries.setName("Точки во внутренней фигуре");
        outerPointsSeries.setName("Точки во внешней фигуре");
        accuracySeries.setName("AVG площади с учётом предыдущих экспериментов");
    }

    private void prepareTables() {
        // Создание колонок
        TreeTableColumn<ExperimentsWrapper, String> expNameCol = new TreeTableColumn<ExperimentsWrapper, String>("Наименование");
        expNameCol.prefWidthProperty().set(200);
        TreeTableColumn<ExperimentsWrapper, Integer> expElemsNumCol = new TreeTableColumn<ExperimentsWrapper, Integer>("Кол-во элементов");
        expElemsNumCol.prefWidthProperty().set(100);
        TreeTableColumn<ExperimentsWrapper, Double> expAreaValueCol = new TreeTableColumn<ExperimentsWrapper, Double>("Значение площади");
        expAreaValueCol.prefWidthProperty().set(120);
        TreeTableColumn<ExperimentsWrapper, Double> expAvgAreaValueCol = new TreeTableColumn<ExperimentsWrapper, Double>("AVG площадь");
        expAvgAreaValueCol.prefWidthProperty().set(100);
        TreeTableColumn<ExperimentsWrapper, String> expAdditionalCol = new TreeTableColumn<ExperimentsWrapper, String>("Доп.информация");
        expAdditionalCol.prefWidthProperty().set(200);

        // Указываем, какую переменную из класса нужно получить с помощью соответствующего геттера и вставить в ячейку соотвествующей колонки
        expNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, String>("name"));
        expElemsNumCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, Integer>("amountOfElements"));
        expAreaValueCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, Double>("areaValue"));
        expAvgAreaValueCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, Double>("avgAreaValue"));
        expAdditionalCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, String>("additionalInfo"));

        data_table_inGeneral.getColumns().addAll(expNameCol, expElemsNumCol, expAreaValueCol, expAvgAreaValueCol, expAdditionalCol);
    }
}
