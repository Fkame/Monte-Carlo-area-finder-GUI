package app.controllers.areaFinder1d.wrappers;

/**
 * Класс, который нужен для вывода данных в TreeTableView. Каждая переменная это стобец в таблице.
 * <p>Пакет, в котором он находится, нужно отметить в {@code module-info.java} как {@code opens ... to javafx.base}, иначе
 * будет ошибка {@code IllegalStateException}, когда Treetableview попробует извлечь значение из объекта с помощью рекурсии.
 */
public class ExperimentsWrapper1d {
    private Integer number;
    private String name;
    private Integer amountOfInnerElements;
    private Double possibilityValue;
    private Integer amountOfSuccess;
    private String additionalInfo;

    public ExperimentsWrapper1d(Integer number, String name, Integer amountOfInnerElements, Double possibilityValue, Integer amountOfSuccess, 
                                String additionalInfo) {
        this.number = number;
        this.name = name;
        this.amountOfInnerElements = amountOfInnerElements;
        this.possibilityValue = possibilityValue;
        this.amountOfSuccess = amountOfSuccess;
        this.additionalInfo = additionalInfo;
    }

    public Integer getNumber() { return this.number; }
    public String getName() { return this.name; }
    public Integer getAmountOfInnerElements() { return this.amountOfInnerElements; }
    public Double getPossibilityValue() { return this.possibilityValue; }
    public Integer getAmountOfSuccess() { return this.amountOfSuccess; }
    public String getAdditionalInfo() { return this.additionalInfo; }

    public void setNumber(Integer number) { this.number = number; }
    public void setName(String name) { this.name = name; }
    public void setAmountOfInnerElements(Integer amountOfInnerElements) { this.amountOfInnerElements = amountOfInnerElements; }
    public void setPossibilityValue(Double possibilityValue) { this.possibilityValue = possibilityValue; }
    public void setAmountOfSuccess(Integer amountOfSuccess) { this.amountOfSuccess = amountOfSuccess; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}
