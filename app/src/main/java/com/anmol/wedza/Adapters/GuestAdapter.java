package com.anmol.wedza.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.anmol.wedza.Model.Guest;
import com.anmol.wedza.Model.Keypeople;
import com.anmol.wedza.Model.Timeline;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anmol on 12/27/2017.
 */

public class GuestAdapter extends ArrayAdapter<Guest> {
    private Activity context;
    private int resource;
    private List<Guest> guests;

    public GuestAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Guest> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        guests = objects;
    }
    public int getViewTypeCount() {
        return getCount();
    }

    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            return convertView;
        }
        else{
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource,null);
            TextView name = (TextView)v.findViewById(R.id.name);
            CircleImageView profpic = (CircleImageView) v.findViewById(R.id.profilepic);
            CircleImageView teamstatus = (CircleImageView)v.findViewById(R.id.teamicon);
            final LinearLayout adminlayout = (LinearLayout)v.findViewById(R.id.adminlayout);
            final Button btnmk = (Button)v.findViewById(R.id.btnmk);
            final Button btnmad = (Button)v.findViewById(R.id.btnmad);
            TextView txtmk = (TextView)v.findViewById(R.id.txtmk);
            TextView txtmad = (TextView)v.findViewById(R.id.txtmad);
            String team = guests.get(position).getTeam();
            if(team.contains("groom")){
                teamstatus.setBackgroundResource(R.drawable.groomblue);
            }
            else if(team.contains("bride")){
                teamstatus.setBackgroundResource(R.drawable.brider);
            }
            final Boolean keypeople = guests.get(position).getKeypeople();
            final Boolean admin = guests.get(position).getAdmin();
            if(keypeople){
                btnmk.setBackgroundResource(R.drawable.keypeopleblue);
                txtmk.setText("Already a Keyperson");
            }
            else {
                btnmk.setBackgroundResource(R.drawable.keypin);
                txtmk.setText("Make a Keyperson");
            }
            if(admin){
                btnmad.setBackgroundResource(R.drawable.admin);
                txtmad.setText("Already an Admin");
            }
            else{
                btnmad.setBackgroundResource(R.drawable.adminin);
                txtmad.setText("Make an admin");
            }

            db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String weddingid = task.getResult().getString("currentwedding");
                    db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Boolean admin = task.getResult().getBoolean("admin");
                                    if(admin){
                                        adminlayout.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                    btnmk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!keypeople){
                                final Dialog dialog = new Dialog(context);
                                dialog.setTitle("Add Key Person Details");
                                dialog.setContentView(R.layout.editkey);
                                dialog.show();
                            }
                        }
                    });
                    btnmad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!admin){
                                final Dialog dialog = new Dialog(context);
                                dialog.setTitle("Confirm admin details");
                                dialog.setContentView(R.layout.editad);
                                dialog.show();
                            }
                        }
                    });
                }
            });
            Glide.with(context).load(guests.get(position).getProfilepicturepath()).into(profpic);
            name.setText(guests.get(position).getName());
            return v;
        }

    }
}
