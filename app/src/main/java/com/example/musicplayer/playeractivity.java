package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

import java.util.ArrayList;

public class playeractivity extends AppCompatActivity {

    Button btn_next,btn_previous,btn_pause;
    TextView songname;
    SeekBar seekbar;
    static MediaPlayer mymediaplayer;
    int position;
    String nameofsong;

    ArrayList<File> Mysongs;
    Thread updateSeekBar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_playeractivity);
            ImageView aa;
            aa=(ImageView)findViewById(R.id.imageView);
            aa.setBackgroundResource(R.drawable.music);


            btn_next = (Button)findViewById(R.id.next);
            btn_pause = (Button) findViewById(R.id.pause);
            btn_previous = (Button) findViewById(R.id.previous);
            songname = (TextView) findViewById(R.id.song_name);
            seekbar = (SeekBar) findViewById(R.id.seekbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Now Playing");
            updateSeekBar = new Thread() {

                @Override
                public void run() {
                    int totalDuration = mymediaplayer.getDuration();
                    int currentPosition = 0;
                    while (currentPosition < totalDuration) {
                        try {
                            //sleep(500);
                            currentPosition = mymediaplayer.getCurrentPosition();
                            seekbar.setProgress(currentPosition);
                        } catch (Exception e) {
                            System.out.println("ok");
                        }

                    }
                }
            };

            if (mymediaplayer != null) {
                mymediaplayer.stop();
                mymediaplayer.release();
            }


            Intent i = getIntent();
            Bundle b = i.getExtras();

            Mysongs = (ArrayList) b.getParcelableArrayList("songs");

            nameofsong = Mysongs.get(position).getName().toString();

            String SongName = i.getStringExtra("name");
            songname.setText(SongName);
            songname.setSelected(true);

            position = b.getInt("pos", 0);
            Uri u = Uri.parse(Mysongs.get(position).toString());

            mymediaplayer = MediaPlayer.create(getApplicationContext(), u);
            mymediaplayer.start();


            seekbar.setMax(mymediaplayer.getDuration());
            updateSeekBar.start();
            seekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            seekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            seekbar.setOnSeekBarChangeListener(new
                                                       SeekBar.OnSeekBarChangeListener() {
                                                           @Override
                                                           public void onProgressChanged(SeekBar seekBar, int i,
                                                                                         boolean b) {
                                                           }

                                                           @Override
                                                           public void onStartTrackingTouch(SeekBar seekBar) {
                                                           }

                                                           @Override
                                                           public void onStopTrackingTouch(SeekBar seekBar) {
                                                               mymediaplayer.seekTo(seekBar.getProgress());

                                                           }
                                                       });
            btn_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        seekbar.setMax(mymediaplayer.getDuration());
                        if (mymediaplayer.isPlaying()) {
                            btn_pause.setBackgroundResource(R.drawable.play);
                            mymediaplayer.pause();

                        } else {
                            btn_pause.setBackgroundResource(R.drawable.pause);
                            mymediaplayer.start();
                        }
                    } catch (Exception e) {
                        System.out.println("c");
                    }
                }
            });

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mymediaplayer.stop();
                        mymediaplayer.release();
                        position = ((position + 1) % Mysongs.size());
                        Uri u = Uri.parse(Mysongs.get(position).toString());
                        // songNameText.setText(getSongName);
                        mymediaplayer = MediaPlayer.create(getApplicationContext(), u);

                        nameofsong = Mysongs.get(position).getName().toString();
                        songname.setText(nameofsong);

                        try {
                            mymediaplayer.start();
                        } catch (Exception e) {
                        }

                    } catch (Exception e) {
                        System.out.println("b");
                    }
                }
            });
            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mymediaplayer.stop();
                        mymediaplayer.release();

                        position = ((position - 1) < 0) ? (Mysongs.size() - 1) : (position - 1);
                        Uri u = Uri.parse(Mysongs.get(position).toString());
                        mymediaplayer = MediaPlayer.create(getApplicationContext(), u);
                        nameofsong = Mysongs.get(position).getName().toString();
                        songname.setText(nameofsong);
                        mymediaplayer.start();
                    } catch (Exception e) {
                        System.out.println("w");
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("ee");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
