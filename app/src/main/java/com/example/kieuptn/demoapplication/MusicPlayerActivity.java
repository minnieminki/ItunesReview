package com.example.kieuptn.demoapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by kieuptn on 7/14/15.
 */
public class MusicPlayerActivity extends Activity implements Button.OnClickListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {

    static final String TABLE_NAME = "Favories";

    MediaPlayer mediaPlayer;
    Button playButton, preButton, nextButton, favoriesButton, backButton;
    ObjectItem objectItem;
    TextView textView, curentTimeTextView, totalTimeTextView;
    ImageView imageView;
    ProgressBar progressBar, mediaProgressBar;
    boolean favoriesFlag;
    Database database;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        setUIInLayout();

        mediaPlayer = new MediaPlayer();

        // get database
        database = new Database(this.getBaseContext());

        // get data from parent fagment
        Intent intent = getIntent();
        if (intent != null) {
            objectItem = (ObjectItem) intent.getSerializableExtra("Object");
            textView.setText(objectItem.getSong());
            favoriesFlag = database.checkDataExists(TABLE_NAME, objectItem.getId());
            changeBackgroundButton();

            new LoadImage(imageView, progressBar).execute(objectItem.getImageURLString());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(objectItem.getLinkString());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnBufferingUpdateListener(this);
                mediaPlayer.setOnPreparedListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        database.close();
    }

    @Override
    public void onClick(View view) {
        if (view == playButton) /*when click button play*/ {
            if (!mediaPlayer.isPlaying()) {
                mediaProgressBar.setProgress(0);
                mediaPlayer.start();
                playButton.setBackgroundResource(R.drawable.icon_pause);
                countDownTimer.start();
            } else {
                mediaPlayer.pause();
                playButton.setBackgroundResource(R.drawable.icon_play);
                countDownTimer.cancel();
            }
        } else if (view == backButton) /*when click button back*/ {
            mediaPlayer.stop();
            mediaPlayer = null;
            finish();
        } else if (view == favoriesButton) /*when click button favories*/ {
            if (favoriesFlag == false) {
                database.insertData(TABLE_NAME, objectItem, null);
                favoriesFlag = true;
            } else {
                database.deleteData(TABLE_NAME, objectItem.getId().toString());
                favoriesFlag = false;
            }
            changeBackgroundButton();
        } else /*when click previous or next button*/ {
            countDownTimer.cancel();
            int position = mediaPlayer.getCurrentPosition();
            if (view == preButton) {
                position = position - (int)(position * 0.2);
            } else {
                position /= 0.9;
            }
            mediaPlayer.seekTo(position);
            countDownTimer.start();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Integer bufPos;
        if (i == 100) {
            bufPos = mediaProgressBar.getMax();
        } else {
            bufPos = i * mediaPlayer.getDuration() / 100;
        }
        mediaProgressBar.setSecondaryProgress(bufPos);
    }

    @Override
    public void onPrepared(final MediaPlayer mediaPlayer) {
        mediaProgressBar.setMax((int)TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration()));
        playButton.setVisibility(View.VISIBLE);
        totalTimeTextView.setText(formatTime(mediaPlayer.getDuration()));
        countDownTimer = new CountDownTimer(mediaPlayer.getDuration() + 1000, 1000) {
            @Override
            public void onTick(long miliSeconds) {
                mediaProgressBar.setProgress((int)TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()));
                curentTimeTextView.setText(formatTime(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                countDownTimer.cancel();
                playButton.setBackgroundResource(R.drawable.icon_replay);
            }
        };
    }

    // function change background image for button favories
    public void changeBackgroundButton() {
        if (favoriesFlag == false) {
            favoriesButton.setBackgroundResource(R.drawable.icon_unfavories);
        } else {
            favoriesButton.setBackgroundResource(R.drawable.icon_favories);
        }
    }

    // function get id for UI in layout
    protected void setUIInLayout() {
        // UI in tabbar
        textView = (TextView) findViewById(R.id.songTextView);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        favoriesButton = (Button) findViewById(R.id.favoriesButton);
        favoriesButton.setOnClickListener(this);

        // UI in player
        progressBar = (ProgressBar) findViewById(R.id.imageProgressBar);
        imageView = (ImageView) findViewById(R.id.songImageView);

        // UI control
        playButton = (Button) findViewById(R.id.playButton);
        playButton.setVisibility(View.INVISIBLE);
        playButton.setOnClickListener(this);

        preButton = (Button) findViewById(R.id.previousButton);
        preButton.setOnClickListener(this);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        curentTimeTextView = (TextView) findViewById(R.id.curentTimeTextView);
        totalTimeTextView = (TextView) findViewById(R.id.totalTimeTextView);

        mediaProgressBar = (ProgressBar)findViewById(R.id.mediaProgressBar);
    }

    protected String formatTime(long miliSeconds) {
        DateFormat dateFormat = new SimpleDateFormat("mm:ss");

        return dateFormat.format(miliSeconds);
    }
}
