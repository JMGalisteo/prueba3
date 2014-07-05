package mysql;

public class ClaseClassMarks {
	
	ClaseCuatrimestres [] cuatris = new ClaseCuatrimestres [3];
	int lon=0;

	public ClaseCuatrimestres[] getCuatris() {
		return cuatris;
	}
	
    public ClaseCuatrimestres getCuatrimestrebyid(int i) {
		
		return cuatris[i];
	}

	public void setCuatris(ClaseCuatrimestres[] cuatris) {
		this.cuatris = cuatris;
	}
	
	public void setCuatrimestre(ClaseCuatrimestres cuatri , int i) {
		lon++;
		cuatris[i] = cuatri;
	}

	public int getLon() {
		return lon;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

}
