package com.anmol.wedza.Fragments;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.anmol.wedza.R;

/**
 * Created by anmol on 12/25/2017.
 */

public class home extends Fragment implements AbsListView.OnScrollListener{
    ListView lv;

    ImageView coverpic;
    int lastTopValue = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home,container,false);
        lv = (ListView)view.findViewById(R.id.newsfeed);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup)layoutInflater.inflate(R.layout.listheader,lv,false);
        lv.addHeaderView(header,null,false);
        coverpic = (ImageView)header.findViewById(R.id.listHeaderImage);
        lv.setOnScrollListener(this);
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        Rect rect = new Rect();
        coverpic.getLocalVisibleRect(rect);
        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            coverpic.setY((float) (rect.top / 2.0));
        }
    }
}
