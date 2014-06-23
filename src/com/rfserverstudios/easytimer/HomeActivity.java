package com.rfserverstudios.easytimer;

import com.rfserverstudios.easytimer.fragments.CountDownFragment;
import com.rfserverstudios.easytimer.fragments.HomeFragment;
import com.rfserverstudios.easytimer.fragments.TimerFragment;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, HomeFragment.newInstance()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void buttonPress(View v)
    {
        switch(v.getId()) 
        {
            case R.id.button_timer:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TimerFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                
                // Toast.makeText(this, "Timer pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_count_down:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, CountDownFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                
                // Toast.makeText(this, "Count Down pressed", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        
    }

}
