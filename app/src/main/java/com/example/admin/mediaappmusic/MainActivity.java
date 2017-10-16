package com.example.admin.mediaappmusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView txtTitle,txtBegin,txtTotal;
    SeekBar sbSong;
    ImageView imgDisc;
    ImageButton ibPre,ibPlay,ibStop,ibNext;
    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaplayer;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animation = AnimationUtils.loadAnimation(this,R.anim.disc_rotate);
        AnhXa();
        AddSong();
        KhoiTaoMedia();
        final Intent intent = new Intent(MainActivity.this,PlaySongService.class);
        ibPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaplayer.isPlaying()){
                    mediaplayer.stop();
                }
                position--;
                if(position < 0){
                    position = arraySong.size() - 1;
                }
                KhoiTaoMedia();
                mediaplayer.start();
                ibPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaplayer.isPlaying()){
                    mediaplayer.stop();
                }
                position++;
                if(position > arraySong.size() -1 ){
                    position = 0;

                }
                KhoiTaoMedia();
                mediaplayer.start();
                ibPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });
        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.stop();
                mediaplayer.release(); // giải phóng tài nguyên gắn với đối tượng media
                ibPlay.setImageResource(R.drawable.play);
                KhoiTaoMedia();
                stopService(intent);
            }
        });
        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaplayer.isPlaying()){
                    mediaplayer.pause();
                    ibPlay.setImageResource(R.drawable.play);
                }else{
                    mediaplayer.start();
                    ibPlay.setImageResource(R.drawable.pause);
                }
                SetTimeTotal();
                UpdateTimeSong();
                imgDisc.startAnimation(animation);
                startService(intent);
            }
        });

        sbSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { // hàm bắt sự kiện sau khi nhả tay khỏi seekbar
                mediaplayer.seekTo(sbSong.getProgress());
            }
        });
    }

    private void AddSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Âm thầm bên em",R.raw.am_tham_ben_em));
        arraySong.add(new Song("Bèo dạt mây trôi",R.raw.beo_dat_may_troi));
        arraySong.add(new Song("Blue",R.raw.blue));
        arraySong.add(new Song("Em của ngày hôm qua",R.raw.em_cua_ngay_hom_qua));
    }

    private void UpdateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhdangGio = new SimpleDateFormat("mm:ss");
                txtBegin.setText(dinhdangGio.format(mediaplayer.getCurrentPosition()));
                //update progress sbSong
                sbSong.setProgress(mediaplayer.getCurrentPosition()); //bắt sự kiện chạy của mediaplayer

                mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(mediaplayer.isPlaying()){
                            mediaplayer.stop();
                        }
                        position++;
                        if(position > arraySong.size() -1 ){
                            position = 0;

                        }
                        KhoiTaoMedia();
                        mediaplayer.start();
                        ibPlay.setImageResource(R.drawable.pause);
                        SetTimeTotal();
                        UpdateTimeSong();
                    }
                });

                handler.postDelayed(this,500);
            }
        },100);
    }

    private void SetTimeTotal(){
        SimpleDateFormat dingdangGio = new SimpleDateFormat("mm:ss");
        txtTotal.setText(dingdangGio.format(mediaplayer.getDuration()));
        // gán max sbSong = max time song
        sbSong.setMax(mediaplayer.getDuration());
    }

    private void KhoiTaoMedia(){
        mediaplayer = MediaPlayer.create(MainActivity.this,arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
    }

    private void AnhXa() {
        txtTitle    = (TextView) findViewById(R.id.textViewTitle);
        txtBegin    = (TextView) findViewById(R.id.tvBegin);
        txtTotal    = (TextView) findViewById(R.id.tvEnd);
        sbSong      = (SeekBar) findViewById(R.id.sbProgress);
        ibNext      = (ImageButton) findViewById(R.id.imgNext);
        ibStop      = (ImageButton) findViewById(R.id.imgStop);
        ibPre       = (ImageButton) findViewById(R.id.imgPre);
        ibPlay      = (ImageButton) findViewById(R.id.imgPlay);
        imgDisc     = (ImageView) findViewById(R.id.imgdisc);
    }
}
