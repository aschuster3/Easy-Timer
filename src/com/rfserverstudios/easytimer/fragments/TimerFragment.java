package com.rfserverstudios.easytimer.fragments;

import com.rfserverstudios.easytimer.R;
import com.rfserverstudios.easytimer.views.RotatingTimerView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class TimerFragment extends Fragment implements OnTouchListener
{
    public static final String FRAG_TYPE = "type";
    
    private static final int OTHER = 0,
                             CLOCKWISE = 1,
                             COUNTER_CLOCKWISE = 2;
    private float prevX, prevY;
    private VelocityTracker velocityTracker = null;
    
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
        
        rootView.findViewById(R.id.arc_test).setOnTouchListener(this);
        
        return rootView;
    }

    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        boolean consumed = true;
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);
        int quadrant = detectQuadrant(v, event);
        
        //Log.i("TimerFragment", "Quadrant: " + quadrant);
        
        switch(action) 
        {
            case MotionEvent.ACTION_DOWN:
                prevX = event.getX();
                prevY = event.getY();
                
                if(velocityTracker == null)
                {
                    velocityTracker = VelocityTracker.obtain();
                }
                else
                {
                    velocityTracker.clear();
                }
                
                velocityTracker.addMovement(event);
                break;
        
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                
                velocityTracker.computeCurrentVelocity(1000);
                int motionPattern = detectCircularPattern(quadrant, event);
                float sweepAngle = (float) Math.log( 
                        (Math.abs(VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId)) + 
                         Math.abs(VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId)))/ 2);
                
                //Log.i("TimerFragment", "Sweep Angle: " + sweepAngle);
                
                if(motionPattern == CLOCKWISE)
                {
                    ((RotatingTimerView) v).decreaseSweepAngle(sweepAngle);
                }
                else if(motionPattern == COUNTER_CLOCKWISE)
                {
                    ((RotatingTimerView) v).increaseSweepAngle(sweepAngle);
                }

                
                prevX = event.getX();
                prevY = event.getY();
                break;
                
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                break;
            default:
                consumed = false;
                break;
                
        }
        
        return consumed;
    }
    
    private int detectQuadrant(View v, MotionEvent event)
    {
        int quadrant = -1;
        float centerX = (v.getWidth()) / 2;
        float centerY = (v.getHeight()) / 2;
        
        if(event.getX() > centerX && event.getY() > centerY)
        {
            quadrant = 4;
        }
        else if(event.getX() <= centerX && event.getY() > centerY)
        {
            quadrant = 3;
        }
        else if(event.getX() <= centerX && event.getY() <= centerY)
        {
            quadrant = 2;
        }
        else if(event.getX() > centerX && event.getY() <= centerY)
        {
            quadrant = 1;
        }
        return quadrant;
    }
    
    private int detectCircularPattern(int quadrant, MotionEvent event)
    {
        int clockwise = OTHER;
        
        switch(quadrant)
        {
            case 1:
                if(event.getX() > prevX && event.getY() > prevY)
                {
                    clockwise = CLOCKWISE;
                }
                else if(event.getX() < prevX && event.getY() < prevY)
                {
                    clockwise = COUNTER_CLOCKWISE;
                }
                break;
            case 2:
                if(event.getX() > prevX && event.getY() < prevY)
                {
                    clockwise = CLOCKWISE;
                }
                else if(event.getX() < prevX && event.getY() > prevY)
                {
                    clockwise = COUNTER_CLOCKWISE;
                }
                break;
            case 3:
                if(event.getX() < prevX && event.getY() < prevY)
                {
                    clockwise = CLOCKWISE;
                }
                else if(event.getX() > prevX && event.getY() > prevY)
                {
                    clockwise = COUNTER_CLOCKWISE;
                }
                break;
            case 4:
                if(event.getX() < prevX && event.getY() > prevY)
                {
                    clockwise = CLOCKWISE;
                }
                else if(event.getX() > prevX && event.getY() < prevY)
                {
                    clockwise = COUNTER_CLOCKWISE;
                }
                break;
            default:
                break;
        }
        return clockwise;
    }
}
