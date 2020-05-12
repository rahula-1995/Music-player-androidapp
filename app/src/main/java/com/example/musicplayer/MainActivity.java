package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView mysonglistview;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mysonglistview = (ListView) findViewById(R.id.mysong);

            runtimepermission();
        } catch (Exception e) {
            System.out.println("ff");
        }
    }

    public void runtimepermission(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                display();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

        public ArrayList<File> findsong(File file){

        ArrayList<File> arraylist=new ArrayList<>();

        File[] files = file.listFiles();

        try {
            for (File singlefile : files) {

                if (singlefile.isDirectory() && !singlefile.isHidden()) {

                    arraylist.addAll(findsong(singlefile));

                }
                else {
                    if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {

                        arraylist.add(singlefile);

                    }
                }
            }

        } catch (Exception e) {
            System.out.println("cv");
        }

        return arraylist;
    }

    void display() {
        try {

            final ArrayList<File> Mysong = findsong(Environment.getExternalStorageDirectory());


            if (Mysong != null) {

                items = new String[Mysong.size()];

                for (int i = 0; i < Mysong.size(); i++) {
                    items[i] = Mysong.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");


                }
                ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
                mysonglistview.setAdapter(myadapter);
            }
            mysonglistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                    String songName = mysonglistview.getItemAtPosition(i).toString();
                    startActivity(new Intent(getApplicationContext(), playeractivity.class).putExtra("songs", Mysong).putExtra("name", songName).putExtra("pos", i));
                }
            });
        } catch (Exception e) {
            System.out.println("vv");
        }
    }

}
