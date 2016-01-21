package lybrica.easyreminders.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import lybrica.easyreminders.Activities.MainActivity;
import lybrica.easyreminders.DBAdapter;
import lybrica.easyreminders.R;

public class FragmentMore extends DialogFragment {

    String style = "";
    DBAdapter myDb;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myDb.close();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        myDb = new DBAdapter(getActivity());
        myDb.open();

        if (getArguments().getInt("style") == 1) {
            style = "Insistent notification";
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.custom_dialog_title, null);

        //Change the Dialog title according to KEY_COLOR from db
        LinearLayout layour = (LinearLayout)view.findViewById(R.id.titleBg);
        TextView titleText = (TextView)view.findViewById(R.id.itt_title);
        final String color = getArguments().getString("color");

        if (color.equals("0")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemNA));
            titleText.setTextColor(getResources().getColor(R.color.itemAltText));
        }
        if (color.equals("2130837580")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemRed));
        }
        if (color.equals("2130837578")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemPink));
        }
        if (color.equals("2130837585")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemYellow));
            titleText.setTextColor(getResources().getColor(R.color.itemAltText));
        }
        if (color.equals("2130837572")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemLime));
            titleText.setTextColor(getResources().getColor(R.color.itemAltText));
        }
        if (color.equals("2130837562")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemGreen));
        }
        if (color.equals("2130837560")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemBlue));
        }
        if (color.equals("2130837561")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemDark));
        }
        if (color.equals("2130837579")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemPurple));
        }
        if (color.equals("2130837559")) {
            layour.setBackgroundColor(getResources().getColor(R.color.itemBlack));
        }

        titleText.setText(getArguments().getString("title"));

         return new AlertDialog.Builder(getActivity())
                .setCustomTitle(view)
                .setMessage(getArguments().getString("content")+
                        "\n\n"+
                        getArguments().getString("time")+
                        "\n"+
                        style)
                .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("three", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putLong("id", getArguments().getLong("id"));
                        editor.putString("title", getArguments().getString("title"));
                        editor.putString("content", getArguments().getString("content"));
                        editor.putString("time", getArguments().getString("time"));
                        editor.putString("color", color);
                        editor.putInt("style", getArguments().getInt("style"));
                        editor.commit();

                        dialog.dismiss();
                        ((MainActivity) getActivity()).saveTab(0);
                        ((MainActivity)getActivity()).redraw();
                    }
                })
                .setPositiveButton("Delete",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).itemDone(getArguments().getLong("id"));

                    }
                })
                .create();
    }
}