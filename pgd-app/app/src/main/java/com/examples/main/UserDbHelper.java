package com.examples.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by YashJain on 12/24/2016.
 */
public class UserDbHelper extends SQLiteOpenHelper {
    private static final String DatabaseName = "userInfo.db";
    Context context;
    private static final int DatabaseVersion = 1;
    public static final String TABLE_NAME1 = "UserRegisteredProblems";
    public static final String TABLE_NAME2 = "inAbsence";
    private static final String Create_table1 = "CREATE TABLE " + TABLE_NAME1 + " (Srn TEXT);";
    private static final String Create_table2 = "CREATE TABLE " + TABLE_NAME2 + " (Image1 Text,D_Report Text,Loc1 Real,Loc2 Real,Type Integer,Description Text,Imagename Text);";

    public UserDbHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context=context;
        Log.e("Database operation", "Database created or opened");
    }


    public void onCreate(SQLiteDatabase db)//For creating the table.
    {
        db.execSQL(Create_table1);
        db.execSQL(Create_table2);
        Log.e("Database operation", "Tables created");
    }
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        onCreate(db);
    }
    public Long insertinformation(String Srn, String Operation, String Image1, String D_Report, double Loc1, double Loc2, int Type, String Description, String Imagename )//Adding the Entries into database.
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = 0;
        if (Operation.equals("insertSrn")) {
            contentValues.put("Srn", Srn);
            //contentValues.put("Loc1", Loc1);
            //contentValues.put("Loc2", Loc2);
            //contentValues.put("D_Report", D_Report);

            Cursor res=checkMainEntry(Srn);
            if (res.getCount()==0) {
                result = db.insert(TABLE_NAME1, null, contentValues);
                if (result == -1) {
                    Log.e("Database operation", "Srn not inserted");
                } else {
                    Log.e("Database operation", "Srn inserted");
                }
            }

        }
        else if (Operation.equals("insertInformation")){
            contentValues.put("Image1", Image1);
            contentValues.put("D_Report", D_Report);
            contentValues.put("Loc1", Loc1);
            contentValues.put("Loc2", Loc2);
            contentValues.put("Type", Type);
            contentValues.put("Description", Description);
            contentValues.put("Imagename", Imagename);

            result = db.insert(TABLE_NAME2, null, contentValues);
            if (result == -1) {

                Log.e("Database operation", "Information not inserted");
            } else {
                Toast.makeText(context,"problem saved",Toast.LENGTH_SHORT).show();
                Log.e("Local Db operation", "Information inserted");
            }

        }
        return result;
    }
    public Cursor CheckStatusforEntry(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME2,null );
        return res;
    }
    public Cursor toDisplayMycompalints(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME1,null );
        return res;
    }
    public int toDisplayDummyEntry(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME2,null );
        int v=res.getCount();;
        return v;
    }
    public Cursor CheckPosition(int Srn){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT Loc1,Loc2 FROM 'UserRegisteredProblems' Where Srn="+Srn+";",null );
        return res;
    }
    public void deleteEntries(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("delete  from "+TABLE_NAME2+";",null );
        int t=toDisplayDummyEntry();

    }
    public int delMainEntry(String Srn){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("delete from 'UserRegisteredProblems' where Srn='"+Srn+"';",null);
        //Log.e("Table 2","Single entry deleted");
        int count=res.getCount();
        return count;
    }
    public void deldummyEntry(String v){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("delete from 'inAbsence' where Imagename='"+v+"';",null);
        int y=res.getCount();
        int x=toDisplayDummyEntry();
        Log.e("Table 2","Single entry deleted "+x);
    }
    public Cursor checkMainEntry(String Srn){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from 'UserRegisteredProblems' where Srn="+Srn+";",null);
        return res;
    }
}
