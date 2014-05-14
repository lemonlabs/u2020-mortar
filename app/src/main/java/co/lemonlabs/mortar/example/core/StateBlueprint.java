package co.lemonlabs.mortar.example.core;

import android.os.Parcelable;
import android.util.SparseArray;

import mortar.Blueprint;

public interface StateBlueprint extends Blueprint {

    public void setViewState(SparseArray<Parcelable> viewState);

    public void setTransitions(int[] transitions);
    public int[] getTransitions();

}
