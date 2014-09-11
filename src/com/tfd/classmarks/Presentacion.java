package com.tfd.classmarks;

import java.util.Timer;
import java.util.TimerTask;

import mysql.BaseDatos;
import mysql.ClaseCuatrimestres;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

public class Presentacion extends Activity {
	
	private Timer time = new Timer();
	private Intent in;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentacion_act);
		
		TextView txt = (TextView) findViewById(R.id.TVtfd);
		Typeface font = Typeface.createFromAsset(getAssets(), "TELE2.ttf");
		
		txt.setTypeface(font);
		txt.setTextColor(Color.BLACK);
		txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

		TimerTask tasko = new TimerTask() {
			
			@Override
			public void run() {
				in = new Intent(getApplicationContext(), Principal.class);
				
				BaseDatos cn = new BaseDatos(getApplicationContext());
				SQLiteDatabase db = cn.getWritableDatabase();

				ClaseCuatrimestres CT = new ClaseCuatrimestres();
				int i = 1;
				try {
					 i= cn.getClassMarks().getLon();
				}catch (Exception e) {
				}
				
				if (i  != 0){

				}else{
					CT.setCuatrimestre("Primero");
					cn.InsertarCuatrimestre(CT);
				}
				cn.closeDB();
				db.close();
				
				startActivity(in);
			}
		};	
		time.schedule(tasko, 1500);
	}
	
}
