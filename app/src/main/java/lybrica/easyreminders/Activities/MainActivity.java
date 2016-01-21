package lybrica.easyreminders.Activities;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.telly.mrvector.MrVector;

import lybrica.easyreminders.DBAdapter;
import lybrica.easyreminders.Fragments.FragmentA;
import lybrica.easyreminders.Fragments.FragmentB;
import lybrica.easyreminders.Fragments.FragmentC;
import lybrica.easyreminders.Fragments.FragmentMore;
import lybrica.easyreminders.R;
import lybrica.easyreminders.Services.AlarmService;
import lybrica.easyreminders.Services.NotifyService;
import lybrica.easyreminders.Views.SlidingTabLayout;

public class MainActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    boolean styleBoot;

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_using_tab_library);

        int fix = 1;
        myDb = new DBAdapter(this);
        myDb.open();

        SharedPreferences sharedPrefs = getSharedPreferences("two", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int tab = sharedPrefs.getInt("tab", 1);
        editor.clear();
        editor.commit();

        setupToolbar();
        setupTabs();

        mPager.setCurrentItem(tab);

        if (getIntent().getIntExtra("more",0)==1) {
            if (fix == 1) {

                Cursor cursor = myDb.getRow(getIntent().getLongExtra("idLong", 101));
                if (cursor.moveToFirst()) {
                    long idDB = cursor.getLong(DBAdapter.COL_ROWID);
                    String title = cursor.getString(DBAdapter.COL_TITLE);
                    String name = cursor.getString(DBAdapter.COL_NAME);
                    String studentNum = cursor.getString(DBAdapter.COL_STUDENTNUM);
                    String favColour = String.valueOf(cursor.getString(DBAdapter.COL_FAVCOLOUR));
                    int style = cursor.getInt(DBAdapter.COL_STYLE);

                    showMore(idDB, title, name, studentNum, favColour, style);
                }
                cursor.close();
            }
        }

    }

    public void saveTab(int pos) {

        SharedPreferences sharedPrefs = getSharedPreferences("two", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("tab", pos);
        editor.commit();
    }


    public void showMore(long idInDb, String title, String content, String time, String color, int style) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Create and show the dialog.
        FragmentMore newFragment = new FragmentMore ();
        Bundle args = new Bundle();
        args.putLong("id", idInDb);
        args.putString("title", title);
        args.putString("content", content);
        args.putString("time", time);
        args.putString("color", color);
        args.putInt("style", style);

        newFragment.setArguments(args);
        newFragment.show(ft, "dialog");
    }

    public void updateItemForId(long idInDB, String title, String content, String time, int color, boolean style) {
        Cursor cursor = myDb.getRow(idInDB);
        if (cursor.moveToFirst()) {

            myDb.updateRow(idInDB, title, content, time, color, style);
        }
        cursor.close();
        redraw();
    }


    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupTabs() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        //make sure all tabs take the full horizontal screen space and divide it equally amongst themselves
        mTabs.setDistributeEvenly(true);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //color of the tab indicator
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.white));
        mTabs.setViewPager(mPager);
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

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
            Intent intent = new Intent("android.intent.action.SETTINGS");
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_about) {
            Intent intent = new Intent("android.intent.action.ABOUT");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);




    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    public void cancelAlarm(long id){

        int safeId = safeLongToInt(id);

        Cursor cursor = myDb.getRow(id);
        if (cursor.moveToFirst()) {
            String title = cursor.getString(DBAdapter.COL_TITLE);
            String name = cursor.getString(DBAdapter.COL_NAME);
            int color = cursor.getInt(DBAdapter.COL_FAVCOLOUR);
            int style = cursor.getInt(DBAdapter.COL_STYLE);


            if (style == 1)
                styleBoot = true;
            if (style == 0)
                styleBoot = false;


            Intent intent = new Intent(this, NotifyService.class);
            intent.putExtra("id", safeId);
            intent.putExtra("title", title);
            intent.putExtra("text", name);
            intent.putExtra("color", color);
            intent.putExtra("style", styleBoot);
            PendingIntent sender = PendingIntent.getBroadcast(this, safeId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            am.cancel(sender);

            removeExact(id);
        }
        cursor.close();

    }

    public void removeExact(long id) {

        SharedPreferences sharedPrefs = this.getSharedPreferences("times", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(String.valueOf(id));
        editor.commit();
    }

    public void itemDone(final long id) {

        //use the MrVector library to inflate vector drawable inside tab
        Drawable drawable = MrVector.inflate(getResources(), R.drawable.vector_warning);
        //set the size of drawable to 36 pixels
        drawable.setBounds(0, 0, 36, 36);


        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this reminder?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cancelAlarm(id);
                        removeExact(id);
                        myDb.deleteRow(id);
                        redraw();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(drawable)
                .show();
    }


    public void redraw() {
        Intent resultIntent = new Intent(this,MainActivity.class);
        resultIntent.putExtra("more", 0);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        overridePendingTransition(0, 0);
        startActivity(resultIntent);
    }

    public void OnSetAClick(View view) {

        getSupportActionBar().setTitle("title");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myDb.close();
    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] tabText = getResources().getStringArray(R.array.tabs);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);

        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment=null;

            if (position == 0)
                fragment = new FragmentA();
            if (position == 1)
                fragment = new FragmentB();
            return fragment;
        }




        @Override
        public CharSequence getPageTitle(int position) {

            SpannableStringBuilder sb = new SpannableStringBuilder(" ");

            if (position == 0) {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_action_add);
                drawable.setBounds(0, 0, 48, 48);
                ImageSpan imageSpan = new ImageSpan(drawable);
                //to make our tabs icon only, set the Text as blank string with white space
                sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (position == 1) {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_action_list_2);
                drawable.setBounds(0, 0, 48, 48);
                ImageSpan imageSpan = new ImageSpan(drawable);
                //to make our tabs icon only, set the Text as blank string with white space
                sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return sb;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
