package wookdev.team.ownnamegen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wook on 2017-05-24.
 */

public class PopUpActivity extends Activity {
    private static final String TAG = "Final";
    private DBManager dbManager;

    private TextView TV_TitleBarKrName;
    private TextView TV_HanjaMean1;
    private TextView TV_HanjaMean2;
    private TextView TV_Hanja1;
    private TextView TV_Hanja2;
    private TextView TV_birth_date;
    private TextView TV_birth_saju;
    private TextView TV_SajuFiveEleTree;
    private TextView TV_SajuFiveEleFire;
    private TextView TV_SajuFiveEleSoil;
    private TextView TV_SajuFiveEleMetal;
    private TextView TV_SajuFiveEleWater;
    private TextView TV_SuriChild;
    private TextView TV_SuriYoung;
    private TextView TV_SuriOld;
    private TextView TV_SuriTotal;


    private String fullKrName;
    private String fullChName;
    private int fullNameLen;
    private String birthDate;
    private String birthSaju;
    private ArrayList<Letter> fullNameLetterArrayList;

    private int saju_five_ele_tree;
    private int saju_five_ele_fire;
    private int saju_five_ele_soil;
    private int saju_five_ele_metal;
    private int saju_five_ele_water;

    private Suri81[] suri81s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(this);
        suri81s = new Suri81[4];

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);


        //UI 객체생성
        TV_TitleBarKrName = (TextView) findViewById(R.id.TitleKoreanNameBig);
        //TV_BirthSummary = (TextView) findViewById(R.id.birthSummary);
        TV_birth_date = (TextView) findViewById(R.id.birth_date);
        TV_birth_saju = (TextView) findViewById(R.id.birth_saju);
        TV_SajuFiveEleTree = (TextView) findViewById(R.id.TV_FiveEleTree);
        TV_SajuFiveEleFire = (TextView) findViewById(R.id.TV_FiveEleFire);
        TV_SajuFiveEleSoil = (TextView) findViewById(R.id.TV_FiveEleSoil);
        TV_SajuFiveEleMetal = (TextView) findViewById(R.id.TV_FiveEleMetal);
        TV_SajuFiveEleWater = (TextView) findViewById(R.id.TV_FiveEleWater);
        TV_SuriChild = (TextView) findViewById(R.id.suri_child);
        TV_SuriYoung = (TextView) findViewById(R.id.suri_young);
        TV_SuriOld = (TextView) findViewById(R.id.suri_old);
        TV_SuriTotal = (TextView) findViewById(R.id.suri_total);

        //데이터 가져오기
        Intent intent = getIntent();
        fullKrName = intent.getStringExtra(Main2Activity.EXTRA_KEY_FULL_KR_NAME);
        fullChName = intent.getStringExtra(Main2Activity.EXTRA_KEY_FULL_CH_NAME);
        fullNameLen = intent.getIntExtra(Main2Activity.EXTRA_KEY_FULL_NAME_LEN, 3);
        birthDate = intent.getStringExtra(Main2Activity.EXTRA_KEY_BIRTH_DATE);
        birthSaju = intent.getStringExtra(Main2Activity.EXTRA_KEY_BIRTH_SAJU);
        saju_five_ele_tree = intent.getIntExtra(Main2Activity.EXTRA_KEY_FIVE_ELE_TREE, 0);
        saju_five_ele_fire = intent.getIntExtra(Main2Activity.EXTRA_KEY_FIVE_ELE_FIRE, 0);
        saju_five_ele_soil = intent.getIntExtra(Main2Activity.EXTRA_KEY_FIVE_ELE_SOIL, 0);
        saju_five_ele_metal = intent.getIntExtra(Main2Activity.EXTRA_KEY_FIVE_ELE_METAL, 0);
        saju_five_ele_water = intent.getIntExtra(Main2Activity.EXTRA_KEY_FIVE_ELE_WATER, 0);
        suri81s[0] = dbManager.getSuri(intent.getIntExtra(Main2Activity.EXTRA_KEY_SURI_FOUR_FORM_WON, 0));
        suri81s[1] = dbManager.getSuri(intent.getIntExtra(Main2Activity.EXTRA_KEY_SURI_FOUR_FORM_HYUNG, 0));
        suri81s[2] = dbManager.getSuri(intent.getIntExtra(Main2Activity.EXTRA_KEY_SURI_FOUR_FORM_EE, 0));
        suri81s[3] = dbManager.getSuri(intent.getIntExtra(Main2Activity.EXTRA_KEY_SURI_FOUR_FORM_JEONG, 0));


        fullNameLetterArrayList = new ArrayList<Letter>(fullNameLen);
        fullNameLetterArrayList.add(0, dbManager.getLetterFromCh(fullChName.charAt(0)+"", DBManager.TYPE_FAMILY_NAME));
        for (int i = 1; i < fullNameLen; i++){
            fullNameLetterArrayList.add(i, dbManager.getLetterFromCh(fullChName.charAt(i)+"", DBManager.TYPE_NORMAL_NAME));
        }

        TV_TitleBarKrName.setText(fullKrName + " (" + fullChName +")");
        TV_HanjaMean1 = (TextView) findViewById(R.id.hanja_mean1);
        TV_HanjaMean2 = (TextView) findViewById(R.id.hanja_mean2);
        TV_Hanja1 = (TextView) findViewById(R.id.hanja1);
        TV_Hanja2 = (TextView) findViewById(R.id.hanja2);

        TV_HanjaMean1.setText(fullNameLetterArrayList.get(1).getMean());
        TV_HanjaMean2.setText(fullNameLetterArrayList.get(2).getMean());
        TV_Hanja1.setText(fullNameLetterArrayList.get(1).getChLetter());
        TV_Hanja2.setText(fullNameLetterArrayList.get(2).getChLetter());
        TV_birth_date.setText(birthDate);
        TV_birth_saju.setText(birthSaju);
        TV_SajuFiveEleTree.setText("木 : " + saju_five_ele_tree);
        TV_SajuFiveEleFire.setText("火 : " + saju_five_ele_fire);
        TV_SajuFiveEleSoil.setText("土 : " + saju_five_ele_soil);
        TV_SajuFiveEleMetal.setText("金 : " + saju_five_ele_metal);
        TV_SajuFiveEleWater.setText("水 : " + saju_five_ele_water);

        TV_SuriChild.setText("초년운 [" + suri81s[0].getName() +"]\n" +suri81s[0].getExplain());
        TV_SuriYoung.setText("청년운 [" + suri81s[1].getName() +"]\n" +suri81s[1].getExplain());
        TV_SuriOld.setText("장년운 [" + suri81s[2].getName() +"]\n" +suri81s[2].getExplain());
        TV_SuriTotal.setText("종합운 [" + suri81s[3].getName() +"]\n" +suri81s[3].getExplain());


        for (int i = 0; i < fullNameLen; i++) {

            Log.d(TAG, "kr = " + fullNameLetterArrayList.get(i).getKrLetter() + " ch = " + fullNameLetterArrayList.get(i).getChLetter());
        }
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;

    }
}




