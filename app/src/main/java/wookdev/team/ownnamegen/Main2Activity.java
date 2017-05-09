package wookdev.team.ownnamegen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    private String fullNameKr;
    private Letter famlilyNameLetter;
    private int fullNameLen;
    private DBManager dbManager;
    private ArrayList<ArrayList<Letter>> nameCandidatesLetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        dbManager = new DBManager(this);;
        this.famlilyNameLetter = dbManager.getLetterFromCh(intent.getStringExtra(MainActivity.EXTRA_KEY_CH_FAMILY_NAME), DBManager.TYPE_FAMILY_NAME);
        this.fullNameKr = this.famlilyNameLetter.getKrLetter() + intent.getStringExtra(MainActivity.EXTRA_KEY_KR_NAME);
        this.fullNameLen = this.fullNameKr.length();
        this.nameCandidatesLetters = new ArrayList<ArrayList<Letter>>();



        Log.d(TAG, this.fullNameLen + ":" + fullNameKr + ":" + famlilyNameLetter.getChLetter() + ":" + this.nameCandidatesLetters);
    }
}
