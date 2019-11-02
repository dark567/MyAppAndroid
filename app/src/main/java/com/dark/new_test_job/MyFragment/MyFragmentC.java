package com.dark.new_test_job.MyFragment;

/**
 * Created by dark on 03.09.2016.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dark.new_test_job.R;

public class MyFragmentC extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_c, container, false);
        return myFragmentView;
    }

}