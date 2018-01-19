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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anmol.wedza.Adapters.GuestAdapter;
import com.anmol.wedza.Model.Guest;
import com.anmol.wedza.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    RelativeLayout el,bl,gl;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.guestlist,container,false);
        getActivity().setTitle("Guests");
        glv = (ListView)vi.findViewById(R.id.guestlv);
        everyone = (Button)vi.findViewById(R.id.everyone);
        tgr = (Button)vi.findViewById(R.id.teamgr);
        tbr = (Button)vi.findViewById(R.id.teambr);
        el = (RelativeLayout)vi.findViewById(R.id.everyonel);
        bl = (RelativeLayout)vi.findViewById(R.id.tbrl);
        gl = (RelativeLayout)vi.findViewById(R.id.tgrl);
        guests = new ArrayList<>();
        everyone.setBackgroundResource(R.drawable.everyonered);
        tgr.setBackgroundResource(R.drawable.groomblue);
        tbr.setBackgroundResource(R.drawable.brideblue);
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final String weddingid = task.getResult().getString("currentwedding");
                everyguest(weddingid);
                el.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        everyguest(weddingid);
                        everyone.setBackgroundResource(R.drawable.everyonered);
                        tgr.setBackgroundResource(R.drawable.groomblue);
                        tbr.setBackgroundResource(R.drawable.brideblue);
                    }
                });
                gl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        teamgroom(weddingid);
                        everyone.setBackgroundResource(R.drawable.everyoneblue);
                        tgr.setBackgroundResource(R.drawable.groomr);
                        tbr.setBackgroundResource(R.drawable.brideblue);
                    }
                });
                bl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        teambride(weddingid);
                        everyone.setBackgroundResource(R.drawable.everyoneblue);
                        tgr.setBackgroundResource(R.drawable.groomblue);
                        tbr.setBackgroundResource(R.drawable.brider);
                    }
                });
            }
        });


        return vi;
    }
    private void teambride(String weddingid) {
        guests.clear();
        db.collection("weddings").document(weddingid).collection("users").whereEqualTo("team","bride")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                             for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                                 Guest guest = new Guest(doc.getString("username"),doc.getString("profilepicturepath")
                                         ,doc.getString("team"),doc.getString("relation"),doc.getBoolean("keypeople"),doc.getBoolean("admin"),doc.getId());
                                 guests.add(guest);
                             }
                        if(!guests.isEmpty()){
                            guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
                            glv.setAdapter(guestAdapter);
                        }

                    }
                });
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot doc:task.getResult()){
//                    Guest guest = new Guest(doc.getString("username"),doc.getString("profilepicturepath")
//                    ,doc.getString("team"),doc.getBoolean("keypeople"),doc.getBoolean("admin"));
//                    guests.add(guest);
//                }
//                if(!guests.isEmpty()){
//                    guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
//                    glv.setAdapter(guestAdapter);
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void teamgroom(String weddingid) {
        guests.clear();
        db.collection("weddings").document(weddingid).collection("users").whereEqualTo("team","groom")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                            Guest guest = new Guest(doc.getString("username"),doc.getString("profilepicturepath")
                                    ,doc.getString("team"),doc.getString("relation"),doc.getBoolean("keypeople"),doc.getBoolean("admin"),doc.getId());
                            guests.add(guest);
                        }
                        if(!guests.isEmpty()){
                            guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
                            glv.setAdapter(guestAdapter);
                        }

                    }
                });
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot doc:task.getResult()){
//                    Guest guest = new Guest(doc.getString("username"),doc.getString("profilepicturepath")
//                            ,doc.getString("team"),doc.getBoolean("keypeople"),doc.getBoolean("admin"));
//                    guests.add(guest);
//                }
//                if(!guests.isEmpty()){
//                    guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
//                    glv.setAdapter(guestAdapter);
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void everyguest(String weddingid) {
        guests.clear();
        db.collection("weddings").document(weddingid).collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                            Guest guest = new Guest(doc.getString("username"),doc.getString("profilepicturepath")
                                    ,doc.getString("team"),doc.getString("relation"),doc.getBoolean("keypeople"),doc.getBoolean("admin"),doc.getId());
                            guests.add(guest);
                        }
                        if(!guests.isEmpty()){
                            guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
                            glv.setAdapter(guestAdapter);
                        }

                    }
                });
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot doc:task.getResult()){
//                    Guest guest = new Guest(doc.getString("username"),doc.getString("profilepicturepath")
//                            ,doc.getString("team"),doc.getBoolean("keypeople"),doc.getBoolean("admin"));
//                    guests.add(guest);
//                }
//                if(!guests.isEmpty()){
//                    guestAdapter = new GuestAdapter(getActivity(),R.layout.guestlayout,guests);
//                    glv.setAdapter(guestAdapter);
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
