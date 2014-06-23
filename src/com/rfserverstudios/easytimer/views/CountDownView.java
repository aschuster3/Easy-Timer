package com.rfserverstudios.easytimer.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

public class CountDownView extends TextView
{
    private static final long SECOND = 1000,
                              MINUTE = SECOND * 60,
                              HOUR = MINUTE * 60,
                              COUNTDOWN_INCREMENT = 5;
    
    /**
     * Callbacks for CountDownView
     */
    public interface OnCountDownListener 
    {
        void onCountDownDone(CountDownView view);
    }
    
    private OnCountDownListener onCountDownListener = null;
    private CountDownTimer countDownTimer;
    private long timeLeftMillis;
    
    public CountDownView(Context context)
    {
        this(context, null, 0);
    }
    
    public CountDownView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init()
    {
        Typeface type = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
        setTypeface(type);
        setClock(0);
    }
    
    public void setClock(long milliseconds)
    {
        timeLeftMillis = milliseconds;
        setText(millisToTimeFormat(milliseconds));
    }
    
    @SuppressLint("DefaultLocale")
    private String millisToTimeFormat(long millis)
    {
        String conversion = null;
        
        long hours = millis / HOUR;
        long minutes = (millis % HOUR) / MINUTE;
        long seconds = ((millis % HOUR) % MINUTE) / SECOND;
        long milliseconds = (((millis % HOUR) % MINUTE) % SECOND);
        
        if(hours > 0)
        {
            conversion = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
        }
        else
        {
            conversion = String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
        }
        
        return conversion;
    }
    
    public final void start()
    {
        countDownTimer = new CountDownTimer(timeLeftMillis, COUNTDOWN_INCREMENT) 
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                setClock(millisUntilFinished);
            }

            @Override
            public void onFinish()
            {
                stop();
            }
        };
        countDownTimer.start();
    }
    
    public final void pause()
    {
        countDownTimer.cancel();
    }
    
    public final void stop()
    {
        setClock(0);
        if(onCountDownListener != null)
        {
            onCountDownListener.onCountDownDone(this);
        }
    }
    

    
    public void setOnCountDownListener(OnCountDownListener onCountDownListener)
    {
        this.onCountDownListener = onCountDownListener;
    }
    
    public OnCountDownListener getOnCountDownListener()
    {
        return this.onCountDownListener;
    }
    
    public long getTimeLeftMillis()
    {
        return this.timeLeftMillis;
    }
}
