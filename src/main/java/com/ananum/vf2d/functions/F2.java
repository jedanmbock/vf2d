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
public class F2 implements Function{

    @Override
    public double u(double x, double y) {
        return Math.sin(Math.PI*x); 
    }

    @Override
    public double f(double x, double y) {
        return Math.PI*Math.PI*Math.sin(Math.PI*x); 
    }
    
}
