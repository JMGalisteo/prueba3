package com.tfd.classmarks;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{

	protected Activity activity;
	protected ArrayList<Item> items;
	
	public ListAdapter(Activity activity, ArrayList<Item> items){
		this.activity = activity;
		this.items = items;
	}
	@Override
	public int getCount() {
		return items.size();
		
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
		
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View vi = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.listview_forma, null);
		}
		
		Item item = items.get(position);
		
		TextView evaluable = (TextView)vi.findViewById(R.id.tveva);
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "Roboto-Light.ttf");
		evaluable.setTypeface(tf );
		evaluable.setText(item.getEvaluable());
		
		TextView porcentaje = (TextView)vi.findViewById(R.id.tvpor);
		porcentaje.setTypeface(tf);
		porcentaje.setText(item.getPorcentaje());
		
		TextView nota = (TextView)vi.findViewById(R.id.tvnot);
		nota.setTypeface(tf);
		nota.setText(item.getNota());
		
		return vi;
		
	}

}
