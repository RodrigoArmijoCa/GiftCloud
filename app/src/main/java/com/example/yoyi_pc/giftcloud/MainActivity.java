package com.example.yoyi_pc.giftcloud;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> listaDeAtributos = new ArrayList<String>(Arrays.asList("giroscopio", "magnetometro", "sensorluz", "acelerometro", "barometro", "humedad"));
        final JSONObject JSONaEnviar = crearJSON(listaDeAtributos);
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                //String variable = String.valueOf(JSONaEnviar.length());
                int variable = 0;
                try {
                    variable = JSONaEnviar.toString().getBytes("UTF-8").length;

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d("El largo del JSON es: ", String.valueOf(variable));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnableCode);
        mSensorListener = new SensorEventListener()
        {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event)
            {
                Sensor sensor = event.sensor;
                if (sensor.getType() == Sensor.TYPE_GYROSCOPE)
                {
                    TextView giroscopio = (TextView) findViewById(R.id.giroscopio);
                    String texto = event.values[0] + "," + event.values[1] + "," + event.values[2];
                    giroscopio.setText(texto);
                    editarElementoEnJSON(JSONaEnviar, "giroscopio", texto);
                }
                else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                {
                    TextView magnetometro = (TextView) findViewById(R.id.magnetometro);
                    magnetometro.setText(String.valueOf(event.values[0]));
                    editarElementoEnJSON(JSONaEnviar, "magnetometro", String.valueOf(event.values[0]));
                }
                else if (sensor.getType() == Sensor.TYPE_LIGHT)
                {
                    TextView sensorluz = (TextView) findViewById(R.id.sensorluz);
                    sensorluz.setText(String.valueOf(event.values[0]));
                    editarElementoEnJSON(JSONaEnviar, "sensorluz", String.valueOf(event.values[0]));
                }
                else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                {
                    TextView acelerometro = (TextView) findViewById(R.id.acelerometro);
                    acelerometro.setText(String.valueOf(event.values[0]));
                    editarElementoEnJSON(JSONaEnviar, "acelerometro", String.valueOf(event.values[0]));
                }
                else if (sensor.getType() == Sensor.TYPE_PRESSURE)
                {
                    TextView barometro = (TextView) findViewById(R.id.barometro);
                    barometro.setText(String.valueOf(event.values[0]));
                    editarElementoEnJSON(JSONaEnviar, "barometro", String.valueOf(event.values[0]));
                }
                else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY)
                {
                    TextView humedad = (TextView) findViewById(R.id.humedad);
                    humedad.setText(String.valueOf(event.values[0]));
                    editarElementoEnJSON(JSONaEnviar, "humedad", String.valueOf(event.values[0]));
                }
            }
        };
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_GAME);
    }

    public JSONObject crearJSON( ArrayList<String> listaDeAtributos)
    {
        JSONObject JSONaEnviar = new JSONObject();
        int i;
        for(i=0; i<listaDeAtributos.size();i++)
        {
            insertarElementoEnJSON(JSONaEnviar, listaDeAtributos.get(i), "");
        }
        return JSONaEnviar;
    }

    public void insertarElementoEnJSON(JSONObject JSONAEditar, String nombreAtributo, String valorAtributo)
    {
        try
        {
            JSONAEditar.put(nombreAtributo, valorAtributo);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void editarElementoEnJSON(JSONObject JSONAEditar, String nombreAtributo, String valorAtributo)
    {
        try
        {
            JSONAEditar.remove(nombreAtributo);
            JSONAEditar.put(nombreAtributo, valorAtributo);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
