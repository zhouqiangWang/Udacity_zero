package com.udacity.zhouq.zero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PortalActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_portal);
    findViewById(R.id.btn_pop_movie).setOnClickListener(btnClickListener);
    findViewById(R.id.btn_stock_hawk).setOnClickListener(btnClickListener);
    findViewById(R.id.btn_build_bigger).setOnClickListener(btnClickListener);
    findViewById(R.id.btn_material_app).setOnClickListener(btnClickListener);
    findViewById(R.id.btn_go_ubiquitous).setOnClickListener(btnClickListener);
    findViewById(R.id.btn_capstone).setOnClickListener(btnClickListener);
  }
  View.OnClickListener btnClickListener = new View.OnClickListener() {
    @Override public void onClick(View view) {
      String message = String.format(getString(R.string.toast_description), ((Button)view).getText().toString());
      Toast.makeText(PortalActivity.this, message, Toast.LENGTH_LONG).show();
    }
  };
}
