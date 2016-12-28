package com.pers.myc.testactivity.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Random;

/**
 * Created by Administrator on 2016/12/27.
 */

public class BoxModel {

    public Point[] boxs;//方块
    public int boxType;//方块类型
    public int boxSize;//方块大小

    Paint boxPaint;//方块画笔

    public BoxModel(int boxSize) {
        boxs = new Point[]{};
        this.boxSize = boxSize;
        //方块画笔初始化
        boxPaint = new Paint();
        boxPaint.setColor(Color.parseColor("#967249"));
        boxPaint.setAntiAlias(true);
    }

    //生成新的方块
    public void newBox() {
        Random random = new Random();
        boxType = random.nextInt(7);
        switch (boxType) {
            case 0:
                boxs = new Point[]{new Point(5, 1), new Point(6, 0), new Point(5, 0), new Point(4, 1)};
                break;
            case 1:
                boxs = new Point[]{new Point(5, 1), new Point(6, 1), new Point(5, 0), new Point(4, 0)};
                break;
            case 2:
                boxs = new Point[]{new Point(5, 1), new Point(5, 0), new Point(4, 1), new Point(6, 1)};
                break;
            case 3:
                boxs = new Point[]{new Point(4, 1), new Point(4, 0), new Point(5, 2), new Point(4, 2)};
                break;
            case 4:
                boxs = new Point[]{new Point(5, 1), new Point(5, 0), new Point(5, 2), new Point(4, 2)};
                break;
            case 5:
                boxs = new Point[]{new Point(5, 1), new Point(4, 0), new Point(5, 0), new Point(4, 1)};
                break;
            case 6:
                boxs = new Point[]{new Point(4, 2), new Point(4, 1), new Point(4, 3), new Point(4, 0)};
                break;
        }
    }

    //绘制方块
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void drawBoxs(Canvas canvas) {
        for (int i = 0; i < boxs.length; i++) {
            canvas.drawRoundRect(
                    boxs[i].x * boxSize + boxSize / 20,
                    boxs[i].y * boxSize + boxSize / 20,
                    boxs[i].x * boxSize + boxSize - boxSize / 20,
                    boxs[i].y * boxSize + boxSize - boxSize / 20,
                    boxSize / 8,
                    boxSize / 8,
                    boxPaint
            );
        }
    }

    //移动

    public boolean move(int x, int y, MapsModel mapsModel) {
        for (int i = 0; i < boxs.length; i++) {
            if (checkBoundary(boxs[i].x + x, boxs[i].y + y, mapsModel)) {
                return false;
            }
        }
        for (int i = 0; i < boxs.length; i++) {
            boxs[i].x += x;
            boxs[i].y += y;
        }
        return true;
    }

    //出界判断
    public boolean checkBoundary(int x, int y, MapsModel mapsModel) {
        return (x < 0 || y < 0 || x >= mapsModel.maps.length || y >= mapsModel.maps[0].length || mapsModel.maps[x][y]);
    }

    //旋转
    public boolean rotate(MapsModel mapsModel) {
        if (boxType == 5) {
            return false;
        }

        for (int i = 0; i < boxs.length; i++) {
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            if (checkBoundary(checkX, checkY, mapsModel)) {
                return false;
            }
        }
        //顺时针旋转90度
        for (int i = 0; i < boxs.length; i++) {
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            boxs[i].x = checkX;
            boxs[i].y = checkY;
        }
        return true;
    }
}
