package com.rocky.utouu.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocky.utouu.MainActivity;
import com.rocky.utouu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftFragment extends Fragment {


    private View view;
    private MainActivity activity;

    public LeftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = ((MainActivity) context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_left, container, false);
        return view;
    }

}
