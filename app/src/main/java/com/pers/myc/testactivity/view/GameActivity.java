package com.pers.myc.testactivity.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pers.myc.testactivity.Config;
import com.pers.myc.testactivity.R;
import com.pers.myc.testactivity.control.GameControl;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    FrameLayout mGameLayout;
    //分数显示
    TextView mScoreTextView;
    //游戏区控件
    View gamePanel;

    //游戏控制器
    GameControl gameControl;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String type = (String) msg.obj;
            if (type == null) {
                return;
            }
            if (type.equals("invalidate")) {
                //重绘
                gamePanel.invalidate();
            } else if (type.equals("moveButtom")) {
                //向下移动
                gameControl.moveButtom();
            } else if (type.equals("updateScore")) {
                mScoreTextView.setText(String.valueOf(gameControl.score));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameLayout = (FrameLayout) findViewById(R.id.activity_game_frame_layout);
        mScoreTextView = (TextView) findViewById(R.id.activity_main_text_view_score);
        //实例化游戏控制器
        gameControl = new GameControl(handler, this);
        initView();//初始化视图
        initListener();//初始化监听器
    }

    public void initView() {
        gamePanel = new View(this) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                //绘制
                gameControl.draw(canvas);
            }
        };
        gamePanel.setLayoutParams(new ViewGroup.LayoutParams(Config.xWidth, Config.yHeight));
        gamePanel.setBackgroundColor(Color.parseColor("#ccc0b2"));
        mGameLayout.addView(gamePanel);
    }

    public void initListener() {
        findViewById(R.id.activity_main_btn_left).setOnClickListener(this);
        findViewById(R.id.activity_main_btn_top).setOnClickListener(this);
        findViewById(R.id.activity_main_btn_right).setOnClickListener(this);
        findViewById(R.id.activity_main_btn_bottom).setOnClickListener(this);
        findViewById(R.id.activity_main_btn_start).setOnClickListener(this);
        findViewById(R.id.activity_main_btn_pause).setOnClickListener(this);
    }


    //捕捉点击事件
    @Override
    public void onClick(View v) {
        gameControl.onClick(v.getId());
        gamePanel.invalidate();
    }


}
