package com.anmol.wedza.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anmol.wedza.R;

/**
 * Created by anmol on 12/29/2017.
 */

public class story extends Fragment {
    Button liked,wishes;
    RecyclerView listimg;
    TextView storycontent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.story,container,false);
        liked = (Button)vi.findViewById(R.id.likedit);
        wishes = (Button)vi.findViewById(R.id.wishes);
        storycontent = (TextView)vi.findViewById(R.id.storycontent);
        listimg = (RecyclerView)vi.findViewById(R.id.listimg);
        listimg.setHasFixedSize(true);
        listimg.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        return vi;
    }
}
