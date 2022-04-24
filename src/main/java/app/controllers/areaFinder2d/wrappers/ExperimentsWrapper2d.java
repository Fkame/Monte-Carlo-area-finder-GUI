package app.controllers.areaFinder2d.wrappers;

/**
 * Класс, который нужен для вывода данных в TreeTableView. Каждая переменная это стобец в таблице.
 * <p>Пакет, в котором он находится, нужно отметить в {@code module-info.java} как {@code opens ... to javafx.base}, иначе
 * будет ошибка {@code IllegalStateException}, когда Treetableview попробует извлечь значение из объекта с помощью рекурсии.
 */
public class ExperimentsWrapper2d {
    public String name;
    public Integer amountOfElements;
    public Double areaValue;
    public Double avgAreaValue;
    public String additionalInfo;

    public ExperimentsWrapper2d(String name, Integer amountOfElements, Double areaValue, Double avgAreaValue, String additionalInfo) {
        this.name = name;
        this.amountOfElements = amountOfElements;
        this.areaValue = areaValue;
        this.avgAreaValue = avgAreaValue;
        this.additionalInfo = additionalInfo;
    }

    public String getName() { return this.name; }
    public Integer getAmountOfElements() { return this.amountOfElements; }
    public Double getAreaValue() { return this.areaValue; }
    public Double getAvgAreaValue() { return this.avgAreaValue; }
    public String getAdditionalInfo() { return this.additionalInfo; }

    public void setName(String name) { this.name = name; }
    public void setAmountOfElements(Integer amountOfElements) { this.amountOfElements = amountOfElements; }
    public void setAreaValue(Double areaValue) { this.areaValue = areaValue; }
    public void setAvgAreaValue(Double avgAreaValue) { this.avgAreaValue = avgAreaValue; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}
