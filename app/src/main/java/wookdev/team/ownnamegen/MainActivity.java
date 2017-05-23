package wookdev.team.ownnamegen;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_KEY_KR_NAME = "only_name_kr";
    public static final String EXTRA_KEY_CH_FAMILY_NAME = "fam_name_ch";
    public static final String EXTRA_KEY_BIRTH_YEAR = "birth_year";
    public static final String EXTRA_KEY_BIRTH_MONTH = "birth_month";
    public static final String EXTRA_KEY_BIRTH_DAY = "birth_day";
    public static final String EXTRA_KEY_BIRTH_HOUR = "birth_hour";
    public static final String EXTRA_KEY_BIRTH_MINUTE = "birth_minute";
    public static final String EXTRA_KEY_BIRTH_LOCATION = "birth_location";
    private static final int MAX_LEN_NAME = 3;
    private static final int POS_FAMILY_NAME = 0;
    private static final int FIVE_ELE_TREE = 0;
    private static final int FIVE_ELE_FIRE = 1;
    private static final int FIVE_ELE_SOIL = 2;
    private static final int FIVE_ELE_METAL = 3;
    private static final int FIVE_ELE_WATER = 4;
    private static final int PRO_FIVE_ELE_BEST = 0;
    private static final int PRO_FIVE_ELE_GOOD = 1;
    private static final int PRO_FIVE_ELE_BAD = 2;
    private static final int PRO_YY_ELE_BEST = 0;
    private static final int PRO_YY_ELE_GOOD = 1;
    private static final int PRO_YY_ELE_BAD = 2;

    private static final String[] FIVE_ELE_TREE_KR_ARRAY = {"ㄱ", "ㅋ"};
    private static final String[] FIVE_ELE_FIRE_KR_ARRAY = {"ㄴ", "ㄷ","ㄹ","ㅌ"};
    private static final String[] FIVE_ELE_SOIL_KR_ARRAY = {"ㅇ", "ㅎ"};
    private static final String[] FIVE_ELE_METAL_KR_ARRAY = {"ㅅ", "ㅈ","ㅊ"};
    private static final String[] FIVE_ELE_WATER_KR_ARRAY = {"ㅁ", "ㅂ","ㅍ"};

    private static final String[] FIVE_ELE_TREE_KR_ARRAY_MINOR = {"ㄱ", "ㅋ"};
    private static final String[] FIVE_ELE_FIRE_KR_ARRAY_MINOR = {"ㄴ", "ㄷ","ㄹ","ㅌ"};
    private static final String[] FIVE_ELE_SOIL_KR_ARRAY_MINOR = {"ㅇ", "ㅎ"};
    private static final String[] FIVE_ELE_METAL_KR_ARRAY_MINOR = {"ㅅ", "ㅈ","ㅊ"};
    private static final String[] FIVE_ELE_WATER_KR_ARRAY_MINOR = {"ㅁ", "ㅂ","ㅍ"};

    private static final String[] PRO_YANG = {
            "ㅏ", "ㅐ", "ㅑ", "ㅒ","ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ",
    };

    private static final String[] PRO_YIN = {
            "ㅓ", "ㅔ", "ㅕ", "ㅖ",
            "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ",
            "ㅢ", "ㅣ"
    };

    private ArrayList<String> PRO_YANG_KR_ARRAYLIST = new ArrayList<String>(Arrays.asList(PRO_YANG));
    private ArrayList<String> PRO_YIN_KR_ARRAYLIST = new ArrayList<String>(Arrays.asList(PRO_YIN));;

    private ArrayList<String> FIVE_ELE_TREE_KR_ARRAYLIST;
    private ArrayList<String> FIVE_ELE_FIRE_KR_ARRAYLIST;
    private ArrayList<String> FIVE_ELE_SOIL_KR_ARRAYLIST;
    private ArrayList<String> FIVE_ELE_METAL_KR_ARRAYLIST;
    private ArrayList<String> FIVE_ELE_WATER_KR_ARRAYLIST;

    private int name_len;
    private AutoCompleteTextView fn_autoCompleteTV;
    private Spinner fn_spinner;
    private ArrayAdapter<String> fn_autoCompleteTVAdapter;
    private ArrayAdapter<String> fn_spinnerAdapter;
    private Button birth_DateButton;
    private Button birth_TimeButton;
    private Button birth_LocationButton;
    private ArrayAdapter<String> birthLocationButton_ArrayAdapter;

    private RadioGroup nametype_RadioGroup;
    private RadioButton nametype_RadioButton1;
    private RadioButton nametype_RadioButton2;

    private ProgressBar krProgressBar;

    private CheckBox proOptionApply_CB;
    private CheckBox proOptionYY_CB;
    private RadioGroup proOptionFE_RG;
    private RadioButton proOptionFE_first_RB;
    private RadioButton proOptionFE_firstlast_RB;
    private RadioButton proOptionFE_firstorlast_RB;


    private ArrayList<Letter> ownName;
    private ArrayList<String> namesFullList;
    private ArrayList<String> namesAppliedList;

    private TextView resultCntsTV;
    private AutoCompleteTextView resultNames_autoCompleteTV;
    private ArrayAdapter<String> resultNames_autoCompleteTVAdapter;

    private DBManager dbManager;
    //private MakeKrNameAsyncTask doMakeKrNamesAsyncTask;

    private boolean pro_option_apply = false;
    private boolean pro_option_yinyang = false;
    private boolean pro_option_five_ele_first = false;
    private boolean pro_option_five_ele_firstlast = false;
    private boolean pro_option_five_ele_firstorlast = false;

    private int saju_year;
    private int saju_month;
    private int saju_day;
    private int saju_hour;
    private int saju_minute;
    private String saju_location;

    /**
     * 초성
     */
    private static final char[] firstSounds = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ',
            'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ',
            'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    //한글 중성
    private static final char[] middleSounds = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ',
            'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ',
            'ㅢ', 'ㅣ'};
    //한글 종성
    private static final char[] lastSounds = {' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ',
            'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
            'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    private static final String[] locations = {
            "목포", "서산", "제주", "보령", "서귀포", "인천", "군산",
            "정읍", "광주", "서울", "수원", "평택", "전주",
            "천안", "남원", "대전", "청주", "춘천", "여수", "충주",
            "원주", "사천", "김천", "상주", "통영", "마산", "속초",
            "대구", "안동", "강릉", "태백", "부산", "동해", "경주",
            "울산", "포항", "울진"
    };



    public static int[] kr_letter_split(char c){
        int sub[] = new int[3];
        sub[0] = (c - 0xAC00) / (21*28); //초성의 위치
        sub[1] = ((c - 0xAC00) % (21*28)) / 28; //중성의 위치
        sub[2] = (c -0xAC00) % (28);//종성의 위치
        return sub;
    }

    public static boolean isHangul(char c) {
        if( c < 0xAC00 || c > 0xD7A3 )
            return false;
        return true;
    }

    public static int getLastElementCode(char c) {
        if( ! isHangul(c) )
            return -1;
        return (c - 0xAC00) % 28;
    }

    final TextWatcher textChecker = new TextWatcher() {
        public void afterTextChanged(Editable s) {}

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            ArrayList<String> familyNamesKr;
            fn_autoCompleteTVAdapter.clear();
            fn_spinnerAdapter.clear();
            familyNamesKr = dbManager.getFamilyNameKr(s.toString());
            fn_autoCompleteTVAdapter.addAll(familyNamesKr);
        }
    };

    final TextWatcher textChecker2 = new TextWatcher() {
        public void afterTextChanged(Editable s) {}

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            resultNames_autoCompleteTVAdapter.clear();
            //Log.d(TAG, "size = " + namesAppliedList);
            resultNames_autoCompleteTVAdapter.addAll(namesAppliedList);
        }
    };
    final RadioButton.OnClickListener nametype_RadioButtonOnClickListener =
            new RadioButton.OnClickListener() {
                public void onClick(View v) {
                    setNameLen();
                    resultNames_autoCompleteTV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(name_len)});
                }
            };
    final RadioButton.OnClickListener profiveele_RadioButtonOnClickListener =
            new RadioButton.OnClickListener() {
                public void onClick(View v) {
                    View radioButton = proOptionFE_RG.findViewById(proOptionFE_RG.getCheckedRadioButtonId());
                    int idx = proOptionFE_RG.indexOfChild(radioButton);
                    switch (idx){
                        case 0:
                            pro_option_five_ele_first = true;
                            pro_option_five_ele_firstlast = false;
                            pro_option_five_ele_firstorlast = false;
                            break;
                        case 1:
                            pro_option_five_ele_first = false;
                            pro_option_five_ele_firstlast = true;
                            pro_option_five_ele_firstorlast = false;
                            break;
                        case 2:
                            pro_option_five_ele_first = false;
                            pro_option_five_ele_firstlast = false;
                            pro_option_five_ele_firstorlast = true;
                            break;
                        default:
                            pro_option_five_ele_first = true;
                            pro_option_five_ele_firstlast = false;
                            pro_option_five_ele_firstorlast = false;
                            break;
                    }
                    //DO!!!!
                    MakeKrNameAsyncTask doMakeKrNamesAsyncTask = new MakeKrNameAsyncTask();
                    doMakeKrNamesAsyncTask.execute();
                }
            };


    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            saju_year = year;
            saju_month = monthOfYear + 1;
            saju_day = dayOfMonth;

            birth_DateButton.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(saju_year).append("년 ").append(saju_month).append("월 ")
                    .append(saju_day).append("일"));
        }
    }

    private void controlKrUI(boolean flag) {
            fn_autoCompleteTV.setEnabled(flag);
            fn_autoCompleteTV.setCursorVisible(flag);
            fn_spinner.setEnabled(flag);
            fn_spinner.setClickable(flag);
            birth_DateButton.setEnabled(flag);
            birth_DateButton.setClickable(flag);
            birth_TimeButton.setEnabled(flag);
            birth_TimeButton.setClickable(flag);
            birth_LocationButton.setEnabled(flag);
            birth_LocationButton.setClickable(flag);
            nametype_RadioButton1.setClickable(false);
            nametype_RadioButton1.setEnabled(false);
            nametype_RadioButton2.setClickable(flag);
            nametype_RadioButton2.setEnabled(flag);
            proOptionApply_CB.setEnabled(flag);
            if(pro_option_apply)
                controlKrProUI(flag);
            if(resultNames_autoCompleteTV != null) {
                resultNames_autoCompleteTV.setEnabled(flag);
            }
    }

    private void controlKrProUI(boolean flag) {
        proOptionYY_CB.setEnabled(flag);
        proOptionFE_first_RB.setEnabled(flag);
        proOptionFE_firstlast_RB.setEnabled(flag);
        proOptionFE_firstorlast_RB.setEnabled(flag);

        proOptionFE_first_RB.setChecked(pro_option_five_ele_first);
        proOptionFE_firstlast_RB.setChecked(pro_option_five_ele_firstlast);
        proOptionFE_firstorlast_RB.setChecked(pro_option_five_ele_firstorlast);
        proOptionYY_CB.setChecked(pro_option_yinyang);

    }

    class MakeKrNameAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            controlKrUI(false);

            krProgressBar.setMax(100);
            namesAppliedList.clear();
            if (resultNames_autoCompleteTV != null) {
                resultNames_autoCompleteTV.setText("");
            }
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
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
                /* Progress Bar */
                if (addFlag)
                    namesAppliedList.add(namesFullList.get(i));

                cur_progress = i * 100 / namesFullList.size();

                if (cur_progress > past_progress) {
                    past_progress = cur_progress;
                    publishProgress(Integer.valueOf(past_progress));
                }

            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            krProgressBar.setProgress(params[0].intValue());
            //Log.d(TAG, "i = " + params[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            controlKrUI(true);
            if(!namesAppliedList.isEmpty()) {

                Set<String> hs = new HashSet<String>();
                hs.addAll((ArrayList<String>) namesAppliedList.clone());
                namesAppliedList.clear();
                namesAppliedList.addAll(hs);
            }

            Log.d(TAG, "possible names = " + namesAppliedList.size());
            resultCntsTV.setText("가능한 이름 조합의 수: " +  namesAppliedList.size());

            super.onPostExecute(result);

        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saju_year = -1;
        saju_month = -1;
        saju_day = -1;
        saju_hour = -1;
        saju_minute = -1;
        saju_location = getString(R.string.BirthLocation);

        ownName = new ArrayList<Letter>(MAX_LEN_NAME);
        FIVE_ELE_TREE_KR_ARRAYLIST = new ArrayList<String>(Arrays.asList(FIVE_ELE_TREE_KR_ARRAY));
        FIVE_ELE_FIRE_KR_ARRAYLIST = new ArrayList<String>(Arrays.asList(FIVE_ELE_FIRE_KR_ARRAY));
        FIVE_ELE_SOIL_KR_ARRAYLIST = new ArrayList<String>(Arrays.asList(FIVE_ELE_SOIL_KR_ARRAY));
        FIVE_ELE_METAL_KR_ARRAYLIST = new ArrayList<String>(Arrays.asList(FIVE_ELE_METAL_KR_ARRAY));
        FIVE_ELE_WATER_KR_ARRAYLIST = new ArrayList<String>(Arrays.asList(FIVE_ELE_WATER_KR_ARRAY));

        krProgressBar = (ProgressBar) findViewById(R.id.progressBarKr);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DBManager(this);
        fn_spinner = (Spinner) findViewById(R.id.FamilyNameSpinner);
        fn_spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item);
        fn_spinnerAdapter.setNotifyOnChange(true);
        fn_spinner.setAdapter(fn_spinnerAdapter);
        fn_autoCompleteTVAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line);
        fn_autoCompleteTVAdapter.setNotifyOnChange(true);
        fn_autoCompleteTV = (AutoCompleteTextView) findViewById(R.id.FamilyNameACTextView);

        fn_autoCompleteTV.setAdapter(fn_autoCompleteTVAdapter);
        fn_autoCompleteTV.addTextChangedListener(textChecker);
        fn_autoCompleteTV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        fn_autoCompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ArrayList<String> spinnerAL = new ArrayList<String>();
                fn_spinnerAdapter.clear();
                ArrayList<Letter> familyNamesAL = dbManager.getFamilyNameInfo(fn_autoCompleteTVAdapter.getItem(position).toString());
                for ( int i = 0; i < familyNamesAL.size(); i++) {
                    Letter eachLetter = familyNamesAL.get(i);
                    spinnerAL.add(eachLetter.getChLetter() + " [" + eachLetter.getMean() +"]");
                    //Log.d(TAG, "kr = " + familyNamesAL.get(i).getKrLetter() + " ch = " + familyNamesAL.get(i).getChLetter() + " mean = " + familyNamesAL.get(i).getMean());
                }
                for ( int i = 0; i < spinnerAL.size(); i++) {
                    Log.d(TAG, spinnerAL.get(i));
                }
                fn_spinnerAdapter.addAll(spinnerAL);
            }
        });
        fn_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int cutIdx;
                String ch_letter;
                String str = (String) fn_spinner.getSelectedItem();
                Letter familyNameLetter;

                cutIdx = str.indexOf("[");
                ch_letter = str.substring(0, cutIdx-1);
                familyNameLetter = dbManager.getLetterFromCh(ch_letter, DBManager.TYPE_FAMILY_NAME);
                if (!familyNameLetter.isLetter) {
                    Log.e(TAG, "ERR: cannot find family name.");
                    return;
                }
                ownName.clear();
                familyNameLetter.setPos(POS_FAMILY_NAME);
                ownName.add(familyNameLetter.getPos(), familyNameLetter);

                //DO!!
                MakeKrNameAsyncTask doMakeKrNamesAsyncTask = new MakeKrNameAsyncTask();
                doMakeKrNamesAsyncTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
        namesAppliedList = new ArrayList<String>();

        birth_DateButton = (Button) findViewById(R.id.birthDateButton);
        birth_TimeButton = (Button) findViewById(R.id.birthTimeButton);
        birth_DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });
        birth_TimeButton.setOnClickListener(new View.OnClickListener() {
            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    saju_hour = hourOfDay;
                    saju_minute = minute;
                    birth_TimeButton.setText(saju_hour + ":" + saju_minute);
                }
            };
            @Override
            public void onClick(View v) {

                TimePickerDialog dialog = new TimePickerDialog(MainActivity.this,
                        listener, 15, 24, false);

                dialog.show();
            }
        });
        birth_LocationButton = (Button) findViewById(R.id.birthLocationButton);
        birthLocationButton_ArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        birthLocationButton_ArrayAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int compare = o1.compareTo(o2);
                return compare;
            }
        });
        birth_LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.BirthLocation)
                        .setAdapter(birthLocationButton_ArrayAdapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: user specific action
                                saju_location = locations[which];
                                birth_LocationButton.setText(saju_location);
                                dialog.dismiss();
                            }
                        }).create().show();
                //birth_LocationButton.setText(saju_location);
            }
        });

        nametype_RadioGroup = (RadioGroup) findViewById(R.id.nametypeRradioGroup);
        setNameLen();
        namesFullList = dbManager.getNamesFullList(name_len);
        nametype_RadioButton1 = (RadioButton) findViewById(R.id.nametypeRB1);
        nametype_RadioButton2 = (RadioButton) findViewById(R.id.nametypeRB2);
        nametype_RadioButton1.setOnClickListener(nametype_RadioButtonOnClickListener);
        nametype_RadioButton2.setOnClickListener(nametype_RadioButtonOnClickListener);

        proOptionFE_RG = (RadioGroup) findViewById(R.id.prooptionfiveeleRG);
        proOptionFE_first_RB = (RadioButton) findViewById(R.id.prooptionfirstRB);
        proOptionFE_firstlast_RB = (RadioButton) findViewById(R.id.prooptionfirstlastRB);
        proOptionFE_firstorlast_RB = (RadioButton) findViewById(R.id.prooptionfirstorlastRB);
        proOptionFE_first_RB.setOnClickListener(profiveele_RadioButtonOnClickListener);
        proOptionFE_firstlast_RB.setOnClickListener(profiveele_RadioButtonOnClickListener);
        proOptionFE_firstorlast_RB.setOnClickListener(profiveele_RadioButtonOnClickListener);
        proOptionYY_CB = (CheckBox) findViewById(R.id.prooptionYYCB);
        proOptionApply_CB = (CheckBox) findViewById(R.id.prooptionallCB);
        proOptionApply_CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (buttonView.getId() == R.id.prooptionallCB) {
                    if (isChecked) {
                        pro_option_apply = true;
                       // pro_option_five_ele_first = true;
                    } else {
                        pro_option_apply = false;
                        pro_option_yinyang = false;
                        pro_option_five_ele_first = false;
                        pro_option_five_ele_firstlast = false;
                        pro_option_five_ele_firstorlast = false;
                    }
                    proOptionFE_first_RB.setChecked(pro_option_five_ele_first);
                    proOptionFE_firstlast_RB.setChecked(pro_option_five_ele_firstlast);
                    proOptionFE_firstorlast_RB.setChecked(pro_option_five_ele_firstorlast);
                    proOptionYY_CB.setChecked(pro_option_yinyang);
                    controlKrProUI(pro_option_apply);


                    //DO!!!!
                    MakeKrNameAsyncTask doMakeKrNamesAsyncTask = new MakeKrNameAsyncTask();
                    doMakeKrNamesAsyncTask.execute();
                }
            }
        });

        controlKrProUI(pro_option_apply);

        proOptionYY_CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (buttonView.getId() == R.id.prooptionYYCB) {
                    if (isChecked) {
                        pro_option_yinyang = true;
                        // pro_option_five_ele_first = true;
                    } else {
                        pro_option_yinyang = false;
                    }
                    //DO!!!!
                    MakeKrNameAsyncTask doMakeKrNamesAsyncTask = new MakeKrNameAsyncTask();
                    doMakeKrNamesAsyncTask.execute();
                }
            }
        });

        //controlKrProUI(pro_option_apply);
        resultCntsTV = (TextView) findViewById(R.id.possiblecntsTV);
        //DO!!!
        MakeKrNameAsyncTask doMakeKrNamesAsyncTask = new MakeKrNameAsyncTask();
        doMakeKrNamesAsyncTask.execute();


        resultNames_autoCompleteTVAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);

        resultNames_autoCompleteTVAdapter.setNotifyOnChange(true);
        resultNames_autoCompleteTV = (AutoCompleteTextView) findViewById(R.id.possibleNamesACTV);
        resultNames_autoCompleteTV.setAdapter(resultNames_autoCompleteTVAdapter);
        resultNames_autoCompleteTV.addTextChangedListener(textChecker2);
        resultNames_autoCompleteTV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(name_len)});
        resultNames_autoCompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((saju_year == -1) || (saju_month == -1) || (saju_day == -1)) {
                    Toast.makeText(MainActivity.this, "생년월일을 입력하세요.",Toast.LENGTH_LONG).show();
                } else if ((saju_hour == -1) || (saju_minute == -1)) {
                    Toast.makeText(MainActivity.this, "탄생시각을 입력하세요.",Toast.LENGTH_LONG).show();
                } else if (saju_location == getString(R.string.BirthLocation)) {
                    Toast.makeText(MainActivity.this, "탄생지역을 입력하세요.",Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra(EXTRA_KEY_KR_NAME, resultNames_autoCompleteTVAdapter.getItem(position));
                    intent.putExtra(EXTRA_KEY_CH_FAMILY_NAME, ownName.get(POS_FAMILY_NAME).getChLetter());
                    intent.putExtra(EXTRA_KEY_BIRTH_YEAR, ""+saju_year);
                    intent.putExtra(EXTRA_KEY_BIRTH_MONTH, ""+saju_month);
                    intent.putExtra(EXTRA_KEY_BIRTH_DAY, ""+saju_day);
                    intent.putExtra(EXTRA_KEY_BIRTH_HOUR, ""+saju_hour);
                    intent.putExtra(EXTRA_KEY_BIRTH_MINUTE, ""+saju_minute);
                    intent.putExtra(EXTRA_KEY_BIRTH_LOCATION, saju_location);

                    startActivity(intent);
                }
            }
        });
    }

    private int consonant_to_fiveele(String consonant) {
        if (FIVE_ELE_TREE_KR_ARRAYLIST.contains(consonant)) {
            return FIVE_ELE_TREE;
        } else if (FIVE_ELE_FIRE_KR_ARRAYLIST.contains(consonant)) {
            return FIVE_ELE_FIRE;
        } else if (FIVE_ELE_SOIL_KR_ARRAYLIST.contains(consonant)) {
            return FIVE_ELE_SOIL;
        } else if (FIVE_ELE_METAL_KR_ARRAYLIST.contains(consonant)) {
            return FIVE_ELE_METAL;
        } else if (FIVE_ELE_WATER_KR_ARRAYLIST.contains(consonant)) {
            return FIVE_ELE_WATER;
        } else {
            return -1;
        }
    }

    private int vowel_to_yy(String vowel) {
        if (PRO_YANG_KR_ARRAYLIST.contains(vowel)) {
            return 1;
        } else if (PRO_YIN_KR_ARRAYLIST.contains(vowel)){
            return 0;
        } else {
            return -1;
        }
    }

    private int setNameLen() {
        View radioButton = nametype_RadioGroup.findViewById(nametype_RadioGroup.getCheckedRadioButtonId());
        int idx = nametype_RadioGroup.indexOfChild(radioButton);
        switch (idx){
            case 0:
                name_len = 1;
                break;
            case 1:
                name_len = 2;
                break;
            default:
                name_len = 2;
                break;
        }
        return name_len;
    }

    private int evaluate_pro_five_ele_best_first_only(String name) {
        int cnt_best = 0;
        int cnt_good = 0;
        int cnt_bad = 0;

        ArrayList<String> consonantArrayList = new ArrayList<String>();
        ArrayList<Integer> consonantFiveEleArrayList = new ArrayList<Integer>();

        for (int i = 0; i < name.length(); i++) {
            int[] consonantIdxArray = kr_letter_split(name.charAt(i));
            //int last_consonantIdx = getLastElementCode(name.charAt(i));

            consonantFiveEleArrayList.add(consonant_to_fiveele(String.valueOf(firstSounds[consonantIdxArray[0]])));

        }

        for (int i = 0; i < consonantFiveEleArrayList.size() - 1; i++) {
            int cur_ele = consonantFiveEleArrayList.get(i);
            int next_ele = consonantFiveEleArrayList.get(i+1);
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

        if (cnt_bad == 0)
            return PRO_FIVE_ELE_BEST;

        return PRO_FIVE_ELE_BAD;
    }

    private int evaluate_pro_yy(String name) {
        ArrayList<Integer> vowelArrayList = new ArrayList<Integer>();

        for (int i = 0; i < name.length(); i++) {
            int[] consonantIdxArray = kr_letter_split(name.charAt(i));
            //int last_consonantIdx = getLastElementCode(name.charAt(i));

            vowelArrayList.add(vowel_to_yy(String.valueOf(middleSounds[consonantIdxArray[1]])));
        }
        if(vowelArrayList.size() == 3) {
            if (vowelArrayList.get(0) != vowelArrayList.get(2)) {
                return PRO_YY_ELE_BEST;
            } else if ((vowelArrayList.get(0) != vowelArrayList.get(1))) {
                return PRO_YY_ELE_GOOD;
            } else {
                return PRO_YY_ELE_BAD;
            }
        } else if (vowelArrayList.size() == 2) {
            if ((vowelArrayList.get(0) != vowelArrayList.get(1))) {
                return PRO_YY_ELE_BEST;
            } else {
                return PRO_YY_ELE_BAD;
            }
        }

        return -1;
    }

    private int evaluate_pro_five_ele_best_firstlast_only(String name) {
        int cnt_best = 0;
        int cnt_good = 0;
        int cnt_bad = 0;

        ArrayList<String> consonantArrayList = new ArrayList<String>();
        ArrayList<Integer> consonantFiveEleArrayList = new ArrayList<Integer>();

        for (int i = 0; i < name.length(); i++) {
            int[] consonantIdxArray = kr_letter_split(name.charAt(i));
            int last_consonantIdx = getLastElementCode(name.charAt(i));

            consonantFiveEleArrayList.add(consonant_to_fiveele(String.valueOf(firstSounds[consonantIdxArray[0]])));
            if ((last_consonantIdx != -1) && (last_consonantIdx != 0)) {
                consonantFiveEleArrayList.add(consonant_to_fiveele(String.valueOf(lastSounds[last_consonantIdx])));
            }
        }

        for (int i = 0; i < consonantFiveEleArrayList.size() - 1; i++) {
            int cur_ele = consonantFiveEleArrayList.get(i);
            int next_ele = consonantFiveEleArrayList.get(i+1);
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

        if (cnt_bad == 0)
            return PRO_FIVE_ELE_BEST;

        return PRO_FIVE_ELE_BAD;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
