package bogdan_mihalchenko.words.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bogdan_mihalchenko.words.model.helper.Constants;
import bogdan_mihalchenko.words.model.pojo.Category;
import bogdan_mihalchenko.words.model.pojo.Word;

/**
 * Created by Shipohvost on 26.09.2016.
 */

public class DataBase extends SQLiteOpenHelper {
    public static final String  LOG_TAG = "DataBase";
    protected Cursor cursor;

    public DataBase(Context context) {
        super(context, Constants.DATABASE.DB_NAME, null, Constants.DATABASE.DB_VERSION);
    }

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Constants.DATABASE.CREATE_CATEGORIES_TABLE_QUERY);
            db.execSQL(Constants.DATABASE.CREATE_WORDS_TABLE_QUERY);
        }catch (SQLException ex){
            Log.d(LOG_TAG, ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Constants.DATABASE.DROP_CATEGORIES_TABLE_QUERY);
        db.execSQL(Constants.DATABASE.DROP_WORDS_TABLE_QUERY);
        this.onCreate(db);
    }


   /*
   * Categories data UTIL
   * */

    public List<Category> getCategoriesList(){
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(Constants.DATABASE.GET_CATEGORIES_QUERRY, null);

        List<Category> list = new ArrayList<>();

        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    Category category =  new Category();

                    category.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Constants.DATABASE.CATEGORY_AUTO_ID)));
                    category.setCategoryTitle(cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATABASE.CATEGORY_TITLE)));
                    category.setComplete(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.DATABASE.CATEGORY_STATUS))>0);

                    Log.d(LOG_TAG, category.getId() + "catrgory NUMBER");
                    Log.d(LOG_TAG, category.getCategoryTitle() + "catrgory Title");
                    list.add(category);

                }
                while (cursor.moveToNext());
            }


        }
        db.close();

        return list;


    }


    public void addCategoryItem(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Constants.DATABASE.CATEGORY_TITLE, category.getCategoryTitle());
        cv.put(Constants.DATABASE.CATEGORY_STATUS, 0);

        try{

            db.insert(Constants.DATABASE.CATEGORIES_TABLE_NAME, null, cv);

        }catch (Exception e){

            Log.d(LOG_TAG, e+" log");

        }
        db.close();
    }

    public void deleteCategoryItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL(Constants.DATABASE.DELETE_CATEGORY_ITEM + String.valueOf(id));
            db.execSQL(Constants.DATABASE.DELETE_WORDS_FROM_CATEGORY + String.valueOf(id));
            //db.rawQuery(Constants.DATABASE.DELETE_CATEGORY_ITEM + String.valueOf(id), null);

        }catch (Exception e){
            Log.d(LOG_TAG, e+" log");
        }
        db.close();
    }

    public void editCategoryItem(long id, String string) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constants.DATABASE.CATEGORY_TITLE, string);
        db.update(Constants.DATABASE.CATEGORIES_TABLE_NAME, cv,
                Constants.DATABASE.CATEGORY_AUTO_ID + "=" + id, null);
        db.close();

    }
    /*
   * The end of Categories data UTIL
   * */

    public List<Word> getWordsList(long categoryId, boolean isFromGame){



        SQLiteDatabase db = this.getReadableDatabase();

        if(isFromGame){
            cursor = db.rawQuery(Constants.DATABASE.GET_WORDS_QUERY + " WHERE " +
                    Constants.DATABASE.WORD_CATEGORY + " = " + categoryId +" AND " +
                    Constants.DATABASE.WORD_STATUS + " = 0"+ " ORDER BY " +
                    Constants.DATABASE.WORD_AUTO_ID + " DESC", null);


        }else{
            cursor = db.rawQuery(Constants.DATABASE.GET_WORDS_QUERY + " WHERE " +
                    Constants.DATABASE.WORD_CATEGORY + " = " + categoryId + " ORDER BY " +
                    Constants.DATABASE.WORD_AUTO_ID + " DESC", null);

        }

        List<Word> list = new ArrayList<>();

        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    Word word = new Word();

                    word.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Constants.DATABASE.WORD_AUTO_ID)));
                    word.setWordOrign(cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATABASE.WORD_ORIGIN)));
                    word.setWordTrancription(cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATABASE.WORD_TRANSCRIPTION)));
                    word.setWordTranslate(cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATABASE.WORD_TRANSLATE)));
                    word.setWordStatus(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.DATABASE.WORD_STATUS))>0);
                    word.setWordCategory(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.DATABASE.WORD_CATEGORY)));

                    Log.d(LOG_TAG, word.getId() + "catrgory NUMBER");
                    Log.d(LOG_TAG, word.getWordOrign() + "catrgory Title");

                    list.add(word);

                }
                while (cursor.moveToNext());
            }


        }
        db.close();

        return list;
    }

    public void addWordItem(Word word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Constants.DATABASE.WORD_ORIGIN, word.getWordOrign());
        cv.put(Constants.DATABASE.WORD_TRANSLATE, word.getWordTranslate());
        cv.put(Constants.DATABASE.WORD_TRANSCRIPTION, word.getWordTrancription());
        cv.put(Constants.DATABASE.WORD_CATEGORY, word.getWordCategory());
        cv.put(Constants.DATABASE.WORD_STATUS, 0);

        try{
            db.insert(Constants.DATABASE.WORDS_TABLE_NAME, null, cv);

        }catch (Exception e){

            Log.d(LOG_TAG, e+" log");

        }
        db.close();
    }

    public void editWordItem(long id, String wordOrigin, String wordTranslate, String wordTranscription){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Constants.DATABASE.WORD_ORIGIN, wordOrigin);
        cv.put(Constants.DATABASE.WORD_TRANSLATE, wordTranslate);
        cv.put(Constants.DATABASE.WORD_TRANSCRIPTION, wordTranscription);

        db.update(Constants.DATABASE.WORDS_TABLE_NAME, cv,
                Constants.DATABASE.WORD_AUTO_ID + "=" + id, null);
        db.close();

    }

    public void deleteWordItem(long id){

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL(Constants.DATABASE.DELETE_WORD_ITEM_QUERY+ String.valueOf(id));
            //db.rawQuery(Constants.DATABASE.DELETE_CATEGORY_ITEM + String.valueOf(id), null);

        }catch (Exception e){
            Log.d(LOG_TAG, e+" log");
        }
        db.close();


    }


    public void updateWordStatus(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.DATABASE.WORD_STATUS, 1);
        db.update(Constants.DATABASE.WORDS_TABLE_NAME, cv,
                Constants.DATABASE.WORD_AUTO_ID + "=" + id, null);
        db.close();
    }

    public void uncompleteAllWordStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.DATABASE.WORD_STATUS, 0);
        db.update(Constants.DATABASE.WORDS_TABLE_NAME, cv, null, null);
        db.close();


    }


    public void updateCategoryStatus(long id, boolean isComplete) {
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(isComplete){
            cv.put(Constants.DATABASE.CATEGORY_STATUS, 1);
        }else{
            cv.put(Constants.DATABASE.CATEGORY_STATUS, 0);
        }
        db.update(Constants.DATABASE.CATEGORIES_TABLE_NAME, cv,
                Constants.DATABASE.CATEGORY_AUTO_ID + "=" + id, null);

        db.close();
    }


}
