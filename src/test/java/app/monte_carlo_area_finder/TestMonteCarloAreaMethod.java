package app.monte_carlo_area_finder;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.Test;

public class TestMonteCarloAreaMethod {
    
    @Test
    public void testFindProbabilityOfSuccess() {
        int leftLimit = 3;
        int rightLimit = 4;
        IFigureWithCalculatedArea innerFigure = point -> {
            return (point.getX() >= leftLimit) & (point.getX() <= rightLimit);
        };

        int startGen = 1;
        int endGen = 12;
        IPointsGenerator generator = MonteCarloSupport.createSimpleDoubleGenerator(startGen, endGen, 0, 0, 5);

        int amountOfTries = 10;
        int amountOfExperiments = 20;
        MonteCarloAreaMethod method = new MonteCarloAreaMethod(5);
        BigDecimal probabilityPractice = method.findProbabilityOfSuccess(innerFigure, generator, amountOfTries, amountOfExperiments);

        List<StatePoint2D>listOfPoints = method.getLastRunGeneratedPoints();
        int countExp = 1;
        int countOfExpsWithSuccess = 0;
        for (int i = 0; i < listOfPoints.size(); i += amountOfTries) {
            List<StatePoint2D>expList = listOfPoints.subList(i, i + amountOfTries);
            int amountOfSuccessInExp = method.getAmountOfPointsInInnerArea(expList);
            if (amountOfSuccessInExp > 0) countOfExpsWithSuccess += 1;
            System.out.println(String.format("Exp#%d, points in inner area = %d", countExp, amountOfSuccessInExp));
            
            int pointCount = 1;
            for (StatePoint2D statePoint2D : expList) {
                System.out.println(String.format("\tPoint#%d = %s", pointCount, statePoint2D));
                pointCount += 1;
            }
            countExp += 1;
        }
        System.out.println("Amount of success experiments = " + countOfExpsWithSuccess);
        BigDecimal probabilityTheory = BigDecimal.valueOf(countOfExpsWithSuccess)
                                .divide(BigDecimal.valueOf(amountOfExperiments), 5, RoundingMode.CEILING);

        System.out.println(
            String.format("Practice value (my method) = %.5f, Theory value (by point calcutations) = %.5f", probabilityPractice, probabilityTheory)
        );

        assertTrue(Math.abs(probabilityPractice.subtract(probabilityTheory).doubleValue()) <= 0.01);
    }
}
