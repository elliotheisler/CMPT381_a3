package com.example.a3_cmpt381.model;

public class InteractionModel {
    private enum InteractionState { POINT, MOVE, LINK };
    private InteractionState state = InteractionState.POINT;
}
