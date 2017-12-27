package com.anmol.wedza.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.anmol.wedza.R;

import java.util.List;

/**
 * Created by anmol on 12/27/2017.
 */

public class guestlist extends Fragment {
    ListView glv;
    Button everyone,tgr,tbr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.guestlist,container,false);
        glv = (ListView)vi.findViewById(R.id.guestlv);
        everyone = (Button)vi.findViewById(R.id.everyone);
        tgr = (Button)vi.findViewById(R.id.teamgr);
        tbr = (Button)vi.findViewById(R.id.teambr);
        everyone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return vi;
    }
}
