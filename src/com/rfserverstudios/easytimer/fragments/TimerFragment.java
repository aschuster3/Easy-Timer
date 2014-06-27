package com.rfserverstudios.easytimer.fragments;

import com.rfserverstudios.easytimer.R;
import com.rfserverstudios.easytimer.views.CountDownView;
import com.rfserverstudios.easytimer.views.CountDownView.OnCountDownListener;
import com.rfserverstudios.easytimer.views.RotatingTimerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class TimerFragment extends Fragment implements OnTouchListener, OnClickListener, OnCountDownListener
{
    public static final String FRAG_TYPE = "type";
    
    private static final int OTHER = 0,
                             CLOCKWISE = 1,
                             COUNTER_CLOCKWISE = 2;
    private float prevX, prevY;
    private VelocityTracker velocityTracker = null;
    private RotatingTimerView rotatingTimerView;
    private CountDownView countDownView;
    private boolean isStarted = false;
    
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
        
        rotatingTimerView = (RotatingTimerView) rootView.findViewById(R.id.arc_test);
        //rotatingTimerView.setOnTouchListener(this);
        
        countDownView = (CountDownView) rootView.findViewById(R.id.center_clock);
        countDownView.setOnCountDownListener(this);
        countDownView.setClickable(true);
        countDownView.setOnClickListener(this);
        
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
                
                if(insideCircle(event))
                {
                    velocityTracker.addMovement(event);
                    
                    velocityTracker.computeCurrentVelocity(1000);
                    int motionPattern = detectCircularPattern(quadrant, event);
                    
                    float theta = getTheta(event.getX(), event.getY());
                    
                    float Vx = (float) Math.abs(VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId) * Math.cos(theta)),
                          Vy = (float) Math.abs(VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId) * Math.sin(theta));
                    
                    float sweepAngle = (float) Math.log(Vx + Vy);
                    
                    if(sweepAngle == Float.NEGATIVE_INFINITY)
                    {
                        sweepAngle = 1;
                    }
                    
                    if(motionPattern == CLOCKWISE)
                    {
                        // ((RotatingTimerView) v).decreaseSweepAngle(sweepAngle);
                    }
                    else if(motionPattern == COUNTER_CLOCKWISE)
                    {
                        // ((RotatingTimerView) v).increaseSweepAngle(sweepAngle);
                    }
                    
                    // countDownView.setClock(((RotatingTimerView) v).getSweepAngle() * 1000 / 3);
                    
                    prevX = event.getX();
                    prevY = event.getY();
                }
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

    @Override
    public void onClick(View v)
    {
        if(((CountDownView) v).getTimeLeftMillis() > 0)
        {
            if(isStarted)
            {
                ((CountDownView) v).pause();
                rotatingTimerView.setEnabled(true);
            }
            else
            {
                ((CountDownView) v).start();
                rotatingTimerView.setEnabled(false);
            }
        
            isStarted = !isStarted;
        }
    }
    
    @Override
    public void onCountDownTick(CountDownView view)
    {
    }

    @Override
    public void onCountDownDone(CountDownView view)
    {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 300, 500, 300, 500, 300};
        v.vibrate(pattern, -1);
    }
    
    private float getTheta(float x, float y)
    {
        return (float) Math.abs(Math.atan((y - prevY) / (x - prevX)));
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

    private boolean insideCircle(MotionEvent event)
    {
        float xPosition = event.getX();
        float yPosition = event.getY();
        
        float circCenterX = rotatingTimerView.getDrawingScape().centerX();
        float circCenterY = rotatingTimerView.getDrawingScape().centerY();
        
        float dist = (float) Math.sqrt((Math.pow(xPosition - circCenterX, 2) + Math.pow(yPosition - circCenterY, 2)));
        
        //Log.i("Rotating", "Dist: " + dist + "\ncircleRadius: " + rotatingTimerView.getCircleRadius());
        
        return true;
    }
}
