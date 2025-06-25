/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.vf2d.functions;

import com.ananum.vf2d.utils.Function;

/**
 *
 * @author JD
 */
public class F1 implements Function{

    @Override
    public double u(double x, double y) {
        return x*x + y*y; 
    }

    @Override
    public double f(double x, double y) {
        return -4.0; 
    }
    
}
