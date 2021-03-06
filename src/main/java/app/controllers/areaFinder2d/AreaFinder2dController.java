package app.controllers.areaFinder2d;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import app.controllers.ISceneController;
import app.controllers.areaFinder2d.wrappers.ExperimentsWrapper2d;
import app.controllers.areaFinder2d.tab_rulers.FunctionDrawingTabRuler;
import app.controllers.areaFinder2d.tab_rulers.GeneralStatTabRuler;
import app.controllers.areaFinder2d.tab_rulers.ScatterChartsTabRuler;
import app.controllers.areaFinder2d.wrappers.InputDataWrapperFor2D;
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
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import app.controllers.ChooseFunctionalitySceneController;
/** 
 * 
 */
public class AreaFinder2dController implements ISceneController{

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

     /**
     * Объект, в который вынесен весь функционал по управлению выводом в UI элементы во вкладке отрисовкой графиков функции. 
     */
    private FunctionDrawingTabRuler functionDrawingTabRuler;

    /* ============================ Элементы общего запуска модели =======================*/

    
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

     /**
     * Прямоугольник можно задать 2-мя точками по диагонали. Это поле ввода левого нижнего угла по Y описывающего фигуру прямоугольника.
     */
    @FXML
    private TextField enter_outershapeY1;

     /**
     * Прямоугольник можно задать 2-мя точками по диагонали. Это поле ввода правого верхнего угла по Y описывающего фигуру прямоугольника.
     */
    @FXML
    private TextField enter_outershapeY2;


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
    private TreeTableView<ExperimentsWrapper2d> data_table_inGeneral;

    @FXML
    private Accordion statisticPanesContainer;

    @FXML 
    private TitledPane scatter_chart_pane;

    /* ============================ Элементы вкладки с отрисовкой графиков функций по желанию ======================= */

    /**
     * График, в котором отрисовываются все введённые пользователем функции до запуска модели.
     */
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
    private CheckBox autoY_check;

    /* ==================== Элементы вкладки с отрисовкой точечных графиков под каждый отдельных эксперимент =========== */

    @FXML
    private FlowPane pointCharts_container;

    /* ============================================================================== */

    // JavaFX он нужен пустой
    public AreaFinder2dController() {}

    /**
     * Метод вызывается нажатием на кнопку запауска поиска площади.
     * <p>Метод запуска модели на поиск площади замкнутой фигуры, описанной функциями.
     * <p>Алгоритм следующий:
     * <ol>
     *  <li>Очистка графиков (таблица очищается сама по себе в алгоритме). </li>
     *  <li>Валидация значений в полях (чтобы количество точек не было отрицательным и т.д.). </li>
     *  <li>Поочередное выполнение экспериментов с добавлением результатов в элементы UI.</li>
     *  <li>После выполнения всех экспериментов, все данные заносятся в таблицу.</li>
     * </ol>
     * @param event
     */
    @FXML
    public void startMethod(ActionEvent event) {
        this.generalStatTabRuler.clearAllPointsChart();
        this.generalStatTabRuler.clearAccuracyChart();
        this.scatterChartsTabRuler.clearChartsContainer();

        InputDataWrapperFor2D dataWrap = validateAndParseDataFromMainModelFields();
        if (dataWrap.getErrorMessages().size() > 0) {
            SupplyMethods.getErrorAlert(String.join("\n", dataWrap.getErrorMessages())).showAndWait();
            return;
        }

        MonteCarloAreaMethod areaFinder = new MonteCarloAreaMethod();
        IFigureWithCalculatedArea innerFigure = point -> {
            double upperY = Math.log(point.getX());
            double bottomY = BigDecimal.valueOf(point.getX())
                            .subtract(BigDecimal.valueOf(3.0))
                            .doubleValue();
            if (point.getY() < upperY & point.getY() > bottomY) return true;
            return false;
        };

        final Rectangle2D bigArea = dataWrap.getBigArea();
        IPointsGenerator generator = () -> {
            double randX = MonteCarloSupport.generateDoubleInInterval(bigArea.getMinX(), bigArea.getMaxX(), 5);
            double randY = MonteCarloSupport.generateDoubleInInterval(bigArea.getMinY(), bigArea.getMaxY(), 5);
            return new Point2D(randX, randY);
        };

        double bigAreaValue = BigDecimal.valueOf(bigArea.getWidth())
                        .multiply(BigDecimal.valueOf(bigArea.getHeight()))
                        .setScale(3, RoundingMode.CEILING)
                        .doubleValue();

        ArrayList<BigDecimal> areasList = new ArrayList<>();
        ArrayList<Double> currAvgAreasValuesList = new ArrayList<>();

        for (int i = 0; i < dataWrap.getAmountOfExperiments(); i++) {
            BigDecimal areaValue = areaFinder.findAreaValue(innerFigure, generator, dataWrap.getAmountOfPoints(), bigAreaValue);

            areasList.add(areaValue);

            List<StatePoint2D> unmodifPointsList = areaFinder.getLastRunGeneratedPoints();
            this.generalStatTabRuler.showDataOnPointsChart(unmodifPointsList);
            this.scatterChartsTabRuler.drawExperimentPointsChart(i + 1, unmodifPointsList);

            double currAvgAreaValue = SupplyMethods.calcAvgValueInList(i + 1, areasList, 3);
            this.generalStatTabRuler.showDataOnLinesChart(i + 1, currAvgAreaValue);

            currAvgAreasValuesList.add(currAvgAreaValue);
        }
        this.generalStatTabRuler.showDataOnDataTable(dataWrap.getAmountOfPoints(), dataWrap.toString(), areasList, currAvgAreasValuesList);
    }

