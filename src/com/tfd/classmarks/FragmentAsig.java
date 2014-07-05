package com.tfd.classmarks;

import java.util.ArrayList;

import mysql.BaseDatos;
import mysql.ClaseAsignaturas;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class FragmentAsig extends Fragment{
	//COMENTARIO QUE HAY QUE BORRAR
	public String mText;
	public TextView txtnotaexfin, txttotal, txtmedia, txtsobre, txtañadir;
	public ListView lv;
	public Principal prin;
	//holaaaa
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		final ArrayList<Item> items = new ArrayList<Item>();
		final ListAdapter adap = new ListAdapter(getActivity(), items);
		adap.notifyDataSetChanged();
		
		View footer = ((LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_forma_footer, null, false);
		footer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addMark();
			}
		});
		
		final BaseDatos cn = new BaseDatos(this.getActivity());
		SQLiteDatabase db = cn.getReadableDatabase();

		View fragment = inflater.inflate(R.layout.asignatura_frag, container, false);
		
		//Configuración de objetos
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Light.ttf");
		
		TextView txt = (TextView)fragment.findViewById(R.id.textViewAnd);
        txt.setText(mText);
        txt.setTypeface(tf);
        
        txt.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				cn.EliminarAsignatura(cn.IdAsignatura(mText));
				deleteSubject();
				return true;
			}
		});
		
        final TextView txttotal = (TextView)fragment.findViewById(R.id.TVtotal);
		final TextView txtmedia = (TextView)fragment.findViewById(R.id.TVmedia);
		final TextView txtnotaexfin = (TextView)fragment.findViewById(R.id.TVnotaexfinal);
		final TextView txtsobre = (TextView)fragment.findViewById(R.id.TVsobre);
		
		txttotal.setTypeface(tf);
		txtmedia.setTypeface(tf);
		txtnotaexfin.setTypeface(tf);
		txtsobre.setTypeface(tf);

		txtnotaexfin.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				calcularNotaFinal();
				}
		});
		
		TextView tvcrearnota = (TextView)footer.findViewById(R.id.tvanadir);
		tvcrearnota.setTypeface(tf);
		//Fin de la configuración de objetos
		lv = (ListView)fragment.findViewById(R.id.listView1);
	    lv.addFooterView(footer);
        lv.setAdapter(adap);
        
		lv.setLongClickable(true);
		
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				cn.EliminarNota(cn.IdNota(items.get(arg2).getEvaluable()));
				items.remove(arg2);
				adap.notifyDataSetChanged();
				
				float txtsob = cn.SumaPorcentajes(cn.IdAsignatura(mText));
				double txttot = cn.TotalProducto(cn.IdAsignatura(mText));
				double txtmed = Math.round((txttot/(txtsob/ 100))*100.0)/100.0;
				
				if(txtmed >= 5){
				txtsobre.setText(getString(R.string.Sobre)+" "+txtsob+ " %");
				}else{
					txtsobre.setText(getString(R.string.Sobress)+" "+txtsob+ " %");
					}
				txttotal.setText(getString(R.string.Total)+ " "+txttot);
				txtmedia.setText(getString(R.string.Media)+" "+ txtmed);
				txtnotaexfin.setText(getString(R.string.recuadroo));
				
				return true;
			}
		});
		
		//Mostramos los datos recogidos de la base de datos
		ClaseAsignaturas Asignatura = cn.getAsignaturaDataBase(mText);
		
		int i=0;
		
		while (i < Asignatura.getLon())
		{
		   items.add(new Item(0, Asignatura.getNotas(i).getEvaluable(), Double.toString(Asignatura.getNotas(i).getPorcentaje())+" %", Double.toString((Asignatura.getNotas(i).getNota()))));
		   i++;
		}
		
		float txtsob = cn.SumaPorcentajes(cn.IdAsignatura(mText));
		double txttot = cn.TotalProducto(cn.IdAsignatura(mText));
		double txtmed = Math.round((txttot/(txtsob/ 100))*100.0)/100.0;
		
		if(txtmed >= 5){
		txtsobre.setText(getString(R.string.Sobre)+" "+txtsob+ " %");
		}else{
			txtsobre.setText(getString(R.string.Sobress)+" "+txtsob+ " %");
			}
		txttotal.setText(getString(R.string.Total)+ " "+txttot);
		txtmedia.setText(getString(R.string.Media)+" "+ txtmed);
		txtnotaexfin.setText(getString(R.string.recuadroo));
		
		cn.closeDB();
		db.close();

		Log.d("FRAGMENT","Cargado: "+mText);
        return fragment;
	}
	
	protected void calcularNotaFinal() {
		//calc = (5-total)/% examen final
		//Crear asignatura
		
		LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View newAsig = inflater.inflate(R.layout.porcentagefinal_act, null);
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
		Typeface tft = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Light.ttf");
		
		TextView txt1 = (TextView)newAsig.findViewById(R.id.textviewAsignatura);
		txt1.setTypeface(tf);  

		TextView txt = (TextView)newAsig.findViewById(R.id.textviewCalcularPorcen);
		txt.setTypeface(tf);

		final EditText edtxt = (EditText)newAsig.findViewById(R.id.edittextPorcen);
		edtxt.setTypeface(tf);
		
		Button btn = (Button)newAsig.findViewById(R.id.buttonCalcularPorcen);
		btn.setTypeface(tft);
		
		Button btn1 = (Button)newAsig.findViewById(R.id.buttonSalirPorcen);
		btn1.setTypeface(tft);
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//verificar si la casilla rellenar  nota esta vacia para mostrar mensaje
				if (edtxt.getText().length()==0)
				{
					Toast.makeText(getActivity().getApplicationContext(),"Campo sin rellenar",Toast.LENGTH_SHORT).show();
					
				}
				else{
					BaseDatos cn = new BaseDatos(getActivity().getApplicationContext());
					SQLiteDatabase db = cn.getWritableDatabase();
					Double porcen = Double.parseDouble(edtxt.getText().toString());
					
					ClaseAsignaturas Asignatura = new ClaseAsignaturas();
					Asignatura.setNotaFinal(porcen);
					
					//txt1.setText(text);

					cn.closeDB();
					db.close();

					edtxt.setText("");
				}
			}
		});
		
		
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(newAsig);
	    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	    
	    btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	    
		//double calc = (5 - )/ );
		//Toast.makeText(getActivity(), "Debes sacar un "+calc +"X en el examen final para aprobar", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.d("FRAGMENT","onViewCreated DONE");
	}

	public FragmentAsig() {
	}

	public FragmentAsig(String text) {
		this.mText = text;
	}

	public String NombreAsig() {
		return mText;
	}
	
	public void addMark(){
		this.getActivity().showDialog(1);	
	}
	
	public void deleteSubject(){
		Intent in = new Intent(getActivity().getBaseContext(), Principal.class);
		startActivity(in);
	}
}
