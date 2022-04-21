package ru.dartanum.passwordmanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import ru.dartanum.passwordmanager.domain.Category;
import ru.dartanum.passwordmanager.domain.Field;
import ru.dartanum.passwordmanager.domain.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "DB";
    private static final int VERSION = 1;
    private static final String CREATE_TABLE_CATEGORY =
        "create table category (" +
            "id     integer primary key autoincrement," +
            "name   text unique not null," +
            "img    blob" +
        ")";
    private static final String CREATE_TABLE_FIELD =
        "create table field (" +
            "id         integer primary key autoincrement," +
            "title      text unique not null," +
            "is_secure  numeric" +
        ")";
    private static final String CREATE_TABLE_RECORD =
        "create table record (" +
            "id             integer primary key autoincrement," +
            "category_id    integer," +
            "name           text unique not null," +
            "foreign key(category_id) references category(id)" +
        ")";
    private static final String CREATE_TABLE_RECORD_FIELD =
        "create table record_field (" +
            "record_id   integer," +
            "field_id    integer," +
            "value       text not null," +
            "foreign key(record_id) references record(id)," +
            "foreign key(field_id) references field(id)" +
        ")";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String TITLE_COL = "title";
    private static final String IS_SECURE_COL = "is_secure";
    private static final String IMG_COL = "img";
    private static final String CATEGORY_ID_COL = "category_id";
    private static final String RECORD_ID_COL = "record_id";
    private static final String FIELD_ID_COL = "field_id";
    private static final String VALUE_COL = "value_id";
    private static final String CATEGORY_TABLE = "category";
    private static final String FIELD_TABLE = "field";
    private static final String RECORD_TABLE = "record";
    private static final String RECORD_FIELD_TABLE = "record_field";

    public DbManager(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_FIELD);
        db.execSQL(CREATE_TABLE_RECORD);
        db.execSQL(CREATE_TABLE_RECORD_FIELD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Category> getCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from category", null);
        List<Category> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Category entry = new Category();
                entry.setId(cursor.getInt(cursor.getColumnIndex(ID_COL)));
                entry.setName(cursor.getString(cursor.getColumnIndex(NAME_COL)));

                result.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return result;
    }

    public List<Field> getFields() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from field", null);
        List<Field> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Field entry = new Field();
                entry.setId(cursor.getInt(cursor.getColumnIndex(ID_COL)));
                entry.setTitle(cursor.getString(cursor.getColumnIndex(TITLE_COL)));
                entry.setSecure(cursor.getInt(cursor.getColumnIndex(IS_SECURE_COL)) == 1);

                result.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return result;
    }

    public List<Record> getRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor recordCursor = db.rawQuery("select r.id as id, r.name as name, category_id, cat.name as category_name" +
            " from record r join category cat on r.category_id = cat.id", null);
        List<Record> result = new ArrayList<>();
        if (recordCursor.moveToFirst()) {
            do {
                Record entry = new Record();
                entry.setId(recordCursor.getInt(recordCursor.getColumnIndex(ID_COL)));
                entry.setName(recordCursor.getString(recordCursor.getColumnIndex(NAME_COL)));

                Category category = new Category();
                category.setId(recordCursor.getInt(recordCursor.getColumnIndex(CATEGORY_ID_COL)));
                category.setName(recordCursor.getString(recordCursor.getColumnIndex("category_name")));

                entry.setCategory(category);
                Cursor valueCursor = db.rawQuery("select value, rf.field_id as field_id, f.title as title, f.is_secure as is_secure" +
                    " from record_field rf join field f on record_id = ? and rf.field_id = f.id", new String[]{String.valueOf(entry.getId())});
                List<Pair<Field, String>> valuesByField = new ArrayList<>();
                if (valueCursor.moveToFirst()) {
                    do {
                        Field field = new Field();
                        field.setId(valueCursor.getInt(valueCursor.getColumnIndex(FIELD_ID_COL)));
                        field.setTitle(valueCursor.getString(valueCursor.getColumnIndex(TITLE_COL)));
                        field.setSecure(valueCursor.getInt(valueCursor.getColumnIndex(IS_SECURE_COL)) == 1);
                        String value = valueCursor.getString(valueCursor.getColumnIndex(VALUE_COL));

                        valuesByField.add(Pair.create(field, value));
                    } while (valueCursor.moveToNext());
                }
                valueCursor.close();
                entry.setValuesByField(valuesByField);
                result.add(entry);
            } while (recordCursor.moveToNext());
        }
        recordCursor.close();
        db.close();

        return result;
    }

    public Record getRecordByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor recordCursor = db.rawQuery("select r.id as id, r.name as name, category_id, cat.name as category_name" +
            " from record r join category cat on r.category_id = cat.id and r.name = ?", new String[]{name});
        Record result = new Record();
        if (recordCursor.moveToFirst()) {
            result.setId(recordCursor.getInt(recordCursor.getColumnIndex(ID_COL)));
            result.setName(name);

            Category category = new Category();
            category.setId(recordCursor.getInt(recordCursor.getColumnIndex(CATEGORY_ID_COL)));
            category.setName(recordCursor.getString(recordCursor.getColumnIndex("category_name")));

            result.setCategory(category);

            Cursor valueCursor = db.rawQuery("select value, rf.field_id as field_id, f.title as title, f.is_secure as is_secure" +
                " from record_field rf join field f on record_id = ? and rf.field_id = f.id", new String[]{String.valueOf(result.getId())});
            List<Pair<Field, String>> valuesByField = new ArrayList<>();
            if (valueCursor.moveToFirst()) {
                do {
                    Field field = new Field();
                    field.setId(valueCursor.getInt(valueCursor.getColumnIndex(FIELD_ID_COL)));
                    field.setTitle(valueCursor.getString(valueCursor.getColumnIndex(TITLE_COL)));
                    field.setSecure(valueCursor.getInt(valueCursor.getColumnIndex(IS_SECURE_COL)) == 1);
                    String value = valueCursor.getString(valueCursor.getColumnIndex(VALUE_COL));

                    valuesByField.add(Pair.create(field, value));
                } while (valueCursor.moveToNext());
            }
            valueCursor.close();
            result.setValuesByField(valuesByField);
        }
        recordCursor.close();
        db.close();

        return result;
    }

    public boolean saveCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(NAME_COL, category.getName());
        boolean result = db.insert(CATEGORY_TABLE, null, content) != -1;
        db.close();

        return result;
    }

    public boolean saveField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(TITLE_COL, field.getTitle());
        content.put(IS_SECURE_COL, field.isSecure());
        boolean result = db.insert(FIELD_TABLE, null, content) != -1;
        db.close();

        return result;
    }

    public boolean saveRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues content = new ContentValues();
        content.put(CATEGORY_ID_COL, record.getCategory().getId());
        content.put(NAME_COL, record.getName());

        db.insert(RECORD_TABLE, null, content);
        content.clear();

        record.getValuesByField().forEach(entry -> {
            content.put(RECORD_ID_COL, getRecordByName(record.getName()).getId());
            content.put(FIELD_ID_COL, entry.first.getId());
            content.put(VALUE_COL, entry.second);
            db.insert(RECORD_FIELD_TABLE, null, content);
            content.clear();
        });
        db.close();

        return true;
    }
}
