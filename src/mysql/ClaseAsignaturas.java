package mysql;

import android.database.sqlite.SQLiteDatabase;

public class ClaseAsignaturas {
	
	int id;
	String Nombre;
	int idcuatrimestre;
	double notafinal;
	ClaseNotas[]  Notas = new ClaseNotas[100];
	int lon=0;
	
	public ClaseNotas getNotas(int i) {
		return Notas[i];
	}

	public int getLon() {
		return lon;
	}
	public void setLon(int lon) {
		this.lon = lon;
	}
	public void setNotas(ClaseNotas notas, int i) {
		lon++;
		Notas[i] = notas;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public int getIdcuatrimestre() {
		return idcuatrimestre;
	}
	public void setIdcuatrimestre(int idcuatrimestre) {
		this.idcuatrimestre = idcuatrimestre;
	}
	
	public double getNotaFinal(){
		return  notafinal;
	}
	public void setNotaFinal(double notafinal1){
		this.notafinal = notafinal1;
	}
	
	

}
