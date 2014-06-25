package com.rfserverstudios.easytimer.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RotatingTimerView extends View
{   
    private static final float STROKE_WIDTH = 40f; 
    private static final float WALL_PADDING = 100f;
    private static final float STARTING_ANGLE = 90f;
    private static final int[] COLOR_WHEEL = {Color.BLUE, Color.RED, Color.YELLOW};
    private RectF drawingScape;
    private Paint paint;
    private float sweepAngle = 0; 
    
    public RotatingTimerView(Context context)
    {
        this(context, null, 0);
    }

    public RotatingTimerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RotatingTimerView(Context context, AttributeSet attrs,
            int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        
        init();
    }
    
    private void init()
    {
        paint = new Paint();
        paint.setColor(COLOR_WHEEL[0]);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        
        drawingScape = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawingScape.set(0 + WALL_PADDING, 0 + WALL_PADDING, 
                this.getWidth() - WALL_PADDING, this.getHeight() - WALL_PADDING);
        
        if(sweepAngle > 360)
        {
            int ndx = (int) ((Math.round(sweepAngle) / 360) % 3);
            
            paint.setColor(COLOR_WHEEL[(ndx - 1) < 0 ? 2 : ndx - 1]);
            //canvas.drawArc(drawingScape, STARTING_ANGLE, 360, false, paint);
            
            canvas.drawCircle(drawingScape.centerX(), drawingScape.centerY(), this.getWidth() - drawingScape.centerX(),paint);
            
            paint.setColor(COLOR_WHEEL[ndx]);
        }
        else
        {
            paint.setColor(COLOR_WHEEL[0]);
        }
        
        float baseSweep = snap(sweepAngle * -1) % 360;
        
        canvas.drawArc(drawingScape, STARTING_ANGLE, baseSweep, false, paint);
    }
    
    private float snap(float x)
    {
        x = (float) Math.floor(x);
        x += x % 5;
        return x;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
    
    public void increaseSweepAngle(float amt)
    {
        sweepAngle += amt;
        invalidate();
    }
    
    public void decreaseSweepAngle(float amt)
    {
        sweepAngle -= amt;
        if(sweepAngle < 0)
        {
            sweepAngle = 0;
        }
        invalidate();
    }

}
