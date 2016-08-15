package com.example.swiperefreshlayout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

private Context context;
private Button insert,delete,update,query;
    private  MyDBOpenHelper myDBOpenHelper;
    private SQLiteDatabase db;
    private StringBuilder sb;
    private int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        myDBOpenHelper =new MyDBOpenHelper(context,"my.db",null,1);
        bindView();



    }
    private void bindView(){
        insert= (Button) findViewById(R.id.insert);
        delete= (Button) findViewById(R.id.delete);
        update= (Button) findViewById(R.id.update);
        query= (Button) findViewById(R.id.query);
        insert.setOnClickListener( this);
        query.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        db=myDBOpenHelper.getWritableDatabase();
        switch (v.getId()){
            case R.id.insert:
                ContentValues values1=new ContentValues();
                ContentValues values3=new ContentValues();
                values1.put("name","呵呵~"+i);
                values1.put("age","20");
                i++;
                //参数依次是：表名，强行插入null值得数据列的列名，一行记录的数据
                db.insert("person1", null, values1);

                Toast.makeText(context, "插入完毕~", Toast.LENGTH_SHORT).show();


                break;
            case R.id.delete:
                //参数依次是表名，以及where条件与约束
                db.delete("person1", "personid = ?", new String[]{"3"});
                break;
            case R.id.query:
                sb = new StringBuilder();
                //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，进一步约束
                //指定查询结果的排序方式
                Cursor cursor = db.query("person1", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        int pid = cursor.getInt(cursor.getColumnIndex("personid"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        if(cursor.getString(cursor.getColumnIndex("phone"))!=null){
                        String age=cursor.getString(cursor.getColumnIndex("phone"));
                        sb.append("id：" + pid + "：" + name +"age:"+age+ "\n");}else
                        { sb.append("id：" + pid + "：" + name +"age:"+"0"+ "\n");}
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.update:
                ContentValues values2 = new ContentValues();
                values2.put("name", "嘻嘻~");
                //参数依次是表名，修改后的值，where条件，以及约束，如果不指定三四两个参数，会更改所有行
                db.update("person1", values2, "name = ?", new String[]{"呵呵~2"});
                break;


        }

    }


    public class  MyDBOpenHelper extends SQLiteOpenHelper{

    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE person1(personid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAE(20),age VARCHAE(20)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL("ALTER TABLE person1 ADD phone VARCHAR(12) NULL");
    }
}

}
