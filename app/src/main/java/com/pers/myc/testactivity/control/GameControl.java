package com.pers.myc.testactivity.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.pers.myc.testactivity.Config;
import com.pers.myc.testactivity.R;
import com.pers.myc.testactivity.model.BoxModel;
import com.pers.myc.testactivity.model.MapsModel;

/**
 * Created by Administrator on 2016/12/28.
 */

public class GameControl {
    //常量区
    private final String INVALIDATE = "invalidate";
    private final String MOVE_BUTTOM = "moveButtom";
    private final String UPDATE_SCORE = "updateScore";

    //重绘视图
    public Handler handler;
    //分数
    public int score = 0;
    //方块模型
    BoxModel boxModel;
    //地图模型
    MapsModel mapsModel;
    //方块大小
    int boxSize;
    //自动下落进程
    public Thread downThread;
    //游戏暂停
    public boolean isPause;
    //游戏结束
    public boolean isOver;

    public GameControl(Handler handler, Context context) {
        this.handler = handler;
        //初始化数据
        this.initData(context);

    }

    //初始化数据
    public void initData(Context context) {
        score = 0;
        int width = getScreenWidth(context);
        Config.xWidth = width * 2 / 3;
        Config.yHeight = Config.xWidth * 2;

        isPause = true;

        boxSize = Config.xWidth / Config.MAPX;
        //实例化地图模型
        mapsModel = new MapsModel(Config.xWidth, Config.yHeight, boxSize);
        //实例化方块模型
        boxModel = new BoxModel(boxSize);


    }

    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //开始游戏
    public void startGamen() {
        //清空地图
        mapsModel.cleanMaps();
        isPause = false;
        isOver = false;
        score = 0;
        this.sendMessage(handler, UPDATE_SCORE);
        if (downThread == null) {
            downThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            //休眠500毫秒
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //判断游戏是否处于结束状态
                        //判断是否处于暂停状态
                        if (isPause || isOver)
                            continue;
                        sendMessage(handler, INVALIDATE);
                        //执行一次下落
                        sendMessage(handler, MOVE_BUTTOM);
                    }
                }
            };
            downThread.start();
        }
        boxModel.newBox();
    }

    //下落
    public boolean moveButtom() {
        //移动成功 不作处理
        if (boxModel.move(0, 1, mapsModel)) {
            return true;
        }
        //移动失败 堆积处理
        for (int i = 0; i < boxModel.boxs.length; i++) {
            mapsModel.maps[boxModel.boxs[i].x][boxModel.boxs[i].y] = true;
        }
        //消行
        score = mapsModel.cleanLine(score);
        //更新得分
        this.sendMessage(handler, UPDATE_SCORE);
        //生成新方块
        boxModel.newBox();

        //游戏结束判断
        isOver = checkOver();
        return false;
    }


    //结束判断
    private boolean checkOver() {
        for (int i = 0; i < boxModel.boxs.length; i++) {
            if (mapsModel.maps[boxModel.boxs[i].x][boxModel.boxs[i].y]) {
                return true;
            }
        }
        return false;
    }

    //设置暂停状态
    private void setPause() {
        if (isPause) isPause = false;
        else isPause = true;
    }

    //控制绘制
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void draw(Canvas canvas) {
        //绘制地图
        mapsModel.drawMaps(canvas);
        //绘制方块
        boxModel.drawBoxs(canvas);
        //绘制状态
        mapsModel.drawState(canvas, isPause, isOver, score);
    }

    //处理点击事件
    public void onClick(int id) {
        switch (id) {
            case R.id.activity_main_btn_left:
                if (isPause)
                    return;
                boxModel.move(-1, 0, mapsModel);
                break;
            case R.id.activity_main_btn_right:
                if (isPause)
                    return;
                boxModel.move(1, 0, mapsModel);
                break;
            case R.id.activity_main_btn_top:
                if (isPause)
                    return;
                boxModel.rotate(mapsModel);
                break;
            case R.id.activity_main_btn_bottom:
                if (isPause)
                    return;
                while (true) {
                    if (!moveButtom())
                        break;
                }
                break;
            case R.id.activity_main_btn_start:
                startGamen();
                break;
            case R.id.activity_main_btn_pause:
                setPause();
                break;
            default:
                break;
        }
    }

    private void sendMessage(Handler handler, String type) {
        Message msg = new Message();
        msg.obj = type;
        handler.sendMessage(msg);
    }
}
