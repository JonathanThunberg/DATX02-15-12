package datx021512.chalmers.se.greenme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Tuna on 18/02/15.
 */
public class databaseHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "EnviromentalImpact.sqlite";
    private static final int DATABASE_VERSION = 1;

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

  /**
   * How to use:
   * db = new databaseHelper(this);
    categories = db.getCategories(); // you would not typically call this on the main thread

    ArrayAdapter<String> itemsAdapter =
            new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);

    ListView lW = (ListView) findViewById(R.id.listView);

    lW.setAdapter(itemsAdapter);
   **/

    public ArrayList<String> getCategories () {
        Cursor categories = getEkoCategories();
        ArrayList<String> mArrayList = new ArrayList<>();
        categories.moveToFirst();
        while(!categories.isAfterLast()){
            if(!mArrayList.contains((categories.getString(categories.getColumnIndex("NAME"))))){
                    mArrayList.add((categories.getString(categories.getColumnIndex("NAME"))));
            }

                    /**+ "   " +
                    categories.getString(categories.getColumnIndex("COUNTRY"))+ "   Ekologisk: " +
                    categories.getString(categories.getColumnIndex("EKOLOGICAL"))+ "   " +
                    categories.getString(categories.getColumnIndex("IMPACT")))); //add the item**/
            categories.moveToNext();
        }

        categories = getMeanCategories();
        categories.moveToFirst();
        while(!categories.isAfterLast()){
            mArrayList.add((categories.getString(categories.getColumnIndex("NAME")) ));
                    //+ "   " + categories.getString(categories.getColumnIndex("IMPACT")))); //add the item
            categories.moveToNext();
        }

        return mArrayList;
    }



    public Cursor getMeanCategories() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"NAME", "IMPACT"};
        String sqlTables = "meanCategories";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getEkoCategories() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"NAME","COUNTRY","IMPACT","EKOLOGICAL"};
        String sqlTables = "vegetableCategories";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }
    public Cursor getImpactFromDatabase(String sqlTables,String s){
          SQLiteDatabase db = getReadableDatabase();
          Cursor c = db.rawQuery("SELECT NAME,IMPACT FROM "+sqlTables+" WHERE NAME LIKE '%"+s+"%'", null);

          SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
          String [] sqlSelect = {"IMPACT"};
          qb.setTables(sqlTables);
          String []selectionArgs = {s + "%"};
//          Cursor c = qb.query(db, sqlSelect, null, selectionArgs,
  //                null, null, null);
          c.moveToFirst();
          return c;
    }


    public ArrayList<String> getImpact(String st){
        ArrayList<String> mArrayList = new ArrayList<>();
        for(String s :st.split("\\s+")) {
            Cursor categories = getImpactFromDatabase("vegetableCategories", s);
            categories.moveToFirst();
            while (!categories.isAfterLast()) {
                mArrayList.add((categories.getString(categories.getColumnIndex("IMPACT"))));
                categories.moveToNext();
            }

            categories = getImpactFromDatabase("meanCategories", s);
            categories.moveToFirst();
            while (!categories.isAfterLast()) {
                mArrayList.add((categories.getString(categories.getColumnIndex("IMPACT"))));
                categories.moveToNext();
            }
            categories = getImpactFromDatabase("OwnCategories", s);
            categories.moveToFirst();
            while (!categories.isAfterLast()) {
                mArrayList.add((categories.getString(categories.getColumnIndex("IMPACT"))));
                categories.moveToNext();
            }
        }
        return mArrayList;
    }

    public ArrayList<String> getImpactName(String st){
        ArrayList<String> mArrayList = new ArrayList<>();
        for(String s :st.split("\\s+")) {
            Cursor categories = getImpactFromDatabase("vegetableCategories", s);
            categories.moveToFirst();
            while (!categories.isAfterLast()) {
                mArrayList.add((categories.getString(categories.getColumnIndex("NAME"))));
                categories.moveToNext();
            }
            categories = getImpactFromDatabase("meanCategories", s);
            categories.moveToFirst();
            while (!categories.isAfterLast()) {
                mArrayList.add((categories.getString(categories.getColumnIndex("NAME"))));
                categories.moveToNext();
            }
            categories = getImpactFromDatabase("OwnCategories", s);
            categories.moveToFirst();
            while (!categories.isAfterLast()) {
                mArrayList.add((categories.getString(categories.getColumnIndex("NAME"))));
                categories.moveToNext();
            }
        }
        return mArrayList;
    }

    public void createNewItem(String text, double text2) {
        SQLiteDatabase db = getWritableDatabase();
      /*  db.execSQL("INSERT INTO "
                + "OwnCategories"
                + "(NAME, IMPACT)"
                + " VALUES ('"+text+"', "+text2+");");
    */
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + "OwnCategories" + "(NAME VARCHAR, IMPACT DOUBLE);";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.close();
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", text);
        values.put("IMPACT", text2);
        db.insert("OwnCategories", null, values);
        db.close();

    }
}
