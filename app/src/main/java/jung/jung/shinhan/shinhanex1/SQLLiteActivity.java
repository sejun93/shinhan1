package jung.jung.shinhan.shinhanex1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import adapter.DBHelper;

public class SQLLiteActivity extends AppCompatActivity {
    private DBHelper events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqllite);

        events = new DBHelper(this);
        addEvent();//삽입
        Cursor cursor = getEvents();//검색
        showEvents(cursor);//출력
    }

    private void addEvent() {
        SQLiteDatabase db = events.getWritableDatabase();
        String sql = "INSERT INTO contacts (id, company,department,service,grade,name,ph1,ph2,email,photo) " +
                "VALUES (NULL,'회사1', '부서1','업무1','직급1','정혜민','01032467775','2','cmcm1284@naver.com','2');";
        db.execSQL(sql);
        sql = "INSERT INTO contacts (id, company,department,service,grade,name,ph1,ph2,email,photo) " +
                "VALUES (NULL,'회사2', '부서2','업무1','직급1','이름1','1','2','1','2');";
        db.execSQL(sql);
    }



    private Cursor getEvents() {
        SQLiteDatabase db = events.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contacts ORDER BY id DESC", null);
        startManagingCursor(cursor);

        return cursor;
    }

    private void showEvents(Cursor cursor) {
        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            String tel = cursor.getString(2);

            buffer.append(id).append(":");
            buffer.append(name).append(":");
            buffer.append(tel).append("\n");
        }

        TextView text = (TextView) findViewById(R.id.textView);
        text.setText(buffer);
    }
}

