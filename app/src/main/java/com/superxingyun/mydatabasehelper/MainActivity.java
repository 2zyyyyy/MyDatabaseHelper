package com.superxingyun.mydatabasehelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this, "Book_Store.db", null, 2);

        Button create_database = (Button) findViewById(R.id.create_database);
        Button add_data = (Button) findViewById(R.id.add_data);
        Button update_data = (Button) findViewById(R.id.update_data);
        Button delete_data = (Button) findViewById(R.id.delete_data);
        Button query_data = (Button) findViewById(R.id.query_data);
        Button replace_data = (Button) findViewById(R.id.replace_data);

        //Create db
        create_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.getWritableDatabase();
            }
        });
        //Insert data
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 552);
                values.put("price", 69.9);
                db.insert("Book", null, values);
                values.clear();//记得的清空
                //组装第二条数据
                values.put("name", "The Lost Symbo1");
                values.put("author", "Dan Brown");
                values.put("pages", 552);
                values.put("price", 9.9);
                db.insert("Book", null, values);

                Toast.makeText(MainActivity.this, "Insert data", Toast.LENGTH_SHORT).show();
            }
        });
        //Update data
        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 1.99);
                db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});
                Toast.makeText(MainActivity.this, "Update data", Toast.LENGTH_SHORT).show();
            }
        });
        //Delete data
        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "price > ?", new String[] { "5" });
                Toast.makeText(MainActivity.this, "Delete data", Toast.LENGTH_SHORT).show();
            }
        });
        //Query data
        query_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //查询所有数据
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        //遍历所有Curse对象
                        String name =cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.i("info", "Book name is:" + name);
                        Log.i("info", "Book author is:" + author);
                        Log.i("info", "Book pages is:" + pages);
                        Log.i("info", "Book price is:" + price);
                    } while (cursor.moveToNext());
                }
                cursor.close();//关闭游标
            }
        });
    replace_data.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //开启事务 （事务——特性为可以保证让某一系列的操作要么全部完成，要么一个都不完成，场景:转账扣款方金额减少
            // 与收款方金额增加必须同时成功）
            db.beginTransaction();

            try {
                db.delete("Book", null, null);
                if (true) {
                    //在这里手动抛出一个空指针异常,让事务失败
                    //throw new NullPointerException();
                }
                ContentValues values = new ContentValues();
                values.put("name", "Game of Thrones");
                values.put("author", "George Martin");
                values.put("pages", 720);
                values.put("price", 19.9);
                db.insert("Book", null, values);
                db.setTransactionSuccessful();//事务已经成功执行
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();//结束事务
            }
        }
    });
    }

}
