package com.example.zhouwn.circlemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    private CircleMenuView circleMenuView;
//    private CircleMenuLayout circleMenuLayout;


    private int[] drawables = {
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan,
            R.mipmap.xiaoyuan

    };
    private static final int[] TITLES = new int[]
            {
                    R.string.title1,
                    R.string.title2,
                    R.string.title3,
                    R.string.title4,
                    R.string.title5,
                    R.string.title6,
                    R.string.title7,
                    R.string.title8,
                    R.string.title9,
                    R.string.title10,
                    R.string.title11,
                    R.string.title12,
                    R.string.title13,
                    R.string.title14,
                    R.string.title15,
                    R.string.title16,
                    R.string.title17,
                    R.string.title18,
                    R.string.title19,
                    R.string.title20
            };

    private static final String[] numbers = {

            "第一个",
            "第二个",
            "第三个",
            "第四个",
            "第五个",
            "第六个",
            "第七个",
            "第八个",
            "第九个",
            "第十个",
            "第十一个",
            "第十二个",
            "第十三个",
            "第十四个",
            "第十五个",
            "第十六个",
            "第十七个",
            "第十八个",
            "第十九个",
            "第二十个",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleMenuView = (CircleMenuView) findViewById(R.id.circleMenu);
        circleMenuView.setMenuResource(R.mipmap.xiaoyuan, TITLES, TITLES, getIntent().getIntExtra("type", CircleMenuView.CIRCLE));

//        circleMenuLayout = (CircleMenuLayout) findViewById(R.id.circleMenuLayout);
//        circleMenuLayout.setMenuResource(drawables, TITLES_, TITLES_);
    }


}
