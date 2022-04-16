package app.monte_carlo_method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Random;

import org.junit.Test;

import app.monte_carlo_method.MonteCarloAreaMethod;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public class TestMonteCarloAreaMethod {
    
    @Test
    public void testConstructorForBadData() {
        MonteCarloAreaMethod areaFinder = null;
        int[] goodData = {1, 5, 2, 4};
        int[] badData1 = {5, 1, 2, 4};
        int[] badData2 = {1, 5, 4, 2};
        int[] badData3 = {5, 1, 4, 2};
        Random notNullGenerator = new Random();

        try {
            areaFinder = new MonteCarloAreaMethod(goodData[0], goodData[1], goodData[2], goodData[3], notNullGenerator);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            assertTrue(false);
        }

        try {
            areaFinder = new MonteCarloAreaMethod(goodData[0], goodData[1], goodData[2], goodData[3], null);
            assertTrue(false);
        } catch (Exception e) { }

        try {
            areaFinder = new MonteCarloAreaMethod(badData1[0], badData1[1], badData1[2], badData1[3], notNullGenerator);
            assertTrue(false);
        } catch (Exception e) { }

        try {
            areaFinder = new MonteCarloAreaMethod(badData2[0], badData2[1], badData2[2], badData2[3], notNullGenerator);
            assertTrue(false);
        } catch (Exception e) { }

        try {
            areaFinder = new MonteCarloAreaMethod(badData3[0], badData3[1], badData3[2], badData3[3], notNullGenerator);
            assertTrue(false);
        } catch (Exception e) { }
    }

    @Test
    public void testGetBigAreaValue() {
        Rectangle2D rect = new Rectangle2D(0, 0, 3, 2);
        Rectangle2D rect2 = new Rectangle2D(5, 3, 10, 17);

        // #1
        MonteCarloAreaMethod areaFinder = 
                        new MonteCarloAreaMethod(rect.getMinX(), rect.getMaxX(), rect.getMinY(), rect.getMaxY(), new Random());
        double expectedArea = BigDecimal.valueOf(rect.getHeight())
                        .multiply(BigDecimal.valueOf(rect.getWidth()))
                        .doubleValue();
        double actualArea = areaFinder.getBigAreaValue().doubleValue();
        if (Math.abs(expectedArea - actualArea) > 0.001) assertTrue(false);
        System.out.println("Additional Test info: testGetBigAreaValue\nRectange: " + rect.toString() + "\nExpected: " + 
                            String.valueOf(expectedArea) + "\nActual: " + String.valueOf(actualArea) + "\n");

        // #2
        areaFinder = new MonteCarloAreaMethod(rect2.getMinX(), rect2.getMaxX(), rect2.getMinY(), rect2.getMaxY(), new Random());
        expectedArea = BigDecimal.valueOf(rect2.getHeight())
                        .multiply(BigDecimal.valueOf(rect2.getWidth()))
                        .doubleValue();
        actualArea = areaFinder.getBigAreaValue().doubleValue();
        if (Math.abs(expectedArea - actualArea) > 0.001) assertTrue(false);
        System.out.println("Additional Test info: testGetBigAreaValue\nRectange: " + rect2.toString() + "\nExpected: " + 
                            String.valueOf(expectedArea) + "\nActual: " + String.valueOf(actualArea) + "\n");
    }

    @Test 
    public void testGenerateRandomPointInBigArea() {
        Rectangle2D rect = new Rectangle2D(0, 0, 3, 2);
        MonteCarloAreaMethod areaFinder = 
                        new MonteCarloAreaMethod(rect.getMinX(), rect.getMaxX(), rect.getMinY(), rect.getMaxY(), new Random());
        for (int i = 0; i < 100; i++) {
            Point2D randPoint = areaFinder.generateRandomPointInBigArea();
            if (randPoint.getX() < rect.getMinX() | randPoint.getX() > rect.getMaxX()) assertTrue(false);
            if (randPoint.getY() < rect.getMinY() | randPoint.getY() > rect.getMaxY()) assertTrue(false);
        }

        Rectangle2D rect2 = new Rectangle2D(5, 3, 10, 17);
        areaFinder = new MonteCarloAreaMethod(rect2.getMinX(), rect2.getMaxX(), rect2.getMinY(), rect2.getMaxY(), new Random());
        for (int i = 0; i < 100; i++) {
            Point2D randPoint = areaFinder.generateRandomPointInBigArea();
            if (randPoint.getX() < rect2.getMinX() | randPoint.getX() > rect2.getMaxX()) assertTrue(false);
            if (randPoint.getY() < rect2.getMinY() | randPoint.getY() > rect2.getMaxY()) assertTrue(false);
        }
    }

    @Test 
    public void testIsPointInSearchArea() {

    }

    @Test 
    public void testGetAmountOfPointsInInnerArea() {

    }

    @Test
    public void testFindAreaValue_isWorking() {
        Rectangle2D rect = new Rectangle2D(0, -5, 10, 10);
        System.out.println("Outer rectangle: " + rect.toString());
        MonteCarloAreaMethod areaFinder = 
                        new MonteCarloAreaMethod(rect.getMinX(), rect.getMaxX(), rect.getMinY(), rect.getMaxY(), new Random());

        int amountOfExperiments = 10;
        for (int i = 1; i < amountOfExperiments + 1; i++) {
           
        }
    }

}
