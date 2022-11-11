package com.example.a3_cmpt381.model.interaction_model;
/* state changes to the model.
    Instead of either:
    a) updating everything in the view naively every time something changes (unoptimized )
    b) inferring what was changed from the many state variables             (hard to read)
    I instead opted to store an enum for every state change the view needs to worry about.
    It makes the modelChanged() code more understandable, and fast because switch statements are O(1)
 */
public enum ModelTransition {
    NONE,
    ADD_NODE,
    END_LINKING,
    DELETE,
    SELECT,
    DESELECT,
    DRAGGING_NODE,
    DRAGGING_LINK,
    UPDATE_LINKING,
    UPDATE_PANNING,
    UPDATE_TEXT
}
