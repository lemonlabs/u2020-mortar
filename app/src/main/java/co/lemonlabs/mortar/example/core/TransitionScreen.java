package co.lemonlabs.mortar.example.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import java.lang.reflect.Array;

import mortar.Blueprint;

public abstract class TransitionScreen extends Screen implements Blueprint, Parcelable {


    @Override public final int describeContents() {
        return 0;
    }

    @Override public final void writeToParcel(Parcel parcel, int i) {
        //noinspection unchecked
        SparseArray<Object> sparseArray = (SparseArray) getViewState();
        int[] transitions = getTransitions();
        parcel.writeSparseArray(sparseArray);
        parcel.writeIntArray(transitions);
        doWriteToParcel(parcel, i);
    }

    /**
     * Store additional parameters to parcel
     */
    protected void doWriteToParcel(Parcel parcel, int flags) {

    }

    public static <T extends TransitionScreen> Parcelable.Creator<T> CREATOR(final Class<T> screenClass) {
        return new ScreenCreator<T>() {
            @Override protected T doCreateFromParcel(Parcel source) {
                try {
                    return screenClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override public T[] newArray(int size) {
                //noinspection unchecked
                return (T[]) Array.newInstance(screenClass, size);
            }
        };
    }

    public static abstract class ScreenCreator<T extends TransitionScreen> implements Parcelable.Creator<T> {

        @Override public final T createFromParcel(Parcel source) {
            ClassLoader classLoader = getClassLoader();
            //noinspection unchecked
            SparseArray<Parcelable> sparseArray = source.readSparseArray(classLoader);
            int[] transitions = source.createIntArray();
            T created = doCreateFromParcel(source);
            created.setViewState(sparseArray);
            created.setTransitions(transitions);
            return created;
        }

        protected abstract T doCreateFromParcel(Parcel source);

        protected ClassLoader getClassLoader() {
            return TransitionScreen.class.getClassLoader();
        }
    }
}
