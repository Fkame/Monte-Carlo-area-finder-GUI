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
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.FlowPane;

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
    private ScatterChart<Number, Number> all_points_chart;

    /**
     * Набор точек, которые попали во внутреннюю фигуру на графике {@link MainController#all_points_chart}
     */
    private XYChart.Series<Number, Number> scatterSeries1;
    /**
     * Набор точек, которые НЕ попали во внутреннюю фигуру на графике {@link MainController#all_points_chart}
     */
    private XYChart.Series<Number, Number> scatterSeries2;

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
        scatterSeries1.getData().clear();
        scatterSeries2.getData().clear();
        accuracySeries.getData().clear();
        pointCharts_container.getChildren().clear();

        InputDataWrapper dataWrap = validateAndParseDataFromFields();
        if (dataWrap.getErrorMessages().size() > 0) {
            this.showErrorAlert(String.join("\n", dataWrap.getErrorMessages()));
            return;
        }

        MonteCarloAreaMethod areaFinder = new MonteCarloAreaMethod(dataWrap.getBigArea(), new Random());

        ArrayList<BigDecimal> areasList = new ArrayList<>();
        ArrayList<Double> currAvgAreasValuesList = new ArrayList<>();

        for (int i = 0; i < dataWrap.getAmountOfExperiments(); i++) {
            BigDecimal areaValue = areaFinder.findAreaValue(dataWrap.getAmountOfPoints());
            areasList.add(areaValue);

            List<StatePoint2D> unmodifPointsList = areaFinder.getLastRunGeneratedPoints();
            this.showDataOnPointsChart(unmodifPointsList);
            this.drawExperimentPointsChart(i + 1, unmodifPointsList);

            double currAvgAreaValue = calcAvgValueInList(i + 1, areasList, 3);
            this.showDataOnLinesChart(i + 1, currAvgAreaValue);

            currAvgAreasValuesList.add(currAvgAreaValue);
        }
        this.showDataOnDataTable(dataWrap, areasList, currAvgAreasValuesList);
    }

    @FXML
    public void draw_functions_method(ActionEvent event) {
        
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
        
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();        
        ScatterChart<Number,Number> scatChart = new ScatterChart<>(xAxis, yAxis);
        scatChart.setOnMouseClicked(event -> this.openChartInOwnWindow());
        scatChart.setPrefSize(300, 300);
        scatChart.legendVisibleProperty().set(false);
        scatChart.getData().addAll(inInnerFigurePoints, inOuterFigurePoints);
        scatChart.setTitle("Эксперимент №" + number);

        pointCharts_container.getChildren().add(scatChart);
    }

    private void openChartInOwnWindow() {
        this.showErrorAlert("Functionality of opening charts is not being ready yet");
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

    private void showDataOnLinesChart(int numOfExperiment, double currAvgAreaValue) {       
        XYChart.Data<Number, Number> currAvgValuePoint = new XYChart.Data<Number, Number>(numOfExperiment, currAvgAreaValue);
        this.accuracySeries.getData().add(currAvgValuePoint);
    }

    /** 
     * TODO: Сделать вывод текущего среднего значения для каждого эксперимента, чтобы было видно, как оно меняется.
    */
    private void showDataOnDataTable(InputDataWrapper inputDataWrapper, List<BigDecimal> areasList, List<Double> avgValuesList) {
        Double lastAvgValue = avgValuesList.get(avgValuesList.size() - 1);
        int amountOfExperiments = areasList.size();

        ExperimentsWrapper rootValue = new ExperimentsWrapper("Список экспериментов", amountOfExperiments, null, lastAvgValue, "");
        TreeItem<ExperimentsWrapper> generalTable_itemRoot = new TreeItem<ExperimentsWrapper>(rootValue);
        data_table_inGeneral.setRoot(generalTable_itemRoot);

        int pointAmount = inputDataWrapper.getAmountOfPoints();
        for (int i = 0; i < amountOfExperiments; i++) {
            double areaValue = areasList.get(i).doubleValue();
            Double currAvgAreaValue = avgValuesList.get(i);
            ExperimentsWrapper eInfo = 
                        new ExperimentsWrapper("Эксперимент №" + (i + 1), pointAmount, areaValue, currAvgAreaValue, "");
            TreeItem<ExperimentsWrapper> eItem = new TreeItem<>(eInfo);
            generalTable_itemRoot.getChildren().add(eItem);
        }

        data_table_inGeneral.autosize();
    }

    private double calcAvgValueInList(int limitNumber, List<BigDecimal> values, int accuracy) throws IllegalArgumentException {
        BigDecimal numLimit = BigDecimal.valueOf(limitNumber);
        if (limitNumber <= 0) throw new IllegalArgumentException("limitNumber <= 0!!!");
        if (limitNumber > values.size()) throw new IllegalArgumentException("limit number > size of values list!!!");
        BigDecimal sumValue = BigDecimal.ZERO;
        for (int i = 0; i < numLimit.intValue(); i++) {
            sumValue = sumValue.add(values.get(i));
        }
        double currAvgAreaValue = sumValue.divide(numLimit, accuracy, RoundingMode.CEILING).doubleValue(); 
        return currAvgAreaValue;  
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
    }

    /**
     * TODO: добавить создание осей и графика точек с 0, чтобы нормально отображалась легенда
     */
    private void prepareCharts() {

        // Создание наборов значений для графиков
        scatterSeries1 = new XYChart.Series<Number, Number>();
        scatterSeries2 = new XYChart.Series<Number, Number>();
        accuracySeries = new XYChart.Series<Number, Number>();

        all_points_chart.getData().clear();
        all_points_chart.getData().addAll(scatterSeries1, scatterSeries2);

        accuracy_chart.getData().clear();
        accuracy_chart.getData().add(accuracySeries);

        // Названия графиков
        all_points_chart.setTitle("Сгенерированные точки для поиска площади внутренней фигуры");
        accuracy_chart.setTitle("Среднее значение площади экспериментов от 1 до текущего");

        // Подписи осей
        accuracy_chart.getXAxis().setLabel("Номер эксперимента");
        accuracy_chart.getYAxis().setLabel("Значение площади фигуры");

        // Наименования наборов значений для подписей в легенде графика
        scatterSeries1.setName("Точки во внутренней фигуре");
        scatterSeries2.setName("Точки во внешней фигуре");
        accuracySeries.setName("AVG площади с учётом предыдущих экспериментов");
    }

    private void prepareTables() {
        // Создание колонок
        TreeTableColumn<ExperimentsWrapper, String> expNameCol = new TreeTableColumn<ExperimentsWrapper, String>("Наименование");
        TreeTableColumn<ExperimentsWrapper, Integer> expElemsNumCol = new TreeTableColumn<ExperimentsWrapper, Integer>("Кол-во элементов");
        TreeTableColumn<ExperimentsWrapper, Double> expAreaValueCol = new TreeTableColumn<ExperimentsWrapper, Double>("Значение площади");
        TreeTableColumn<ExperimentsWrapper, Double> expAvgAreaValueCol = new TreeTableColumn<ExperimentsWrapper, Double>("AVG площадь");
        TreeTableColumn<ExperimentsWrapper, String> expAdditionalCol = new TreeTableColumn<ExperimentsWrapper, String>("Доп.информация");

        // Указываем, какую переменную из класса нужно получить с помощью соответствующего геттера и вставить в ячейку соотвествующей колонки
        expNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, String>("name"));
        expElemsNumCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, Integer>("amountOfElements"));
        expAreaValueCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, Double>("areaValue"));
        expAvgAreaValueCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, Double>("avgAreaValue"));
        expAdditionalCol.setCellValueFactory(new TreeItemPropertyValueFactory<ExperimentsWrapper, String>("additionalInfo"));

        data_table_inGeneral.getColumns().addAll(expNameCol, expElemsNumCol, expAreaValueCol, expAvgAreaValueCol, expAdditionalCol);
    }
}
