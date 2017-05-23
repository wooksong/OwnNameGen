package wookdev.team.ownnamegen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;


import static java.util.Calendar.MONDAY;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";

    public static final String EXTRA_KEY_BIRTH_YEAR = "birth_year";
    public static final String EXTRA_KEY_BIRTH_MONTH = "birth_month";
    public static final String EXTRA_KEY_BIRTH_DAY = "birth_day";
    public static final String EXTRA_KEY_BIRTH_HOUR = "birth_hour";
    public static final String EXTRA_KEY_BIRTH_MINUTE = "birth_minute";
    public static final String EXTRA_KEY_BIRTH_DATE = "birth_date";
    public static final String EXTRA_KEY_BIRTH_SAJU = "birth_saju";
    public static final String EXTRA_KEY_FULL_KR_NAME = "full_name_kr";
    public static final String EXTRA_KEY_FULL_CH_NAME = "full_name_ch";
    public static final String EXTRA_KEY_FULL_NAME_LEN = "full_name_len";

    public static final String EXTRA_KEY_FIVE_ELE_TREE = "five_ele_tree";
    public static final String EXTRA_KEY_FIVE_ELE_FIRE = "five_ele_fire";
    public static final String EXTRA_KEY_FIVE_ELE_SOIL = "five_ele_soil";
    public static final String EXTRA_KEY_FIVE_ELE_METAL = "five_ele_metal";
    public static final String EXTRA_KEY_FIVE_ELE_WATER = "five_ele_water";
    public static final String EXTRA_KEY_SURI_FOUR_FORM_WON = "suri_four_form_won";
    public static final String EXTRA_KEY_SURI_FOUR_FORM_HYUNG = "suri_four_form_hyung";
    public static final String EXTRA_KEY_SURI_FOUR_FORM_EE = "suri_four_form_ee";
    public static final String EXTRA_KEY_SURI_FOUR_FORM_JEONG = "suri_four_form_jeong";


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
    private static final int FOUR_FORM_SURI_WON_IDX = 0;
    private static final int FOUR_FORM_SURI_HYUNG_IDX = 1;
    private static final int FOUR_FORM_SURI_EE_IDX = 2;
    private static final int FOUR_FORM_SURI_JEONG_IDX = 3;


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
    private ArrayList<ArrayList<Letter>> nameAppliedCandidatesLetters;

    //private String[] nameCandidatesListMenu;
    private ArrayList<String> nameCandidatesListMenu;

    private ArrayAdapter nameCandidatesListMenuAdapter;

    private TextView TV_TitleKrName;
    private TextView TV_FiveEleTree;
    private TextView TV_FiveEleFire;
    private TextView TV_FiveEleSoil;
    private TextView TV_FiveEleMetal;
    private TextView TV_FiveEleWater;

    private TextView TV_ResultChNames;
    private ListView LV_ResultChNames;

    private ProgressBar mainProgressBar;
    private CheckBox CB_HanjaSuri81;
    private CheckBox CB_HanjaSuriFiveEle;
    private CheckBox CB_HanjaSuriYinYang;
    private CheckBox CB_HanjaSajuFiveEle;

    private boolean is_applied_haja_suri_81 = false;
    private boolean is_applied_hanja_suri_five_ele = false;
    private boolean is_applied_hanja_suri_yinyang = false;
    private boolean is_applied_hanja_saju_five_ele = false;

    private void controlUI(boolean flag) {
        CB_HanjaSuri81.setEnabled(flag);
        CB_HanjaSuri81.setChecked(is_applied_haja_suri_81);
        CB_HanjaSuriFiveEle.setEnabled(flag);
        CB_HanjaSuriFiveEle.setChecked(is_applied_hanja_suri_five_ele);
        CB_HanjaSuriYinYang.setEnabled(flag);
        CB_HanjaSuriYinYang.setChecked(is_applied_hanja_suri_yinyang);
        CB_HanjaSajuFiveEle.setEnabled(flag);
        CB_HanjaSajuFiveEle.setChecked(is_applied_hanja_saju_five_ele);
    }

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

    private void initHanjaNameOptionCBs() {
        CB_HanjaSuri81 = (CheckBox) findViewById(R.id.hanja_suri_81);
        CB_HanjaSuri81.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_applied_haja_suri_81 =isChecked;
                MakeNamesAsyncTask doMakeNamesAsyncTask = new MakeNamesAsyncTask();
                doMakeNamesAsyncTask.execute();
            }
        });
        CB_HanjaSuriFiveEle = (CheckBox) findViewById(R.id.hanja_suri_five_ele);
        CB_HanjaSuriFiveEle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_applied_hanja_suri_five_ele = isChecked;
                MakeNamesAsyncTask doMakeNamesAsyncTask = new MakeNamesAsyncTask();
                doMakeNamesAsyncTask.execute();
            }
        });
        CB_HanjaSuriYinYang = (CheckBox) findViewById(R.id.hanja_suri_yinyang);
        CB_HanjaSuriYinYang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_applied_hanja_suri_yinyang = isChecked;
                MakeNamesAsyncTask doMakeNamesAsyncTask = new MakeNamesAsyncTask();
                doMakeNamesAsyncTask.execute();
            }
        });
        CB_HanjaSajuFiveEle = (CheckBox) findViewById(R.id.hanja_saju_five_ele);
        CB_HanjaSajuFiveEle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_applied_hanja_saju_five_ele = isChecked;
                MakeNamesAsyncTask doMakeNamesAsyncTask = new MakeNamesAsyncTask();
                doMakeNamesAsyncTask.execute();
            }
        });


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
        nameAppliedCandidatesLetters = new ArrayList<ArrayList<Letter>>();
        nameCandidatesListMenu = new ArrayList<String>();
        for (int i = 0; i < nameCandidatesLetters.size(); i++) {
            nameCandidatesListMenu.add(i, "[ " +nameCandidatesLetters.get(i).get(MIDDLE_NAME_IDX).getMean() + " ] "
                    + nameCandidatesLetters.get(i).get(MIDDLE_NAME_IDX).getChLetter()
                    + " [ " + nameCandidatesLetters.get(i).get(LAST_NAME_IDX).getMean() + " ] "
                    + nameCandidatesLetters.get(i).get(LAST_NAME_IDX).getChLetter());
            nameAppliedCandidatesLetters.add(i, nameCandidatesLetters.get(i));
        }

        nameCandidatesListMenuAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, nameCandidatesListMenu);
        nameCandidatesListMenuAdapter.setNotifyOnChange(true);

        LV_ResultChNames = (ListView) findViewById(R.id.LV_ResultChNames);
        LV_ResultChNames.setAdapter(nameCandidatesListMenuAdapter);
        LV_ResultChNames.setMinimumHeight(180);
        LV_ResultChNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Letter> finalChoice = nameAppliedCandidatesLetters.get(position);
                String krName = new String();
                String chName = new String();
                for(int i = 0; i < finalChoice.size(); i++) {
                    Log.d(TAG, "i = " + i + " " +finalChoice.get(i).getChLetter());
                    krName = krName + finalChoice.get(i).getKrLetter();
                    chName = chName + finalChoice.get(i).getChLetter();
                }
                Intent intent = new Intent(Main2Activity.this, PopUpActivity.class);
                intent.putExtra(EXTRA_KEY_FULL_CH_NAME, chName);
                intent.putExtra(EXTRA_KEY_FULL_KR_NAME, krName);
                intent.putExtra(EXTRA_KEY_FULL_NAME_LEN, fullNameLen);
                String birthDate = birthYear + "년 " + birthMonth + "월 " + birthDay + "일 ";
                if (Integer.parseInt(birthHour) < 10)
                    birthDate = birthDate + "0" + birthHour + "시 ";
                else
                    birthDate = birthDate + birthHour + "시 ";
                if (Integer.parseInt(birthMinute) < 10)
                    birthDate = birthDate + "0" + birthMinute + "분 ";
                else
                    birthDate = birthDate + birthMinute + "분 ";

                intent.putExtra(EXTRA_KEY_BIRTH_DATE, birthDate);
                intent.putExtra(EXTRA_KEY_BIRTH_SAJU, saju[0] + "년 " + saju[1] +
                        "월 " + saju[2] + "일 " + saju[3]  + "시");

                int saju_five_ele_tree_local = saju_five_ele_tree;
                int saju_five_ele_fire_local = saju_five_ele_fire;
                int saju_five_ele_soil_local = saju_five_ele_soil;
                int saju_five_ele_metal_local = saju_five_ele_metal;
                int saju_five_ele_water_local = saju_five_ele_water;

                for (int j = 1; j < finalChoice.size(); j++) {
                    Letter eachLetter = finalChoice.get(j);
                    if (eachLetter.getRadFiveElement().equals("木")) {
                        saju_five_ele_tree_local++;
                    } else if (eachLetter.getRadFiveElement().equals("火")) {
                        saju_five_ele_fire_local++;
                    } else if (eachLetter.getRadFiveElement().equals("土")) {
                        saju_five_ele_soil_local++;
                    } else if (eachLetter.getRadFiveElement().equals("金")) {
                        saju_five_ele_metal_local++;
                    } else if (eachLetter.getRadFiveElement().equals("水")) {
                        saju_five_ele_water_local++;
                    }
                }
                intent.putExtra(EXTRA_KEY_FIVE_ELE_TREE, saju_five_ele_tree_local);
                intent.putExtra(EXTRA_KEY_FIVE_ELE_FIRE, saju_five_ele_fire_local);
                intent.putExtra(EXTRA_KEY_FIVE_ELE_SOIL, saju_five_ele_soil_local);
                intent.putExtra(EXTRA_KEY_FIVE_ELE_METAL, saju_five_ele_metal_local);
                intent.putExtra(EXTRA_KEY_FIVE_ELE_WATER, saju_five_ele_water_local);

                ArrayList<Integer> fourFormSuri = getFourFormSuri(finalChoice);
                intent.putExtra(EXTRA_KEY_SURI_FOUR_FORM_WON, dbManager.getSuri(fourFormSuri.get(FOUR_FORM_SURI_WON_IDX)).getSuri());
                intent.putExtra(EXTRA_KEY_SURI_FOUR_FORM_HYUNG, dbManager.getSuri(fourFormSuri.get(FOUR_FORM_SURI_HYUNG_IDX)).getSuri());
                intent.putExtra(EXTRA_KEY_SURI_FOUR_FORM_EE, dbManager.getSuri(fourFormSuri.get(FOUR_FORM_SURI_EE_IDX)).getSuri());
                intent.putExtra(EXTRA_KEY_SURI_FOUR_FORM_JEONG, dbManager.getSuri(fourFormSuri.get(FOUR_FORM_SURI_JEONG_IDX)).getSuri());

                startActivity(intent);
                /*
                MakeNamesAsyncTask doMakeNamesAsyncTask = new MakeNamesAsyncTask();
                doMakeNamesAsyncTask.execute();
                */
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
        /*
        Log.d(TAG, saju[0] + " " + saju[1] + " " + saju[2] + " " +saju[3]);
        */
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
                name.add(MIDDLE_NAME_IDX, dbManager.getLetterFromCh(middleName.get(i).getChLetter(), DBManager.TYPE_NORMAL_NAME));
                name.add(LAST_NAME_IDX, dbManager.getLetterFromCh(lastName.get(j).getChLetter(), DBManager.TYPE_NORMAL_NAME));
                nameCandidatesLetters.add(name);
            }
        }
        /*
        for (int i = 0; i < nameCandidatesLetters.size(); i++) {
            Log.d(TAG, nameCandidatesLetters.get(i).get(FAMILY_NAME_IDX).getChLetter()
                    + nameCandidatesLetters.get(i).get(MIDDLE_NAME_IDX).getChLetter()
                    + nameCandidatesLetters.get(i).get(LAST_NAME_IDX).getChLetter());
        }
        */
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        dbManager = new DBManager(this);;
        mainProgressBar = (ProgressBar) findViewById(R.id.progressBarCh);

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

        initHanjaNameOptionCBs();

        //Log.d(TAG, this.fullNameLen + ":" + fullNameKr + ":" + famlilyNameLetter.getChLetter() + ":" + this.nameCandidatesLetters);
    }

    private ArrayList<Integer> getFourFormSuri(ArrayList<Letter> name) {
        Letter familyNameLetter = name.get(FAMILY_NAME_IDX);
        Letter firstNameLetter = name.get(FAMILY_NAME_IDX+1);
        Letter lastNameLetter = name.get(LAST_NAME_IDX);

        ArrayList<Integer> result = new ArrayList<Integer>(FOUR_FORM_SURI_JEONG_IDX + 1);
        /*
        Log.d(TAG, familyNameLetter.getChLetter() + " familyNameLetter.getStroke() = " + familyNameLetter.getStroke());
        Log.d(TAG, firstNameLetter.getChLetter() + " firstNameLetter.getStroke() = " + firstNameLetter.getStroke());
        Log.d(TAG, lastNameLetter.getChLetter() + " lastNameLetter.getStroke() = " + lastNameLetter.getStroke());
        */
        result.add(FOUR_FORM_SURI_WON_IDX, (firstNameLetter.getStroke() + lastNameLetter.getStroke()));
        result.add(FOUR_FORM_SURI_HYUNG_IDX, (familyNameLetter.getStroke() + firstNameLetter.getStroke()));
        result.add(FOUR_FORM_SURI_EE_IDX, (familyNameLetter.getStroke() + lastNameLetter.getStroke()));
        result.add(FOUR_FORM_SURI_JEONG_IDX, (familyNameLetter.getStroke() + firstNameLetter.getStroke() + lastNameLetter.getStroke()));

        /*
        for (int i = 0; i < result.size(); i++) {
            Log.d(TAG, " FOUR FORM " + i + " " + result.get(i));
        }
        */

        return result;
    }

    class MakeNamesAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            //controlKrUI(false);
            nameCandidatesListMenu.clear();
            mainProgressBar.setMax(100);
            controlUI(false);

            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            nameAppliedCandidatesLetters = new ArrayList<ArrayList<Letter>>();
            int cnt = 0;
            for (int i = 0; i < nameCandidatesLetters.size(); i++) {
                boolean is_needed_to_add = true;
                ArrayList<Letter> nameCandidates = nameCandidatesLetters.get(i);

                /*
                for (int j = 0; j < nameCandidates.size(); j++) {
                    Log.d(TAG, nameCandidates.get(j).getKrLetter() + " " + nameCandidates.get(j).getChLetter());
                }
                */
                ArrayList<Integer> fourFormSuri = getFourFormSuri(nameCandidates);
                if (is_applied_haja_suri_81) {
                    for (int j = 0; j < fourFormSuri.size(); j++) {
                        Suri81 eachSuri = dbManager.getSuri(fourFormSuri.get(j));
                        if (!eachSuri.getEvaluation().equals(Suri81.EVAL_GOOD))
                            is_needed_to_add = is_needed_to_add && false;
                    }
                }
                if (is_applied_hanja_suri_five_ele) {
                    ArrayList<Integer> name_five_eles_value = new ArrayList<Integer>();
                    ArrayList<Integer> name_five_eles = new ArrayList<Integer>();
                    name_five_eles_value.add(FAMILY_NAME_IDX, fourFormSuri.get(FOUR_FORM_SURI_EE_IDX)%10);
                    name_five_eles_value.add(MIDDLE_NAME_IDX, fourFormSuri.get(FOUR_FORM_SURI_HYUNG_IDX)%10);
                    name_five_eles_value.add(LAST_NAME_IDX, fourFormSuri.get(FOUR_FORM_SURI_WON_IDX)%10);

                    for (int j = 0; j < name_five_eles_value.size(); j++) {
                        if ((name_five_eles_value.get(j) == 1) || (name_five_eles_value.get(j) == 2)) {
                            name_five_eles.add(j, FIVE_ELE_TREE);
                        } else if ((name_five_eles_value.get(j) == 3) || (name_five_eles_value.get(j) == 4)) {
                            name_five_eles.add(j, FIVE_ELE_FIRE);
                        } else if ((name_five_eles_value.get(j) == 5) || (name_five_eles_value.get(j) == 6)) {
                            name_five_eles.add(j, FIVE_ELE_SOIL);
                        } else if ((name_five_eles_value.get(j) == 7) || (name_five_eles_value.get(j) == 8)) {
                            name_five_eles.add(j, FIVE_ELE_METAL);
                        } else if((name_five_eles_value.get(j) == 9) || (name_five_eles_value.get(j) == 0)) {
                            name_five_eles.add(j, FIVE_ELE_WATER);
                        }
                    }
                    int cnt_best = 0;
                    int cnt_bad = 0;
                    for (int j = 0; j < name_five_eles.size() - 1; j++) {
                        int cur_ele = name_five_eles.get(j);
                        int next_ele = name_five_eles.get(j+1);
                        if (cur_ele == FIVE_ELE_TREE) {
                            if ((next_ele == FIVE_ELE_FIRE) || (next_ele == FIVE_ELE_WATER)) {
                                cnt_best++;
                            } else {
                                cnt_bad++;
                            }
                        } else {
                            int res = (next_ele - cur_ele);
                            if ((res == -1) || (res == 1))
                                cnt_best++;
                            else if (res == 0)
                                ;
                            else
                                cnt_bad++;
                        }
                    }

                    if (!((cnt_best > 0) && (cnt_bad == 0))) {
                        is_needed_to_add = is_needed_to_add && false;
                    }
                }
                if (is_applied_hanja_suri_yinyang) {
                    int cnt_yin = 0;
                    int cnt_yang = 0;
                    for (int j = 0; j < nameCandidates.size(); j++) {
                        if ((nameCandidates.get(j).getStroke() % 2) == 0){
                            cnt_yin++;
                        } else {
                            cnt_yang++;
                        }
                    }
                    if ((cnt_yin == 0) || (cnt_yang ==0)) {
                        is_needed_to_add = is_needed_to_add && false;
                    }
                }

                if (is_applied_hanja_saju_five_ele) {
                    int saju_five_ele_tree_local = saju_five_ele_tree;
                    int saju_five_ele_fire_local = saju_five_ele_fire;
                    int saju_five_ele_soil_local = saju_five_ele_soil;
                    int saju_five_ele_metal_local = saju_five_ele_metal;
                    int saju_five_ele_water_local = saju_five_ele_water;

                    for (int j = 1; j < nameCandidates.size(); j++) {
                        Letter eachLetter = nameCandidates.get(j);
                        if (eachLetter.getRadFiveElement().equals("木")) {
                            saju_five_ele_tree_local++;
                        } else if (eachLetter.getRadFiveElement().equals("火")) {
                            saju_five_ele_fire_local++;
                        } else if (eachLetter.getRadFiveElement().equals("土")) {
                            saju_five_ele_soil_local++;
                        } else if (eachLetter.getRadFiveElement().equals("金")) {
                            saju_five_ele_metal_local++;
                        } else if (eachLetter.getRadFiveElement().equals("水")) {
                            saju_five_ele_water_local++;
                        }
                    }

                    if ((saju_five_ele_tree_local == 0) ||
                            (saju_five_ele_fire_local == 0) ||
                            (saju_five_ele_soil_local == 0) ||
                            (saju_five_ele_metal_local == 0) ||
                            (saju_five_ele_water_local == 0)) {
                        is_needed_to_add = is_needed_to_add && false;
                    }
                }
                    if (is_needed_to_add) {
                    nameCandidatesListMenu.add("[ " + nameCandidatesLetters.get(i).get(MIDDLE_NAME_IDX).getMean() + " ] "
                            + nameCandidatesLetters.get(i).get(MIDDLE_NAME_IDX).getChLetter()
                            + " [ " + nameCandidatesLetters.get(i).get(LAST_NAME_IDX).getMean() + " ] "
                            + nameCandidatesLetters.get(i).get(LAST_NAME_IDX).getChLetter());
                    nameAppliedCandidatesLetters.add(nameCandidates);
                }
            }
            /*
            String name = new String();
            int past_progress = 0;
            int cur_progress;
            publishProgress(past_progress);
            String[] namesAppliedArray = new String[namesFullList.size()];

            if (ownName.isEmpty())
                return 0;

            if (pro_option_apply == false) {
                namesAppliedList.addAll(namesFullList);
            }

            name = name + ownName.get(POS_FAMILY_NAME).getKrLetter();

            for (int i = 0; i < namesFullList.size(); i++) {
                String eachName = name + namesFullList.get(i);
                Boolean addFlag = true;
                int result;

                if (pro_option_yinyang) {
                    result = evaluate_pro_yy(eachName);
                    if (result == PRO_YY_ELE_BEST) {
                        addFlag &= true;
                        //namesAppliedList.add(eachName);
                    } else {
                        addFlag &= false;
                    }
                }

                if (pro_option_five_ele_first) {
                    result = evaluate_pro_five_ele_best_first_only(eachName);
                    if (result == PRO_FIVE_ELE_BEST) {
                        addFlag &= true;
                    } else {
                        addFlag &= false;
                    }
                } else if (pro_option_five_ele_firstlast) {
                    result = evaluate_pro_five_ele_best_firstlast_only(eachName);
                    if (result == PRO_FIVE_ELE_BEST) {
                        addFlag &= true;
                    } else {
                        addFlag &= false;
                    }
                } else if (pro_option_five_ele_firstorlast) {
                    if ((evaluate_pro_five_ele_best_first_only(eachName) == PRO_FIVE_ELE_BEST) ||
                            (evaluate_pro_five_ele_best_firstlast_only(eachName) == PRO_FIVE_ELE_BEST)) {
                        addFlag &= true;
                    } else {
                        addFlag &= false;
                    }
                }
                */
                /* Progress Bar */
                /*
                if (addFlag)
                    namesAppliedList.add(namesFullList.get(i));

                cur_progress = i * 100 / namesFullList.size();

                if (cur_progress > past_progress) {
                    past_progress = cur_progress;
                    publishProgress(Integer.valueOf(past_progress));
                }

            }*/
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            //krProgressBar.setProgress(params[0].intValue());
            //Log.d(TAG, "i = " + params[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            nameCandidatesListMenuAdapter.notifyDataSetChanged();
            TV_ResultChNames.setText(getString(R.string.ResultChNamesTitle) + ": " + nameCandidatesListMenu.size());
            controlUI(true);
            /*
            controlKrUI(true);
            if(!namesAppliedList.isEmpty()) {

                Set<String> hs = new HashSet<String>();
                hs.addAll((ArrayList<String>) namesAppliedList.clone());
                namesAppliedList.clear();
                namesAppliedList.addAll(hs);
            }

            Log.d(TAG, "possible names = " + namesAppliedList.size());
            resultCntsTV.setText("가능한 이름 조합의 수: " +  namesAppliedList.size());
            */
            super.onPostExecute(result);

        }
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
            }
        }
    }*/



}



