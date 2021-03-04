package com.example.andoid.filmhub.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.andoid.filmhub.R;

public class SplashActivity extends AppCompatActivity {


    public static int SPLASH_TIME_OUT = 2000;
    public static int SPLASH_TIME_OUT_ERROR = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        internetResponseValidation();

    }

    /**
     * Check internet conectivity
     **/
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    /**
     * Internet response validation and actions
     **/
    private void internetResponseValidation() {
        if (isNetworkConnected()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Start intent if response to internet = true
                    Intent MainActivity = new Intent(SplashActivity.this, com.example.andoid.filmhub.MainActivity.class);
                    startActivity(MainActivity);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Display dialog if there is no internet connection
                    showWarningDialog();
                }
            }, SPLASH_TIME_OUT_ERROR);
        }
    }

    /**
     * Dialog to display if there is no internet connection
     **/
    private void showWarningDialog() {
        // Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.splash_error, (ConstraintLayout) findViewById(R.id.dialogContainerLayout));
        builder.setView(view);
        // Setting the content of the dialog
        ((TextView) view.findViewById(R.id.warningHeaderTextView)).setText(getResources().getString(R.string.dialog_warning_title));
        ((TextView) view.findViewById(R.id.warningContentTextView)).setText(getResources().getString(R.string.dialog_warning_message));
        ((Button) view.findViewById(R.id.warningButtonAction)).setText(getResources().getString(R.string.dialog_warning_button));
        ((ImageView) view.findViewById(R.id.warningHeaderImageIcon)).setImageResource(R.drawable.ic_warning);

        AlertDialog alertDialog = builder.create();
        // Setting the button action
        view.findViewById(R.id.warningButtonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivity = new Intent(SplashActivity.this, com.example.andoid.filmhub.MainActivity.class);
                startActivity(MainActivity);
                finish();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}
