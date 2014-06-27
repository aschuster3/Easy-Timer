package com.rfserverstudios.easytimer.fragments;

import com.rfserverstudios.easytimer.R;
import com.rfserverstudios.easytimer.views.CountDownView;
import com.rfserverstudios.easytimer.views.CountDownView.OnCountDownListener;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CountDownFragment extends Fragment implements OnClickListener, OnCountDownListener
{
    public static final String FRAG_TYPE = "type";
    private CountDownView countDown;
    private Button countDownToggle;
    
    private boolean timerRunning = false; 
    
    public CountDownFragment() {};
    
    public static CountDownFragment newInstance() {
        CountDownFragment frag =  new CountDownFragment();

        return frag;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_countdown,
                container, false);
        
        countDown = (CountDownView)rootView.findViewById(R.id.text_timer_count_down);
        countDown.setClock(1000 * 5);
        countDown.setOnCountDownListener(this);
        
        countDownToggle = (Button) rootView.findViewById(R.id.button_timer_count_down);
        countDownToggle.setOnClickListener(this);
        
        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        if(timerRunning)
        {
            countDown.pause();
            ((Button) v).setText(R.string.toggle_timer_start);
        }
        else
        {
            countDown.start();
            ((Button) v).setText(R.string.toggle_timer_stop);
        }
        
        timerRunning = !timerRunning;
    }
    
    @Override
    public void onCountDownTick(CountDownView view)
    {
        //do nada
    }

    @Override
    public void onCountDownDone(CountDownView view)
    {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 300, 500, 300, 500, 300};
        v.vibrate(pattern, -1);
        
        countDownToggle.setEnabled(false);
        countDownToggle.setText(R.string.toggle_timer_start);
    }
}
