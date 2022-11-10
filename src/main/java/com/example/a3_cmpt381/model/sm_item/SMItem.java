package com.example.a3_cmpt381.model.sm_item;


public abstract class SMItem extends CustomRectangle {
    public static final double WIDTH = 0;
    public static final double HEIGHT = 0;
    public SMItemType type;
    public SMItem(double v, double v1, double v2, double v3) {
        super(v, v1, v2, v3);
    }
}
