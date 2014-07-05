package mysql;

public class ClaseNotas {
	
	int id;
	double porcentaje;
	String evaluable;
	double nota;
	int idasignatura;
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public String getEvaluable() {
		return evaluable;
	}
	public void setEvaluable(String evaluable) {
		this.evaluable = evaluable;
	}
	public double getNota() {
		return nota;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
	public int getIdasignatura() {
		return idasignatura;
	}
	public void setIdasignatura(int idasignatura) {
		this.idasignatura = idasignatura;
	}
}
