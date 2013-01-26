package me.yongbo.sdk;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
	public void open(View view){
		PushManager.init(this).push();
    }
	public void open2(View view){
		AppOfferManager.init(this).showWall();
	}
}
