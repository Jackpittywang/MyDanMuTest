package com.example.monidanmu2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.VideoView;

import com.opendanmaku.DanmakuItem;
import com.opendanmaku.DanmakuView;
import com.opendanmaku.IDanmakuItem;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    private DanmakuView mDanmakuView;

    private List<IDanmakuItem> mIDanmakuItems;
    public VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = (VideoView) findViewById(R.id.vvv);
        String palyUrl = "http://sohu.vod.cdn.myqcloud.com/228/94/MTW1ErKwRpmwJ1IIyEpHIB.mp4?key=XTU-IrpGtF35bzwh1ZICG4DcmdFZkI6I&n=1&a=2813&cip=1.180.208.153&pt=1&rb=1&prod=flash";
        mVideoView.setVideoURI(Uri.parse(palyUrl));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mDanmakuView = (DanmakuView) findViewById(R.id.danmakuView);
        mIDanmakuItems = initItems();
        mDanmakuView.addItem(mIDanmakuItems, true);
        mDanmakuView.setPickItemInterval(4000);
        mDanmakuView.show();
    }

    private List<IDanmakuItem> initItems() {
        List<IDanmakuItem> list = new ArrayList<>();
        /*for (int i = 0; i < 100; i++) {
            IDanmakuItem item = new DanmakuItem(this, i + " : plain text danmuku", mDanmakuView.getWidth());
            list.add(item);
        }*/

        String msg = " : text with image   ";
        for (int i = 0; i < 100; i++) {
            ImageSpan imageSpan = new ImageSpan(this, R.drawable.em);
            //ImageSpan imageSpan = new ImageSpan(this, R.drawable.ic_default_circle);
            SpannableString spannableString = new SpannableString(i + msg);
            spannableString.setSpan(imageSpan, spannableString.length() - 2, spannableString.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            IDanmakuItem item = new DanmakuItem(this, spannableString, mDanmakuView.getWidth(), 0, 0, 0, 1.5f);
            list.add(item);
        }
        return list;
    }

}
