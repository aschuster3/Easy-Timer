package com.rfserverstudios.easytimer.fragments;

import com.rfserverstudios.easytimer.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment
{
    public HomeFragment() {}
    
    public static HomeFragment newInstance() {
        HomeFragment frag = new HomeFragment();
        
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_selector,
                container, false);
        
        return rootView;
    }

    
}
