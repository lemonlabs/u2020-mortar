package co.lemonlabs.mortar.example.core;

import android.os.Parcelable;
import android.util.SparseArray;

public class Screen {

    private int[]                   transitions;
    private SparseArray<Parcelable> viewState;

    public final void setTransitions(int[] transitions) {
        this.transitions = transitions;
    }

    public final int[] getTransitions() { return transitions; }

    public final void setViewState(SparseArray<Parcelable> viewStateToSave) {
        viewState = viewStateToSave;
    }

    public final SparseArray<Parcelable> getViewState() {
        return viewState;
    }

}
