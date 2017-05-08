package wookdev.team.ownnamegen;

/**
 * Created by wook on 2017-04-25.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.widget.Toast;

//DB를 총괄관리
public class DBManager {

    // DB관련 상수 선언
    private static final String DB_NAME_CH = "ch_letters.db";
    private static final String DB_NAME_MANSE = "cal_manse_new.db";
    private static final String DB_MANSE_TABLE_NAME = "c_data";
    private static final String DB_CH_TABLE_NAME = "HanjaList";
    private static final String TAG = "DBManager";

    public static final int dbVersion = 1;
    public static final int TYPE_FAMILY_NAME = 2;
    public static final int TYPE_NORMAL_NAME = 1;

    // DB관련 객체 선언
    private OpenHelper opener_ch; // DB opener
    private OpenHelper opener_manse; // DB opener
    private SQLiteDatabase db_ch; // DB controller
    private SQLiteDatabase db_manse; // DB controller

    // 부가적인 객체들
    private Context context;
    private String DB_PATH;


    // 생성자
    public DBManager(Context context) {
        this.context = context;
        DB_PATH = "/data/data/" + context.getApplicationContext().getPackageName()+"/databases/";

        Log.d("OwnNameGen", "DB_PATH = " + DB_PATH);

        try {
            copydatabase(DB_NAME_CH);
            copydatabase(DB_NAME_MANSE);
        } catch (IOException e) {
            Log.d("OwnNameGen", e.toString());
        }

        File f = new File(DB_PATH + DB_NAME_CH);

        //Toast.makeText(context, "DB_PATH = "+ DB_PATH + " e = " +f.exists(), Toast.LENGTH_LONG).show();

        this.opener_ch = new OpenHelper(context, DB_NAME_CH, null, dbVersion);
        this.db_ch = opener_ch.getWritableDatabase();
        this.opener_manse = new OpenHelper(context, DB_NAME_MANSE, null, dbVersion);
        this.db_manse = opener_manse.getReadableDatabase();

    }

    // Opener of DB and Table
    private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory,
                          int version) {
            super(context, name, null, version);
            // TODO Auto-generated constructor stub

        }

        // 생성된 DB가 없을 경우에 한번만 호출됨
        @Override
        public void onCreate(SQLiteDatabase arg0) {
            // String dropSql = "drop table if exists " + tableName;
            // db.execSQL(dropSql);

            /*
            String createSql = "create table " + tableName + " ("
                    + "id integer primary key autoincrement, " + "SSID text, "
                    + "capabilities integer, " + "passwd text)";
            arg0.execSQL(createSql);
            Toast.makeText(context, "DB is opened", 0).show();
            */
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
        }
    }
    // Data 읽기(꺼내오기)
    public void selectData(int index){

        String sql = "select * from " + DB_CH_TABLE_NAME + " where No = "+index+";";
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            int no = result.getInt(0);
            String hanja = result.getString(2);

            Log.d("OwnNameGen", "No = "+ no + " hanja = " + hanja);
        }
        result.close();

        sql = "select * from " + DB_MANSE_TABLE_NAME + " where cd_no = "+index+";";
        result = db_manse.rawQuery(sql, null);
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            int no = result.getInt(0);
            String hanja = result.getString(9);

            Log.d("OwnNameGen", "No = "+ no + " hanja = " + hanja);
        }
        result.close();

    }

    public ArrayList<String> getFamilyNameKr(String familyNameKr) {

        String sql = "select * from " + DB_CH_TABLE_NAME + " where Hangul = '"+ familyNameKr+"' and Position = 2;";
        ArrayList<String> resultArrayList = new ArrayList<String>();
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            while (!result.isAfterLast()){
                String resultEntry = result.getString(1).toString();
                if (!resultArrayList.contains(resultEntry)) {
                    resultArrayList.add(resultEntry);
                }
                Log.d(TAG, result.getString(1));

                result.moveToNext();
            }
        }
        result.close();
        return  resultArrayList;
    }

    public ArrayList<Letter> getFamilyNameInfo(String familyNameKr) {

        String sql = "select * from " + DB_CH_TABLE_NAME + " where Hangul = '"+ familyNameKr+"' and Position = 2;";
        ArrayList<Letter> resultArrayList = new ArrayList<Letter>();
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            while (!result.isAfterLast()) {
                String familyNameKrGot = result.getString(1).toString();
                String familyNameCh = result.getString(2).toString();
                String familyNameMean = result.getString(3).toString();
                Letter familyNameLetter = new Letter(familyNameKrGot, familyNameCh, familyNameMean);
                resultArrayList.add(familyNameLetter);
                result.moveToNext();
            }
        }
        result.close();
        return  resultArrayList;
    }

    public Letter getLetterFromCh(String familyNameCh, int type) {
        String sql = "select * from " + DB_CH_TABLE_NAME + " where Hanja = '"+ familyNameCh+"' and Position = " + type +";";
        Letter familyNameLetter = new Letter();
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
                String familyNameKr = result.getString(1).toString();
                String familyNameChGot = result.getString(2).toString();
                String familyNameMean = result.getString(3).toString();
                familyNameLetter = new Letter(familyNameKr, familyNameChGot, familyNameMean);
                familyNameLetter.setKrLetter(familyNameKr);
                familyNameLetter.setChLetter(familyNameChGot);
                familyNameLetter.setMean(familyNameMean);
                familyNameLetter.setStroke(result.getInt(4));
                familyNameLetter.setProFiveElement(result.getString(5).toString());
                familyNameLetter.setRadFiveElement(result.getString(6).toString());
                familyNameLetter.setLetter(true);
        }
        result.close();
        return  familyNameLetter;
    }

    public ArrayList<String> getNamesFullList(int name_len) {
        ArrayList<String> resultList = new ArrayList<String>();
        ArrayList<String> allKoreanLetters = new ArrayList<String>();
        String name = new String();
        int size;

        String sql = "select * from " + DB_CH_TABLE_NAME + " where Position = 1;";
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            while (!result.isAfterLast()) {
                String familyNameKr = result.getString(1).toString();
                if(!allKoreanLetters.contains(familyNameKr))
                    allKoreanLetters.add(familyNameKr);
                result.moveToNext();
            }
        }

        size = allKoreanLetters.size();
        for (int i = 0; i < size; i++) {
            String eachName1 = allKoreanLetters.get(i);
            if (name_len > 1 ) {
                for (int j = 0; j < size; j++) {
                    String eachName2 = eachName1.substring(0, 1)  + allKoreanLetters.get(j);
                    if (name_len > 2 ) {
                        for (int k = 0; k < size; k++) {
                            String eachName3 = eachName2.substring(0, 2) + allKoreanLetters.get(k);
                            resultList.add(eachName3);
                        }
                    } else {
                        resultList.add(eachName2);
                    }
                }
            } else {
                resultList.add(eachName1);
            }
        }
        result.close();
        return resultList;
    }

    /*
    // 데이터 추가
    public void insertData(APinfo info) {
        String sql = "insert into " + tableName + " values(NULL, '"
                + info.getSSID() + "', " + info.getCapabilities() + ", '"
                + info.getPasswd() + "');";
        db.execSQL(sql);
    }

    // 데이터 갱신
    public void updateData(APinfo info, int index) {
        String sql = "update " + tableName + " set SSID = '" + info.getSSID()
                + "', capabilities = " + info.getCapabilities()
                + ", passwd = '" + info.getPasswd() + "' where id = " + index
                + ";";
        db.execSQL(sql);
    }

    // 데이터 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // 데이터 검색
    public APinfo selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index
                + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            APinfo info = new APinfo(result.getInt(0), result.getString(1),
                    result.getInt(2), result.getString(3));
            result.close();
            return info;
        }
        result.close();
        return null;
    }

    // 데이터 전체 검색
    public ArrayList<apinfo> selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        ArrayList<apinfo> infos = new ArrayList<apinfo>();

        while (!results.isAfterLast()) {
            APinfo info = new APinfo(results.getInt(0), results.getString(1),
                    results.getInt(2), results.getString(3));
            infos.add(info);
            results.moveToNext();
        }
        results.close();
        return infos;
    }
    */
    private void copydatabase(String dbName) throws IOException {

        //Open your local db as the input stream
        InputStream myinput = this.context.getAssets().open(dbName);
        // Path to the just created empty db
        String outfilename = DB_PATH + dbName;
        File outfile = new File(outfilename);

        if (!outfile.exists()) {
            outfile.createNewFile();
        }

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);
        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0)
        {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

}