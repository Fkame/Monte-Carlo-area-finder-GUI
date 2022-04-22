package app.monte_carlo_area_finder;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class TestMonteCarloSupport {
    
    @Test
    public void testDoubleGeneration() {
        Random rand = new Random();
        int experiments = 20;
        int repeats = 50;
        while (experiments > 0) {
            double leftLimit = 1;
            double rightLimit = 0;
            while (leftLimit > rightLimit) {
                leftLimit = 10 * rand.nextDouble() + 0;
                rightLimit = 10 * rand.nextDouble() + 0;
            }
            System.out.println(String.format("Exp#%d, leftLimit=%.3f, rightLimit=%.3f", experiments, leftLimit, rightLimit));
            for (int i = 0; i < repeats; i++) {
                double randValue = MonteCarloSupport.generateDoubleInInterval(leftLimit, rightLimit, 5);
                System.out.println(String.format("\tRepeat#%d, randValue=%.3f", i, randValue));
                assertTrue(randValue <= rightLimit & randValue >= leftLimit);
            }
            experiments -= 1;
        }
    }

    @Test
    public void testIntGeneration() {
        //System.out.println("TestIntGeneration. Experiments = 20, repeats on each exp = 50");
        Random rand = new Random();
        int experiments = 20;
        int repeats = 50;
        while (experiments > 0) {
            int leftLimit = 1;
            int rightLimit = 0;
            while (leftLimit > rightLimit) {
                leftLimit = rand.nextInt(100) + 0;
                rightLimit = rand.nextInt(100) + 0;
            }
            //System.out.println(String.format("Exp#%d, leftLimit=%d, rightLimit=%d", experiments, leftLimit, rightLimit));
            for (int i = 0; i < repeats; i++) {
                int randValue = MonteCarloSupport.generateIntInInterval(leftLimit, rightLimit);
                //System.out.println(String.format("\tRepeat#%d, randValue=%d", i, randValue));
                assertTrue(randValue <= rightLimit & randValue >= leftLimit);
            }
            experiments -= 1;
        }
    }
}
