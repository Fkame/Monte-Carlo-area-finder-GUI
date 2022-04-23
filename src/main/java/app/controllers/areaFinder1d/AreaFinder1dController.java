package app.controllers.areaFinder1d;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.controllers.ChooseFunctionalitySceneController;
import app.controllers.ISceneController;
import app.controllers.areaFinder1d.wrappers.ExperimentsWrapper1d;
import app.controllers.areaFinder1d.wrappers.InputDataWrapperFor1D;
import app.controllers.areaFinder1d.tab_rulers.GeneralStatTabRuler1d;
import app.controllers.areaFinder1d.tab_rulers.ScatterChartsTabRuler1d;
import app.controllers.support.SupplyMethods;
import app.monte_carlo_area_finder.IFigureWithCalculatedArea;
import app.monte_carlo_area_finder.IPointsGenerator;
import app.monte_carlo_area_finder.MonteCarloAreaMethod;
import app.monte_carlo_area_finder.MonteCarloSupport;
import app.monte_carlo_area_finder.StatePoint2D;
import app.wrappers.SceneInfoWrapper;
import app.wrappers.ScenesInfoContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
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
    private GeneralStatTabRuler1d generalStatTabRuler;

     /**
     * Объект, в который вынесен весь функционал по управлению выводом в UI элементы во вкладке с графиками точек каждого эксперимента. 
     */
    private ScatterChartsTabRuler1d scatterChartsTabRuler;

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

    @FXML 
    private ChoiceBox<String> generation_mode_choise;

    /* ============================ Элементы вкладки с Общей Статистикой =======================*/
    
    /**
     * График из точек, на котором отображаются точки во внешней фигуре и попавшие во внутреннюю фигуру
     */
    @FXML
    private ScatterChart<Number, Number> all_points_chart;

    /**
     * Набор точек, которые попали во внутреннюю фигуру на графике {@link AreaFinder1dController#all_points_chart}
     */
    private XYChart.Series<Number, Number> innerPointsSeries;
    /**
     * Набор точек, которые НЕ попали во внутреннюю фигуру на графике {@link AreaFinder1dController#all_points_chart}
     */
    private XYChart.Series<Number, Number> outerPointsSeries;

    /**
     * Среднее значение площади в зависимости от числа экспериментов
     */
    @FXML
    private LineChart<Number, Number> success_amount_chart;

    /**
     * Набор точек 
     */
    private  XYChart.Series<Number, Number> successSeries;

    /**
     * TreeTableView элемент, в котором записываются результаты каждого эксперимента и считается среднее значение.
     */
    @FXML
    private TreeTableView<ExperimentsWrapper1d> data_table_inGeneral;

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

        ArrayList<BigDecimal> areasList = new ArrayList<>();
        ArrayList<Double> currAvgAreasValuesList = new ArrayList<>();

        MonteCarloAreaMethod areaFinder = new MonteCarloAreaMethod();

        // Создание определителя попадания внутрь пользовательского интервала
        final double leftInnerLimit = dataWrap.getInnerInterval().getStartX();
        final double rightInnerLimit = dataWrap.getInnerInterval().getEndX();
        IFigureWithCalculatedArea innerFigure = point -> {
            return (point.getX() >= leftInnerLimit) & (point.getX() <= rightInnerLimit);
        };

        // Создание генератора чисел в пределах большого интервала.
        final double leftOuterLimit = dataWrap.getBigInterval().getStartX();
        final double rightOuterLimit = dataWrap.getBigInterval().getEndX();
        IPointsGenerator generator = null;
        if (this.generation_mode_choise.getSelectionModel().getSelectedIndex() == 0) {
            generator = MonteCarloSupport.createSimpleDoubleGenerator(leftOuterLimit, rightOuterLimit, 0, 0, 5);
        }
        else {
            generator = MonteCarloSupport.createSimpleIntGenerator((int)leftOuterLimit, (int)rightOuterLimit, 0, 0);
        }
        
        areaFinder.setScaleValue(5);

        // Сам ответ
        BigDecimal possibilityValue = areaFinder.findProbabilityOfSuccess(innerFigure, generator, 
                                                    dataWrap.getAmountOfPoints(), dataWrap.getAmountOfExperiments());

        // Получение промежуточной информации для статистики
        List<StatePoint2D> unmodifPointsList = areaFinder.getLastRunGeneratedPoints();
        this.generalStatTabRuler.showDataOnPointsChart(unmodifPointsList);

        List<ExperimentsWrapper1d> exps = new ArrayList<>();
        for (int i = 0; i < dataWrap.getAmountOfExperiments(); i++) {
            int startIdx = i * dataWrap.getAmountOfPoints();
            int endIdx = (i + 1) * dataWrap.getAmountOfPoints();

            List<StatePoint2D> currExperimentPoints = unmodifPointsList.subList(startIdx, endIdx);
            this.scatterChartsTabRuler.drawExperimentPointsChart(i + 1, currExperimentPoints);

            int amountOfSuccess = areaFinder.getAmountOfPointsInInnerArea(currExperimentPoints);
            this.generalStatTabRuler.showDataOnLinesChart(i + 1, amountOfSuccess);

            exps.add(new ExperimentsWrapper1d(i + 1, "Эксперимент", dataWrap.getAmountOfPoints(), 
                                        null, amountOfSuccess, ""));
        }
        int amountOfSuccess = (int)exps.stream().filter((exp) -> exp.getAmountOfSuccess() > 0).count();
        this.generalStatTabRuler.showDataOnTable(exps, possibilityValue.doubleValue(), amountOfSuccess, null); 
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

        this.generation_mode_choise.getItems().add("Все значения");
        this.generation_mode_choise.getItems().add("Только целые числа");
        this.generation_mode_choise.setValue("Все значения");
    }

    private void prepareTabRulers() {
        this.generalStatTabRuler = new GeneralStatTabRuler1d(all_points_chart, innerPointsSeries, outerPointsSeries, 
                                            success_amount_chart, successSeries, data_table_inGeneral);
        this.scatterChartsTabRuler = new ScatterChartsTabRuler1d(this.pointCharts_container);
    }

    private void prepareCharts() {

        // Создание наборов значений для графиков
        innerPointsSeries = new XYChart.Series<Number, Number>();
        outerPointsSeries = new XYChart.Series<Number, Number>();
        successSeries = new XYChart.Series<Number, Number>();

        all_points_chart.getData().clear();
        all_points_chart.getData().addAll(innerPointsSeries, outerPointsSeries);

        success_amount_chart.getData().clear();
        success_amount_chart.getData().add(successSeries);

        // Названия графиков
        all_points_chart.setTitle("Сгенерированные точки в интервале");
        success_amount_chart.setTitle("Количество успехов в каждом сеансе");

        // Подписи осей
        success_amount_chart.getXAxis().setLabel("Номер эксперимента");
        success_amount_chart.getYAxis().setLabel("Кол-во успехов");

        // Наименования наборов значений для подписей в легенде графика
        innerPointsSeries.setName("Точки во внутренней линии");
        outerPointsSeries.setName("Точки во внешней линии");
        successSeries.setName("Кол-во успехов за 1 сеанс");
    }

    private void prepareTables() {
        // Создание колонок
        TreeTableColumn<ExperimentsWrapper1d, Integer> numCol = new TreeTableColumn<>("№");
        numCol.prefWidthProperty().set(50);
        TreeTableColumn<ExperimentsWrapper1d, String> nameCol = new TreeTableColumn<>("Наименование");
        nameCol.prefWidthProperty().set(200);
        TreeTableColumn<ExperimentsWrapper1d, Integer> amountOfInnerElementsCol = new TreeTableColumn<>("Кол-во вложенных элементов");
        amountOfInnerElementsCol.prefWidthProperty().set(200);
        TreeTableColumn<ExperimentsWrapper1d, Double> possibilityValueCol = new TreeTableColumn<>("Найденная Вероятность");
        possibilityValueCol.prefWidthProperty().set(200);
        TreeTableColumn<ExperimentsWrapper1d, Integer> amountOfSuccessCol = new TreeTableColumn<>("Количество успехов");
        amountOfSuccessCol.prefWidthProperty().set(200);
        TreeTableColumn<ExperimentsWrapper1d, String> additionalCol = new TreeTableColumn<>("Доп.информация");
        additionalCol.prefWidthProperty().set(200);

        // Указываем, какую переменную из класса нужно получить с помощью соответствующего геттера и вставить в ячейку соотвествующей колонки
        numCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper1d, Integer>("number"));
        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper1d, String>("name"));
        amountOfInnerElementsCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper1d, Integer>("amountOfInnerElements"));
        possibilityValueCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper1d, Double>("possibilityValue"));
        amountOfSuccessCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper1d, Integer>("amountOfSuccess"));
        additionalCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper1d, String>("additionalInfo"));

        data_table_inGeneral.getColumns().addAll(numCol, nameCol, amountOfInnerElementsCol, possibilityValueCol, amountOfSuccessCol, additionalCol);
    }

    @Override
    public void prepareStageBeforeShow() {
        stage.centerOnScreen();
        stage.setTitle("Поиск вероятности выпадения числа за несколько попыток");
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
