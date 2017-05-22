package wookdev.team.ownnamegen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import static java.util.Calendar.MONDAY;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    private static final int FIVE_ELE_TREE = 0;
    private static final int FIVE_ELE_FIRE = 1;
    private static final int FIVE_ELE_SOIL = 2;
    private static final int FIVE_ELE_METAL = 3;
    private static final int FIVE_ELE_WATER = 4;
    private static final String FIVE_ELE_TREE_KR = "목";
    private static final String FIVE_ELE_FIRE_KR = "화";
    private static final String FIVE_ELE_SOIL_KR = "토";
    private static final String FIVE_ELE_METAL_KR = "금";
    private static final String FIVE_ELE_WATER_KR = "수";

    private static final int FAMILY_NAME_IDX = 0;
    private static final int MIDDLE_NAME_IDX = 1;
    private static final int LAST_NAME_IDX = 2;



    private String fullNameKr;
    private Letter famlilyNameLetter;
    private int fullNameLen;

    private String birthYear;
    private String birthMonth;
    private String birthDay;
    private String birthHour;
    private String birthMinute;
    private String birthLocation;

    private String[] saju = new String[4];
    private String sajuYear;
    private String sajuMonth;
    private String sajuDay;
    private String sajuTime;

    private int saju_five_ele_tree = 0;
    private int saju_five_ele_fire = 0;
    private int saju_five_ele_soil = 0;
    private int saju_five_ele_metal = 0;
    private int saju_five_ele_water = 0;

    private DBManager dbManager;
    private ArrayList<ArrayList<Letter>> nameCandidatesLetters;
    private String[] nameCandidatesListMenu;
    private ArrayAdapter nameCandidatesListMenuAdapter;

    private TextView TV_TitleKrName;
    private TextView TV_FiveEleTree;
    private TextView TV_FiveEleFire;
    private TextView TV_FiveEleSoil;
    private TextView TV_FiveEleMetal;
    private TextView TV_FiveEleWater;

    private TextView TV_ResultChNames;
    private ListView LV_ResultChNames;

    private void initSajuFiveEleTVs() {
        TV_FiveEleTree = (TextView) findViewById(R.id.TV_FiveEleTree);
        TV_FiveEleFire = (TextView) findViewById(R.id.TV_FiveEleFire);
        TV_FiveEleSoil = (TextView) findViewById(R.id.TV_FiveEleSoil);
        TV_FiveEleMetal = (TextView) findViewById(R.id.TV_FiveEleMetal);
        TV_FiveEleWater = (TextView) findViewById(R.id.TV_FiveEleWater);

        TV_FiveEleTree.setText(FIVE_ELE_TREE_KR + ": " + saju_five_ele_tree);
        TV_FiveEleFire.setText(FIVE_ELE_FIRE_KR + ": " + saju_five_ele_fire);
        TV_FiveEleSoil.setText(FIVE_ELE_SOIL_KR + ": " + saju_five_ele_soil);
        TV_FiveEleMetal.setText(FIVE_ELE_METAL_KR + ": " + saju_five_ele_metal);
        TV_FiveEleWater.setText(FIVE_ELE_WATER_KR + ": " + saju_five_ele_water);
    }

    private static final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

    // To calculate the total height of all items in ListView call with items = adapter.getCount()
    public static int getItemHeightofListView(ListView listView, int items) {
        ListAdapter adapter = listView.getAdapter();

        int grossElementHeight = 0;
        for (int i = 0; i < items; i++) {
            View childView = adapter.getView(i, null, listView);
            childView.measure(UNBOUNDED, UNBOUNDED);
            grossElementHeight += childView.getMeasuredHeight();
        }
        return grossElementHeight;
    }

    private class CustomAdapter<T> extends ArrayAdapter<T> {
        private static final int MAX_ROW_DISPLAY = 5;
        private List<T> mItems;
        public CustomAdapter(Context context, int resource, List<T> objects) {
            super(context, resource, objects);
            mItems = objects;
        }

        @Override
        public int getCount() {
            if (mItems == null) {
                return 0;
            }
            return Math.min(MAX_ROW_DISPLAY, mItems.size());
        }
    }


    private void initResultLV() {

        nameCandidatesListMenu = new String[nameCandidatesLetters.size()];
        for (int i = 0; i < nameCandidatesListMenu.length; i++) {
            nameCandidatesListMenu[i] = "[ " +nameCandidatesLetters.get(i).get(MIDDLE_NAME_IDX).getMean() + " ] "
                    + nameCandidatesLetters.get(i).get(MIDDLE_NAME_IDX).getChLetter()
                    + " [ " + nameCandidatesLetters.get(i).get(LAST_NAME_IDX).getMean() + " ] "
                    + nameCandidatesLetters.get(i).get(LAST_NAME_IDX).getChLetter();
        }

        nameCandidatesListMenuAdapter = new ArrayAdapter(this, R.layout.simplerow, nameCandidatesListMenu);
        nameCandidatesListMenuAdapter.setNotifyOnChange(true);

        LV_ResultChNames = (ListView) findViewById(R.id.LV_ResultChNames);
        LV_ResultChNames.setAdapter(nameCandidatesListMenuAdapter);
        LV_ResultChNames.setMinimumHeight(180);
        LV_ResultChNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        TV_ResultChNames = (TextView) findViewById(R.id.TitleResultChNames);
        TV_ResultChNames.setText(getString(R.string.ResultChNamesTitle) + ": " + nameCandidatesLetters.size());
    }

    private void setNamesFromIntentSelf(Intent intent) {
        this.famlilyNameLetter = dbManager.getLetterFromCh(intent.getStringExtra(MainActivity.EXTRA_KEY_CH_FAMILY_NAME), DBManager.TYPE_FAMILY_NAME);
        this.fullNameKr = this.famlilyNameLetter.getKrLetter() + intent.getStringExtra(MainActivity.EXTRA_KEY_KR_NAME);
        this.fullNameLen = this.fullNameKr.length();
    }

    private void setSajuFromIntentSelf(Intent intent) {
        this.birthYear = intent.getStringExtra(MainActivity.EXTRA_KEY_BIRTH_YEAR);
        this.birthMonth = intent.getStringExtra(MainActivity.EXTRA_KEY_BIRTH_MONTH);
        this.birthDay = intent.getStringExtra(MainActivity.EXTRA_KEY_BIRTH_DAY);
        this.birthHour = intent.getStringExtra(MainActivity.EXTRA_KEY_BIRTH_HOUR);
        this.birthMinute = intent.getStringExtra(MainActivity.EXTRA_KEY_BIRTH_MINUTE);
        this.birthLocation = intent.getStringExtra(MainActivity.EXTRA_KEY_BIRTH_LOCATION);
        calcManse();
    }
    private static Date addMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }
    private String getOldTime(int hour) {
        Log.d(TAG, " " + hour);
        if ((hour >= 23) && (hour < 1))
            return "자";
        else if ((hour >=1) && (hour <3))
            return "축";
        else if ((hour >= 3) && (hour < 5))
            return "인";
        else if ((hour >= 5) && (hour < 7))
            return "묘";
        else if ((hour >= 7) && (hour < 9))
            return "진";
        else if ((hour >= 9) && (hour < 11))
            return "사";
        else if ((hour >= 11) && (hour < 13))
            return "오";
        else if ((hour >= 13) && (hour < 15))
            return "미";
        else if ((hour >= 15) && (hour < 17))
            return "신";
        else if ((hour >= 17) && (hour < 19))
            return "유";
        else if ((hour >= 19) && (hour < 21))
            return "술";
        else
            return "해";
    }

    private String getSajuTime(String day, String time) {
        Log.d(TAG, "day = " + day + " time = " + time);
        if (day.equals("갑") || day.equals("기")) {
            if (time.equals("자")) {
                return "갑자";
            } else if (time.equals("축")) {
                return "을축";
            } else if (time.equals("인")) {
                return "병인";
            } else if (time.equals("묘")) {
                return "정묘";
            } else if (time.equals("진")) {
                return "무진";
            } else if (time.equals("사")) {
                return "기사";
            } else if (time.equals("오")) {
                return "경오";
            } else if (time.equals("미")) {
                return "신미";
            } else if (time.equals("신")) {
                return "임신";
            } else if (time.equals("유")) {
                return "계유";
            } else if (time.equals("술")) {
                return "갑술";
            } else if (time.equals("해")) {
                return "을해";
            }
        } else if (day.equals("을") || day.equals("경")) {
            if (time.equals("자")) {
                return "병자";
            } else if (time.equals("축")) {
                return "정축";
            } else if (time.equals("인")) {
                return "무인";
            } else if (time.equals("묘")) {
                return "기묘";
            } else if (time.equals("진")) {
                return "경진";
            } else if (time.equals("사")) {
                return "신사";
            } else if (time.equals("오")) {
                return "임오";
            } else if (time.equals("미")) {
                return "계미";
            } else if (time.equals("신")) {
                return "갑신";
            } else if (time.equals("유")) {
                return "을유";
            } else if (time.equals("술")) {
                return "병술";
            } else if (time.equals("해")) {
                return "정해";
            }
        } else if (day.equals("병") || day.equals("신")) {
            if (time.equals("자")) {
                return "무자";
            } else if (time.equals("축")) {
                return "기축";
            } else if (time.equals("인")) {
                return "경인";
            } else if (time.equals("묘")) {
                return "신묘";
            } else if (time.equals("진")) {
                return "임진";
            } else if (time.equals("사")) {
                return "계사";
            } else if (time.equals("오")) {
                return "갑오";
            } else if (time.equals("미")) {
                return "을미";
            } else if (time.equals("신")) {
                return "병신";
            } else if (time.equals("유")) {
                return "정유";
            } else if (time.equals("술")) {
                return "무술";
            } else if (time.equals("해")) {
                return "기해";
            }
        } else if (day.equals("정") || day.equals("임")) {
            if (time.equals("자")) {
                return "경자";
            } else if (time.equals("축")) {
                return "신축";
            } else if (time.equals("인")) {
                return "임인";
            } else if (time.equals("묘")) {
                return "계묘";
            } else if (time.equals("진")) {
                return "갑진";
            } else if (time.equals("사")) {
                return "을사";
            } else if (time.equals("오")) {
                return "병오";
            } else if (time.equals("미")) {
                return "정미";
            } else if (time.equals("신")) {
                return "무신";
            } else if (time.equals("유")) {
                return "기유";
            } else if (time.equals("술")) {
                return "경술";
            } else if (time.equals("해")) {
                return "신해";
            }
        } else if (day.equals("무") || day.equals("계")) {
            if (time.equals("자")) {
                return "임자";
            } else if (time.equals("축")) {
                return "계축";
            } else if (time.equals("인")) {
                return "갑인";
            } else if (time.equals("묘")) {
                return "을묘";
            } else if (time.equals("진")) {
                return "병진";
            } else if (time.equals("사")) {
                return "정사";
            } else if (time.equals("오")) {
                return "무오";
            } else if (time.equals("미")) {
                return "기미";
            } else if (time.equals("신")) {
                return "경신";
            } else if (time.equals("유")) {
                return "신유";
            } else if (time.equals("술")) {
                return "임술";
            } else if (time.equals("해")) {
                return "계해";
            }
        }

        return "";
    }

    private void calcManse() {
        Calendar curDate = Calendar.getInstance();
        int locationAdjustMinDiff = 0;

        curDate.set(new Integer(birthYear), new Integer(birthMonth), new Integer(birthDay),
                new Integer(birthHour), new Integer(birthMinute));

        if (this.birthLocation.equals("목포"))
            locationAdjustMinDiff = -34;
        else if (this.birthLocation.equals("서산"))
            locationAdjustMinDiff = -34;
        else if (this.birthLocation.equals("제주"))
            locationAdjustMinDiff = -33;
        else if (this.birthLocation.equals("보령"))
            locationAdjustMinDiff = -33;
        else if (this.birthLocation.equals("서귀포"))
            locationAdjustMinDiff = -33;
        else if (this.birthLocation.equals("인천"))
            locationAdjustMinDiff = -33;
        else if (this.birthLocation.equals("군산"))
            locationAdjustMinDiff = -33;
        else if (this.birthLocation.equals("정읍"))
            locationAdjustMinDiff = -32;
        else if (this.birthLocation.equals("광주"))
            locationAdjustMinDiff = -32;
        else if (this.birthLocation.equals("서울"))
            locationAdjustMinDiff = -32;
        else if (this.birthLocation.equals("수원"))
            locationAdjustMinDiff = -31;
        else if (this.birthLocation.equals("평택"))
            locationAdjustMinDiff = -31;
        else if (this.birthLocation.equals("전주"))
            locationAdjustMinDiff = -31;
        else if (this.birthLocation.equals("천안"))
            locationAdjustMinDiff = -31;
        else if (this.birthLocation.equals("남원"))
            locationAdjustMinDiff = -30;
        else if (this.birthLocation.equals("대전"))
            locationAdjustMinDiff = -30;
        else if (this.birthLocation.equals("청주"))
            locationAdjustMinDiff = -30;
        else if (this.birthLocation.equals("춘천"))
            locationAdjustMinDiff = -29;
        else if (this.birthLocation.equals("여수"))
            locationAdjustMinDiff = -29;
        else if (this.birthLocation.equals("충주"))
            locationAdjustMinDiff = -28;
        else if (this.birthLocation.equals("원주"))
            locationAdjustMinDiff = -28;
        else if (this.birthLocation.equals("사천"))
            locationAdjustMinDiff = -27;
        else if (this.birthLocation.equals("김천"))
            locationAdjustMinDiff = -27;
        else if (this.birthLocation.equals("상주"))
            locationAdjustMinDiff = 26;
        else if (this.birthLocation.equals("통영"))
            locationAdjustMinDiff = -25;
        else if (this.birthLocation.equals("마산"))
            locationAdjustMinDiff = -25;
        else if (this.birthLocation.equals("속초"))
            locationAdjustMinDiff = -25;
        else if (this.birthLocation.equals("대구"))
            locationAdjustMinDiff = -25;
        else if (this.birthLocation.equals("안동"))
            locationAdjustMinDiff = -25;
        else if (this.birthLocation.equals("강릉"))
            locationAdjustMinDiff = -24;
        else if (this.birthLocation.equals("태백"))
            locationAdjustMinDiff = -24;
        else if (this.birthLocation.equals("부산"))
            locationAdjustMinDiff = -23;
        else if (this.birthLocation.equals("동해"))
            locationAdjustMinDiff = -23;
        else if (this.birthLocation.equals("경주"))
            locationAdjustMinDiff = -23;
        else if (this.birthLocation.equals("울산"))
            locationAdjustMinDiff = -22;
        else if (this.birthLocation.equals("포항"))
            locationAdjustMinDiff = -22;
        else if (this.birthLocation.equals("울진"))
            locationAdjustMinDiff = -22;

        curDate.add(Calendar.MINUTE, locationAdjustMinDiff);

        saju = dbManager.getSajuYearMonthDay(
                curDate.get(Calendar.YEAR)+"",
                curDate.get(Calendar.MONTH)+"",
                curDate.get(Calendar.DATE)+"");

        saju[3] = getSajuTime(saju[2].charAt(0) + "", getOldTime(curDate.get(Calendar.HOUR_OF_DAY)));
        Log.d(TAG, saju[0] + " " + saju[1] + " " + saju[2] + " " +saju[3]);
        setSajuFiveEleSelf();
    }

    private void setSajuFiveEleSelf() {
        for ( int i = 0; i < saju.length; i++) {
            for ( int j = 0; j < saju[i].length(); j++) {
                String eachGanji = saju[i].charAt(j) + "";
                if (eachGanji.equals("갑") || eachGanji.equals("을") || eachGanji.equals("인") || eachGanji.equals("묘")) {
                    saju_five_ele_tree++;
                } else if (eachGanji.equals("병") || eachGanji.equals("정") || eachGanji.equals("사") || eachGanji.equals("오")) {
                    saju_five_ele_fire++;
                } else if (eachGanji.equals("무") || eachGanji.equals("기") || eachGanji.equals("축") || eachGanji.equals("진") || eachGanji.equals("미") || eachGanji.equals("술")) {
                    saju_five_ele_soil++;
                } else if (eachGanji.equals("경") || eachGanji.equals("신") || eachGanji.equals("신") || eachGanji.equals("유")) {
                    saju_five_ele_metal++;
                } else if (eachGanji.equals("임") || eachGanji.equals("계") || eachGanji.equals("자") || eachGanji.equals("해")) {
                    saju_five_ele_water++;
                }
            }
        }
    }

    private void setNameCandidatesLettersSelf() {
        ArrayList<Letter> middleName = dbManager.getLetter(fullNameKr.charAt(MIDDLE_NAME_IDX)+"");
        ArrayList<Letter> lastName = dbManager.getLetter(fullNameKr.charAt(LAST_NAME_IDX)+"");

        for (int i = 0; i < middleName.size(); i++) {
            for (int j = 0; j < lastName.size(); j++) {
                ArrayList<Letter> name = new ArrayList<Letter>();
                name.add(FAMILY_NAME_IDX, famlilyNameLetter);
                name.add(MIDDLE_NAME_IDX, middleName.get(i));
                name.add(LAST_NAME_IDX, lastName.get(j));
                nameCandidatesLetters.add(name);
            }
        }

        for (int i = 0; i < nameCandidatesLetters.size(); i++) {
            Log.d(TAG, nameCandidatesLetters.get(i).get(FAMILY_NAME_IDX).getChLetter()
                    + nameCandidatesLetters.get(i).get(MIDDLE_NAME_IDX).getChLetter()
                    + nameCandidatesLetters.get(i).get(LAST_NAME_IDX).getChLetter());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        dbManager = new DBManager(this);;

        /*
        this.famlilyNameLetter = dbManager.getLetterFromCh(intent.getStringExtra(MainActivity.EXTRA_KEY_CH_FAMILY_NAME), DBManager.TYPE_FAMILY_NAME);
        this.fullNameKr = this.famlilyNameLetter.getKrLetter() + intent.getStringExtra(MainActivity.EXTRA_KEY_KR_NAME);
        this.fullNameLen = this.fullNameKr.length();
        this.nameCandidatesLetters = new ArrayList<ArrayList<Letter>>();
        */
        setNamesFromIntentSelf(intent);
        setSajuFromIntentSelf(intent);

        TV_TitleKrName = (TextView) findViewById(R.id.TitleKoreanNameBig);
        TV_TitleKrName.setText(this.fullNameKr);
        TV_TitleKrName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);

        initSajuFiveEleTVs();

        this.nameCandidatesLetters = new ArrayList<ArrayList<Letter>>();
        setNameCandidatesLettersSelf();

        initResultLV();

        Log.d(TAG, this.fullNameLen + ":" + fullNameKr + ":" + famlilyNameLetter.getChLetter() + ":" + this.nameCandidatesLetters);
    }
}
