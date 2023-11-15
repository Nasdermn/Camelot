package com.example.camelot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 123;

    private RecyclerView recyclerView;
    private ImageView shuffleButton;
    private ImageView unshuffleButton;
    private TextView songCountTextView;
    private final ArrayList<AudioModel> songsList = new ArrayList<>();
    private Parcelable recyclerViewState;
    private boolean isShuffled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        if (checkPermission()) {
            loadSongs();
        } else {
            requestPermission();
        }
        setupRecyclerView();
        setupClickListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        shuffleButton = findViewById(R.id.shuffle);
        unshuffleButton = findViewById(R.id.unshuffle);
        songCountTextView = findViewById(R.id.quantity_text);
    }

    private void loadSongs() {
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        try (Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null)) {

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    AudioModel songData = new AudioModel(
                            cursor.getString(1),
                            cursor.getString(0),
                            cursor.getString(2),
                            cursor.getString(3));

                    if (new File(songData.getPath()).exists()) {
                        songsList.add(songData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!songsList.isEmpty()) {
            songsList.sort(new DateAddedComparator());
            updateSongCountTextView();
        }
    }

    private void setupRecyclerView() {
        // Restore the RecyclerView state if available
        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }

        //recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
    }

    private void setupClickListeners() {
        shuffleButton.setOnClickListener(v -> {
            ButtonAnimator.animateButtonClick(v, 150, 0.8f);
            shuffleSongs();
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        });

        unshuffleButton.setOnClickListener(v -> {
            ButtonAnimator.animateButtonClick(v, 150, 0.8f);
            unshuffleSongs();
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        });
    }

    private void updateSongCountTextView() {
        songCountTextView.setText("Количество песен: " + songsList.size());
    }

    private void shuffleSongs() {
        Collections.shuffle(songsList);
        isShuffled = true;
        updateSongCountTextView();
    }

    private void unshuffleSongs() {
        if (isShuffled) {
            songsList.sort(new DateAddedComparator());
            isShuffled = false;
            updateSongCountTextView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
            if (recyclerViewState != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            outState.putParcelable("recycler_state", recyclerView.getLayoutManager().onSaveInstanceState());
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Пожалуйста, предоставьте доступ к чтению файлов", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongs();
                if (!songsList.isEmpty()) {
                    setupRecyclerView();
                }
            } else {
                Toast.makeText(this, "Доступ к файлам не разрешен", Toast.LENGTH_SHORT).show();
            }
        }
    }
}