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
import java.util.List;

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


    private String fullNameKr;
    private Letter famlilyNameLetter;
    private int fullNameLen;
    private DBManager dbManager;
    private ArrayList<ArrayList<Letter>> nameCandidatesLetters;


    private TextView TV_TitleKrName;
    private TextView TV_FiveEleTree;
    private TextView TV_FiveEleFire;
    private TextView TV_FiveEleSoil;
    private TextView TV_FiveEleMetal;
    private TextView TV_FiveEleWater;

    private ListView LV_ResultChNames;

    private void initSajuFiveEleTVs() {
        TV_FiveEleTree = (TextView) findViewById(R.id.TV_FiveEleTree);
        TV_FiveEleFire = (TextView) findViewById(R.id.TV_FiveEleFire);
        TV_FiveEleSoil = (TextView) findViewById(R.id.TV_FiveEleSoil);
        TV_FiveEleMetal = (TextView) findViewById(R.id.TV_FiveEleMetal);
        TV_FiveEleWater = (TextView) findViewById(R.id.TV_FiveEleWater);

        TV_FiveEleTree.setText(FIVE_ELE_TREE_KR + ": 1");
        TV_FiveEleFire.setText(FIVE_ELE_FIRE_KR + ": 2");
        TV_FiveEleSoil.setText(FIVE_ELE_SOIL_KR + ": 3");
        TV_FiveEleMetal.setText(FIVE_ELE_METAL_KR + ": 4");
        TV_FiveEleWater.setText(FIVE_ELE_WATER_KR + ": 5");
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
        String[] LIST_MENU = {"LIST1", "LIST2", "LIST3", "LIST4", "LIST5", "LIST6", "LIST7", "LIST8", "LIST9",
                                "LIST1", "LIST2", "LIST3", "LIST4", "LIST5", "LIST6", "LIST7", "LIST8", "LIST9"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simplerow, LIST_MENU);

        /*
        ArrayList<String> a = new ArrayList<String>(Arrays.asList(LIST_MENU));
        CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_list_item_1, a);
*/

        LV_ResultChNames = (ListView) findViewById(R.id.LV_ResultChNames);
        LV_ResultChNames.setAdapter(adapter);
        LV_ResultChNames.setMinimumHeight(180);

        Log.d(TAG, " each height: " + getItemHeightofListView(LV_ResultChNames, LV_ResultChNames.getCount()));

    }

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

        TV_TitleKrName = (TextView) findViewById(R.id.TitleKoreanNameBig);
        TV_TitleKrName.setText(this.fullNameKr);
        TV_TitleKrName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);

        initSajuFiveEleTVs();
        initResultLV();



        Log.d(TAG, this.fullNameLen + ":" + fullNameKr + ":" + famlilyNameLetter.getChLetter() + ":" + this.nameCandidatesLetters);
    }
}
