package com.example.camelot;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTv, currentTimeTv, totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay, nextBtn, prevBtn, musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTv = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        nextBtn = findViewById(R.id.next);
        prevBtn = findViewById(R.id.prev);
        musicIcon = findViewById(R.id.music_icon_big);

        titleTv.setSelected(true);

        songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent("SONG_CHANGED");
        sendBroadcast(intent);
    }

    void setResourcesWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSong.getTitle());
        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));
        pausePlay.setOnClickListener(v -> pausePlay());
        nextBtn.setOnClickListener(v -> playNextSong());
        prevBtn.setOnClickListener(v -> playPreviousSong());
        nextBtn.setOnClickListener(v -> {
            playNextSong();
            ButtonAnimator.animateButtonClick(v, 90, 0.8f);
        });
        prevBtn.setOnClickListener(v -> {
            playPreviousSong();
            ButtonAnimator.animateButtonClick(v, 90, 0.8f);
        });

        playMusic();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MyApp", "Error during playback: " + e.getMessage());
            Toast.makeText(this, "Error during playback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Добавлено для обновления SeekBar и времени
        updateSeekBarAndTime();
    }

    private void updateSeekBarAndTime() {
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));

                if (mediaPlayer.isPlaying()) {
                    pausePlay.setImageResource(R.drawable.ic_pause);
                } else {
                    pausePlay.setImageResource(R.drawable.ic_play);
                }

                // Добавлено для рекурсивного обновления
                new Handler().postDelayed(this, 100);
            }
        });
    }

    private void playNextSong() {
        if (MyMediaPlayer.currentIndex == songsList.size() - 1) {
            return;
        }
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
        playMusic();
    }

    private void playPreviousSong() {
        if (MyMediaPlayer.currentIndex == 0) {
            return;
        }
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
        playMusic();
    }

    private void pausePlay() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else mediaPlayer.start();
    }

    public static String convertToMMSS(String duration) {
        long millis = Long.parseLong(duration);
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}