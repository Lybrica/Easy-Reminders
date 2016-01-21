package lybrica.easyreminders.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import lybrica.easyreminders.R;

public class MoreDialog extends Dialog{

    String yolo = "jt";

    public MoreDialog(Context context, int theme) {

        super(context, theme);


        LayoutInflater inflater = getLayoutInflater();

        LinearLayout titleBg = (LinearLayout)findViewById(R.id.titleBg);
        titleBg.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));

        View view=inflater.inflate(R.layout.custom_dialog_title, null);

         AlertDialog.Builder builder = new AlertDialog.Builder(context);

                //.setTitle(getArguments().getString("title"))
                builder.setCustomTitle(view)
                .setMessage("aasddq"+ yolo)
                .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setPositiveButton("Edit",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do something
                    }
                })
                .create();

    }
}
