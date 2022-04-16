package app.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.controllers.support.SupplyMethods;
import app.controllers.tab_rulers.GeneralStatTabRuler;
import app.controllers.tab_rulers.ScatterChartsTabRuler;
import app.monte_carlo_method.MonteCarloAreaMethod;
import app.monte_carlo_method.StatePoint2D;
import app.wrappers.ExperimentsWrapper;
import app.wrappers.InputDataWrapperFor1D;
import app.wrappers.SceneInfoWrapper;
import app.wrappers.ScenesInfoContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class AreaFinder1dController implements ISceneController {
    
    private Stage stage;
    private ScenesInfoContainer scenesWrapper;

    /* ============================ Элементы управлением UI во вкладках =======================*/

    /**
     * Объект, в который вынесен весь функционал по управлению выводом в UI элементы во вкладке с общей статистикой. 
     */
    private GeneralStatTabRuler generalStatTabRuler;

     /**
     * Объект, в который вынесен весь функционал по управлению выводом в UI элементы во вкладке с графиками точек каждого эксперимента. 
     */
    private ScatterChartsTabRuler scatterChartsTabRuler;

    /* ============================ Элементы общего запуска модели =======================*/

    @FXML 
    private TextField enter_innershapeX1;

    @FXML 
    private TextField enter_innershapeX2;

    /**
     * Поле ввода количества точек для генерации.
     */
    @FXML
    private TextField enter_num_points;

    /**
     * Поле воода количества экспериментов
     */
    @FXML
    private TextField enter_num_experiments;

    /**
     * Прямоугольник можно задать 2-мя точками по диагонали. Это поле ввода левого нижнего угла по Х описывающего фигуру прямоугольника.
     */
    @FXML
    private TextField enter_outershapeX1;

    /**
     * Прямоугольник можно задать 2-мя точками по диагонали. Это поле ввода правого врехнего угла по Х описывающего фигуру прямоугольника.
     */
    @FXML
    private TextField enter_outershapeX2;

    /* ============================ Элементы вкладки с Общей Статистикой =======================*/
    
    /**
     * График из точек, на котором отображаются точки во внешней фигуре и попавшие во внутреннюю фигуру
     */
    @FXML
    private ScatterChart<Number, Number> all_points_chart;

    /**
     * Набор точек, которые попали во внутреннюю фигуру на графике {@link AreaFinder2dController#all_points_chart}
     */
    private XYChart.Series<Number, Number> innerPointsSeries;
    /**
     * Набор точек, которые НЕ попали во внутреннюю фигуру на графике {@link AreaFinder2dController#all_points_chart}
     */
    private XYChart.Series<Number, Number> outerPointsSeries;

    /**
     * Среднее значение площади в зависимости от числа экспериментов
     */
    @FXML
    private LineChart<Number, Number> accuracy_chart;

    /**
     * Набор точек 
     */
    private  XYChart.Series<Number, Number> accuracySeries;

    /**
     * TreeTableView элемент, в котором записываются результаты каждого эксперимента и считается среднее значение.
     */
    @FXML
    private TreeTableView<ExperimentsWrapper> data_table_inGeneral;

    @FXML
    private Accordion statisticPanesContainer;

    @FXML 
    private TitledPane scatter_chart_pane;

    /* ==================== Элементы вкладки с отрисовкой точечных графиков под каждый отдельных эксперимент =========== */

    @FXML
    private FlowPane pointCharts_container;

    /* ============================================================================== */

    @FXML
    public void startMethod(ActionEvent event) {
        this.generalStatTabRuler.clearAllPointsChart();
        this.generalStatTabRuler.clearAccuracyChart();
        this.scatterChartsTabRuler.clearChartsContainer();

        InputDataWrapperFor1D dataWrap = validateAndParseDataFromMainModelFields();

        if (dataWrap.getErrorMessages().size() > 0) {
            SupplyMethods.getErrorAlert(String.join("\n", dataWrap.getErrorMessages())).showAndWait();
            return;
        }

        MonteCarloAreaMethod areaFinder = new MonteCarloAreaMethod(dataWrap.getBigInterval(), new Random());
        ArrayList<BigDecimal> areasList = new ArrayList<>();
        ArrayList<Double> currAvgAreasValuesList = new ArrayList<>();

        for (int i = 0; i < dataWrap.getAmountOfExperiments(); i++) {
            BigDecimal areaValue = areaFinder.findAreaValue(point -> {
                return (point.getX() >= dataWrap.getInnerInterval().getStartX()) & 
                        (point.getX() <= dataWrap.getInnerInterval().getEndX()); 
            }, dataWrap.getAmountOfPoints());

            areasList.add(areaValue);

            List<StatePoint2D> unmodifPointsList = areaFinder.getLastRunGeneratedPoints();
            this.generalStatTabRuler.showDataOnPointsChart(unmodifPointsList);
            this.scatterChartsTabRuler.drawExperimentPointsChart(i + 1, unmodifPointsList);

            double currAvgAreaValue = SupplyMethods.calcAvgValueInList(i + 1, areasList, 3);
            this.generalStatTabRuler.showDataOnLinesChart(i + 1, currAvgAreaValue);

            currAvgAreasValuesList.add(currAvgAreaValue);
        }
        this.generalStatTabRuler.showDataOnDataTable(dataWrap.getAmountOfPoints(), dataWrap.toString(), areasList, currAvgAreasValuesList);

        //SupplyMethods.getErrorAlert("Функционал ещё в разработке").showAndWait();
    }

    @FXML
    public void showHelp(ActionEvent event) {
        SupplyMethods.getErrorAlert("Функционал ещё в разработке").showAndWait();
    } 

    @FXML
    public void goBack(ActionEvent event) {
        SceneInfoWrapper<ChooseFunctionalitySceneController> nextSceneWrapper = scenesWrapper.getChooseFunctionalitySceneWrapper();

        Scene nextScene = nextSceneWrapper.getRoot().getScene();
        if (nextScene == null) nextScene = new Scene(nextSceneWrapper.getRoot());
        this.stage.setScene(nextScene);

        nextSceneWrapper.getController().prepareStageBeforeShow();
    }

    private InputDataWrapperFor1D validateAndParseDataFromMainModelFields() {
        InputDataWrapperFor1D dataWrap = new InputDataWrapperFor1D();

        Integer amountOfPointsToGenerate = 
                                SupplyMethods.parseAndValidateInteger(this.enter_num_points.getText(), 1, Integer.MAX_VALUE);
        if (amountOfPointsToGenerate == null) {
            dataWrap.addErrorMessage("Ошибка в поле ввода количества точек для генерации!");
        } else {
            dataWrap.setAmountOfPoints(amountOfPointsToGenerate);
        }

        Integer amountOfExperiments = 
                            SupplyMethods.parseAndValidateInteger(this.enter_num_experiments.getText(), 1, Integer.MAX_VALUE);
        if (amountOfExperiments == null) {
            dataWrap.addErrorMessage("Ошибка в поле ввода количества экспериментов!");
        } else {
            dataWrap.setAmountOfExperiments(amountOfExperiments);
        }

        Double outerX1 = SupplyMethods.parseAndValidateDouble(this.enter_outershapeX1.getText(), -Double.MAX_VALUE, Double.MAX_VALUE);
        Double outerX2 = SupplyMethods.parseAndValidateDouble(this.enter_outershapeX2.getText(), -Double.MAX_VALUE, Double.MAX_VALUE);
        if (outerX1 == null || outerX2 == null || outerX1 > outerX2) {
            dataWrap.addErrorMessage("Ошибка при вводе границ внешнего интервала!");
        } else {
            dataWrap.setBigInterval(new Line(outerX1, 0, outerX2, 0));
        }

        Double innerX1 = SupplyMethods.parseAndValidateDouble(this.enter_innershapeX1.getText(), -Double.MAX_VALUE, Double.MAX_VALUE);
        Double innerX2 = SupplyMethods.parseAndValidateDouble(this.enter_innershapeX2.getText(), -Double.MAX_VALUE, Double.MAX_VALUE);
        if (innerX1 == null || innerX2 == null || innerX1 > innerX2) {
            dataWrap.addErrorMessage("Ошибка при вводе границ внутреннего интервала!");
        }
        else if (innerX1 < outerX1 || innerX2 > outerX2) {
            dataWrap.addErrorMessage("Ошибка: границы внутренного интервала выходят за пределы внешнего интервала!");
        }
        else {
            dataWrap.setInnerInterval(new Line(innerX1, 0, innerX2, 0));
        }

        return dataWrap;
    }

    @Override
    public void prepareAllComponents() {
        this.prepareCharts();
        this.prepareTables();
        this.prepareTabRulers();

        this.statisticPanesContainer.setExpandedPane(this.scatter_chart_pane);
    }

    private void prepareTabRulers() {
        this.generalStatTabRuler = new GeneralStatTabRuler(all_points_chart, innerPointsSeries, outerPointsSeries, 
                                            accuracy_chart, accuracySeries, data_table_inGeneral);
        this.scatterChartsTabRuler = new ScatterChartsTabRuler(this.pointCharts_container);
    }

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
        all_points_chart.setTitle("Сгенерированные точки в интервале");
        accuracy_chart.setTitle("Среднее значение длины линии на интервале с каждым новым экспериментом");

        // Подписи осей
        accuracy_chart.getXAxis().setLabel("Номер эксперимента");
        accuracy_chart.getYAxis().setLabel("Значение длины линии на интервале");

        // Наименования наборов значений для подписей в легенде графика
        innerPointsSeries.setName("Точки во внутренней линии");
        outerPointsSeries.setName("Точки во внешней линии");
        accuracySeries.setName("AVG длины с учётом предыдущих экспериментов");
    }

    private void prepareTables() {
        // Создание колонок
        TreeTableColumn<ExperimentsWrapper, String> expNameCol = new TreeTableColumn<ExperimentsWrapper, String>("Наименование");
        expNameCol.prefWidthProperty().set(200);
        TreeTableColumn<ExperimentsWrapper, Integer> expElemsNumCol = new TreeTableColumn<ExperimentsWrapper, Integer>("Кол-во элементов");
        expElemsNumCol.prefWidthProperty().set(100);
        TreeTableColumn<ExperimentsWrapper, Double> expAreaValueCol = new TreeTableColumn<ExperimentsWrapper, Double>("Значение длины");
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

    @Override
    public void prepareStageBeforeShow() {
        stage.centerOnScreen();
        stage.setTitle("Поиск площади на плоскости");
        stage.setResizable(true);
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void setScenesWrapper(ScenesInfoContainer scenesWrapper) {
        this.scenesWrapper = scenesWrapper;
    }

    @Override
    public ScenesInfoContainer getScenesWrapper() {
        return this.scenesWrapper;
    }
}
