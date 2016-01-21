package lybrica.easyreminders.Fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

import lybrica.easyreminders.Activities.MainActivity;
import lybrica.easyreminders.DBAdapter;
import lybrica.easyreminders.R;
import lybrica.easyreminders.Services.NotifyService;

public class FragmentA extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, NumberPicker.OnValueChangeListener {

    public static FragmentA getInstance(int position) {
        FragmentA fragmentA = new FragmentA();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragmentA.setArguments(args);
        return fragmentA;
    }

    int[] imageIDs = {
            R.drawable.red,
            R.drawable.pink,
            R.drawable.yellow,
            R.drawable.lime,
            R.drawable.green,
            R.drawable.blue,
            R.drawable.dark,
            R.drawable.purple,
            R.drawable.black
    };
    int imageId;

    DBAdapter myDb;
    FragmentB fragmentB;
    NotifyService notifyService = new NotifyService();
    private PendingIntent pendingIntent;
    int year_x,month_x,day_x,month__x;
    int hour_x,minute_x;
    String montH;
    String yrr = "";
    String moon;

    int REQ_CODE_PICK_SOUNDFILE = 2;
    String uriStr = "def";

    String lSupp;
    int three = 0;
    int rStyle = 1;
    int usingDatePicker = 0;
    int usingTimePicker = 0;
    int unique_int = 0;

    long upId;
    String upTitle;
    String upContent;
    String upTime; 
    String upColor;
    int upStyle;

    private static RadioGroup radio_g;
    private static RadioButton radio_b;
    private static RadioButton radio_top, radio_bot;
    EditText contentEdit,titleEdit;
    CheckBox cb;

    Spinner spinnerRing;
    Spinner spinnerDate;
    Spinner spinnerTime;
    Spinner spinnerBot;
    Spinner spinnerWifi;
    ArrayList<String> ringNames=new ArrayList<String>();
    ArrayAdapter<String> ringAdapter;
    ArrayList<String> dateNames=new ArrayList<String>();
    ArrayAdapter<String> dateAdapter;
    ArrayList<String> timeNames=new ArrayList<String>();
    ArrayAdapter<String> timeAdapter;
    ArrayList<String> botNames=new ArrayList<String>();
    ArrayAdapter<String> botAdapter;
    ArrayList<String> wifiNames=new ArrayList<String>();
    ArrayAdapter<String> wifiAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_a, container, false);

        openDB();



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        //Buttons
        Button btnAdd = (Button)layout.findViewById(R.id.btnAdd);
        Button btnDelete = (Button)layout.findViewById(R.id.btnDel);
        //Button btnYest = (Button)layout.findViewById(R.id.btnYest);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        //btnYest.setOnClickListener(this);

        btnDelete.setVisibility(View.GONE);

        //Radios
        RadioButton radioTop = (RadioButton)layout.findViewById(R.id.radioTop);
        RadioButton radioBot = (RadioButton)layout.findViewById(R.id.radioBottom);
        RadioButton radioOff = (RadioButton)layout.findViewById(R.id.radioOff);
        //RadioButton radioWifi = (RadioButton)layout.findViewById(R.id.radioWifi);
        radioTop.setOnClickListener(this);
        radioBot.setOnClickListener(this);
        radioOff.setOnClickListener(this);
        //radioWifi.setOnClickListener(this);

        RadioButton raDef = (RadioButton)layout.findViewById(R.id.r1);
        RadioButton raRed = (RadioButton)layout.findViewById(R.id.r2);
        RadioButton raPink = (RadioButton)layout.findViewById(R.id.r3);
        RadioButton raYellow = (RadioButton)layout.findViewById(R.id.r4);
        RadioButton raLime = (RadioButton)layout.findViewById(R.id.r5);
        RadioButton raGreen = (RadioButton)layout.findViewById(R.id.r6);
        RadioButton raBlue = (RadioButton)layout.findViewById(R.id.r7);
        RadioButton raDark = (RadioButton)layout.findViewById(R.id.r8);
        RadioButton raPurple = (RadioButton)layout.findViewById(R.id.r9);
        RadioButton raBlack = (RadioButton)layout.findViewById(R.id.r10);
        raDef.setOnClickListener(this);
        raRed.setOnClickListener(this);
        raPink.setOnClickListener(this);
        raYellow.setOnClickListener(this);
        raLime.setOnClickListener(this);
        raGreen.setOnClickListener(this);
        raBlue.setOnClickListener(this);
        raDark.setOnClickListener(this);
        raPurple.setOnClickListener(this);
        raBlack.setOnClickListener(this);

        //Calendar
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        moon = "";
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        //EditTexts
        contentEdit = (EditText)layout.findViewById(R.id.editContent);
        titleEdit = (EditText)layout.findViewById(R.id.editTitle);

        //Checkbox
        cb = (CheckBox)layout.findViewById(R.id.checkStyle);
//        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    //spinnerRing.setEnabled(true);
//                } else {
//                    //spinnerRing.setEnabled(false);
//                }
//            }
//        });

        //Spinners
//        //spinnerRing = (Spinner)layout.findViewById(R.id.//spinnerRing);
//        //ringAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, ringNames);
//        //spinnerRing.setAdapter(//ringAdapter);
//        //spinnerRing.setOnItemSelectedListener(this);

        spinnerDate = (Spinner)layout.findViewById(R.id.spinnerDate);
        dateAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, dateNames);
        spinnerDate.setAdapter(dateAdapter);
        spinnerDate.setOnItemSelectedListener(this);

        spinnerTime = (Spinner)layout.findViewById(R.id.spinnerTime);
        timeAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, timeNames);
        spinnerTime.setAdapter(timeAdapter);
        spinnerTime.setOnItemSelectedListener(this);

        spinnerBot = (Spinner)layout.findViewById(R.id.spinnerBottom);
        botAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, botNames);
        spinnerBot.setAdapter(botAdapter);
        spinnerBot.setOnItemSelectedListener(this);

