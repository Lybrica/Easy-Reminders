package lybrica.easyreminders.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import lybrica.easyreminders.R;

public class FragmentC extends Fragment implements View.OnClickListener,NumberPicker.OnValueChangeListener {

    TextView tv;

    public static FragmentC getInstance(int position) {
        FragmentC fragmentC = new FragmentC();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragmentC.setArguments(args);
        return fragmentC;
    }

    public void show()
    {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker npHour = (NumberPicker) d.findViewById(R.id.numberPickerHour);
        final NumberPicker npMinnute = (NumberPicker) d.findViewById(R.id.numberPickerMinute);
        npHour.setMaxValue(24);
        npHour.setMinValue(0);
        npHour.setWrapSelectorWheel(false);
        npHour.setOnValueChangedListener(this);

        npMinnute.setMaxValue(60);
        npMinnute.setMinValue(1);
        npMinnute.setWrapSelectorWheel(false);
        npMinnute.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tv.setText(String.valueOf(npHour.getValue()+":"+String.valueOf(npMinnute.getValue())));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void onClick(View v) {
        show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_c, container, false);


        tv = (TextView) layout.findViewById(R.id.textView1);
        Button b = (Button) layout.findViewById(R.id.button11);
        b.setOnClickListener(this);
        return layout;

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}