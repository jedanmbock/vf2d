/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.vf2d.gui;

import com.ananum.vf2d.functions.F2;
import com.ananum.vf2d.method.FiniteVolumeSolver;
import com.ananum.vf2d.utils.Function;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/**
 *
 * @author JD
 */

public class ConvergenceAnalysis {

    public static void main(String[] args) {
        // Définir la fonction à tester
        Function functionToTest = new F2();

        // Définir les tailles de maillage à tester (n x n)
        int[] meshSizes = {5, 10, 20, 40, 80}; 
        
        List<Double> hValues = new ArrayList<>();
        List<Double> errors = new ArrayList<>();

        System.out.println("Analyse de la convergence pour la méthode des différences finies");
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-10s %-15s %-15s %-15s\n", "N", "h", "Erreur (max)", "Ordre (p)");
        System.out.println("----------------------------------------------------------");

        double previousError = -1;
        double previousH = -1;

        for (int n : meshSizes) {
            // Pour simplifier, on prend un maillage carré m=n
            int m = n;
            FiniteVolumeSolver solver = new FiniteVolumeSolver(n, m, functionToTest);

            // Résoudre le système
            double[] uNumerical = solver.solve();
            double[] uExact = solver.computeExact();

            // Calculer l'erreur maximale (norme L-infini)
            double maxError = 0;
            for (int i = 0; i < n * m; i++) {
                maxError = Math.max(maxError, Math.abs(uNumerical[i] - uExact[i]));
            }

            double h = 1.0 / (n + 1);
            hValues.add(h);
            errors.add(maxError);
            
            String orderStr = "-";
            if (previousError > 0) {
                // Calculer l'ordre de convergence p: p = log(E_prev / E_curr) / log(h_prev / h_curr)
                double order = Math.log(previousError / maxError) / Math.log(previousH / h);
                orderStr = String.format("%.4f", order);
            }

            System.out.printf("%-10d %-15.5e %-15.5e %-15s\n", n, h, maxError, orderStr);

            previousError = maxError;
            previousH = h;
        }
        System.out.println("----------------------------------------------------------");

        // Créer et afficher le graphique
        showErrorChart(hValues, errors);
    }

    private static void showErrorChart(List<Double> hValues, List<Double> errors) {
        XYSeries series = new XYSeries("Erreur maximale");
        for (int i = 0; i < hValues.size(); i++) {
            series.add(hValues.get(i), errors.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Convergence de la Méthode des Différences Finies", // Titre du graphique
                "Pas du maillage (h)",    // Label axe X
                "Erreur Maximale",        // Label axe Y
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Mettre les deux axes en échelle logarithmique
        XYPlot plot = chart.getXYPlot();
        LogarithmicAxis domainAxis = new LogarithmicAxis("Pas du maillage (h)");
        LogarithmicAxis rangeAxis = new LogarithmicAxis("Erreur Maximale");
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
        
        // Afficher dans une fenêtre
        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame("Graphique de Convergence");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
