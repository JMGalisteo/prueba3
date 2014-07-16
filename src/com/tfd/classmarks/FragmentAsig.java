package com.tfd.classmarks;

import java.util.ArrayList;

import com.tfd.classmarks.QuickAction;
import com.tfd.classmarks.ActionItem;
import com.tfd.classmarks.Principal;

import mysql.BaseDatos;
import mysql.ClaseAsignaturas;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class FragmentAsig extends Fragment{
	private int EliminarID;
	public int ModifID;
	private static final int ID_EDIT     = 1;
	private static final int ID_ELIMINAR   = 2;
	public String mText;
	public TextView txtnotaexfin, txttotal, txtmedia, txtsobre, txtañadir;
	public ListView lv;
	public Principal prin;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

		// Configuración de objetos
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Light.ttf");
		
		//TextView nombre de la asignatura
		final TextView txt = (TextView)fragment.findViewById(R.id.textViewAnd);
        txt.setText(mText);
        txt.setTypeface(tf);
        
        //Código para crear y escalar el indicardor verde
        Drawable indic = getActivity().getResources().getDrawable(R.drawable.indicador_verde_x);   
        Bitmap bm = ((BitmapDrawable)indic).getBitmap();
        final Drawable indicator = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm, 22, 22, true));

      //Código para crear y escalar el indicardor rojo
        Drawable indicR = getActivity().getResources().getDrawable(R.drawable.indicador_rojo_x);   
        Bitmap bm1 = ((BitmapDrawable)indicR).getBitmap();
        final Drawable indicatorR = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm1, 22, 22, true));

        
        ImageView x = (ImageView)fragment.findViewById(R.id.imageViewEliminar);
        
        x.setOnClickListener(new View.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				((Principal)getActivity()).removeCurrentItem();
				cn.EliminarAsignatura(cn.IdAsignatura(mText));				

				cn.closeDB();
			}
		});
		
        final TextView txttotal = (TextView)fragment.findViewById(R.id.TVtotal);
		final TextView txtmedia = (TextView)fragment.findViewById(R.id.TVmedia);
		final TextView txtnotaneeded = (TextView)fragment.findViewById(R.id.TVnotaneeded);

		txttotal.setTypeface(tf);
		txtmedia.setTypeface(tf);
		txtnotaneeded.setTypeface(tf);
		
		/*
		 * 
		 *EMPIEZA EL CÓDIGO DEL BOCADILLO (SPEECH BUBBLE)
		 * 
		*/
		
		ActionItem editItem = new ActionItem(ID_EDIT, "Edit", getResources().getDrawable(R.drawable.menu_down_arrow));
		ActionItem eliminarItem = new ActionItem(ID_ELIMINAR, "Eliminar", getResources().getDrawable(R.drawable.menu_up_arrow));

		// Crea un objeto QuickAction y determina que su orientación sea horizontal
		final QuickAction quickAction = new QuickAction(getActivity(),
				QuickAction.HORIZONTAL);

		// add action items into QuickAction
		quickAction.addActionItem(editItem);
		quickAction.addActionItem(eliminarItem);

		// Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, int pos,
					int actionId) {
				//ActionItem actionItem = quickAction.getActionItem(pos);
				
				BaseDatos cn = new BaseDatos(getActivity().getApplicationContext());
				SQLiteDatabase db = cn.getWritableDatabase();
				
				//Acción de editar del bubble
				if (actionId == ID_EDIT) {
					((Principal)getActivity()).setIDmodif(cn.IdNota(items.get(EliminarID).getEvaluable()));
					getActivity().showDialog(2);
					adap.notifyDataSetChanged();
				} 
				//Acción de eliminar del bubble
				else if (actionId == ID_ELIMINAR) {
					cn.EliminarNota(cn.IdNota(items.get(EliminarID).getEvaluable()));
					items.remove(EliminarID);
					Log.d("mtext",mText);

					float txtsob = cn.SumaPorcentajes(cn.IdAsignatura(mText));
					Log.d("txtsob",""+txtsob);

					double txttot = cn.TotalProducto(cn.IdAsignatura(mText));
					Log.d("txttot",""+txttot);

					double txtmed = Math.round((txttot / (txtsob / 100)) * 100.0) / 100.0;
					Log.d("txtmed",""+txtmed);

					if(txtmed >= 5){
						txt.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
					}else{
						txt.setCompoundDrawablesWithIntrinsicBounds(indicatorR, null, null, null);
					}

					txttotal.setText(getString(R.string.Total) + " " + txttot);
					txtmedia.setText(getString(R.string.Media) + " " + txtmed);
					txtnotaneeded.setText(getString(R.string.recuadroo));
					adap.notifyDataSetChanged();

				}
				cn.closeDB();
				db.close();
			}
		});
		
		/*
		 * 
		 *ACABA EL CÓDIGO DEL BOCADILLO (SPEECH BUBBLE)
		 * 
		*/
		
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
        		EliminarID = arg2;
				quickAction.show(arg1);
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
			txt.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
		}else{
			txt.setCompoundDrawablesWithIntrinsicBounds(indicatorR, null, null, null);
			}
		txttotal.setText(getString(R.string.Total)+ " "+txttot);
		txtmedia.setText(getString(R.string.Media)+" "+ txtmed);
		
		cn.closeDB();
		db.close();

		Log.d("FRAGMENT","Cargado: "+mText);
        return fragment;
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
	public void setModifID(int id){
		this.EliminarID= id;
	}
	public int getModifID(){
		
		return EliminarID;
	}
	public void addMark(){
		this.getActivity().showDialog(1);	
	}
	
	public void deleteSubject(){
		Intent in = new Intent(getActivity().getBaseContext(), Principal.class);
		startActivity(in);
	}
	
}
