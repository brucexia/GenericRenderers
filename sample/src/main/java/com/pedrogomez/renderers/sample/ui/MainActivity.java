package com.pedrogomez.renderers.sample.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.pedrogomez.renderers.sample.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ListViewActivity for the Renderers demo.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_open_rv_simple)
    public void openRecyclerViewSample() {
        open(SimpleRecyclerViewActivity.class);
    }

    @OnClick(R.id.bt_open_rv_advanced)
    public void openRecyclerViewAdvanced() {
        open(AdvancedRecyclerViewActivity.class);
    }
    @OnClick(R.id.bt_open_rv_complex)
    public void openRecyclerViewComplex() {
        open(ComplexRecyclerViewActivity.class);
    }

    private void open(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
