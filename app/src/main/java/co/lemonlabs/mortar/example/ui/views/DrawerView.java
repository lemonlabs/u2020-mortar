package co.lemonlabs.mortar.example.ui.views;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CoreView;
import co.lemonlabs.mortar.example.ui.screens.DrawerScreen;
import mortar.Mortar;

public class DrawerView extends ListView {

    @Inject DrawerScreen.Presenter presenter;

    private int mCurrentSelectedPosition = 0;

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        presenter.takeView(this);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        setItemChecked(position, true);

        if (getParent() != null && getParent() instanceof DrawerLayout) {
            ((CoreView) getParent()).closeDrawer(this);
            presenter.goToScreenAtPosition(position);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void setListAdapter() {
        setAdapter(new ArrayAdapter<>(
            getContext(),
            android.R.layout.simple_list_item_activated_1,
            android.R.id.text1,
            getContext().getResources().getStringArray(R.array.drawer_items)
        ));
    }
}
