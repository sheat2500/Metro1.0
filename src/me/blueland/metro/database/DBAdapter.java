package me.blueland.metro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	// 数据库静态信息
	public static final String DATABASE_NAME = "myDB";
	public static final String DATABASE_TABLE = "favourite";
	public static final int DATABASE_VERSION = 1;
	public static final String _ID = "_id";
	public static final String STATIONNAME = "stationName";
	public static final String STATIONCODE = "stationCode";
	public static final String LINE = "line";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String RAIL = "rail";
	static final String DATABASE_CREATE = "CREATE TABLE favourite (_id	INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "line	TEXT NOT NULL, stationName	TEXT NOT NULL, stationCode	TEXT NOT NULL,"
			+ " latitude TEXT NOT NULL,longitude TEXT NOT NULL, rail TEXT NOT NULL);";

	// "CREATE TABLE notebook (_id	INTEGER PRIMARY KEY AUTOINCREMENT,"
	// +
	// "dateforshow	TEXT NOT NULL,dateforsave text nut null, content TEXT, title text);";

	DatabaseHelper DBHelper;
	final Context context;
	SQLiteDatabase db;

	public DBAdapter(Context ctx) {

		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}

	}

	// open the database
	public DBAdapter open() {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public Cursor getAllFavourite() {
		// query can order data set
		return db.query(false, DATABASE_TABLE, null, null, null, null, null,
				null, null, null);
	}

	public Cursor getFavourite(long rowId) {
		return db.query(true, DATABASE_TABLE, null, _ID + "=" + rowId, null,
				null, null, null, null, null);
	}

	public boolean deleteFavourite(long rowId) {
		return db.delete(DATABASE_TABLE, _ID + "=" + rowId, null) > 0;
	}

	// public boolean updateNote(long rowId, String dateforsave,
	// String dateforshow, String content, String title) {
	// ContentValues contentvalue = new ContentValues();
	// contentvalue.put(KEY_DATE_SAVE, dateforsave);
	// contentvalue.put(KEY_DATE_SHOW, dateforshow);
	// contentvalue.put(KEY_CONTENT, content);
	// contentvalue.put(KEY_TITLE, title);
	// return db.update(DATABASE_TABLE, contentvalue, KEY_ROWID + "=" + rowId,
	// null) > 0;
	// }

	public boolean insertFavourate(String line, String stationName,
			String stationCode, String latitude, String longitude, String rail) {
		ContentValues insertvalue = new ContentValues();
		insertvalue.put(LINE, line);
		insertvalue.put(STATIONNAME, stationName);
		insertvalue.put(STATIONCODE, stationCode);
		insertvalue.put(LATITUDE, latitude);
		insertvalue.put(LONGITUDE, longitude);
		insertvalue.put(RAIL, rail);
		return db.insert(DATABASE_TABLE, null, insertvalue) > 0;
	}

	// judge whether database has same entry
	public Cursor queryFavourate(String stationCode) {
		// query whether has same row data in the table
		// MutiParameter "owner=? and price=?", new String[]{ owner, price }
		return db.query(DATABASE_TABLE, null, STATIONCODE + "=?",
				new String[] { stationCode }, null, null, null, null);

	}
}
