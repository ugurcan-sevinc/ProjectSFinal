package com.ugrcaan.projectsfinal.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.math.log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // constants for database name and version
        const val DATABASE_NAME = "projectsdatabase.db"
        const val DATABASE_VERSION = 1
    }

    // constants for table and column names
    val TABLE_CARDS = "cards"
    val COLUMN_CARD_NAME = "name"
    val COLUMN_CARD_NUMBER = "number"
    val COLUMN_CARD_ACTIVE = "active"
    val COLUMN_CARD_TYPE = "type"

    val TABLE_BALANCES = "balances"
    val COLUMN_BALANCE_AMOUNT = "amount"
    val COLUMN_BALANCE_USED_CARD = "usedCard"
    val COLUMN_BALANCE_TIME = "time"

    val TABLE_DRIVE_HISTORY = "driveHistory"
    val COLUMN_DRIVE_HISTORY_SC_ID = "scId"
    val COLUMN_DRIVE_HISTORY_DISTANCE = "distance"
    val COLUMN_DRIVE_HISTORY_PRICE = "price"


    // SQL statement to create the database
    private val SQL_CREATE_TABLE_CARDS = "CREATE TABLE $TABLE_CARDS(" +
            "$COLUMN_CARD_NAME TEXT," +
            "$COLUMN_CARD_NUMBER TEXT," +
            "$COLUMN_CARD_ACTIVE INTEGER," +
            "$COLUMN_CARD_TYPE INTEGER" +
            ")"

    private val SQL_CREATE_TABLE_BALANCES = "CREATE TABLE $TABLE_BALANCES(" +
            "$COLUMN_BALANCE_AMOUNT TEXT," +
            "$COLUMN_BALANCE_USED_CARD TEXT," +
            "$COLUMN_BALANCE_TIME TEXT" +
            ")"

    private val SQL_CREATE_TABLE_DRIVE_HISTORY = "CREATE TABLE $TABLE_DRIVE_HISTORY(" +
            "$COLUMN_DRIVE_HISTORY_SC_ID TEXT," +
            "$COLUMN_DRIVE_HISTORY_DISTANCE TEXT," +
            "$COLUMN_DRIVE_HISTORY_PRICE TEXT" +
            ")"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_BALANCES)
        db?.execSQL(SQL_CREATE_TABLE_CARDS)
        db?.execSQL(SQL_CREATE_TABLE_DRIVE_HISTORY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // drop the table if it exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BALANCES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CARDS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_DRIVE_HISTORY")
        // create the table again
        onCreate(db)
    }

    fun onCreateAgain() {
        val db = writableDatabase
        db.execSQL(SQL_CREATE_TABLE_DRIVE_HISTORY)
    }

    fun insertCard(card: Card) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CARD_NAME, card.name)
            put(COLUMN_CARD_NUMBER, card.number)
            put(COLUMN_CARD_ACTIVE, card.active)
            put(COLUMN_CARD_TYPE, card.type)
        }
        db.insert(TABLE_CARDS, null, values)
    }

    fun updateCard(card: Card) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CARD_NAME, card.name)
            put(COLUMN_CARD_NUMBER, card.number)
            put(COLUMN_CARD_ACTIVE, card.active)
            put(COLUMN_CARD_TYPE, card.type)
        }
        db.update(TABLE_CARDS, values, "$COLUMN_CARD_NUMBER = ?", arrayOf(card.number))
    }

    fun activateCard(card: Card) {
        // Set all cards to inactive
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CARD_ACTIVE, 0)
        }
        db.update(TABLE_CARDS, values, null, null)

        // Set the selected card to active
        val values2 = ContentValues().apply {
            put(COLUMN_CARD_ACTIVE, 1)
        }
        db.update(TABLE_CARDS, values2, "$COLUMN_CARD_NUMBER = ?", arrayOf(card.number))
    }


    fun deleteCard(card: Card) {
        val db = writableDatabase
        db.delete(TABLE_CARDS, "$COLUMN_CARD_NUMBER = ?", arrayOf(card.number))
    }

    fun deleteAllCards() {
        val db = writableDatabase
        db.delete(TABLE_CARDS, null, null)
    }

    fun getActiveCards(): ArrayList<Card> {
        val db = readableDatabase
        val cursor = db.query(TABLE_CARDS, null, "$COLUMN_CARD_ACTIVE = 1", null, null, null, null)
        val cards = ArrayList<Card>()
        while (cursor.moveToNext()) {
            val card = Card(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_ACTIVE)) == 1,
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_TYPE))
            )
            cards.add(card)
        }
        cursor.close()
        return cards
    }

    fun getAllCards(): ArrayList<Card> {
        val db = readableDatabase
        val cursor = db.query(TABLE_CARDS, null, null, null, null, null, null)
        val cards = ArrayList<Card>()
        while (cursor.moveToNext()) {
            val card = Card(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_ACTIVE)) == 1,
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_TYPE))
            )
            cards.add(card)
        }
        cursor.close()
        return cards
    }


    fun insertDriveHistory(driveHistory: DriveHistory) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DRIVE_HISTORY_SC_ID, driveHistory.scId)
            put(COLUMN_DRIVE_HISTORY_DISTANCE, driveHistory.distance)
            put(COLUMN_DRIVE_HISTORY_PRICE, driveHistory.price)
        }
        db.insert(TABLE_DRIVE_HISTORY, null, values)
    }

    fun updateDriveHistory(driveHistory: DriveHistory) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DRIVE_HISTORY_SC_ID, driveHistory.scId)
            put(COLUMN_DRIVE_HISTORY_DISTANCE, driveHistory.distance)
            put(COLUMN_DRIVE_HISTORY_PRICE, driveHistory.price)
        }
        db.update(TABLE_DRIVE_HISTORY, values, "$COLUMN_DRIVE_HISTORY_SC_ID = ?", arrayOf(driveHistory.scId))
    }

    fun deleteDriveHistory(driveHistory: DriveHistory) {
        val db = writableDatabase
        db.delete(TABLE_DRIVE_HISTORY, "$COLUMN_DRIVE_HISTORY_SC_ID = ?", arrayOf(driveHistory.scId))
    }

    fun deleteAllDriveHistory() {
        val db = writableDatabase
        db.delete(TABLE_DRIVE_HISTORY, null, null)
    }

    fun getDriveHistoryForScId(scId: String): List<DriveHistory> {
        val db = readableDatabase
        val cursor = db.query(TABLE_DRIVE_HISTORY, null, "$COLUMN_DRIVE_HISTORY_SC_ID = ?", arrayOf(scId), null, null, null)
        val driveHistoryList = mutableListOf<DriveHistory>()
        while (cursor.moveToNext()) {
            val driveHistory = DriveHistory(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRIVE_HISTORY_SC_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRIVE_HISTORY_DISTANCE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRIVE_HISTORY_PRICE))
            )
            driveHistoryList.add(driveHistory)
        }
        cursor.close()
        return driveHistoryList
    }

    fun getAllDriveHistory(): ArrayList<DriveHistory> {
        val db = readableDatabase
        val cursor = db.query(TABLE_DRIVE_HISTORY, null, null, null, null, null, null)
        val driveHistoryList = ArrayList<DriveHistory>()
        while (cursor.moveToNext()) {
            val driveHistory = DriveHistory(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRIVE_HISTORY_SC_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRIVE_HISTORY_DISTANCE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRIVE_HISTORY_PRICE))
            )
            driveHistoryList.add(driveHistory)
        }
        cursor.close()
        return driveHistoryList
    }


    fun insertBalance(balance: Balance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BALANCE_AMOUNT, balance.amount)
            put(COLUMN_BALANCE_USED_CARD, balance.usedCard)
            put(COLUMN_BALANCE_TIME, balance.time)
        }
        db.insert(TABLE_BALANCES, null, values)
    }

    fun updateBalance(balance: Balance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BALANCE_AMOUNT, balance.amount)
            put(COLUMN_BALANCE_USED_CARD, balance.usedCard)
            put(COLUMN_BALANCE_TIME, balance.time)
        }
        db.update(TABLE_BALANCES, values, "$COLUMN_BALANCE_USED_CARD = ?", arrayOf(balance.usedCard))
    }

    fun deleteBalance(balance: Balance) {
        val db = writableDatabase
        db.delete(TABLE_BALANCES, "$COLUMN_BALANCE_USED_CARD = ?", arrayOf(balance.usedCard))
    }

    fun deleteAllBalance() {
        val db = writableDatabase
        db.delete(TABLE_BALANCES, null, null)
    }

    fun getBalances(): ArrayList<Balance> {
        val db = readableDatabase
        val cursor = db.query(TABLE_BALANCES, null, null, null, null, null, null)
        val balanceList = ArrayList<Balance>()
        while (cursor.moveToNext()) {
            val balance = Balance(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BALANCE_AMOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BALANCE_USED_CARD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BALANCE_TIME))
            )
            balanceList.add(balance)
        }
        cursor.close()
        return balanceList
    }

}
