package co.lemonlabs.mortar.example.ui.views.data;


import android.os.Parcel;
import android.os.Parcelable;

public class ExamplePopupData implements Parcelable {

    public final String content;

    public ExamplePopupData(String content) {this.content = content;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object)this).getClass() != o.getClass()) return false;

        ExamplePopupData that = (ExamplePopupData) o;

        return content.equals(that.content);
    }

    private static final int HASH_PRIME = 37;
    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = HASH_PRIME * result + content.hashCode();
        return result;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
    }

    @SuppressWarnings("UnusedDeclaration")
    public static final Creator<ExamplePopupData> CREATOR = new Creator<ExamplePopupData>() {
        @Override public ExamplePopupData createFromParcel(Parcel parcel) {
            return new ExamplePopupData(parcel.readString());
        }

        @Override public ExamplePopupData[] newArray(int size) {
            return new ExamplePopupData[size];
        }
    };
}