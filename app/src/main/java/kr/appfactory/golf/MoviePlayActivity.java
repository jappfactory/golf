package kr.appfactory.golf;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MoviePlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    String mApiKey = "AIzaSyBepkrOwooKDltImT5MhFKlW7QcnPm_hNE";
    YouTubePlayerView youtubeView;
    YouTubePlayer mPlayer;
    Toolbar myToolbar;
    public String videoId;
    public String subject;
    public String videodesc;
    public String publishedAt;
    public String thum_pic;
    public int viewcnt = 0;
    public int loadingresult = 0;

    DBHelper dbHelper;

    YouTubePlayer.OnInitializedListener listener;


    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewcnt = SharedPreference.getIntSharedPreference(getApplicationContext(), "viewcnt");
        viewcnt ++ ;
        SharedPreference.putSharedPreference(getApplicationContext(), "viewcnt", viewcnt);
        setContentView(R.layout.activity_movie_play);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 화면을 landscape(가로) 화면으로 고정하고 싶은 경우

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView desc = (TextView) findViewById(R.id.movie_desc);

        dbHelper = new DBHelper(getApplicationContext());
        Intent intent = getIntent();
        subject = intent.getStringExtra("title");
        videoId = intent.getStringExtra("videoId");
        videodesc = intent.getStringExtra("videodesc");

        publishedAt = intent.getStringExtra("publishedAt");
        thum_pic = intent.getStringExtra("thum_pic");
        title.setText(subject);
        desc.setText(videodesc);



        loadingresult = viewcnt % 5;
        if (loadingresult == 0 ) AdsFull.getInstance(getApplicationContext()).setAdsFull();

        //Toast.makeText (getApplicationContext(), "클릭" + loadingresult , Toast.LENGTH_LONG).show();
        //AdsFull.getInstance(getApplicationContext()).setAdsFull();

        youtubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        youtubeView.initialize(mApiKey, this);

        //youtubeView.initialize("AIzaSyCBOFrruYNwGIjpNZ9mEHUDyJg3qE3gwco", listener);

        ImageView btn_go_back = (ImageView) findViewById(R.id.back_button);
        btn_go_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Go Back", Toast.LENGTH_LONG).show();
                finish();
            }
        });


        Button favoritesButton = (Button) findViewById(R.id.favoritesButton);
        Button favoritesdelButton = (Button) findViewById(R.id.favoritesdelButton);

        //즐겨찾기 추가여부
        Integer getCnt = Integer.parseInt(dbHelper.getCnt_MyVideo(videoId));
        if(getCnt==0) {
            favoritesButton.setVisibility(View.VISIBLE);
            favoritesdelButton.setVisibility(View.INVISIBLE);
        }else{

            favoritesButton.setVisibility(View.INVISIBLE);
            favoritesdelButton.setVisibility(View.VISIBLE);
        }

        //즐겨찾기저장추가
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHelper.insert_MyVideo(videoId, subject, videodesc, publishedAt,  thum_pic);
                Button favoritesButton = (Button) findViewById(R.id.favoritesButton);
                Button favoritesdelButton = (Button) findViewById(R.id.favoritesdelButton);
                favoritesButton.setVisibility(View.INVISIBLE);
                favoritesdelButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "즐겨찾기에 저장되었습니다~", Toast.LENGTH_SHORT).show();
            }
        });

        //즐겨찾기삭제추가
        favoritesdelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHelper.delete_MyVideo(videoId);
                Button favoritesButton = (Button) findViewById(R.id.favoritesButton);
                Button favoritesdelButton = (Button) findViewById(R.id.favoritesdelButton);
                favoritesButton.setVisibility(View.VISIBLE);
                favoritesdelButton.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), "즐겨찾기에서 삭제되었습니다~", Toast.LENGTH_SHORT).show();
            }
        });

        Button shereButton = (Button) findViewById(R.id.shereButton);
        shereButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String EXTRA_TEXT ="https://www.youtube.com/watch?v="+videoId;
                EXTRA_TEXT +="\n\n" +
                        "언제나 즐거운 골프레슨영상 설치\n" +
                        "https://play.google.com/store/apps/details?id=kr.appfactory.golf";

                //Toast.makeText(getApplicationContext(), "Go Back", Toast.LENGTH_LONG).show();
                Intent msg = new Intent (Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_SUBJECT, subject);
                msg.setType("text/plain");

                msg.putExtra(Intent.EXTRA_TEXT,EXTRA_TEXT);
                startActivity(Intent.createChooser(msg, "공유하기"));

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


        //mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        // 비디오 아이디
        Intent intent = getIntent();
        String videoId = intent.getStringExtra("videoId");
        enableDisableView(youtubeView, true);
        mPlayer = youTubePlayer;
        mPlayer.loadVideo(""+ videoId);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            mPlayer.release();
        }
    }
}


