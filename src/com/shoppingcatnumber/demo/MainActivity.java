package com.shoppingcatnumber.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.shoppingcatnumber.demo.ProductNumberDialog.OnNumberListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProductNumberDialog dialog = new ProductNumberDialog(MainActivity.this);
				dialog.setOnNumberListener(1, 1.1, new OnNumberListener() {
					@Override
					public void onNumberSet(int number) {
					}
				});
				dialog.show();
			}
		});
    }
    
}
