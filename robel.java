package com.example.JU_NAVIGATOR.JU_NAVIGATOR;

import com.esri.android.map.GraphicsLayer;

import java.io.Serializable;

/**
 * Created by asanti on 5/26/2015.
 */
public class robel implements Serializable {
    GraphicsLayer g1;
    robel(MainActivity m2){
        g1=m2.mGraphicsLayer;
    }
}