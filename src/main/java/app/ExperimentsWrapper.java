package app;

/**
 * Класс, который нужен для вывода данных в TreeTableView.
 */
public class ExperimentsWrapper {
    private String name;
    private Integer amountOfElements;
    private Double areaValue;
    private Double avgAreaValue;
    private String additionalInfo;

    public ExperimentsWrapper(String name, Integer amountOfElements, Double areaValue, Double avgAreaValue, String additionalInfo) {
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
