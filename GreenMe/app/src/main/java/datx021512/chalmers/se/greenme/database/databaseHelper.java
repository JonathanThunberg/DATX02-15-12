package datx021512.chalmers.se.greenme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import datx021512.chalmers.se.greenme.adapters.ShopItem;

/**
 * Created by Tuna on 18/02/15.
 */
public class DatabaseHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "EnviromentalImpact.sqlite";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + "ShoppingListsview" + "(NAME VARCHAR,DATE VARCHAR, AMOUNT DOUBLE);";
        db.execSQL(CREATE_CONTACTS_TABLE);
        CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + "OwnCategories" + "(NAME VARCHAR, IMPACT DOUBLE, STANDARDWEIGHT DOUBLE);";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.close();
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
        db.close();
        return c;
    }

    public Cursor getEkoCategories() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"NAME","COUNTRY","IMPACT","EKOLOGIC"};
        String sqlTables = "vegCategories";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();
        db.close();
        return c;
    }


    public Cursor getImpactFromDatabase(String sqlTables,String s){
          SQLiteDatabase db = getReadableDatabase();
          Cursor c = db.rawQuery("SELECT * FROM "+sqlTables+" WHERE NAME LIKE '%"+s+"%'", null);

         // SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
         // String [] sqlSelect = {"IMPACT"};
         // qb.setTables(sqlTables);
          //String []selectionArgs = {s + "%"};
//          Cursor c = qb.query(db, sqlSelect, null, selectionArgs,
  //                null, null, null);
          c.moveToFirst();
            db.close();
          return c;
    }

    public Cursor getFromDatabase(String sqlTables, String Argument){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT "+Argument+" FROM "+sqlTables, null);

        c.moveToFirst();
        db.close();
        return c;
    }


    public ArrayList<String> getImpact(String st){

        SQLiteDatabase db = getWritableDatabase();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + "OwnCategories" + "(NAME VARCHAR, IMPACT DOUBLE, EKOLOGIC INTEGER);";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.close();

        ArrayList<String> mArrayList = new ArrayList<>();
        for(String s :st.split("\\s+")) {
            Cursor categories = getImpactFromDatabase("vegCategories", s);
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

        public ArrayList<Integer> getEco (String st){

        ArrayList<Integer> mArrayList = new ArrayList<>();
        Log.d("TEST", "!!!! Inuti GetEco()");
            Cursor categories = getEcoFromDatabase("vegCategories", st);
            categories.moveToFirst();
            while (!categories.isAfterLast()) {
                mArrayList.add((categories.getInt(categories.getColumnIndex("EKOLOGIC"))));
            categories.moveToNext();
            }
        //The item is no Eco so set it.
        if (mArrayList.size() == 0) {
            mArrayList.add(0);
        }

        return mArrayList;
    }

    public ArrayList<String> getImpactName(String st){
        ArrayList<String> mArrayList = new ArrayList<>();
        for(String s :st.split("\\s+")) {
            Cursor categories = getImpactFromDatabase("vegCategories", s);
            categories.moveToFirst();
            String temp;
            while (!categories.isAfterLast()) {
                temp =categories.getString(categories.getColumnIndex("NAME"))+"  "+categories.getString(categories.getColumnIndex("COUNTRY"));
                if(categories.getInt(categories.getColumnIndex("EKOLOGIC"))>=1) {
                    temp += " Ekologisk";
                }
                mArrayList.add(temp);
                //+"  Ekologisk:"+categories.getString(categories.getColumnIndex("EKOLOGIC"))));
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
        ContentValues values = new ContentValues();
        values.put("NAME", text);
        values.put("IMPACT", text2);
        db.insert("OwnCategories", null, values);
        db.close();
    }
    public void saveList(List<ShopItem> l,String name1) {
        String name=name1.replaceAll("\\s", "");
        SQLiteDatabase db = getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + "Shoppingview"+name + "(NAME VARCHAR, AMOUNT DOUBLE, COUNTRY VARCHAR, EKOLOGIC INTEGER);";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL("delete from " + "Shoppingview" + name);
        db.close();
        double totalimpact =0;
        for(ShopItem s : l){
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NAME", s.getmName());
            values.put("AMOUNT", s.getQuantity());
            values.put("COUNTRY", s.getCountry());
            values.put("EKOLOGIC", s.getEco());
            Log.d("database", "database: " + s.getQuantity());
            db.insert("Shoppingview"+name, null, values);
            db.close();
            totalimpact += s.getmCO2()*s.getQuantity();
        }
        db = getWritableDatabase();
        db.execSQL("UPDATE ShoppingListsview SET AMOUNT='" + totalimpact + "' WHERE NAME='" + name1 + "'");
        db.close();

    }

    public ArrayList<ShopItem> getSavedList(String name) {
        ArrayList<ShopItem> savedList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + "Shoppingview"+name.replaceAll("\\s","") + "(NAME VARCHAR, AMOUNT DOUBLE, COUNTRY VARCHAR, EKOLOGIC INTEGER);";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.close();

        Cursor categories = getFromDatabase("Shoppingview"+name.replaceAll("\\s",""), "NAME, AMOUNT, COUNTRY, EKOLOGIC");
        while (!categories.isAfterLast()) {
            savedList.add(
                    new ShopItem(categories.getString(categories.getColumnIndex("NAME")),
                            Double.parseDouble(getImpact(categories.getString(categories.getColumnIndex("NAME"))).get(0)),
                            categories.getString(categories.getColumnIndex("COUNTRY")),
                            Double.parseDouble(categories.getString(categories.getColumnIndex("AMOUNT"))),
                            Integer.parseInt(categories.getString(categories.getColumnIndex("EKOLOGIC")))));
            Log.d("database", "database: " + categories.getColumnIndex("AMOUNT"));
            categories.moveToNext();
        }
        return savedList;
    }

    public void createNewList(String s, String format) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", s);
        values.put("DATE", format);
        values.put("AMOUNT", 0);
        db.insert("ShoppingListsview", null, values);
        db.close();
    }

    public ArrayList<ShopItem> getShoppingLists() {
        ArrayList<ShopItem> savedList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor categories = getFromDatabase("ShoppingListsview", "NAME, AMOUNT, DATE");
        while (!categories.isAfterLast()) {
            savedList.add(
                    new ShopItem(categories.getString(categories.getColumnIndex("NAME")),
                            Double.parseDouble(categories.getString(categories.getColumnIndex("AMOUNT"))),
                            categories.getString(categories.getColumnIndex("DATE"))));
            categories.moveToNext();
        }
        return savedList;
    }

    public void removeShoppingList(String s) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("ShoppingListsview", "NAME='" + s+"'", null);
        db.execSQL("delete from " + "Shoppingview" + s.replaceAll("\\s", ""));
        db.close();
    }

    public Cursor getEcoFromDatabase(String sqlTables,String s){
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT NAME,EKOLOGIC FROM " + sqlTables + " WHERE NAME LIKE '%" + s + "%'", null);
            c.moveToFirst();
            db.close();
            return c;
    }
    public int getTotalImpact () {
        int total = 0;
        ArrayList<ShopItem> shopinglists = getShoppingLists();

        for (ShopItem s: shopinglists) {
            total += s.getmCO2();
        }
        return total;
    }

}
