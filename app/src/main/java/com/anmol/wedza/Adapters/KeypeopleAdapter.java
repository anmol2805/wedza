package com.anmol.wedza.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.wedza.Model.Event;
import com.anmol.wedza.Model.Keypeople;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anmol on 1/8/2018.
 */

public class KeypeopleAdapter extends ArrayAdapter<Keypeople> {
    private Activity context;
    private int resource;
    private List<Keypeople> keypeoples;

    public KeypeopleAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Keypeople> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        keypeoples = objects;
    }

    public int getViewTypeCount() {
        return getCount();
    }

    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView != null) {
            return convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource, null);
            CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.keyimg);
            TextView name = (TextView) v.findViewById(R.id.keyname);
            final TextView contact = (TextView) v.findViewById(R.id.keycontact);
            TextView work = (TextView) v.findViewById(R.id.keywork);
            Button call = (Button) v.findViewById(R.id.keycall);
            Glide.with(context).load(keypeoples.get(position).getImg()).into(circleImageView);
            name.setText(keypeoples.get(position).getName());
            contact.setText("+91" + keypeoples.get(position).getContact());
            work.setText("(" + keypeoples.get(position).getWork() + ")");
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + "+91" + keypeoples.get(position).getContact()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(intent);
                }
            });
            return v;
        }

    }

}
