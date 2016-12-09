package com.jnj.hackthon;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jnj.hackthon.task.AppTaskExecutor;

public class ControlActivity extends AppCompatActivity implements ControlView {

    private static final long DELAY_TIME = 2500;
    private ControlPresenter mPresenter;
    private Handler mHandler;
    private Runnable radarAction;
    private TextView mRadar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        mHandler = new Handler();

        mPresenter = new ControlPresenter(this, new ControlBO(), new AppTaskExecutor(this));
        final View up = findViewById(R.id.up);
        final View down = findViewById(R.id.down);
        final View left = findViewById(R.id.left);
        final View right = findViewById(R.id.right);
        mRadar = (TextView) findViewById(R.id.radar);


        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.onUpClicked();
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.onDownClicked();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.onLeftClicked();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.onRightClicked();
            }
        });
        radarAction = new Runnable() {
            @Override
            public void run() {
                mPresenter.radar();
                mRadar.postDelayed(radarAction, DELAY_TIME);
            }
        };
        mRadar.postDelayed(radarAction, DELAY_TIME);


    }

    @Override
    public void updateRadar(final RadarResponse result) {
        mRadar.setText("Distancia = "+result.radar);
    }


}
