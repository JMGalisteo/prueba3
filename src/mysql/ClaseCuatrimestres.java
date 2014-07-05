package mysql;

public class ClaseCuatrimestres {
	
	int id;
	String cuatrimestre;
	ClaseAsignaturas[]  Asignatura= new ClaseAsignaturas[100];
	int lon=0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCuatrimestre() {
		return cuatrimestre;
	}
	public void setCuatrimestre(String cuatrimestre) {
		this.cuatrimestre = cuatrimestre;
	}

	public ClaseAsignaturas getAsignatura(int i) {
		
		return Asignatura[i];
	}

	public int getLon() {
		return lon;
	}
	public void setLon(int lon) {
		this.lon = lon;
	}
	
	public void setAsignatura(ClaseAsignaturas asignatura , int i) {
		lon++;
		Asignatura[i] = asignatura;
	}
	

}
