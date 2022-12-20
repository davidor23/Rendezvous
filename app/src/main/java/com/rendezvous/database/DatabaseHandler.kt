package com.rendezvous.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.rendezvous.models.RendezvousModel

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "RendezvousDatabase" // Database name
        private const val TABLE_RENDEZVOUS = "RendezvousTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_RENDEZVOUS_TABLE = ("CREATE TABLE " + TABLE_RENDEZVOUS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_RENDEZVOUS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_RENDEZVOUS")
        onCreate(db)
    }

    /**
     * Function to insert a Rendezvous details to SQLite Database.
     */
    fun addRendezvous(rendezvous: RendezvousModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, rendezvous.title) // RendezvousModelClass TITLE
        contentValues.put(KEY_IMAGE, rendezvous.image) // RendezvousModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            rendezvous.description
        ) // RendezvousModelClass DESCRIPTION
        contentValues.put(KEY_DATE, rendezvous.date) // RendezvousModelClass DATE
        contentValues.put(KEY_LOCATION, rendezvous.location) // RendezvousModelClass LOCATION
        contentValues.put(KEY_LATITUDE, rendezvous.latitude) // RendezvousModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, rendezvous.longitude) // RendezvousModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_RENDEZVOUS, null, contentValues)
        //2nd argument is String containing nullColumnHack  

        db.close() // Closing database connection
        return result
    }

    /**
     * Function to read all the list of Rendezvous data which are inserted.
     */
    fun getRendezvousList(): ArrayList<RendezvousModel> {

        // A list is initialize using the data model class in which I will add the values from cursor.
        val rendezvousList: ArrayList<RendezvousModel> = ArrayList()

        val selectQuery = "SELECT  * FROM $TABLE_RENDEZVOUS" // Database select query

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = RendezvousModel(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                            cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )
                    rendezvousList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return rendezvousList
    }

    /**
     * Function to update record
     */
    fun updateRendezvous(rendezvous: RendezvousModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, rendezvous.title) // RendezvousModelClass TITLE
        contentValues.put(KEY_IMAGE, rendezvous.image) // RendezvousModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            rendezvous.description
        ) // RendezvousModelClass DESCRIPTION
        contentValues.put(KEY_DATE, rendezvous.date) // RendezvousModelClass DATE
        contentValues.put(KEY_LOCATION, rendezvous.location) // RendezvousModelClass LOCATION
        contentValues.put(KEY_LATITUDE, rendezvous.latitude) // RendezvousModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, rendezvous.longitude) // RendezvousModelClass LONGITUDE

        // Updating Row
        val success =
            db.update(TABLE_RENDEZVOUS, contentValues, KEY_ID + "=" + rendezvous.id, null)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    /**
     * Function to delete happy place details.
     */
    fun deleteRendezvous(rendezvous: RendezvousModel): Int {
        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE_RENDEZVOUS, KEY_ID + "=" + rendezvous.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}
