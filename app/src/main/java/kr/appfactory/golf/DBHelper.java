package kr.appfactory.golf;

import android.content.Context;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String Database = "Jappfactory.db";
    private static final String Table_MyVideo = "MyVideo";
    private static final String Table_Setting = "Setting";

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context) {
        super(context, Database, null, 1);

        SQLiteDatabase db=this.getWritableDatabase();
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE "+ Table_MyVideo + " (idx INTEGER PRIMARY KEY AUTOINCREMENT, videoId TEXT, title TEXT, videodesc TEXT, publishedAt TEXT,  thum_pic TEXT);");
        db.execSQL("CREATE TABLE "+ Table_Setting + " (idx INTEGER PRIMARY KEY AUTOINCREMENT, videoId TEXT, title TEXT, videodesc TEXT, publishedAt TEXT,  thum_pic TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Table_MyVideo +"");
        db.execSQL("DROP TABLE IF EXISTS "+Table_Setting +"");
        onCreate(db);

    }

    public void insert_MyVideo(String videoId, String title, String description, String publishedAt, String thum_pic) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가

        //Toast.makeText (context , "INSERT INTO "+ Table + " VALUES(null, '" + videoId + "' , '" + title + "' , '" + description + "' , '" + publishedAt + "' , '" + thum_pic   , Toast.LENGTH_LONG).show();


        //Log.e("temp", "INSERT INTO "+ Table + " VALUES(null, '" + videoId + "' , '" + title + "' , '" + description + "' , '" + publishedAt + "' , '" + thum_pic );

        title = title.replace("'","''");
        description = description.replace("'","''");

        db.execSQL("INSERT INTO "+ Table_MyVideo + " VALUES(null, '" + videoId + "' , '" + title + "' , '" + description + "' , '" + publishedAt + "' , '" + thum_pic + "');");
        db.close();
    }

    public void update_MyVideo(String videoId, String title) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE "+ Table_MyVideo + "  SET title=" + title + " WHERE videoId='" + videoId + "';");
        db.close();
    }

    public void delete_MyVideo(String videoId) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM  "+ Table_MyVideo + " WHERE videoId='" + videoId + "';");
        db.close();
    }

    public String getCnt_MyVideo(String videoId) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT count(*) FROM "+ Table_MyVideo + " where videoId='" + videoId + "'", null);
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }

        return result;
    }
    public  Cursor getResult_MyVideo() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";


        //Log.d("videoId : " ,"결과를 가져옵니다");

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Table_MyVideo + " ", null);

        return cursor;
    }
}