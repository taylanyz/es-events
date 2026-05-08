package com.eskisehir.eventapi.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ThompsonSamplingStrategy {

    private Random random = new Random();

    public static class ArmStatistics {
        public double alpha;
        public double beta;
        public int clicks;
        public int impressions;

        public ArmStatistics() {
            this.alpha = 1.0;
            this.beta = 1.0;
            this.clicks = 0;
            this.impressions = 0;
        }
    }

    public int selectArm(List<ArmStatistics> arms) {
        if (arms.isEmpty()) return 0;

        int bestArm = 0;
        double bestScore = -1;

        for (int i = 0; i < arms.size(); i++) {
            double theta = sampleBeta(arms.get(i).alpha, arms.get(i).beta);
            if (theta > bestScore) {
                bestScore = theta;
                bestArm = i;
            }
        }
        return bestArm;
    }

    private double sampleBeta(double alpha, double beta) {
        double x = sampleGamma(alpha);
        double y = sampleGamma(beta);
        return x / (x + y);
    }

    private double sampleGamma(double shape) {
        if (shape < 1.0) shape += 1;
        double d = shape - 1.0 / 3.0;
        double c = 1.0 / Math.sqrt(9.0 * d);

        while (true) {
            double z = random.nextGaussian();
            double v = 1.0 + c * z;
            if (v > 0) {
                v = v * v * v;
                double u = random.nextDouble();
                if (u < 1.0 - 0.0331 * z * z * z * z) {
                    return d * v;
                }
                if (Math.log(u) < 0.5 * z * z + d * (1.0 - v + Math.log(v))) {
                    return d * v;
                }
            }
        }
    }

    public void updateArm(ArmStatistics arm, boolean clicked) {
        arm.impressions++;
        if (clicked) {
            arm.clicks++;
            arm.alpha++;
        } else {
            arm.beta++;
        }
    }

    public double getConvergenceScore(ArmStatistics arm) {
        return arm.clicks / (double) Math.max(1, arm.impressions);
    }
}
