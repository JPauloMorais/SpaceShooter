package br.jp.redsparrow.game.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import br.jp.redsparrow.R;
import br.jp.redsparrow.engine.core.game.GameView;
import br.jp.redsparrow.game.ReSpGame;

public class PlayActivity extends Activity implements OnTouchListener, SensorEventListener {

	private RelativeLayout mRelLayout;
	private GameView mGameView;
	private ReSpGame game;

	Sensor mSensorAccelerometer;
	SensorManager mSensorManager;

	private Button pauseButton;
	private TextView killPoints;

	//	private boolean rendererSet;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGameView = new GameView(this);
		game = new ReSpGame(this);

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo confInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supES2 = confInfo.reqGlEsVersion >= 0x20000;

		if(supES2){

			mGameView.setEGLContextClientVersion(2);

			mGameView.setOnTouchListener(this);
			mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
			mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);


			mGameView.setRenderer(game.getRenderer());
			mGameView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
			//			rendererSet = true;

		}else{
			Toast.makeText(this, " Aparelho n�o suporta OpenGL ES 2.0 ", Toast.LENGTH_LONG).show();
			return;
		}
		mRelLayout = new RelativeLayout(this);

		mRelLayout.addView(mGameView);

//		ammoDisplay = new Button(this);
//
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		layoutParams.addRule(RelativeLayout.ALIGN_TOP);
//		ammoDisplay.setLayoutParams(layoutParams);
//		ammoDisplay.setBackgroundResource(R.drawable.ammo_display_test);
//		ammoDisplay.setAlpha(0.2f);
//		ammoDisplay.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getApplication(), "Click", Toast.LENGTH_SHORT).show();
//				if(isExStorageWritable()) {
//				}
//			}
//		});
//		mRelLayout.addView(ammoDisplay);

		pauseButton = new Button(this);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		pauseButton.setLayoutParams(layoutParams);
		pauseButton.setBackgroundResource(R.drawable.pause_buton);
		pauseButton.setAlpha(0.5f);
		pauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( !game.getWorld().isPaused() ) {
					game.getWorld().pause();
					pauseButton.setBackgroundResource(R.drawable.play_button_v1);
					killPoints.setVisibility(View.GONE);
//					pauseButton.setX();

				}
				else {
					game.getWorld().resume();
					pauseButton.setBackgroundResource(R.drawable.pause_buton);
					pauseButton.setAlpha(0.5f);
					killPoints.setVisibility(View.VISIBLE);
				}
			}
		});
		mRelLayout.addView(pauseButton);
		
		killPoints = new TextView(this);
		
		layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		killPoints.setLayoutParams(layoutParams);
		killPoints.setBackgroundResource(R.drawable.kill_points);
		killPoints.setText("     "+0);
		killPoints.setAlpha(0.5f);
//		killPoints.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
		mRelLayout.addView(killPoints);

		

		setContentView(mRelLayout);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	}
	
	public void setPoints(final int points) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				killPoints.setText("     "+points);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGameView.onResume();
		game.resume();
		mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGameView.onPause();
		game.pause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		game.stop();
		mSensorManager.unregisterListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ogl, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		final SensorEvent se = event;

		if(event.values!=null) mGameView.queueEvent(new Runnable() {
			@Override
			public void run() {
				if(game!=null) game.getInputHandler().handleSensorChange(se.values);
			}
		});
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.performClick();
		if(event!=null && !game.getWorld().isPaused()){

			final float normalizedX = (event.getX()/(float)v.getWidth())*2-1;
			final float normalizedY = -((event.getY()/(float)v.getHeight())*2-1);

			if(event.getAction()==MotionEvent.ACTION_DOWN || event.getAction()==MotionEvent.ACTION_POINTER_DOWN){
				mGameView.queueEvent(new Runnable() {
					@Override
					public void run() {
						game.getInputHandler().handleTouchPress(normalizedX,normalizedY);
					}
				});
			}else if(event.getAction()==MotionEvent.ACTION_UP){
				mGameView.queueEvent(new Runnable() {
					@Override
					public void run() {
						game.getInputHandler().handleTouchRelease(normalizedX,normalizedY);
					}
				});
			}else if(event.getAction()==MotionEvent.ACTION_MOVE){
				mGameView.queueEvent(new Runnable() {
					@Override
					public void run() {
						game.getInputHandler().handleTouchDrag(normalizedX,normalizedY);
					}
				});
			}
			return true;
		}else return false;
	}

	public boolean isExStorageWritable() {
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
}
