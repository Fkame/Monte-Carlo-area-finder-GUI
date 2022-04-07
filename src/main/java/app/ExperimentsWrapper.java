package app;

public class ExperimentsWrapper {
    private String name;
    private int amountOfElements;
    private double areaValue;
    private String additionalInfo;

    public ExperimentsWrapper(String name, int amountOfElements, double areaValue, String additionalInfo) {
        this.name = name;
        this.amountOfElements = amountOfElements;
        this.areaValue = areaValue;
        this.additionalInfo = additionalInfo;
    }

    public String getName() { return this.name; }
    public int getAmountOfElements() { return this.amountOfElements; }
    public double getAreaValue() { return this.areaValue; }
    public String getAdditionalInfo() { return this.additionalInfo; }

    public void setName(String name) { this.name = name; }
    public void setAmountOfElements(int amountOfElements) { this.amountOfElements = amountOfElements; }
    public void setAreaValue(double areaValue) { this.areaValue = areaValue; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    @Override
    public String toString() {
        return name + ", elements=" + amountOfElements + ", area_value=" + areaValue + ", additional_info=" + additionalInfo;
    }


}
