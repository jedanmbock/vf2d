/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.vf2d.method;

import com.ananum.vf2d.utils.Function;
import com.ananum.vf2d.utils.Gauss;

/**
 *
 * @author JD
 */
public class FiniteVolumeSolver {
    private int n, m;
    private double h, k;
    private double[] li_demi, hj_demi;
    Function funct;
    

    public FiniteVolumeSolver(int n, int m, Function funct) {
        this.n = n;
        this.m = m;
        this.h = 1.0 / n;
        this.k = 1.0/ m;
        this.funct = funct;
        li_demi = new double[n+1];
        hj_demi = new double[m+1];
        for(int i = 1; i<n; i++)
            li_demi[i] = h;
        li_demi[0] = h/2;
        li_demi[n] = h/2;
        for(int j = 1; j<m; j++)
            hj_demi[j] = k;
        hj_demi[0] = k/2;
        hj_demi[m] = k/2;
    }

    public double[] solve() {
        int N = n * m;
        double[][] A = new double[N][N];
        double[] b = new double[N];

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                
                double ih_plus = h*(1/hj_demi[j+1]), ik_plus = k*(1/li_demi[i+1]), ih_moins = h*(1/hj_demi[j]), ik_moins = k*(1/li_demi[i]);
                int idx = j * n + i;
                A[idx][idx] = ih_plus + ih_moins + ik_plus + ik_moins;
                if (i > 0) A[idx][idx - 1] = -ih_moins;
                else       b[idx] += ih_moins*funct.u((i) * h, (j + 1/2) * k); // gauche
                if (i < n - 1) A[idx][idx + 1] = -ih_plus;
                else           b[idx] += ih_plus*funct.u((i + 1) * h, (j + 1/2) * k); // droite
                if (j > 0) A[idx][idx - n] = -ik_moins;
                else       b[idx] += ik_moins*funct.u((i + 1/2) * h, (j) * k); // bas
                if (j < m - 1) A[idx][idx + n] = -ik_plus;
                else           b[idx] += ik_plus*funct.u((i + 1/2) * h, (j + 1) * k); // haut

                b[idx] += funct.f((i+1/2)*h,(j+1/2)*k)*h*k;  
            }
        }

        return Gauss.gaussianElimination(A, b);
    }
    
     public double[] computeExact() {
        double[] u = new double[n * m];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                double x = (i + 1/2) * h;
                double y = (j + 1/2) * k;
                u[j * n + i] = funct.u(x, y);
            }
        }
        return u;
    }
}
