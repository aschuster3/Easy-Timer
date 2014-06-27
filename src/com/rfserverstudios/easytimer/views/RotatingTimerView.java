package com.rfserverstudios.easytimer.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RotatingTimerView extends View
{
    private static final float WALL_PADDING = 30f;
    private static final float BASE_ANGLE = 270f;
    private static final int CIRCLE_BACKGROUND_COLOR = 0x44000000;
    private static final float CIRCLE_BACKGROUND_WIDTH = 30f;
    private static final int PROGRESS_BAR_COLOR = 0xFFAA2211;
    private static final float PROGRESS_BAR_WIDTH = 15f;
    
    private RectF drawingScape;
    private Paint paint;
    private boolean fingerDown = false;
    
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
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        
        drawingScape = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawingScape.set(WALL_PADDING, WALL_PADDING, this.getWidth() - WALL_PADDING, 
                this.getHeight() - WALL_PADDING);
        
        float radius = drawingScape.centerX() - WALL_PADDING;
        
        paint.setColor(CIRCLE_BACKGROUND_COLOR);
        paint.setStrokeWidth(CIRCLE_BACKGROUND_WIDTH);
        canvas.drawCircle(drawingScape.centerX(), drawingScape.centerY(), radius, paint);
        
        paint.setColor(PROGRESS_BAR_COLOR);
        paint.setStrokeWidth(PROGRESS_BAR_WIDTH);
        canvas.drawArc(drawingScape, BASE_ANGLE, 90, false, paint);
        
        float x = (float) (drawingScape.centerX() + (radius * Math.cos(degreesToRadians(90 + BASE_ANGLE)))),
              y = (float) (drawingScape.centerY() + (radius * Math.sin(degreesToRadians(90 + BASE_ANGLE))));
        
        Log.i("RTV", "Center x: " + drawingScape.centerX() +
                "\nCenter y : " + drawingScape.centerY() +
                "\nx : " + x +
                "\ny : " + y);
        
        
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, PROGRESS_BAR_WIDTH, paint);
        
        
        paint.setStyle(Paint.Style.STROKE);
        if(fingerDown)
        {
            paint.setStrokeWidth(3f);
            canvas.drawCircle(x, y, PROGRESS_BAR_WIDTH + 5, paint);
        }
    }
    
    private float degreesToRadians(float num)
    {
        num /= 180;
        num *= Math.PI;
        return (float) num;
    }
    
    @Override
    public boolean performClick()
    {
        return super.performClick();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean consumed = true;
        boolean onCircumference = insideCircle(event);
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(onCircumference)
                    fingerDown = true;
                invalidate();
                break;
                
            case MotionEvent.ACTION_MOVE:
                if(onCircumference)
                    fingerDown = true;
                else
                    fingerDown = false;
                invalidate();
                break;
                
            case MotionEvent.ACTION_UP:
                fingerDown = false;
                invalidate();
                performClick();
                break;
                
            default:
                consumed = false;
                break;
                
        }
        return consumed;
    }
    
    private boolean insideCircle(MotionEvent event)
    {
        float xPosition = event.getX();
        float yPosition = event.getY();
        
        float circCenterX = drawingScape.centerX();
        float circCenterY = drawingScape.centerY();
        
        float dist = (float) Math.sqrt((Math.pow(xPosition - circCenterX, 2) + Math.pow(yPosition - circCenterY, 2)));
        
        return Math.abs(dist - (drawingScape.centerX() - WALL_PADDING))  <= CIRCLE_BACKGROUND_WIDTH * 3  &&
                drawingScape.contains(xPosition, yPosition);
    }
    
    public RectF getDrawingScape()
    {
        return drawingScape;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
