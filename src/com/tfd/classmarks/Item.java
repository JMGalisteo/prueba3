package com.tfd.classmarks;

public class Item {
	protected long id;
	protected String txtevaluable, txtporcentaje, txtnota;
	
	public Item() {
		this.txtevaluable = "";
		this.txtporcentaje = "";
		this.txtnota = "";
	}
	
	public Item(long id, String evaluable, String porcentaje, String nota){
		this.id = id;
		this.txtevaluable = evaluable;
		this.txtporcentaje = porcentaje;
		this.txtnota = nota;
	}
	
	//ID
	public long getId() {
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	
	//Evaluable
	public String getEvaluable(){
		return txtevaluable;
	}
	public void setEvaluable(String evaluable){
		this.txtevaluable = evaluable;
	}
	
	//Porcentaje
	public String getPorcentaje(){
		return txtporcentaje;
	}
	public void setPorcentaje(String porcentaje){
		this.txtporcentaje= porcentaje;
	}
	
	//Nota
	public String getNota(){
		return txtnota;
	}
	public void setNota(String nota){
		this.txtnota = nota;
	}

}