    @FXML
    public void draw_functions_method(ActionEvent event) {
        /*
        Expression e = new ExpressionBuilder("3 * sin(y) - 2 / (x - 2)")
            .variables("x", "y")
            .build()
            .setVariable("x", 2.3)
            .setVariable("y", 3.14);
        double result = e.evaluate();
        */ 
                  
    }

    /**
     * Событие, вызываемое, когда нажимается кнопка с флажком. Если ставится в активное положение - нужно заблокировать поля ввода
     * минимального и максимального У, если ставится в неактивное положение - поля нужно разблокировать.
     * @param event
     */
    @FXML
    public void autoYChanged (ActionEvent event) {
        if (this.autoY_check.selectedProperty().get() == true) {
            this.minY_field.setDisable(true);
            this.maxY_field.setDisable(true);
            this.minY_field.setText("");
            this.maxY_field.setText("");

        } else {
            this.minY_field.setDisable(false);
            this.maxY_field.setDisable(false);
        }
    }

    /**
     * TODO: Вставить встроенный браузер и передавать markdown
     * @param event
     */
    @FXML
    public void showHelp(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION, "Button is in development proccess");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Метод собирает значения с полей, в которых задаются параметры модели. Помимо этого, проверяет их по ряду критериев.
     * @return объект типа {@link InputDataWrapperFor2D}, в котором содержатся спарсенные из полей значения, если они валидны.
     * Если какое-то из значений не валидно, то заполняется поле с сообщениями об ошибках, а значение в поле не вносится.
     */
    private InputDataWrapperFor2D validateAndParseDataFromMainModelFields() {
        InputDataWrapperFor2D dataWrap = new InputDataWrapperFor2D();

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

        Double x1 = SupplyMethods.parseAndValidateDouble(this.enter_outershapeX1.getText(), -Double.MAX_VALUE, Double.MAX_VALUE);
        Double x2 = SupplyMethods.parseAndValidateDouble(this.enter_outershapeX2.getText(), -Double.MAX_VALUE, Double.MAX_VALUE);
        Double y1 = SupplyMethods.parseAndValidateDouble(this.enter_outershapeY1.getText(), -Double.MAX_VALUE, Double.MAX_VALUE);
        Double y2 = SupplyMethods.parseAndValidateDouble(this.enter_outershapeY2.getText(), -Double.MAX_VALUE, Double.MAX_VALUE);
        // Ставятся две вертикальные черты, чтобы проверка закончилась при первом же FALSE, иначе на 2х последних сравнениях будет Exception.
        if (x1 == null || x2 == null || y1 == null || y2 == null || x1 > x2 || y1 > y2) {
            dataWrap.addErrorMessage("Ошибка при вводе диагональных точек описывающего прямоугольника!");
        } else {
            double width = BigDecimal.valueOf(x2).subtract(BigDecimal.valueOf(x1)).doubleValue();
            double height = BigDecimal.valueOf(y2).subtract(BigDecimal.valueOf(y1)).doubleValue();
            dataWrap.setBigArea(new Rectangle2D(x1, y1, width, height));
        }
        return dataWrap;
    }

    @FXML
    public void goBack(ActionEvent event) {
        SceneInfoWrapper<ChooseFunctionalitySceneController> nextSceneWrapper = scenesWrapper.getChooseFunctionalitySceneWrapper();

        Scene nextScene = nextSceneWrapper.getRoot().getScene();
        if (nextScene == null) nextScene = new Scene(nextSceneWrapper.getRoot());
        this.stage.setScene(nextScene);

        nextSceneWrapper.getController().prepareStageBeforeShow();
    }

    /**
     * Перед тем, как пользователь получит доступ к GUI и нажмёт какую-либо кнопку, нужно подготовить все элементы, создать все
     * подконтроллеры.
     */
    @Override
    public void prepareAllComponents() {
        this.prepareCharts();
        this.prepareTables();
        this.prepareTabRulers();

        this.statisticPanesContainer.setExpandedPane(this.scatter_chart_pane);

        this.autoY_check.setSelected(true);
        this.minY_field.setDisable(true);
        this.maxY_field.setDisable(true);
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
        TreeTableColumn<ExperimentsWrapper2d, String> expNameCol = new TreeTableColumn<>("Наименование");
        expNameCol.prefWidthProperty().set(200);
        TreeTableColumn<ExperimentsWrapper2d, Integer> expElemsNumCol = new TreeTableColumn<>("Кол-во элементов");
        expElemsNumCol.prefWidthProperty().set(100);
        TreeTableColumn<ExperimentsWrapper2d, Double> expAreaValueCol = new TreeTableColumn<>("Значение площади");
        expAreaValueCol.prefWidthProperty().set(120);
        TreeTableColumn<ExperimentsWrapper2d, Double> expAvgAreaValueCol = new TreeTableColumn<>("AVG площадь");
        expAvgAreaValueCol.prefWidthProperty().set(100);
        TreeTableColumn<ExperimentsWrapper2d, String> expAdditionalCol = new TreeTableColumn<>("Доп.информация");
        expAdditionalCol.prefWidthProperty().set(200);

        // Указываем, какую переменную из класса нужно получить с помощью соответствующего геттера и вставить в ячейку соотвествующей колонки
        expNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper2d, String>("name"));
        expElemsNumCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper2d, Integer>("amountOfElements"));
        expAreaValueCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper2d, Double>("areaValue"));
        expAvgAreaValueCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper2d, Double>("avgAreaValue"));
        expAdditionalCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper2d, String>("additionalInfo"));

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
