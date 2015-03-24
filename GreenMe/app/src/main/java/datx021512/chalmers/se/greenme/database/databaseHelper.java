package datx021512.chalmers.se.greenme.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

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
            mArrayList.add((categories.getString(categories.getColumnIndex("NAME")) + "   " +
                    categories.getString(categories.getColumnIndex("COUNTRY"))+ "   Ekologisk: " +
                    categories.getString(categories.getColumnIndex("EKOLOGICAL"))+ "   " +
                    categories.getString(categories.getColumnIndex("IMPACT")))); //add the item
            categories.moveToNext();
        }

        categories = getMeanCategories();
        categories.moveToFirst();
        while(!categories.isAfterLast()){
            mArrayList.add((categories.getString(categories.getColumnIndex("NAME")) + "   " + categories.getString(categories.getColumnIndex("IMPACT")))); //add the item
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

}
