package com.example.cs522_hw3;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;


import com.example.cs522_hw3.TextViewUndoRedo;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener, SensorEventListener {

    private EditText editTextMaterial;

    // System sensor manager instance.
    private SensorManager mSensorManager;

    // Accelerometer sensors
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;

    private TextViewUndoRedo helper;

    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];

    private TextView mTextSensorRoll;

    private Display mDisplay;

    private static final float VALUE_DRIFT = 0.05f;

    private Boolean canUndoRedo = true;

    private ProgressBar undoProgress;
    private ProgressBar redoProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button undoBtn = findViewById(R.id.button);
        //Button redoBtn = findViewById(R.id.button2);
        editTextMaterial = (EditText) findViewById(R.id.editTextMaterial);
        //mTextSensorRoll = (TextView) findViewById(R.id.txt_roll);
        undoProgress = (ProgressBar) findViewById(R.id.undoProgress);
        redoProgress = (ProgressBar) findViewById(R.id.redoProgress);

        helper = new TextViewUndoRedo(editTextMaterial);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(mSensorManager);

        mSensorAccelerometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_MAGNETIC_FIELD);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();

//        undoBtn.setOnClickListener(view -> {
//            //Toast.makeText(this,"Hello", Toast.LENGTH_LONG).show();
//            helper.undo();
//        });
//
//        redoBtn.setOnClickListener(view -> {
//            helper.redo();
//        });


    }

    @Override
    public void hearShake() {
        Toast.makeText(this, "Text Cleared! ", Toast.LENGTH_SHORT).show();
        editTextMaterial.getText().clear();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = sensorEvent.values.clone();
                break;
            default:
                return;
        }


        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);

        // Remap the matrix based on current device/activity rotation.
        float[] rotationMatrixAdjusted = new float[9];
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                rotationMatrixAdjusted = rotationMatrix.clone();
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X,
                        rotationMatrixAdjusted);
                break;
            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y,
                        rotationMatrixAdjusted);
                break;
            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X,
                        rotationMatrixAdjusted);
                break;
        }

        float orientationValues[] = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrixAdjusted,
                    orientationValues);
        }

        float roll = orientationValues[2];

        if (Math.abs(roll) < VALUE_DRIFT) {
            roll = 0;
        }
        int rollProgress = (int) (roll * 100);

        if(rollProgress >= 0)
            redoProgress.setProgress(rollProgress);
        if (rollProgress <= 0)
            undoProgress.setProgress(rollProgress * -1);

//        mTextSensorRoll.setText(getResources().getString(
//                R.string.value_format, roll));

        if(roll > 0.9f && canUndoRedo){
            helper.redo();
            canUndoRedo = false;
        }
        else if (roll < -0.9f && canUndoRedo){
            helper.undo();
            canUndoRedo = false;
        }


        if(roll == 0.0f){
            canUndoRedo = true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * Listeners for the sensors are registered in this callback so that
     * they can be unregistered in onStop().
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Listeners for the sensors are registered in this callback and
        // can be unregistered in onStop().

        if (mSensorAccelerometer != null) {
            mSensorManager.registerListener(this, mSensorAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorMagnetometer != null) {
            mSensorManager.registerListener(this, mSensorMagnetometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unregister all sensor listeners in this callback so they don't
        // continue to use resources when the app is stopped.
        mSensorManager.unregisterListener(this);
    }
}
