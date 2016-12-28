package com.pers.myc.testactivity.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.pers.myc.testactivity.Config;

/**
 * Created by Administrator on 2016/12/27.
 */

public class MapsModel {
    //方块大小
    public int boxSize;
    //地图
    public boolean[][] maps;
    //地图画笔
    Paint mapPaint;
    //状态画笔
    Paint statePaint;
    //提示框画笔
    Paint backgroundPaint;
    //结束游戏画笔
    Paint gameoverPaint;
    //地图宽度
    int xWidth;
    //地图高度
    int yHeight;

    public MapsModel(int xWidth, int yHeight, int boxSize) {
        this.boxSize = boxSize;
        this.xWidth = xWidth;
        this.yHeight = yHeight;
        //初始化地图画笔
        mapPaint = new Paint();
        mapPaint.setColor(Color.parseColor("#ca7a2c"));
        mapPaint.setAntiAlias(true);
        //初始化状态画笔
        statePaint = new Paint();
        statePaint.setColor(Color.parseColor("#fbf1df"));
        statePaint.setAntiAlias(true);
        statePaint.setTextSize(100);
        //初始化提示框画笔
        gameoverPaint = new Paint();
        gameoverPaint.setColor(Color.parseColor("#fbf1df"));
        gameoverPaint.setAntiAlias(true);
        gameoverPaint.setTextSize(80);
        //初始化结束游戏画笔
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#b8aa96"));
        backgroundPaint.setAntiAlias(true);
        //初始化地图数据
        maps = new boolean[Config.MAPX][Config.MAPY];
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void drawMaps(Canvas canvas) {
        for (int x = 0; x < maps.length; x++) {
            for (int y = 0; y < maps[x].length; y++) {
                if (maps[x][y] == true) {
                    canvas.drawRoundRect(x * boxSize + boxSize / 20,
                            y * boxSize + boxSize / 20,
                            (x + 1) * boxSize - boxSize / 20,
                            (y + 1) * boxSize - boxSize / 20,
                            boxSize / 8,
                            boxSize / 8,
                            mapPaint);
                }
            }
        }
    }

    //绘制状态
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void drawState(Canvas canvas, boolean isPause, boolean isOver, int score) {
        //画暂停状态
        if (isPause && !isOver) {
            canvas.drawText("暂停", xWidth / 2 - statePaint.measureText("暂停") / 2, yHeight / 2, statePaint);
        }
        //画结束状态
        if (isOver) {
            canvas.drawRoundRect(xWidth / 7,
                    yHeight * 3 / 10,
                    xWidth * 6 / 7,
                    yHeight * 6 / 10,
                    xWidth / 15,
                    yHeight / 15,
                    backgroundPaint);
            canvas.drawText("游戏结束", xWidth / 2 - gameoverPaint.measureText("游戏结束") / 2, yHeight * 4 / 10, gameoverPaint);
            canvas.drawText("得分:" + score, xWidth / 2 - gameoverPaint.measureText("得分:000") / 2, yHeight * 4 / 10 + gameoverPaint.measureText("得") * 2, gameoverPaint);
        }
    }

    //清除地图
    public void cleanMaps() {
        for (int x = 0; x < maps.length; x++) {
            for (int y = 0; y < maps[x].length; y++) {
                maps[x][y] = false;
            }
        }
    }

    //消行处理
    public int cleanLine(int score) {
        for (int y = maps[0].length - 1; y > 0; y--) {
            if (checkLine(y)) {
                //执行消行
                score = deleteLine(y,score);
                y++;
            }
        }
        return score;
    }

    //消行判断
    public boolean checkLine(int y) {
        for (int x = 0; x < maps.length; x++) {
            //如果有一个不为true则该行不能消除
            if (!maps[x][y])
                return false;
        }
        //如果每一个都等于true，消行
        return true;
    }

    //执行消行
    public int deleteLine(int dy, int score) {
        for (int y = dy; y > 0; y--) {
            for (int x = 0; x < maps.length; x++) {
                maps[x][y] = maps[x][y - 1];
            }
        }
        return score + 1;
    }
}
