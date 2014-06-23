package com.rfserverstudios.easytimer.fragments;

import com.rfserverstudios.easytimer.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

public class TimerFragment extends Fragment implements OnClickListener
{
    public static final String FRAG_TYPE = "type";
    public Chronometer timer;
    
    private boolean timerRunning = false; 
    
    public TimerFragment() {};
    
    public static TimerFragment newInstance() {
        TimerFragment frag =  new TimerFragment();

        return frag;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_timer,
                container, false);
        
        timer = (Chronometer)rootView.findViewById(R.id.chronometer_timer);
        
        rootView.findViewById(R.id.button_toggle_timer).setOnClickListener(this);
        
        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        if(timerRunning)
        {
            timer.stop();
            ((TextView) v).setText(R.string.toggle_timer_start);
        }
        else
        {
            timer.start();
            ((TextView) v).setText(R.string.toggle_timer_stop);
        }
        
        timerRunning = !timerRunning;
    }
}