//        //spinnerWifi = (Spinner)layout.findViewById(R.id.//spinnerWifi);
//        wifiAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, wifiNames);
//        //spinnerWifi.setAdapter(wifiAdapter);
//        //spinnerWifi.setOnItemSelectedListener(this);

        //spinnerRing.setEnabled(false);
        spinnerBot.setEnabled(false);
        spinnerDate.setEnabled(false);
        spinnerTime.setEnabled(false);
        ////spinnerWifi.setEnabled(false);
        cb.setEnabled(false);
        rStyle = 1;



//        if (prefs.getBoolean("using_custom_alarm", false)) {
//            uriStr = prefs.getString("alarm_uri","N/A");
//            String displayName = prefs.getString("alarm_name", "N/A");
//            //ringAdapter.add(displayName);
//        }
        //ringAdapter.add("Device default");
        //ringAdapter.add("Choose..");
        //ringAdapter.notifyDataSetChanged();

        dateAdapter.add("Today");
        dateAdapter.add("Tomorrow");
        dateAdapter.add("Pick...");
        dateAdapter.notifyDataSetChanged();

        timeAdapter.add("09:00");
        timeAdapter.add("12:00");
        timeAdapter.add("16:00");
        timeAdapter.add("18:00");
        timeAdapter.add("21:00");
        timeAdapter.add("Pick..");
        timeAdapter.notifyDataSetChanged();

        botAdapter.add("5 min");
        botAdapter.add("10 min");
        botAdapter.add("15 min");
        botAdapter.add("30 min");
        botAdapter.add("45 min");
        botAdapter.add("1 hours");
        botAdapter.add("2 hours");
        botAdapter.add("Custom..");
        botAdapter.notifyDataSetChanged();

