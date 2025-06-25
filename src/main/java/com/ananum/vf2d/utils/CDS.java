/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.vf2d.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author JD
 */

/**
 * Implémentation du stockage par diagonales compressées (Compressed Diagonal Storage - CDS).
 * Ce format est efficace pour les matrices "bandes" où les éléments non nuls 
 * sont concentrés le long de quelques diagonales.
 * 
 * La matrice de l'opérateur de Laplace discrétisé sur un domaine rectangulaire est 
 * une matrice à 5 diagonales, ce qui en fait un candidat parfait pour ce format.
 *
 * @author JD
 */
public class CDS {

    private final int numRows;
    private final int numCols;
    private final double[][] diagValues; // Stocke les valeurs des diagonales
    private final int[] offsets;         // Stocke les décalages des diagonales par rapport à la diagonale principale

    /**
     * Construit une représentation CDS à partir d'une matrice dense.
     * Cette méthode identifie automatiquement les diagonales non nulles.
     *
     * @param matrix La matrice dense à convertir.
     */
    public CDS(double[][] matrix) {
        this.numRows = matrix.length;
        this.numCols = matrix[0].length;

        // Utilise une map pour trouver dynamiquement les diagonales non nulles
        Map<Integer, List<Double>> diagonals = new HashMap<>();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (matrix[i][j] != 0.0) {
                    int offset = j - i;
                    diagonals.computeIfAbsent(offset, k -> new ArrayList<>(Collections.nCopies(numRows, 0.0)))
                             .set(i, matrix[i][j]);
                }
            }
        }
        
        // Convertit la map en tableaux pour un accès plus rapide
        List<Integer> sortedOffsets = new ArrayList<>(diagonals.keySet());
        Collections.sort(sortedOffsets);

        this.offsets = sortedOffsets.stream().mapToInt(Integer::intValue).toArray();
        this.diagValues = new double[this.offsets.length][this.numRows];

        for (int i = 0; i < this.offsets.length; i++) {
            int offset = this.offsets[i];
            List<Double> values = diagonals.get(offset);
            for (int j = 0; j < values.size(); j++) {
                this.diagValues[i][j] = values.get(j);
            }
        }
    }
    
    /**
     * Récupère la valeur à la position (row, col).
     * @param row L'indice de la ligne.
     * @param col L'indice de la colonne.
     * @return La valeur de la matrice à (row, col). Retourne 0 si l'élément n'est pas sur une diagonale stockée.
     */
    public double get(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            throw new IndexOutOfBoundsException("Index (" + row + ", " + col + ") hors limites.");
        }
        for (int i = 0; i < offsets.length; i++) {
            if (col - row == offsets[i]) {
                return diagValues[i][row];
            }
        }
        return 0.0;
    }

    /**
     * Effectue la multiplication matrice-vecteur (A * x).
     *
     * @param vector Le vecteur à multiplier.
     * @return Le vecteur résultat.
     */
    public double[] timesVector(double[] vector) {
        if (vector.length != numCols) {
            throw new IllegalArgumentException("La dimension du vecteur (" + vector.length + ") est incompatible avec celle de la matrice (" + numCols + ").");
        }
        double[] result = new double[numRows];
        for (int i = 0; i < offsets.length; i++) {
            int offset = offsets[i];
            for (int j = 0; j < numRows; j++) {
                int col = j + offset;
                if (col >= 0 && col < numCols) {
                    result[j] += diagValues[i][j] * vector[col];
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CDS Matrix (").append(numRows).append("x").append(numCols).append(")\n");
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                sb.append(String.format("%8.2f", get(i, j)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
