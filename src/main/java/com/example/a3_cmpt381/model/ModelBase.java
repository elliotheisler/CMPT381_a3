package com.example.a3_cmpt381.model;

import com.example.a3_cmpt381.view.ModelListener;

import java.time.Month;
import java.util.ArrayList;

/* ModelBase: everything common to Model and InteractionModel goes here.
 */
public abstract class ModelBase {
    protected ArrayList<ModelListener> subscribers = new ArrayList(1);

    public void addSubscribers(ModelListener... newSubcribers) {
        for (ModelListener m : newSubcribers) {
            subscribers.add(m);
        }
    }

    public void notifySubscribers() {
        subscribers.forEach( s -> s.modelChanged(getClass()) );
    }
}
