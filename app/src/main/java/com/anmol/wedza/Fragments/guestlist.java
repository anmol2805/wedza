package com.anmol.wedza.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.anmol.wedza.Adapters.GuestAdapter;
import com.anmol.wedza.Model.Guest;
import com.anmol.wedza.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 12/27/2017.
 */

public class guestlist extends Fragment {
    ListView glv;
    Button everyone,tgr,tbr;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Guest> guests;
    GuestAdapter guestAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.guestlist,container,false);
        glv = (ListView)vi.findViewById(R.id.guestlv);
        everyone = (Button)vi.findViewById(R.id.everyone);
        tgr = (Button)vi.findViewById(R.id.teamgr);
        tbr = (Button)vi.findViewById(R.id.teambr);
        guests = new ArrayList<>();
        everyguest();
        everyone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                everyguest();
            }
        });
        tgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamgroom();
            }
        });
        tbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teambride();
            }
        });
        return vi;
    }
    private void teambride() {
        guests.clear();
        db.collection("weddings/wedding1/users").whereEqualTo("side","bride").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult()){
                    Guest guest = new Guest(doc.getString("name"));
                    guests.add(guest);
                }
                guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
                glv.setAdapter(guestAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void teamgroom() {
        guests.clear();
        db.collection("weddings/wedding1/users").whereEqualTo("side","groom").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult()){
                    Guest guest = new Guest(doc.getString("name"));
                    guests.add(guest);
                }
                guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
                glv.setAdapter(guestAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void everyguest() {
        guests.clear();
        db.collection("weddings/wedding1/users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult()){
                    Guest guest = new Guest(doc.getString("name"));
                    guests.add(guest);
                }
                guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
                glv.setAdapter(guestAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
