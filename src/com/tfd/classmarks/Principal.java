package com.tfd.classmarks;

import java.util.ArrayList;
import java.util.List;

import mysql.BaseDatos;
import mysql.ClaseAsignaturas;
import mysql.ClaseClassMarks;
import mysql.ClaseCuatrimestres;
import mysql.ClaseNotas;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Principal extends FragmentActivity implements FragmentProvider {
	//Declaramos las variables.
	ImageView ico, sub;
	TextView mAdd, txtsinasig;
	RelativeLayout resul;
	Intent in;
	Spinner spinner;
	int IDmodif;
    ViewPager mPager;
    FragmentAsig And = new FragmentAsig();
    FragmentAsig Ando;

    MyPagerAdapter mAdapter;

    ArrayList<Fragment> frags = new ArrayList<Fragment>();
  
	public void setIDmodif(int id){
    	IDmodif= id;
    }
    @Override
    public Fragment getFragmentForPosition(int position) {
        return frags.get(position);
    }
    @Override
    public int getCount() {
       return frags.size();
    }
    
	
	//Se ejecuta la app por primera vez(onCreate).
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal_act);
		configuracionInicial();
		
		isEmpty();

	}
	
	public void isEmpty() {
		//Método que determina si hay, o no, asignaturas creadas
		BaseDatos cn = new BaseDatos(getApplicationContext());
		SQLiteDatabase db = cn.getReadableDatabase();
		
		txtsinasig = (TextView)findViewById(R.id.txtsinasignaturas);
		Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
		txtsinasig.setTypeface(tf);
		
		if (cn.getCuatrimestreDataBase("Primero").getLon()==0){
			txtsinasig.setText("No existe ninguna asignatura creada para esta carpeta.\n\nPresiona en el icono de la parte superior derecha de la pantalla para añadir una asignatura.");
		}else
			txtsinasig.setText("");
		cn.closeDB();
		db.close();
	}
	@Override
	protected void onStart() {
		super.onStart();
		mPager = (ViewPager) findViewById(R.id.pager);
		//Llamados a la clase publica TabPagerAdapter creada en un archivo independiente TabPagerAdapter.java.
        mAdapter = new MyPagerAdapter(this.getSupportFragmentManager(),this);
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(3);
        //mPager.setPageTransformer(true, new ZoomPag);
        mAdapter.notifyDataSetChanged();
		//Insertamos a 'vp'(nuestro viewpager) el adaptador de fragmentos del viewpager.
		//vp.setAdapter(TabAdapter);
        mPager.setBackgroundColor(Color.rgb(214, 217, 224));
		
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/* Este metodo lo llamamos en el archivo de disenyo principal_act.xml con la funcion 'android:onClick="add"', lo que nos premite
	definir este metodo como la accion que debe hacerse cuando se clica sobre el imageview de anyadir carpeta. */
	public void addSubject(View v){
		showDialog(0);
	}
	
	
	/* Para no petar el onCreate() con configuraciones meramente graficas, creamos este metodo de configuracion para llamarlo
	posteriormente en el onCreate(). */
	private void configuracionInicial(){
		/* Llamamos, mediante la variable 'ico' declarada al principio, a un imageview con su correspondiente id y 
		establecemos un color de fondo. */
		
		ico = (ImageView)findViewById(R.id.imageView3);
		ico.setBackgroundResource(R.drawable.colorprincipal);
		
		sub = (ImageView)findViewById(R.id.subject);
		sub.setBackgroundResource(R.drawable.colorprincipal);
		
//		Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
//		TextView spinnertext = (TextView)findViewById(R.id.txt_spinner);
//		spinnertext.setTypeface(tf);
		
		BaseDatos cn = new BaseDatos(getApplicationContext());
		SQLiteDatabase db = cn.getReadableDatabase();
		spinner =  ( Spinner ) findViewById ( R.id.cuatrimestre_spinner );

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				frags.clear();
				BaseDatos cn = new BaseDatos(getApplicationContext());
				SQLiteDatabase db = cn.getReadableDatabase();
				ClaseCuatrimestres Cuatrimestre = cn.getCuatrimestreDataBase(spinner.getSelectedItem().toString());
				int i=0;

				while (i<Cuatrimestre.getLon())
				{
					frags.add(new FragmentAsig(Cuatrimestre.getAsignatura(i).getNombre()));
				   i++;
				}

				cn.closeDB();
				db.close();	
				mAdapter.notifyDataSetChanged();
		    
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		ClaseClassMarks Classmarks = cn.getClassMarks();
		List<String> list = new ArrayList<String>();
		
		int i=0;
		while (i<Classmarks.getLon())
		{
			list.add(Classmarks.getCuatrimestrebyid(i).getCuatrimestre());
			i++;
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_custom,list);
		adapter.setDropDownViewResource(R.layout.spinner_custom);
		spinner.setAdapter(adapter);

		cn.closeDB();
		db.close();
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		private FragmentProvider mProvider;

		public MyPagerAdapter(FragmentManager fm, FragmentProvider provider) {
			super(fm);
			this.mProvider = provider;
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {
			return mAdapter.mProvider.getFragmentForPosition(position);
			// return
			// Android.newInstance(mProvider.getFragmentForPosition(position));
		}

		@Override
		public int getCount() {
			return mProvider.getCount();
		}
	}

    public void removeCurrentItem() {
        int position = mPager.getCurrentItem();
        frags.remove(position);
        mAdapter.notifyDataSetChanged();
    }
	
	
	@Override
	protected Dialog onCreateDialog(int id){
		switch (id){
		case 0:
			//Crear asignatura
			LayoutInflater inflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View newAsig = inflater.inflate(R.layout.nuevaasignatura_act, null);
			
			Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
			
			TextView txt1 = (TextView)newAsig.findViewById(R.id.textviewCarpeta);
			txt1.setTypeface(tf);  

			TextView txt = (TextView)newAsig.findViewById(R.id.textviewnuevaasig);
			txt.setTypeface(tf);

			final EditText edtxt = (EditText)newAsig.findViewById(R.id.edittextAsig);
			edtxt.setTypeface(tf);
			
			Button btn = (Button)newAsig.findViewById(R.id.buttonCrearAsig);
			btn.setTypeface(tf);
			
			Button btn1 = (Button)newAsig.findViewById(R.id.buttonSalirAsig);
			btn1.setTypeface(tf);
			
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					//verificar si la casilla rellenar  nota esta vacia para mostrar mensaje
					if (edtxt.getText().length()==0)
					{
						Toast.makeText(getApplicationContext(),"¡Campo sin rellenar!",Toast.LENGTH_SHORT).show();
						
					}
					else{
						BaseDatos cn = new BaseDatos(getApplicationContext());
						SQLiteDatabase db = cn.getWritableDatabase();
						String asign = edtxt.getText().toString();
						
						ClaseAsignaturas Asignatura = new ClaseAsignaturas();
						Asignatura.setNombre(asign);

						Asignatura.setIdcuatrimestre(cn.IdCuatrimestre(spinner.getSelectedItem().toString()));							
						cn.InsertarAsignatura(Asignatura);
						
						frags.add(new FragmentAsig(asign));
				        mAdapter.notifyDataSetChanged();

						cn.closeDB();
						db.close();
						mPager.setCurrentItem(frags.size());
						dismissDialog(0);
						
						edtxt.setText("");
						isEmpty();
					}
				}
			});
			
			btn1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismissDialog(0);
				}
			});
			Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    dialog.setContentView(newAsig);
		    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		    return dialog;
			
		case 1:
			//Añadir nota
			LayoutInflater inflater1=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View newNota = inflater1.inflate(R.layout.insertarnota_act, null);
			
			BaseDatos cn = new BaseDatos(getApplicationContext());
			SQLiteDatabase db = cn.getReadableDatabase();

			ClaseCuatrimestres cuatri = cn.getCuatrimestreDataBase(spinner.getSelectedItem().toString());
			
			Typeface tf1 = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
			TextView txt3 = (TextView)newNota.findViewById(R.id.textviewAsignatura);
			String nom = cuatri.getAsignatura(mPager.getCurrentItem()).getNombre();
			
			txt3.setText(nom);
			txt3.setTypeface(tf1);

			TextView txt4 =(TextView)newNota.findViewById(R.id.textviewNombre);
			txt4.setTypeface(tf1);
			
			TextView txt5 =(TextView)newNota.findViewById(R.id.textviewPorcentaje);
			txt5.setTypeface(tf1);
			
			TextView txt6 =(TextView)newNota.findViewById(R.id.textviewNota);
			txt6.setTypeface(tf1);

			final EditText edtxtnombreexa = (EditText)newNota.findViewById(R.id.edittextNombre);
			edtxtnombreexa.setTypeface(tf1);
			final EditText edtxtporcetaje = (EditText)newNota.findViewById(R.id.edittextPorcentaje);
			edtxtporcetaje.setTypeface(tf1);
			final EditText edtxtnota = (EditText)newNota.findViewById(R.id.edittextNota);
			edtxtnota.setTypeface(tf1);
			
			edtxtnombreexa.setMaxLines(1);
			edtxtnombreexa.setLines(1);
			edtxtporcetaje.setMaxLines(1);
			edtxtporcetaje.setLines(1);
			edtxtnota.setMaxLines(1);
			edtxtnota.setLines(1);

			Button btn2 = (Button)newNota.findViewById(R.id.buttonCrearNota);
			btn2.setTypeface(tf1);
			
			Button btn3 = (Button)newNota.findViewById(R.id.buttonSalirNota);
			btn3.setTypeface(tf1);
			
			cn.closeDB();
			db.close();

			btn2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean verdad = true;
					if (edtxtnombreexa.getText().length()==0)
					{
						verdad = false;
					}
					if (edtxtporcetaje.getText().length()==0)
					{
						verdad = false;
					}
					if (edtxtnota.getText().length()==0)
					{
						verdad = false;
					}
					
					if (verdad ==true)
					{
						BaseDatos cn = new BaseDatos(getApplicationContext());
						SQLiteDatabase db = cn.getWritableDatabase();

						String nombreexa = edtxtnombreexa.getText().toString();
						String porcetajeex = edtxtporcetaje.getText().toString();
						String notaex = edtxtnota.getText().toString();

						ClaseNotas notas = new ClaseNotas();
						ClaseCuatrimestres cuatri = cn.getCuatrimestreDataBase(spinner.getSelectedItem().toString());						
						
						notas.setEvaluable(nombreexa);
						notas.setNota(Double.parseDouble(notaex));	
						notas.setPorcentaje(Double.parseDouble(porcetajeex));
						notas.setIdasignatura(cuatri.getAsignatura(mPager.getCurrentItem()).getId());

						cn.InsertarNota(notas);
						cn.closeDB();
						db.close();
						dismissDialog(1);
						mAdapter.notifyDataSetChanged();
						

						edtxtporcetaje.setText("");
						edtxtnota.setText("");
						edtxtnombreexa.setText("");
						edtxtnombreexa.requestFocus();
					}
					else
						Toast.makeText(getApplicationContext(),"¡Falta campos por rellenar!",Toast.LENGTH_SHORT).show();	
				}
			});
			
			btn3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismissDialog(1);
				}
			});
			
			Dialog dialog1 = new Dialog(this);
			dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    dialog1.setContentView(newNota);
		    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		    dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		    return dialog1;

		case 2:
			//Modificar nota
			LayoutInflater inflater2=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View modifNota = inflater2.inflate(R.layout.modificarnota_act, null);
			
			BaseDatos cn1 = new BaseDatos(getApplicationContext());
			SQLiteDatabase db1 = cn1.getReadableDatabase();

			ClaseCuatrimestres cuatri1 = cn1.getCuatrimestreDataBase(spinner.getSelectedItem().toString());
			ClaseNotas notamodif = cn1.getNotaDataBase(IDmodif);
			
			Typeface tf11 = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
			TextView txt31 = (TextView)modifNota.findViewById(R.id.textviewAsignatura);
			String nom1 = cuatri1.getAsignatura(mPager.getCurrentItem()).getNombre();
			
			txt31.setText(nom1);
			txt31.setTypeface(tf11);

			TextView txt41 =(TextView)modifNota.findViewById(R.id.textviewNombre);
			//txt41.setText(notamodif.getEvaluable());

			txt41.setTypeface(tf11);
			
			TextView txt51 =(TextView)modifNota.findViewById(R.id.textviewPorcentaje);
			//txt51.setText(""+notamodif.getPorcentaje());

			txt51.setTypeface(tf11);
			
			TextView txt61 =(TextView)modifNota.findViewById(R.id.textviewNota);
			//txt61.setText(""+notamodif.getNota());

			txt61.setTypeface(tf11);

			final EditText edtxtnombreexa1 = (EditText)modifNota.findViewById(R.id.edittextNombre);
			edtxtnombreexa1.setTypeface(tf11);
			final EditText edtxtporcetaje1 = (EditText)modifNota.findViewById(R.id.edittextPorcentaje);
			edtxtporcetaje1.setTypeface(tf11);
			final EditText edtxtnota1 = (EditText)modifNota.findViewById(R.id.edittextNota);
			edtxtnota1.setTypeface(tf11);
			
			edtxtnombreexa1.setMaxLines(1);
			edtxtnombreexa1.setLines(1);
			edtxtnombreexa1.setText(notamodif.getEvaluable());

			edtxtporcetaje1.setMaxLines(1);
			edtxtporcetaje1.setLines(1);
			edtxtporcetaje1.setText(""+notamodif.getPorcentaje());

			edtxtnota1.setMaxLines(1);
			edtxtnota1.setLines(1);
			edtxtnota1.setText(""+notamodif.getNota());


			Button btn21 = (Button)modifNota.findViewById(R.id.buttonModificarNota);
			btn21.setTypeface(tf11);
			
			Button btn31 = (Button)modifNota.findViewById(R.id.buttonSalirNota);
			btn31.setTypeface(tf11);
			
			cn1.closeDB();
			db1.close();

			btn21.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean verdad = true;
					if (edtxtnombreexa1.getText().length()==0)
					{
						verdad = false;
					}
					if (edtxtporcetaje1.getText().length()==0)
					{
						verdad = false;
					}
					if (edtxtnota1.getText().length()==0)
					{
						verdad = false;
					}
					
					if (verdad ==true)
					{
						BaseDatos cn2 = new BaseDatos(getApplicationContext());
						SQLiteDatabase db2 = cn2.getWritableDatabase();

						String nombreexa = edtxtnombreexa1.getText().toString();
						String porcetajeex = edtxtporcetaje1.getText().toString();
						String notaex = edtxtnota1.getText().toString();

						ClaseNotas notas = new ClaseNotas();
						ClaseCuatrimestres cuatri2 = cn2.getCuatrimestreDataBase(spinner.getSelectedItem().toString());						
						
						notas.setId(IDmodif);
						notas.setEvaluable(nombreexa);
						notas.setNota(Double.parseDouble(notaex));	
						notas.setPorcentaje(Double.parseDouble(porcetajeex));
						notas.setIdasignatura(cuatri2.getAsignatura(mPager.getCurrentItem()).getId());
					    
						cn2.updateNota(notas);
						cn2.closeDB();
						db2.close();
						dismissDialog(2);
						mAdapter.notifyDataSetChanged();
						

						edtxtporcetaje1.setText("");
						edtxtnota1.setText("");
						edtxtnombreexa1.setText("");
						edtxtnombreexa1.requestFocus();
					}
					else
						Toast.makeText(getApplicationContext(),"¡Falta campos por rellenar!",Toast.LENGTH_SHORT).show();	
				}
			});
			
			btn31.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismissDialog(2);
				}
			});
			
			Dialog dialog2 = new Dialog(this);
			dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog2.setContentView(modifNota);
		    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		    dialog2.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		    return dialog2;
		     
		case 3:
			//Confirmación eliminar asignatura
			final BaseDatos cn3= new BaseDatos(getApplicationContext());
			
			LayoutInflater inflater3 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View confirmarEliminar = inflater3.inflate(R.layout.confimar_eliminar_act, null);
			
			ClaseCuatrimestres cuatri2 = cn3.getCuatrimestreDataBase(spinner.getSelectedItem().toString());
			final String nombreAsig = cuatri2.getAsignatura(mPager.getCurrentItem()).getNombre();
			
			TextView txtAsigEliminar = (TextView)confirmarEliminar.findViewById(R.id.textviewAsignatura);
			txtAsigEliminar.setText(nombreAsig);
			
			Button btnSi = (Button)confirmarEliminar.findViewById(R.id.buttonConfirmacionEliminarSi);
			Button btnNo = (Button)confirmarEliminar.findViewById(R.id.buttonConfirmacionEliminarNo);
			
			btnSi.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					//SQLiteDatabase db3= cn3.getWritableDatabase();
					
					removeCurrentItem();
					
					cn3.EliminarAsignatura(cn3.IdAsignatura(nombreAsig));
					
					cn3.closeDB();
					dismissDialog(3);
					
				}
			});
			
			btnNo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					cn3.closeDB();
					dismissDialog(3);
				}
			});
			
			Dialog dialog3 = new Dialog(this);
			dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog3.setContentView(confirmarEliminar);
		    return dialog3;
			
		}
		return null;

	}
	
	@SuppressLint("CutPasteId")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		BaseDatos cn = new BaseDatos(getApplicationContext());
		SQLiteDatabase db = cn.getReadableDatabase();
		switch (id){
			case 0:
				String nom2 = spinner.getSelectedItem().toString();
				TextView txt1 = (TextView)dialog.findViewById(R.id.textviewCarpeta);
				txt1.setText(nom2); 
				break;
				
			case 1:	
				ClaseCuatrimestres cuatri = cn.getCuatrimestreDataBase(spinner.getSelectedItem().toString());
				String nom = cuatri.getAsignatura(mPager.getCurrentItem()).getNombre();
				TextView txt3 = (TextView)dialog.findViewById(R.id.textviewAsignatura);
				txt3.setText(nom);
				EditText ed = (EditText)dialog.findViewById(R.id.edittextNombre);
				ed.requestFocus();
				break;
				
			case 2:	
				ClaseCuatrimestres cuatri1 = cn.getCuatrimestreDataBase(spinner.getSelectedItem().toString());
				ClaseNotas notamodif = cn.getNotaDataBase(IDmodif);
				String nom1 = cuatri1.getAsignatura(mPager.getCurrentItem()).getNombre();
				TextView txt31 = (TextView)dialog.findViewById(R.id.textviewAsignatura);
				txt31.setText(nom1);
				EditText ed1 = (EditText)dialog.findViewById(R.id.edittextNombre);
				ed1.requestFocus();
				
				final EditText edtxtnombreexa1 = (EditText)dialog.findViewById(R.id.edittextNombre);
				final EditText edtxtporcetaje1 = (EditText)dialog.findViewById(R.id.edittextPorcentaje);
				final EditText edtxtnota1 = (EditText)dialog.findViewById(R.id.edittextNota);
				edtxtnombreexa1.setText(notamodif.getEvaluable());

				edtxtporcetaje1.setText(""+notamodif.getPorcentaje());

				edtxtnota1.setText(""+notamodif.getNota());
				break;
				
			case 3:
				
				ClaseCuatrimestres cuatri2 = cn.getCuatrimestreDataBase(spinner.getSelectedItem().toString());
				final String nombreAsig = cuatri2.getAsignatura(mPager.getCurrentItem()).getNombre();
				
				TextView txtAsigEliminar = (TextView)dialog.findViewById(R.id.textviewAsignatura);
				txtAsigEliminar.setText(nombreAsig);
				
				break;
		}
		
		cn.closeDB();
		db.close();
			
	}

}
