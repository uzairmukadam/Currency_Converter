package utv.uzitech.umusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaybackActivity extends AppCompatActivity {

    ImageButton prev, play_pause, next;
    ImageView artwork;
    TextView title, album, artist;
    ArrayList<String> allMusic, allArtworks;
    int pos, prev_track;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        mediaPlayer = new MediaPlayer();

        allMusic = getIntent().getStringArrayListExtra("allMusic");
        allArtworks = getIntent().getStringArrayListExtra("allArtwork");

        pos = getIntent().getIntExtra("pos", 0);

        prev = findViewById(R.id.butt_prev);
        play_pause = findViewById(R.id.butt_play_pause);
        next = findViewById(R.id.butt_next);

        artwork = findViewById(R.id.now_art);

        title = findViewById(R.id.now_title);
        album = findViewById(R.id.now_album);
        artist = findViewById(R.id.now_artist);

        checkQueue();
        setData(pos);

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        play_pause.setImageDrawable(ContextCompat.getDrawable(PlaybackActivity.this, android.R.drawable.ic_media_play));
                    }else{
                        mediaPlayer.setDataSource(allMusic.get(pos).split(", ")[3]);
                        prev_track = pos;
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        play_pause.setImageDrawable(Conte