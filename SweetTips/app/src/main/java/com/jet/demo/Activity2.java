package com.jet.demo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jet.sweettips.toast.SweetToast;
import com.jet.sweettips.util.SnackbarUtils;

public class Activity2 extends AppCompatActivity{

    private Button bt = null;
    private FloatingActionButton fab = null;
    private int positionIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_2);
        bt = (Button) findViewById(R.id.bt);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionIndex++;
                switch (positionIndex%5){
                    case 0:
                        SnackbarUtils.Long(fab,"Snackbar位置:顶部").gravityCoordinatorLayout(Gravity.TOP).show();
                        break;
                    case 1:
                        SnackbarUtils.Long(fab,"Snackbar位置:底部").gravityCoordinatorLayout(Gravity.BOTTOM).show();
                        break;
                    case 2:
                        SnackbarUtils.Long(fab,"Snackbar位置:居中").gravityCoordinatorLayout(Gravity.CENTER).show();
                        break;
                    case 3:
                        SnackbarUtils.Long(fab,"Snackbar位置:上方").aboveCoordinatorLayout(bt,0,0,0).show();
                        break;
                    case 4:
                        SnackbarUtils.Long(fab,"Snackbar位置:下方").bellowCoordinatorLayout(bt,0,0,0).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
