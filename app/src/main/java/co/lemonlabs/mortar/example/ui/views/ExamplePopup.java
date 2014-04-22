package co.lemonlabs.mortar.example.ui.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import co.lemonlabs.mortar.example.ui.views.data.ExamplePopupData;
import mortar.Popup;
import mortar.PopupPresenter;

public class ExamplePopup implements Popup<ExamplePopupData, Boolean> {

    private final Context context;

    private AlertDialog dialog;

    public ExamplePopup(Context context) {
        this.context = context;
    }

    @Override
    public void show(ExamplePopupData info, boolean withFlourish, final PopupPresenter<ExamplePopupData, Boolean> presenter) {
        dialog = new AlertDialog.Builder(context).setTitle("Example")
            .setMessage(info.content)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int which) {
                    dialog = null;
                    presenter.onDismissed(true);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int which) {
                    dialog = null;
                    presenter.onDismissed(false);
                }
            })
            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override public void onCancel(DialogInterface d) {
                    dialog = null;
                    presenter.onDismissed(false);
                }
            })
            .show();
    }

    @Override public boolean isShowing() {
        return dialog != null;
    }

    @Override public void dismiss(boolean withFlourish) {
        dialog.dismiss();
        dialog = null;
    }

    @Override public Context getContext() {
        return context;
    }

}
