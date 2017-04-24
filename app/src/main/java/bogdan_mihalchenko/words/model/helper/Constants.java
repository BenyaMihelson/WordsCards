package bogdan_mihalchenko.words.model.helper;

/**
 * Created by Shipohvost on 26.09.2016.
 */

public class Constants {

    public static final class REFERENCE{
        public static final int OVER_TIME = 86400;

    }

    public static final class Config{
        public static final String PACKAGE_NAME = "bogdan_mihalchenko.words.";



    }

    public static final class DATABASE{
        public static final String DB_NAME = "MyDb";
        public static final int DB_VERSION = 1;
        public static final String WORDS_TABLE_NAME = "words";
        public static final String CATEGORIES_TABLE_NAME = "categories";
        public static final String WORDS_DROP_QUERY = "DROP TABLE IF EXISTS " + WORDS_TABLE_NAME;
        public static final String CATEGORIES_DROP_QUERY = "DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME;

        /*
         *fields of categories table
         */
        public static final String CATEGORY_AUTO_ID = "id";
        public static final String CATEGORY_TITLE  = "title";
        public static final String CATEGORY_STATUS  = "status";

        /*
         *fields of words table
         */
        public static final String WORD_AUTO_ID = "id";
        public static final String WORD_ORIGIN = "word_ru";
        public static final String WORD_TRANSLATE = "word_en";
        public static final String WORD_TRANSCRIPTION = "transcription";
        public static final String WORD_STATUS = "status";
        public static final String WORD_CATEGORY = "category";


        public static final String CREATE_CATEGORIES_TABLE_QUERY = "create table " + CATEGORIES_TABLE_NAME +
                " (" + CATEGORY_AUTO_ID + " integer primary key autoincrement, " + CATEGORY_TITLE + " text, " +
                CATEGORY_STATUS + " int);";

        public static final String CREATE_WORDS_TABLE_QUERY = "create table " + WORDS_TABLE_NAME +
                " (" + WORD_AUTO_ID + " integer primary key autoincrement, " + WORD_ORIGIN + " text, " +
                WORD_TRANSLATE + " text, " + WORD_TRANSCRIPTION + " text, " + WORD_STATUS + " int, " +
                WORD_CATEGORY + " int);";

        public static final String DROP_CATEGORIES_TABLE_QUERY = "DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME;
        public static final String DROP_WORDS_TABLE_QUERY = "DROP TABLE IF EXISTS " + WORDS_TABLE_NAME;

        public static final String GET_CATEGORIES_QUERRY = "SELECT * FROM " + CATEGORIES_TABLE_NAME + " ORDER by " + CATEGORY_AUTO_ID + " DESC" ;
        public static final String DELETE_CATEGORY_ITEM = "DELETE FROM " + CATEGORIES_TABLE_NAME + " WHERE "
                + CATEGORY_AUTO_ID + " = ";

        public static final String GET_WORDS_QUERY = "SELECT * FROM " + WORDS_TABLE_NAME;

        public static final String DELETE_WORD_ITEM_QUERY = "DELETE FROM " + WORDS_TABLE_NAME + " WHERE "
                + WORD_AUTO_ID + " = ";
        public static final String DELETE_WORDS_FROM_CATEGORY = "DELETE FROM " + WORDS_TABLE_NAME + " WHERE " +
                WORD_CATEGORY + " = ";

    }

}