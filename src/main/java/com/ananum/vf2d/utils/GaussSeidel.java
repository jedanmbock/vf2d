/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.vf2d.utils;

/**
 *
 * @author JD
 */
public class GaussSeidel {

    public static double[] resolver(double[][] A, double[] b, double[] x0, double tolerance, int maxIterations) {
        int n = b.length;
        double[] x = x0.clone();
        double[] x_old = new double[n];
        int iteration = 0;
        double error = tolerance + 1;

        while (error > tolerance && iteration < maxIterations) {
            System.arraycopy(x, 0, x_old, 0, n);
            for (int i = 0; i < n; i++) {
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        sum += A[i][j] * x[j];
                    }
                }
                x[i] = (b[i] - sum) / A[i][i];
            }
            error = calculerErreur(x, x_old);
            iteration++;
        }

        if (iteration == maxIterations) {
            System.out.println("La méthode n'a pas convergé après " + maxIterations + " itérations.");
        }
        return x;
    }

    private static double calculerErreur(double[] x, double[] x_old) {
        double maxDiff = 0;
        for (int i = 0; i < x.length; i++) {
            maxDiff = Math.max(maxDiff, Math.abs(x[i] - x_old[i]));
        }
        return maxDiff;
    }

//    public static void main(String[] args) {
//        // Exemple d'utilisation
//        double[][] A = {
//                {4, -1, 0},
//                {-1, 4, -1},
//                {0, -1, 4}
//        };
//        double[] b = {3, 1, 3};
//        double[] x0 = {0, 0, 0};
//        double tolerance = 1e-6;
//        int maxIterations = 100;
//
//        double[] solution = resolver(A, b, x0, tolerance, maxIterations);
//
//        System.out.println("Solution:");
//        for (double val : solution) {
//            System.out.println(val);
//        }
//    }
}