//        wifiAdapter.add("Not connected");
//        wifiAdapter.add("");
//        wifiAdapter.add("");
//        wifiAdapter.notifyDataSetChanged();

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("three", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        upId = sharedPrefs.getLong("id", 0);
        upTitle = sharedPrefs.getString("title", "");
        upContent = sharedPrefs.getString("content", "");
        upTime = sharedPrefs.getString("time", "");
        upColor = sharedPrefs.getString("color", "");
        upStyle = sharedPrefs.getInt("style", 0);

        if (!upTitle.isEmpty()) {
            three = 1;

            btnAdd.setText("Update reminder");
            btnDelete.setVisibility(View.VISIBLE);
            radioBot.setEnabled(false);
            spinnerBot.setEnabled(false);

        }

        if (upTitle.isEmpty()) {
            radioOff.setChecked(true);
            spinnerBot.setEnabled(false);
            spinnerDate.setEnabled(false);
            spinnerTime.setEnabled(false);
            //spinnerWifi.setEnabled(false);
            cb.setEnabled(false);
            rStyle = 1;
        }
        else {

            String[] splited = upTime.split("\\s+");

            if (splited.length == 1) { //no reminder/just text
                radioBot.setChecked(false);
                radioTop.setChecked(false);
                spinnerBot.setEnabled(false);
                spinnerDate.setEnabled(false);
                spinnerTime.setEnabled(false);
                //spinnerWifi.setEnabled(false);
                cb.setEnabled(false);
                //spinnerRing.setEnabled(false);
                rStyle = 1;
            }
            if (splited.length == 3) {
                radioTop.setChecked(true);
                spinnerBot.setEnabled(false);
                spinnerDate.setEnabled(true);
                spinnerTime.setEnabled(true);
                //spinnerWifi.setEnabled(false);
                cb.setEnabled(true);
                //spinnerRing.setEnabled(true);
                rStyle = 2;

                day_x = Integer.parseInt(splited[1]);
                moon = splited[0];

                dateAdapter.add(splited[0] + " " + splited[1]);

                Collections.swap(dateNames, 3, 0);
                Collections.swap(dateNames, 3, 1);
                Collections.swap(dateNames, 3, 2);

                dateAdapter.notifyDataSetChanged(); // Refresh spinner
                spinnerDate.setSelection(0);


                if (splited[2].equals("09:00")) {
                    spinnerTime.setSelection(0);
                }
                if (splited[2].equals("12:00")) {
                    spinnerTime.setSelection(1);
                }
                if (splited[2].equals("16:00")) {
                    spinnerTime.setSelection(2);
                }
                if (splited[2].equals("18:00")) {
                    spinnerTime.setSelection(3);
                }
                if (splited[2].equals("21:00")) {
                    spinnerTime.setSelection(4);
                } else {
                    timeAdapter.add(splited[2]);

                    Collections.swap(timeNames, 6, 0);
                    Collections.swap(timeNames, 6, 1);
                    Collections.swap(timeNames, 6, 2);
                    Collections.swap(timeNames, 6, 3);
                    Collections.swap(timeNames, 6, 4);
                    Collections.swap(timeNames, 6, 5);

                    timeAdapter.notifyDataSetChanged(); // Refresh spinner
                    spinnerTime.setSelection(0);
                }

            }
            if (splited.length == 4) {
                radioTop.setChecked(true);
                spinnerBot.setEnabled(false);
                spinnerDate.setEnabled(true);
                spinnerTime.setEnabled(true);
                //spinnerWifi.setEnabled(false);
                cb.setEnabled(true);
                //spinnerRing.setEnabled(true);
                rStyle = 2;

                year_x = Integer.parseInt(splited[2]);
                day_x = Integer.parseInt(splited[1]);
                moon = splited[0];

                dateAdapter.add(splited[0] + " " + splited[1] + " " + splited[2]);

                Collections.swap(dateNames, 3, 0);
                Collections.swap(dateNames, 3, 1);
                Collections.swap(dateNames, 3, 2);

                dateAdapter.notifyDataSetChanged(); // Refresh spinner
                spinnerDate.setSelection(0);


                if (splited[3].equals("09:00")) {
                    spinnerTime.setSelection(0);
                }
                if (splited[3].equals("12:00")) {
                    spinnerTime.setSelection(1);
                }
                if (splited[3].equals("16:00")) {
                    spinnerTime.setSelection(2);
                }
                if (splited[3].equals("18:00")) {
                    spinnerTime.setSelection(3);
                }
                if (splited[3].equals("21:00")) {
                    spinnerTime.setSelection(4);
                } else {
                    timeAdapter.add(splited[3]);

                    Collections.swap(timeNames, 6, 0);
                    Collections.swap(timeNames, 6, 1);
                    Collections.swap(timeNames, 6, 2);
                    Collections.swap(timeNames, 6, 3);
                    Collections.swap(timeNames, 6, 4);
                    Collections.swap(timeNames, 6, 5);

                    timeAdapter.notifyDataSetChanged(); // Refresh spinner
                    spinnerTime.setSelection(0);
                }

            }
        }

        if (upColor.equals("0")) {
            raDef.setChecked(true);
            imageId = 0;
        }
        if (upColor.equals("2130837580")) {
            raRed.setChecked(true);
            imageId = imageIDs[0];
        }
        if (upColor.equals("2130837578")) {
            raPink.setChecked(true);
            imageId = imageIDs[1];
        }
        if (upColor.equals("2130837585")) {
            raYellow.setChecked(true);
            imageId = imageIDs[2];
        }
        if (upColor.equals("2130837572")) {
            raLime.setChecked(true);
            imageId = imageIDs[3];
        }
        if (upColor.equals("2130837562")) {
            raGreen.setChecked(true);
            imageId = imageIDs[4];
        }
        if (upColor.equals("2130837560")) {
            raBlue.setChecked(true);
            imageId = imageIDs[5];
        }
        if (upColor.equals("2130837561")) {
            raDark.setChecked(true);
            imageId = imageIDs[6];
        }
        if (upColor.equals("2130837579")) {
            raPurple.setChecked(true);
            imageId = imageIDs[7];
        }
        if (upColor.equals("2130837559")) {
            raBlack.setChecked(true);
            imageId = imageIDs[8];
        }


        if (upStyle == 1) {
            cb.setChecked(true);
        }

        titleEdit.setText(upTitle);
        contentEdit.setText(upContent);

        editor.clear().commit(); // Clear three.txt

        return layout;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnAdd:

                if (three == 1) { //Update reminder

                    if (rStyle == 1) {
                        ((MainActivity) getActivity()).updateItemForId(upId, titleEdit.getText().toString(), contentEdit.getText().toString(), "", imageId, cb.isChecked());
                        ((MainActivity) getActivity()).cancelAlarm(upId);
                        ((MainActivity) getActivity()).redraw();
                    }
                    if (rStyle == 2) {

                        Calendar cur_cal = new GregorianCalendar();
                        cur_cal.setTimeInMillis(System.currentTimeMillis());
                        int helper = 0;

                        if (usingDatePicker == 0 && usingTimePicker == 1) {
                            Calendar cal = Calendar.getInstance();

                            if (spinnerDate.getSelectedItem().toString().equals("Today")) {
                                day_x = cur_cal.get(Calendar.DAY_OF_MONTH);
                            }
                            if (spinnerDate.getSelectedItem().toString().equals("Tomorrow")) {
                                day_x = cur_cal.get(Calendar.DAY_OF_MONTH) + 1;
                            } else {
                                helper = 1;

                                if (moon.equals("Jan"))
                                    month_x = 1;
                                if (moon.equals("Feb"))
                                    month_x = 2;
                                if (moon.equals("Feb"))
                                    month_x = 3;
                                if (moon.equals("Apr"))
                                    month_x = 4;
                                if (moon.equals("May"))
                                    month_x = 5;
                                if (moon.equals("Jun"))
                                    month_x = 6;
                                if (moon.equals("Jul"))
                                    month_x = 7;
                                if (moon.equals("Aug"))
                                    month_x = 8;
                                if (moon.equals("Sep"))
                                    month_x = 9;
                                if (moon.equals("Oct"))
                                    month_x = 10;
                                if (moon.equals("Nov"))
                                    month_x = 11;
                                if (moon.equals("Dec"))
                                    month_x = 12;
                            }

                            month__x = cur_cal.get(Calendar.MONTH) + 1;

                            if (month__x == 1)
                                montH = "Jan";
                            if (month__x == 2)
                                montH = "Feb";
                            if (month__x == 3)
                                montH = "Mar";
                            if (month__x == 4)
                                montH = "Apr";
                            if (month__x == 5)
                                montH = "May";
                            if (month__x == 6)
                                montH = "Jun";
                            if (month__x == 7)
                                montH = "Jul";
                            if (month__x == 8)
                                montH = "Aug";
                            if (month__x == 9)
                                montH = "Sep";
                            if (month__x == 10)
                                montH = "Oct";
                            if (month__x == 11)
                                montH = "Nov";
                            if (month__x == 12)
                                montH = "Dec";


                            cal.set(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
                            cal.set(Calendar.HOUR_OF_DAY, hour_x);
                            cal.set(Calendar.MINUTE, minute_x);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            cal.set(Calendar.DATE, day_x);
                            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                            cal.set(Calendar.YEAR, year_x);

                            if (helper == 0) {

                                ((MainActivity) getActivity()).updateItemForId(upId, titleEdit.getText().toString(), contentEdit.getText().toString(), montH + " " + day_x + " " + spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                                ((MainActivity) getActivity()).cancelAlarm(upId); //Cancel existing alarm
                            } else {

                                ((MainActivity) getActivity()).updateItemForId(upId, titleEdit.getText().toString(), contentEdit.getText().toString(), spinnerDate.getSelectedItem().toString() + " " + spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                                ((MainActivity) getActivity()).cancelAlarm(upId); //Cancel existing alarm
                            }

                            alarmMethod(0, 0, 0, cal.getTimeInMillis(), upId, cb.isChecked());
                        } else if (usingDatePicker == 1 && usingTimePicker == 1) {
                            Log.w("fgfg", "11");
                            Calendar cal = Calendar.getInstance();

                            cal.set(Calendar.DAY_OF_YEAR, year_x);
                            cal.set(Calendar.HOUR_OF_DAY, hour_x);
                            cal.set(Calendar.MINUTE, minute_x);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            cal.set(Calendar.DATE, day_x);
                            cal.set(Calendar.MONTH, month_x);
                            cal.set(Calendar.YEAR, year_x);

                            ((MainActivity) getActivity()).updateItemForId(upId, titleEdit.getText().toString(), contentEdit.getText().toString(), spinnerDate.getSelectedItem().toString() + " " + spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                            ((MainActivity) getActivity()).cancelAlarm(upId); //Cancel existing alarm

                            alarmMethod(0, 0, 0, cal.getTimeInMillis(), upId, cb.isChecked());
                        } else if (usingDatePicker == 1 && usingTimePicker == 0) {
                            Log.w("fgfg","10");
                            Calendar cal = Calendar.getInstance();

                            if (spinnerTime.getSelectedItem().toString() == "09:00") {
                                hour_x = 9;
                                minute_x = 0;
                            } else if (spinnerTime.getSelectedItem().toString() == "12:00") {
                                hour_x = 12;
                                minute_x = 0;
                            } else if (spinnerTime.getSelectedItem().toString() == "16:00") {
                                hour_x = 16;
                                minute_x = 0;
                            } else if (spinnerTime.getSelectedItem().toString() == "18:00") {
                                hour_x = 18;
                                minute_x = 0;
                            } else if (spinnerTime.getSelectedItem().toString() == "21:00") {
                                hour_x = 21;
                                minute_x = 0;
                            }

                            cal.set(Calendar.DAY_OF_YEAR, year_x);
                            cal.set(Calendar.HOUR_OF_DAY, hour_x);
                            cal.set(Calendar.MINUTE, minute_x);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            cal.set(Calendar.DATE, day_x);
                            cal.set(Calendar.MONTH, month_x);
                            cal.set(Calendar.YEAR, year_x);

                            ((MainActivity) getActivity()).updateItemForId(upId, titleEdit.getText().toString(), contentEdit.getText().toString(), spinnerDate.getSelectedItem().toString() + " " + spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                            ((MainActivity) getActivity()).cancelAlarm(upId); //Cancel existing alarm

                            alarmMethod(0, 0, 0, cal.getTimeInMillis(), upId, cb.isChecked());
                        } else if (usingDatePicker == 0 && usingTimePicker == 0) {
                            Calendar cal = Calendar.getInstance();

                            if (spinnerDate.getSelectedItem().toString().equals("Today")) {
                                day_x = cur_cal.get(Calendar.DAY_OF_MONTH);
                            }
                            if (spinnerDate.getSelectedItem().toString().equals("Tomorrow")) {
                                day_x = cur_cal.get(Calendar.DAY_OF_MONTH) + 1;
                            } else {
                                helper = 1;

                                if (!moon.equals("")) {

                                    if (moon.equals("Jan"))
                                        month_x = 1;
                                    if (moon.equals("Feb"))
                                        month_x = 2;
                                    if (moon.equals("Feb"))
                                        month_x = 3;
                                    if (moon.equals("Apr"))
                                        month_x = 4;
                                    if (moon.equals("May"))
                                        month_x = 5;
                                    if (moon.equals("Jun"))
                                        month_x = 6;
                                    if (moon.equals("Jul"))
                                        month_x = 7;
                                    if (moon.equals("Aug"))
                                        month_x = 8;
                                    if (moon.equals("Sep"))
                                        month_x = 9;
                                    if (moon.equals("Oct"))
                                        month_x = 10;
                                    if (moon.equals("Nov"))
                                        month_x = 11;
                                    if (moon.equals("Dec"))
                                        month_x = 12;
                                }
                            }

                            month__x = cur_cal.get(Calendar.MONTH) + 1;

                            if (month__x == 1)
                                montH = "Jan";
                            if (month__x == 2)
                                montH = "Feb";
                            if (month__x == 3)
                                montH = "Mar";
                            if (month__x == 4)
                                montH = "Apr";
                            if (month__x == 5)
                                montH = "May";
                            if (month__x == 6)
                                montH = "Jun";
                            if (month__x == 7)
                                montH = "Jul";
                            if (month__x == 8)
                                montH = "Aug";
                            if (month__x == 9)
                                montH = "Sep";
                            if (month__x == 10)
                                montH = "Oct";
                            if (month__x == 11)
                                montH = "Nov";
                            if (month__x == 12)
                                montH = "Dec";

                            if (spinnerTime.getSelectedItem().toString() == "09:00") {
                                hour_x = 9;
                                minute_x = 0;
                            } else if (spinnerTime.getSelectedItem().toString() == "12:00") {
                                hour_x = 12;
                                minute_x = 0;
                            } else if (spinnerTime.getSelectedItem().toString() == "16:00") {
                                hour_x = 16;
                                minute_x = 0;
                            } else if (spinnerTime.getSelectedItem().toString() == "18:00") {
                                hour_x = 18;
                                minute_x = 0;
                            } else if (spinnerTime.getSelectedItem().toString() == "21:00") {
                                hour_x = 21;
                                minute_x = 0;
                            }

                            cal.set(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
                            cal.set(Calendar.HOUR_OF_DAY, hour_x);
                            cal.set(Calendar.MINUTE, minute_x);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            cal.set(Calendar.DATE, day_x);
                            cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
                            cal.set(Calendar.YEAR, year_x);

                            if (helper == 0) {

                                ((MainActivity) getActivity()).updateItemForId(upId, titleEdit.getText().toString(), contentEdit.getText().toString(), montH + " " + day_x + " " + spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                                ((MainActivity) getActivity()).cancelAlarm(upId); //Cancel existing alarm
                            } else {

                                ((MainActivity) getActivity()).updateItemForId(upId, titleEdit.getText().toString(), contentEdit.getText().toString(), spinnerDate.getSelectedItem().toString() + " " + spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                                ((MainActivity) getActivity()).cancelAlarm(upId); //Cancel existing alarm
                            }

                            alarmMethod(0, 0, 0, cal.getTimeInMillis(), upId, cb.isChecked());
                        }

                        ((MainActivity) getActivity()).redraw();
                    }

                }
                if (three == 0) {
                    
                    String temp = titleEdit.getText().toString();
                    if (temp.matches(""))
                        Toast.makeText(getActivity(), "Please enter some text", Toast.LENGTH_SHORT).show();
                    else {

                        if (rStyle == 1) {
                            long id = myDb.insertRow(titleEdit.getText().toString(), contentEdit.getText().toString(), "", imageId, cb.isChecked());
                            ((MainActivity) getActivity()).redraw();
                        }

                        if (rStyle == 2) {


                            if (usingDatePicker == 0 && usingTimePicker == 1) {
                                Calendar cal = Calendar.getInstance();
                                Calendar cur_cal = new GregorianCalendar();
                                cur_cal.setTimeInMillis(System.currentTimeMillis());

                                if (spinnerDate.getSelectedItem().toString().equals("Today")) {
                                    day_x = cur_cal.get(Calendar.DAY_OF_MONTH);
                                }
                                if (spinnerDate.getSelectedItem().toString().equals("Tomorrow")) {
                                    day_x = cur_cal.get(Calendar.DAY_OF_MONTH) + 1;
                                }

                                cal.set(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
                                cal.set(Calendar.HOUR_OF_DAY, hour_x);
                                cal.set(Calendar.MINUTE, minute_x);
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                                cal.set(Calendar.DATE, day_x);
                                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                                cal.set(Calendar.YEAR, year_x);

                                String moo = "0";
                                if (cal.get(Calendar.MONTH) == 0) {
                                    moo = "Jan";
                                }
                                if (cal.get(Calendar.MONTH) == 1) {
                                    moo = "Feb";
                                }
                                if (cal.get(Calendar.MONTH) == 2) {
                                    moo = "Mar";
                                }
                                if (cal.get(Calendar.MONTH) == 3) {
                                    moo = "Apr";
                                }
                                if (cal.get(Calendar.MONTH) == 4) {
                                    moo = "May";
                                }
                                if (cal.get(Calendar.MONTH) == 5) {
                                    moo = "Jun";
                                }
                                if (cal.get(Calendar.MONTH) == 6) {
                                    moo = "Jul";
                                }
                                if (cal.get(Calendar.MONTH) == 7) {
                                    moo = "Aug";
                                }
                                if (cal.get(Calendar.MONTH) == 8) {
                                    moo = "Sep";
                                }
                                if (cal.get(Calendar.MONTH) == 9) {
                                    moo = "Oct";
                                }
                                if (cal.get(Calendar.MONTH) == 10) {
                                    moo = "Nov";
                                }
                                if (cal.get(Calendar.MONTH) == 11) {
                                    moo = "Dec";
                                }

                                long id = myDb.insertRow(titleEdit.getText().toString(), contentEdit.getText().toString(), moo+" "+day_x+yrr+" "+spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                                alarmMethod(0, 0, 0, cal.getTimeInMillis(), id, cb.isChecked());
                            } else if (usingDatePicker == 1 && usingTimePicker == 1) {
                                Calendar cal = Calendar.getInstance();
                                Calendar cur_cal = new GregorianCalendar();
                                cur_cal.setTimeInMillis(System.currentTimeMillis());

                                cal.set(Calendar.DAY_OF_YEAR, year_x);
                                cal.set(Calendar.HOUR_OF_DAY, hour_x);
                                cal.set(Calendar.MINUTE, minute_x);
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                                cal.set(Calendar.DATE, day_x);
                                cal.set(Calendar.MONTH, month_x);
                                cal.set(Calendar.YEAR, year_x);

                                long id = myDb.insertRow(titleEdit.getText().toString(), contentEdit.getText().toString(), spinnerDate.getSelectedItem().toString()+" "+spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                                alarmMethod(0, 0, 0, cal.getTimeInMillis(), id, cb.isChecked());
                            } else if (usingDatePicker == 1 && usingTimePicker == 0) {
                                Calendar cal = Calendar.getInstance();
                                Calendar cur_cal = new GregorianCalendar();
                                cur_cal.setTimeInMillis(System.currentTimeMillis());

                                if (spinnerTime.getSelectedItem().toString() == "09:00") {
                                    hour_x = 9;
                                    minute_x = 0;
                                } else if (spinnerTime.getSelectedItem().toString() == "12:00") {
                                    hour_x = 12;
                                    minute_x = 0;
                                } else if (spinnerTime.getSelectedItem().toString() == "16:00") {
                                    hour_x = 16;
                                    minute_x = 0;
                                } else if (spinnerTime.getSelectedItem().toString() == "18:00") {
                                    hour_x = 18;
                                    minute_x = 0;
                                } else if (spinnerTime.getSelectedItem().toString() == "21:00") {
                                    hour_x = 21;
                                    minute_x = 0;
                                }

                                cal.set(Calendar.DAY_OF_YEAR, year_x);
                                cal.set(Calendar.HOUR_OF_DAY, hour_x);
                                cal.set(Calendar.MINUTE, minute_x);
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                                cal.set(Calendar.DATE, day_x);
                                cal.set(Calendar.MONTH, month_x);
                                cal.set(Calendar.YEAR, year_x);


                                long id = myDb.insertRow(titleEdit.getText().toString(), contentEdit.getText().toString(), spinnerDate.getSelectedItem().toString()+" "+spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                                alarmMethod(0, 0, 0, cal.getTimeInMillis(), id, cb.isChecked());
                            } else if (usingDatePicker == 0 && usingTimePicker == 0) {
                                Calendar cal = Calendar.getInstance();
                                Calendar cur_cal = new GregorianCalendar();
                                cur_cal.setTimeInMillis(System.currentTimeMillis());

                                if (spinnerDate.getSelectedItem().toString().equals("Today")) {
                                    day_x = cur_cal.get(Calendar.DAY_OF_MONTH);
                                }
                                if (spinnerDate.getSelectedItem().toString().equals("Tomorrow")) {
                                    day_x = cur_cal.get(Calendar.DAY_OF_MONTH) + 1;
                                }

                                if (spinnerTime.getSelectedItem().toString() == "09:00") {
                                    hour_x = 9;
                                    minute_x = 0;
                                } else if (spinnerTime.getSelectedItem().toString() == "12:00") {
                                    hour_x = 12;
                                    minute_x = 0;
                                } else if (spinnerTime.getSelectedItem().toString() == "16:00") {
                                    hour_x = 16;
                                    minute_x = 0;
                                } else if (spinnerTime.getSelectedItem().toString() == "18:00") {
                                    hour_x = 18;
                                    minute_x = 0;
                                } else if (spinnerTime.getSelectedItem().toString() == "21:00") {
                                    hour_x = 21;
                                    minute_x = 0;
                                }

                                cal.set(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
                                cal.set(Calendar.HOUR_OF_DAY, hour_x);
                                cal.set(Calendar.MINUTE, minute_x);
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                                cal.set(Calendar.DATE, day_x);
                                cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
                                cal.set(Calendar.YEAR, year_x);

                                if (year_x != cur_cal.get(Calendar.YEAR)) {
                                    yrr = " " + year_x;
                                }

                                String moo = "0";
                                if (cal.get(Calendar.MONTH) == 0) {
                                    moo = "Jan";
                                }
                                if (cal.get(Calendar.MONTH) == 1) {
                                    moo = "Feb";
                                }
                                if (cal.get(Calendar.MONTH) == 2) {
                                    moo = "Mar";
                                }
                                if (cal.get(Calendar.MONTH) == 3) {
                                    moo = "Apr";
                                }
                                if (cal.get(Calendar.MONTH) == 4) {
                                    moo = "May";
                                }
                                if (cal.get(Calendar.MONTH) == 5) {
                                    moo = "Jun";
                                }
                                if (cal.get(Calendar.MONTH) == 6) {
                                    moo = "Jul";
                                }
                                if (cal.get(Calendar.MONTH) == 7) {
                                    moo = "Aug";
                                }
                                if (cal.get(Calendar.MONTH) == 8) {
                                    moo = "Sep";
                                }
                                if (cal.get(Calendar.MONTH) == 9) {
                                    moo = "Oct";
                                }
                                if (cal.get(Calendar.MONTH) == 10) {
                                    moo = "Nov";
                                }
                                if (cal.get(Calendar.MONTH) == 11) {
                                    moo = "Dec";
                                }

                                long id = myDb.insertRow(titleEdit.getText().toString(), contentEdit.getText().toString(), moo+" "+day_x+yrr+" "+spinnerTime.getSelectedItem().toString(), imageId, cb.isChecked());

                                alarmMethod(0, 0, 0, cal.getTimeInMillis(), id, cb.isChecked());
                            }

                            ((MainActivity) getActivity()).redraw();

                        } else if (rStyle == 3) {

                            if (lSupp == "0") {
                                Toast.makeText(getActivity(), "Please enter a date", Toast.LENGTH_SHORT).show();
                            } else {

                                //long id = myDb.insertRow(titleEdit.getText().toString(), contentEdit.getText().toString(), spinnerBot.getSelectedItem().toString(), imageId, cb.isChecked());
                                String frmN = spinnerBot.getSelectedItem().toString();

                                int minToAdd = 0;
                                int hoursToAdd = 0;

                                int pre = 0;

                                if (frmN == "5 min") {
                                    pre = 1;
                                    minToAdd = 5;
                                }
                                else if (frmN == "10 min") {
                                    pre = 1;
                                    minToAdd = 10;
                                }
                                else if (frmN == "15 min") {
                                    pre = 1;
                                    minToAdd = 15;
                                }
                                else if (frmN == "30 min") {
                                    pre = 1;
                                    minToAdd = 30;
                                }
                                else if (frmN == "45 min") {
                                    pre = 1;
                                    minToAdd = 45;
                                }
                                else if (frmN == "1 hours") {
                                    pre = 1;
                                    hoursToAdd = 1;
                                }
                                else if (frmN == "2 hours") {
                                    pre = 1;
                                    hoursToAdd = 2;
                                } else {

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(System.currentTimeMillis());

                                    String toDate = "0";

                                    String[] botSplit = frmN.split("\\s+");
                                    int inInt1 = 0, inInt2 = 0, inInt3 = 0;


                                    if (botSplit.length == 1) {

                                        String inStrHelp = frmN.replaceAll("\\D+", ""); // Remove non-digits from string
                                        int inInt = Integer.parseInt(inStrHelp);

                                        if (lSupp == "d") { //days
                                            calendar.add(Calendar.DAY_OF_MONTH, inInt);
                                            toDate = getDate(calendar.getTimeInMillis(), "MMM dd hh:mm");
                                            alarmMethod(0, 0, inInt, 0, upId, cb.isChecked());
                                        }
                                        if (lSupp == "h") { //hours
                                            calendar.add(Calendar.HOUR_OF_DAY, inInt);
                                            calendar.add(Calendar.DAY_OF_MONTH, inInt1);
                                            toDate = getDate(calendar.getTimeInMillis(), "MMM dd hh:mm");
                                            alarmMethod(0, inInt, inInt1, 0, upId, cb.isChecked());
                                        }
                                        if (lSupp == "m") { //minutes
                                            calendar.add(Calendar.MINUTE, inInt);
                                            toDate = getDate(calendar.getTimeInMillis(), "MMM dd hh:mm");
                                            alarmMethod(inInt, 0, 0, 0, upId, cb.isChecked());
                                        }
                                    }
                                    if (botSplit.length == 2) {

                                        String inStr1 = botSplit[0].replaceAll("\\D+", ""); // Remove non-digits from string
                                        String inStr2 = botSplit[1].replaceAll("\\D+", ""); //
                                        inInt1 = Integer.parseInt(inStr1);
                                        inInt2 = Integer.parseInt(inStr2);

                                        if (lSupp == "dm") { //days and min
                                            calendar.add(Calendar.MINUTE, inInt2);
                                            calendar.add(Calendar.DAY_OF_MONTH, inInt1);
                                            toDate = getDate(calendar.getTimeInMillis(), "MMM dd hh:mm");
                                            alarmMethod(inInt2, 0, inInt1, 0, upId, cb.isChecked());
                                        }
                                        if (lSupp == "dh") { //days and hours
                                            calendar.add(Calendar.DAY_OF_MONTH, inInt1);
                                            calendar.add(Calendar.HOUR_OF_DAY, inInt2);
                                            toDate = getDate(calendar.getTimeInMillis(), "MMM dd hh:mm");
                                            alarmMethod(0, inInt2, inInt1, 0, upId, cb.isChecked());
                                        }
                                        if (lSupp == "hm") { //Hours and minutes
                                            calendar.add(Calendar.HOUR_OF_DAY, inInt1);
                                            calendar.add(Calendar.MINUTE, inInt2);
                                            toDate = getDate(calendar.getTimeInMillis(), "MMM dd hh:mm");
                                            alarmMethod(inInt2, inInt1, 0, 0, upId, cb.isChecked());
                                        }
                                    }
                                    if (botSplit.length == 3) {

                                        String inStr1 = botSplit[0].replaceAll("\\D+", ""); //
                                        String inStr2 = botSplit[1].replaceAll("\\D+", ""); // Remove non-digits from string
                                        String inStr3 = botSplit[2].replaceAll("\\D+", ""); //
                                        inInt1 = Integer.parseInt(inStr1);
                                        inInt2 = Integer.parseInt(inStr2);
                                        inInt3 = Integer.parseInt(inStr3);

                                        calendar.add(Calendar.MINUTE, inInt3);
                                        calendar.add(Calendar.HOUR_OF_DAY, inInt2);
                                        calendar.add(Calendar.DAY_OF_MONTH, inInt1);
                                        toDate = getDate(calendar.getTimeInMillis(), "MMM dd hh:mm");
                                        alarmMethod(inInt3, inInt2, inInt1, 0, upId, cb.isChecked());
                                    }

                                    long id = myDb.insertRow(titleEdit.getText().toString(), contentEdit.getText().toString(), toDate, imageId, cb.isChecked());

                                    ((MainActivity) getActivity()).redraw();

                                }

                                if (pre == 1) {

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                    calendar.add(Calendar.MINUTE, minToAdd);
                                    calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);

                                    String toDate = getDate(calendar.getTimeInMillis(), "MMM dd hh:mm");

                                    long id = myDb.insertRow(titleEdit.getText().toString(), contentEdit.getText().toString(), toDate, imageId, cb.isChecked());

                                    alarmMethod(minToAdd, hoursToAdd, 0, 0, id, cb.isChecked());

                                    ((MainActivity) getActivity()).redraw();
                                }
                            }

                        }
                    }
                }
                break;

            case R.id.btnDel:
                ((MainActivity)getActivity()).itemDone(upId);
                break;

//            case R.id.btnYest:
//                //alarmMethod(0,0,0,System.currentTimeMillis(),59,cb.isChecked());
//                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("times", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//
//                editor.clear();
//                editor.commit();
//                myDb.deleteAll();
//                break;

            case R.id.radioOff:
                spinnerBot.setEnabled(false);
                spinnerDate.setEnabled(false);
                spinnerTime.setEnabled(false);
                //spinnerWifi.setEnabled(false);
                //spinnerRing.setEnabled(false);
                cb.setEnabled(false);
                rStyle = 1;
                break;

            case R.id.radioTop:
                spinnerBot.setEnabled(false);
                spinnerDate.setEnabled(true);
                spinnerTime.setEnabled(true);
                //spinnerWifi.setEnabled(false);
                cb.setEnabled(true);
                rStyle = 2;
                break;

            case R.id.radioBottom:
                spinnerBot.setEnabled(true);
                spinnerDate.setEnabled(false);
                spinnerTime.setEnabled(false);
                //spinnerWifi.setEnabled(false);
                cb.setEnabled(true);
                rStyle = 3;
                break;

//            case R.id.radioWifi:
//                spinnerBot.setEnabled(false);
//                spinnerDate.setEnabled(false);
//                spinnerTime.setEnabled(false);
//                //spinnerWifi.setEnabled(true);
//                cb.setEnabled(true);
//                rStyle = 4;
//                break;
            case R.id.r1:
                imageId = 0;
                break;
            case R.id.r2:
                imageId = imageIDs[0];
                break;
            case R.id.r3:
                imageId = imageIDs[1];
                break;
            case R.id.r4:
                imageId = imageIDs[2];
                break;
            case R.id.r5:
                imageId = imageIDs[3];
                break;
            case R.id.r6:
                imageId = imageIDs[4];
                break;
            case R.id.r7:
                imageId = imageIDs[5];
                break;
            case R.id.r8:
                imageId = imageIDs[6];
                break;
            case R.id.r9:
                imageId = imageIDs[7];
                break;
            case R.id.r10:
                imageId = imageIDs[8];
                break;
        }


    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    private void alarmMethod(int minToAdd, int hoursToAdd, int daysToAdd, long exactTime, long id, boolean style){

        int safeId = safeLongToInt(id);
        String idStr = Integer.toString(safeId);

        Intent intent = new Intent(getActivity(), NotifyService.class);
        intent.putExtra("id", safeId);
        intent.putExtra("idLong", id);
        intent.putExtra("title", titleEdit.getText().toString());
        intent.putExtra("text", contentEdit.getText().toString());
        intent.putExtra("color", imageId);
        intent.putExtra("style", style);
        //intent.putExtra("uri", uri);
        PendingIntent sender = PendingIntent.getBroadcast(getActivity().getBaseContext(), safeId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (minToAdd == 0 && hoursToAdd == 0 && daysToAdd == 0) {

            AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, exactTime, sender);

            SharedPreferences sharedPrefs = getActivity().getSharedPreferences("times", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putLong(idStr, exactTime);
            editor.commit();

        }
        else {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MINUTE, minToAdd);
            calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

            AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    private void openDB() {
        myDb = new DBAdapter(getActivity());
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeDB();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_SOUNDFILE) {
            if (resultCode == Activity.RESULT_OK) {
                if ((data != null) && (data.getData() != null)) {
                    Uri audioFileUri = data.getData();
                    uriStr = audioFileUri.toString();
                    File myFile = new File(uriStr);
                    String displayName = null;

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putBoolean("using_custom_alarm", true);

                    if (uriStr.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getActivity().getContentResolver().query(audioFileUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriStr.startsWith("file://")) {
                        displayName = myFile.getName();
                    }

                    editor.putString("alarm_uri", uriStr); //save uri
                    editor.putString("alarm_name", displayName); //save namne
                    editor.apply();

                    if (ringNames.size() == 2) {
                        ringNames.add(0, displayName);
                        //ringAdapter.setNotifyOnChange(true);
                        //spinnerRing.setSelection(0);
                    } else {
                        ringNames.remove(0);
                        ringNames.add(0, displayName);
                        //ringAdapter.setNotifyOnChange(true);
                        //spinnerRing.setSelection(0);
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //spinnerRing.setSelection(0);
            }
        }
    }

    public void toast(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
        String Text = parent.getSelectedItem().toString();

        if (Text.equals("Device default")) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean("using_custom_alarm", false);
            editor.apply();
        }
        if (Text.equals("Choose..")) {

            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Audio"), REQ_CODE_PICK_SOUNDFILE);
        }
        if (Text.equals("Pick...")) {

            DatePickerDialog datePicker;
            datePicker = new DatePickerDialog(getActivity(),
                    R.style.DialogTheme, datePickerListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
            datePicker.setCancelable(false);
            datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    spinnerDate.setSelection(0);
                }
            });
            datePicker.show();
        }
        if (Text.equals("Pick..")) {

            TimePickerDialog timePicker;
            timePicker = new TimePickerDialog(getActivity(),
                    R.style.DialogTheme, timePickerListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true);
            timePicker.setCancelable(false);
            timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    spinnerTime.setSelection(0);
                }
            });
                    timePicker.show();
        }
        if (Text.equals("Custom..")) {

            final Dialog d = new Dialog(getActivity());
            d.setContentView(R.layout.dialog);
            Button b1 = (Button) d.findViewById(R.id.button1);
            Button b2 = (Button) d.findViewById(R.id.button2);
            final NumberPicker npDays = (NumberPicker) d.findViewById(R.id.numberPickerDays);
            final NumberPicker npHour = (NumberPicker) d.findViewById(R.id.numberPickerHour);
            final NumberPicker npMinnute = (NumberPicker) d.findViewById(R.id.numberPickerMinute);

            npDays.setMaxValue(365);
            npDays.setWrapSelectorWheel(false);
            npDays.setOnValueChangedListener(this);

            npHour.setMaxValue(24);
            npHour.setWrapSelectorWheel(false);
            npHour.setOnValueChangedListener(this);

            npMinnute.setMaxValue(60);
            npMinnute.setWrapSelectorWheel(false);
            npMinnute.setOnValueChangedListener(this);


            b1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    int nDay = npDays.getValue();
                    int nHour = npHour.getValue();
                    int nMinute = npMinnute.getValue();

                    String toAdd = "0d 0h 0m";

                    if (nDay != 0) {
                        toAdd = nDay+"d";
                        lSupp = "d";

                        if (nHour != 0) {
                            toAdd = nDay+"d "+nHour+"h";
                            lSupp = "dh";

                            if (nMinute != 0) {
                                toAdd = nDay+"d "+nHour+"h "+nMinute+"m";
                                lSupp = "dhm";
                            }
                        }
                        else if (nMinute != 0) {
                            toAdd = nDay+"d "+nMinute+"m";
                            lSupp = "dm";
                        }
                    }
                    else if (nHour != 0) {
                        toAdd = nHour+"h";
                        lSupp = "h";

                        if (nMinute != 0) {
                            toAdd = nHour+"h "+nMinute+"m";
                            lSupp = "hm";
                        }
                    }
                    else if (nMinute != 0) {
                        toAdd = nMinute+"m";
                        lSupp = "m";
                    }
                    else {
                        Toast.makeText(getActivity(),"No time was inserted",Toast.LENGTH_SHORT).show();
                    }

                    if (botNames.get(0) == "5 min")
                        botAdapter.add(toAdd);
                    else {
                        botAdapter.remove(botNames.get(0));
                        botAdapter.add(toAdd);
                    }

                    Collections.swap(botNames, 8, 0);
                    Collections.swap(botNames, 8, 1);
                    Collections.swap(botNames, 8, 2);
                    Collections.swap(botNames, 8, 3);
                    Collections.swap(botNames, 8, 4);
                    Collections.swap(botNames, 8, 5);
                    Collections.swap(botNames, 8, 6);
                    Collections.swap(botNames, 8, 7);

                    botAdapter.notifyDataSetChanged();
                    spinnerBot.setSelection(0);
                    d.dismiss();
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinnerBot.setSelection(0);
                    d.dismiss();
                }
            });
            d.show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            usingDatePicker = 1;

            Calendar cur_cal = new GregorianCalendar();
            cur_cal.setTimeInMillis(System.currentTimeMillis());

            month__x = monthOfYear + 1;

            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;

            if (month__x == 1)
                montH = "Jan";
            if (month__x == 2)
                montH = "Feb";
            if (month__x == 3)
                montH = "Mar";
            if (month__x == 4)
                montH = "Apr";
            if (month__x == 5)
                montH = "May";
            if (month__x == 6)
                montH = "Jun";
            if (month__x == 7)
                montH = "Jul";
            if (month__x == 8)
                montH = "Aug";
            if (month__x == 9)
                montH = "Sep";
            if (month__x == 10)
                montH = "Oct";
            if (month__x == 11)
                montH = "Nov";
            if (month__x == 12)
                montH = "Dec";

            if (year != cur_cal.get(Calendar.YEAR)) {
                yrr = " " + year;
            }

            String dateSet = montH+" "+day_x +yrr;

            if (dateNames.get(0) == "Today")
                dateAdapter.add(dateSet);
            else {
                dateAdapter.remove(dateNames.get(0));
                dateAdapter.add(dateSet);
            }

            Collections.swap(dateNames, 3, 0);
            Collections.swap(dateNames, 3, 1);
            Collections.swap(dateNames, 3, 2);


            dateAdapter.notifyDataSetChanged(); // Refresh spinner
            spinnerDate.setSelection(0);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

            usingTimePicker = 1;

            hour_x = hourOfDay;
            minute_x = minute;

            String zero = "0";
            if (minute < 10) {
                zero = zero+minute;
            } else {
                zero = String.valueOf(minute);
            }

            if (timeNames.get(0) == "09:00")
                timeAdapter.add(hourOfDay + ":" + zero);
            else {
                timeAdapter.remove(timeNames.get(0));
                timeAdapter.add(hourOfDay + ":" + zero);
            }

            Collections.swap(timeNames, 6, 0);
            Collections.swap(timeNames, 6, 1);
            Collections.swap(timeNames, 6, 2);
            Collections.swap(timeNames, 6, 3);
            Collections.swap(timeNames, 6, 4);
            Collections.swap(timeNames, 6, 5);

            timeAdapter.notifyDataSetChanged(); // Refresh spinner
            spinnerTime.setSelection(0);
        }
    };


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }


}