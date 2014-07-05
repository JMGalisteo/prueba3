package mysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDatos extends SQLiteOpenHelper {

	// Logcat tag
    private static final String LOG = BaseDatos.class.getName();
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ClassMarksdb.db";
 
    // Table Names
    private static final String TABLE_CUATRIMESTRE = "Cuatrimestres";
    private static final String TABLE_ASIGNATURA = "Asignaturas";
    private static final String TABLE_NOTAS = "Notas";
 
    // Common column names
    
 
    // Cuatrimestres Table - column names
    private static final String KEY_IdCuatrimestre = "id";
    private static final String KEY_NombreCuatrimeste = "Nombre";
 
    // Asignaturas Table - column names
    private static final String KEY_IdAsignatura = "id";
    private static final String KEY_NombreAsignatura = "Nombre";
    private static final String KEY_IdCuatrimestreReferencia = "IdCuatrimestre";
 
    // Notas Table - column names
    private static final String KEY_IdNotas = "id";
    private static final String KEY_Porcentaje = "Porcentaje";
    private static final String KEY_Evaluable = "Evaluable";
    private static final String KEY_Nota = "Nota";
    private static final String KEY_IdAsignaturaReferencia = "IdAsignatura";
 
    // Table Create Statements
    // Cuatrimestretable create statement
    private static final String CREATE_TABLE_Cuatrimestre = "CREATE TABLE "
            + TABLE_CUATRIMESTRE + "(" + KEY_IdCuatrimestre + " INTEGER PRIMARY KEY," + KEY_NombreCuatrimeste
            + " TEXT" + ");";
 
    // Asignatura table create statement
    private static final String CREATE_TABLE_Asignatura = "CREATE TABLE " + TABLE_ASIGNATURA
            + "(" + KEY_IdAsignatura + " INTEGER PRIMARY KEY," + KEY_NombreAsignatura + " TEXT,"
            + KEY_IdCuatrimestreReferencia + " INTEGER, FOREIGN KEY (" + KEY_IdCuatrimestreReferencia + ") REFERENCES "+TABLE_CUATRIMESTRE+ "("+KEY_IdCuatrimestre+"));" ;
 
    // todo_tag table create statement
    private static final String CREATE_TABLE_NOTAS = "CREATE TABLE "
            + TABLE_NOTAS + "(" + KEY_IdNotas + " INTEGER PRIMARY KEY,"
            + KEY_Porcentaje + " DOUBLE," + KEY_Evaluable + " TEXT,"
            + KEY_Nota + " DOUBLE," +KEY_IdAsignaturaReferencia +" INTEGER, FOREIGN KEY (" + KEY_IdAsignaturaReferencia + ") REFERENCES "+TABLE_ASIGNATURA+ "("+KEY_IdAsignatura+"));";
 
    public BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        // creating required tables
        db.execSQL(CREATE_TABLE_Cuatrimestre);
        db.execSQL(CREATE_TABLE_Asignatura);
        db.execSQL(CREATE_TABLE_NOTAS);
        
        Log.d("BASE DE DATOS","Fin de ejecucion de las Queris");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUATRIMESTRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASIGNATURA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTAS);
 
        // create new tables
        onCreate(db);
    }
    
    //Funcion para insertar nota
    public void InsertarNota(ClaseNotas Nota) {
    	
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_Porcentaje, Nota.getPorcentaje());
        values.put(KEY_Evaluable, Nota.getEvaluable());
        values.put(KEY_Nota, Nota.getNota());
        values.put(KEY_IdAsignaturaReferencia, Nota.getIdasignatura());
        
        db.insert(TABLE_NOTAS, null, values);        
    }
    
    //Funcion para insertar Asignatura
     public void InsertarAsignatura(ClaseAsignaturas Asignatura) {
    	
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NombreAsignatura, Asignatura.getNombre());
        values.put(KEY_IdCuatrimestreReferencia, Asignatura.getIdcuatrimestre());
        
        db.insert(TABLE_ASIGNATURA, null, values);
    }
     
     //Funcion para insertar nuevo cuatrimestre
     public void InsertarCuatrimestre(ClaseCuatrimestres Cuatri) {
     	
         SQLiteDatabase db = this.getWritableDatabase();
  
         ContentValues values = new ContentValues();
         values.put(KEY_NombreCuatrimeste, Cuatri.getCuatrimestre());
         
         db.insert(TABLE_CUATRIMESTRE, null, values);
     }
     
     //apartir del id de una asignatura nos devuelve el Total de la nota que llevamos sobre 10.
     public double TotalProducto(int idasignatura){
    	 
    	 SQLiteDatabase db = this.getReadableDatabase();
    	 Cursor cursor1 = db.rawQuery("SELECT "+ KEY_Porcentaje +", "+KEY_Nota+" FROM "+ TABLE_NOTAS +" WHERE " +KEY_IdAsignaturaReferencia +" = "+idasignatura,null);
    	 float columntotalPorcentajes = 0;
    	 float columntotalNotas = 0;
    	 double TotalNot = 0;
    	 if(cursor1.moveToFirst()) {
    		 for (int y = 0; y < cursor1.getCount(); y++){
    			 columntotalPorcentajes = cursor1.getFloat(cursor1.getColumnIndex(KEY_Porcentaje));
    			 columntotalNotas = cursor1.getFloat(cursor1.getColumnIndex(KEY_Nota));
    			 TotalNot = TotalNot + ((columntotalPorcentajes / 100)*columntotalNotas);
    			 cursor1.moveToNext();   	     
    		 }
	     }
    	 
    	 return Math.round(TotalNot*100.0)/100.0;
    	 
     } 
     
     //suma los porcentajes de las notas vinculadas a una asigantura
     public float SumaPorcentajes(int idasignatura){
    	 
    	 SQLiteDatabase db = this.getReadableDatabase();
    	 Cursor cursor1 = db.rawQuery("SELECT SUM("+ KEY_Porcentaje +") FROM "+ TABLE_NOTAS +" WHERE " +KEY_IdAsignaturaReferencia +" = "+idasignatura,null);
    	 float columntotal = 0;
    	 if(cursor1.moveToFirst()) {
	         columntotal = cursor1.getFloat(0);
	     }
    	 
    	 return columntotal;
    	 
     } 
     //devuelve el id de una asignatura apartir de su nombre
     public int IdAsignatura(String NombreAsignatura){
    	 
    	 SQLiteDatabase cn = this.getReadableDatabase();
    	 Cursor cursor1 = cn.rawQuery("SELECT "+ KEY_IdAsignatura +" FROM "+ TABLE_ASIGNATURA +" WHERE " +KEY_NombreAsignatura +" = '"+NombreAsignatura+"'",null);
    	 int columntotal = 0;
    	 
    	 if(cursor1.moveToFirst()) {
    		 
	         columntotal = cursor1.getInt(0);
	     }
    	 
    	 return columntotal;
    	 
     } 
     
 public int IdCuatrimestre(String NombreCuatrimestre){
    	 
    	 SQLiteDatabase cn = this.getReadableDatabase();
    	 Cursor cursor1 = cn.rawQuery("SELECT "+ KEY_IdCuatrimestre +" FROM "+ TABLE_CUATRIMESTRE +" WHERE " +KEY_NombreCuatrimeste +" = '"+NombreCuatrimestre+"'",null);
    	 int columntotal = 0;
    	 
    	 if(cursor1.moveToFirst()) {
    		 
	         columntotal = cursor1.getInt(0);
	     }
    	 
    	 return columntotal;
    	 
     } 
 
 //Funcion para seleciona notas a traves de nombre de asignatura
 public ClaseNotas getNotaDataBase (int idnota) {
	    //Selecionamos la id de asignatura, para sacar las notas q referencien a ese id
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT * FROM " + TABLE_NOTAS + " WHERE "
	            + KEY_IdNotas + " = '" + idnota+ "'";
	 
	    Cursor c = db.rawQuery(selectQuery, null);
	    ClaseNotas Nota = new ClaseNotas();
	    //int id = c.getInt(c.getColumnIndex(KEY_IdAsignatura));
	    //Log.d("Log2",Integer.toString(id));
	    if (c != null){
	        c.moveToFirst();    
	        //Asignamos parametros a las clase asignatura
	   
	        Nota.setId(c.getInt(c.getColumnIndex(KEY_IdNotas)));
	        Nota.setEvaluable(c.getString(c.getColumnIndex(KEY_Evaluable)));
	        Nota.setPorcentaje(c.getDouble(c.getColumnIndex(KEY_Porcentaje)));
	        Nota.setNota(c.getDouble(c.getColumnIndex(KEY_Nota)));
	        Nota.setIdasignatura(c.getInt(c.getColumnIndex(KEY_IdAsignaturaReferencia)));
	    }
	    
	    return Nota;
	}
     
     //Funcion para seleciona notas a traves de nombre de asignatura
     public ClaseAsignaturas getAsignaturaDataBase (String NombreAsignatura) {
    	    //Selecionamos la id de asignatura, para sacar las notas q referencien a ese id
    	    SQLiteDatabase db = this.getReadableDatabase();
    	 
    	    String selectQuery = "SELECT * FROM " + TABLE_ASIGNATURA + " WHERE "
    	            + KEY_NombreAsignatura + " = '" + NombreAsignatura+ "'";
    	 
    	    Cursor c = db.rawQuery(selectQuery, null);
    	    ClaseAsignaturas Asignatura = new ClaseAsignaturas();
    	    //int id = c.getInt(c.getColumnIndex(KEY_IdAsignatura));
    	    //Log.d("Log2",Integer.toString(id));
    	    if (c != null){
    	        c.moveToFirst();    
    	        //Asignamos parametros a las clase asignatura
    	   
    	        Asignatura.setId(c.getInt(c.getColumnIndex(KEY_IdAsignatura)));
    	        Asignatura.setNombre(c.getString(c.getColumnIndex(KEY_NombreAsignatura)));
    	        Asignatura.setIdcuatrimestre(c.getInt(c.getColumnIndex(KEY_IdCuatrimestreReferencia)));    	        

    	    }
    	    //Recogemos el idasignatura y sacamos las notas
    	    
    	    String selectQuery2 = "SELECT * FROM " + TABLE_NOTAS + " WHERE "
    	            + KEY_IdAsignaturaReferencia + " = " + c.getInt(c.getColumnIndex(KEY_IdAsignatura));
    	    //cambio id
    	 
    	    Cursor d = db.rawQuery(selectQuery2, null);
    	   
    	   // Log.d("d",""+d.getPosition());
    	    if (d != null){
    	    	d.moveToFirst();
	    	    for (int y = 0; y < d.getCount(); y++)
		    		{
	    	    	 ClaseNotas nota = new ClaseNotas();
			    	 nota.setId(d.getInt(d.getColumnIndex(KEY_IdNotas)));
			    	 nota.setEvaluable(d.getString(d.getColumnIndex(KEY_Evaluable)));
			    	 nota.setPorcentaje(d.getDouble(d.getColumnIndex(KEY_Porcentaje)));
			    	 nota.setNota(d.getDouble(d.getColumnIndex(KEY_Nota)));
			    	 nota.setIdasignatura(d.getInt(d.getColumnIndex(KEY_IdAsignaturaReferencia)));
			    	 Asignatura.setNotas(nota,y);
			    	 d.moveToNext();
		    		}
    	    }
    	    return Asignatura;
    	}
     
     //Funcion para seleciona notas a traves de nombre de asignatura
     public ClaseCuatrimestres getCuatrimestreDataBase (String NombreCuatrimestre) {
    	    //Selecionamos la id de asignatura, para sacar las notas q referencien a ese id
    	    SQLiteDatabase db = this.getReadableDatabase();
    	 
    	    String selectQuery = "SELECT * FROM " + TABLE_CUATRIMESTRE + " WHERE "
    	            + KEY_NombreCuatrimeste + " = '" +NombreCuatrimestre+ "'";
    	 
    	    Cursor c = db.rawQuery(selectQuery, null);
    	    ClaseCuatrimestres Cuatrimestre = new ClaseCuatrimestres();
    	    //int id = c.getInt(c.getColumnIndex(KEY_IdAsignatura));
    	    if (c != null){
    	        c.moveToFirst();    
    	        //Asignamos parametros a las clase asignatura
    	   
    	        Cuatrimestre.setId(c.getInt(c.getColumnIndex(KEY_IdCuatrimestre)));
    	        Cuatrimestre.setCuatrimestre(c.getString(c.getColumnIndex(KEY_NombreCuatrimeste)));   	        

    	    }
    	    //Recogemos el idasignatura y sacamos las notas
    	    
    	    String selectQuery2 = "SELECT * FROM " + TABLE_ASIGNATURA + " WHERE "
    	            + KEY_IdCuatrimestreReferencia + " = " + c.getInt(c.getColumnIndex(KEY_IdCuatrimestre));
    	    //cambio id
    	 
    	    Cursor d = db.rawQuery(selectQuery2, null);
    	   // Log.d("d",""+d.getPosition());
    	    if (d != null){
    	    	d.moveToFirst();
	    	    for (int y = 0; y < d.getCount(); y++)
		    		{
	    	    	 ClaseAsignaturas Asignaturas = new ClaseAsignaturas();
	    	    	 Asignaturas.setId(d.getInt(d.getColumnIndex(KEY_IdAsignatura)));
	    	    	 Asignaturas.setNombre(d.getString(d.getColumnIndex(KEY_NombreAsignatura)));
	    	    	 Asignaturas.setIdcuatrimestre(d.getInt(d.getColumnIndex(KEY_IdCuatrimestreReferencia)));
			    	 Cuatrimestre.setAsignatura(Asignaturas, y);
			    	 d.moveToNext();
			    	 }
	    	  }
    	    return Cuatrimestre;
    	}
     
     public ClaseClassMarks getClassMarks ()
     {
    	SQLiteDatabase db = this.getReadableDatabase();
 	    String selectQuery = "SELECT * FROM " + TABLE_CUATRIMESTRE;
 	 
 	    Cursor c = db.rawQuery(selectQuery, null);
 	    ClaseClassMarks Classmarks = new ClaseClassMarks();
 	    
 	    if (c != null){
	        c.moveToFirst();    
	        //Asignamos parametros a las clase asignatura
	        for (int y = 0; y < c.getCount(); y++)
			{
	         ClaseCuatrimestres Cuatrimestre = new ClaseCuatrimestres();
 	         Cuatrimestre.setId(c.getInt(c.getColumnIndex(KEY_IdCuatrimestre)));
 	         Cuatrimestre.setCuatrimestre(c.getString(c.getColumnIndex(KEY_NombreCuatrimeste)));
 	         Classmarks.setCuatrimestre(Cuatrimestre, y);
	   	     c.moveToNext();
	   	     }
	    }
    	 return  Classmarks;
     }
     
 public String NombreAsignatura(int idasignatura){
    	 
    	 SQLiteDatabase cn = this.getReadableDatabase();
    	 Cursor cursor1 = cn.rawQuery("SELECT "+ KEY_NombreAsignatura +" FROM "+ TABLE_ASIGNATURA +" WHERE " +KEY_IdAsignatura +" = '"+ idasignatura+"'",null);
    	 String columntotal=null;
    	 
    	 if(cursor1.moveToFirst()) {
    		 
	         columntotal = cursor1.getString(0);
	     }
    	 return columntotal; 
     }
     
     public String NombreCuatri(int idcuatri){
    	 
    	 SQLiteDatabase cn = this.getReadableDatabase();
    	 Cursor cursor1 = cn.rawQuery("SELECT "+ KEY_NombreCuatrimeste +" FROM "+ TABLE_CUATRIMESTRE +" WHERE " +KEY_IdCuatrimestre +" = '"+ idcuatri+"'",null);
    	 String columntotal=null;
    	 
    	 if(cursor1.moveToFirst()) {
    		 
	         columntotal = cursor1.getString(0);
	     }
    	 return columntotal;
     } 

     public int IdNota(String Evaluable){
    	 
    	 SQLiteDatabase cn = this.getReadableDatabase();
    	 Cursor cursor1 = cn.rawQuery("SELECT "+ KEY_IdNotas +" FROM "+ TABLE_NOTAS +" WHERE " +KEY_Evaluable +" = '"+Evaluable+"'",null);
    	 int columntotal = 0;
    	 
    	 if(cursor1.moveToFirst()) {
    		 
	         columntotal = cursor1.getInt(0);
	     }
    	 
    	 return columntotal;
    	 
     } 
     
     public void EliminarNota(int id) {
    	    SQLiteDatabase db = this.getWritableDatabase();
    	    db.delete(TABLE_NOTAS, KEY_IdNotas + " = ?",new String[] { String.valueOf(id) });
    	}
     
     public void EliminarAsignatura(int id) {
 	    SQLiteDatabase db = this.getWritableDatabase();
 	    ClaseAsignaturas asigantura =getAsignaturaDataBase(NombreAsignatura(id));
 	    int i=0;
 	    db.delete(TABLE_ASIGNATURA, KEY_IdAsignatura + " = ?",new String[] { String.valueOf(id) });
 	    while(i<asigantura.getLon())
	    {
	    	EliminarNota(asigantura.getNotas(i).getId());
	    	i++;
	    }
 	}
     public void EliminarCuatrimestre(int id) {
 	    SQLiteDatabase db = this.getWritableDatabase();
 	    ClaseCuatrimestres cuatri =getCuatrimestreDataBase(NombreCuatri(id));
 	    int i=0;
 	    int y=0;
 	    db.delete(TABLE_CUATRIMESTRE, KEY_IdCuatrimestre+ " = ?", new String[] { String.valueOf(id) });
	    while (y<cuatri.getLon())
	    {
	 	    while(i<cuatri.getAsignatura(y).getLon())
	 	    {
	 	    	EliminarNota(cuatri.getAsignatura(y).getNotas(i).getId());
	 	    	i++;
	 	    }
	 	    EliminarAsignatura(cuatri.getAsignatura(y).getId());
	 	    y++;
	    }
	}
     
     public int updateNota(ClaseNotas nota) {
    	    SQLiteDatabase db = this.getWritableDatabase();
    	 
    	    ContentValues values = new ContentValues();
    	    values.put(KEY_Evaluable, nota.getEvaluable());
    	    values.put(KEY_Nota, nota.getNota());
    	    values.put(KEY_Porcentaje, nota.getPorcentaje());
    	    values.put(KEY_IdAsignaturaReferencia, nota.getIdasignatura());
    	 
    	    // updating row
    	    return db.update(TABLE_NOTAS, values, KEY_IdNotas + " = ?",new String[] { String.valueOf(nota.getId()) });
    	}
    
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
