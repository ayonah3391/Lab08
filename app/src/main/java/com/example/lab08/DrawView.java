package com.example.lab08;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.time.ZonedDateTime;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DrawView extends View {

    // 3120 x 1440 pixel screen for pixel 6 pro
    final int screen_width = 1440;
    final int screen_height = 3120;
    final int hourHandRadius = screen_width/4;
    final int minuteHandRadius = screen_width/3;
    double seconds, minutes, hours;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        // colors
        int royalBlue = Color.rgb(18, 61, 129);
        int blueWhite = Color.rgb(172, 226, 252);
        int offWhite = Color.rgb(221, 229, 225);

        // drawing
        Paint paint1 = new Paint();
        paint1.setShader(new LinearGradient(screen_width/4, screen_width/2, screen_width, getHeight(), royalBlue, blueWhite, Shader.TileMode.MIRROR));


        // watch face
        canvas.drawCircle(screen_width/2, 2*screen_height/5, screen_width/2, paint1);

        // setup paint
        Paint paint2 = new Paint();
        paint2.setColor(offWhite);

        // triangle at top
        drawTriangle(screen_width/2-120, screen_height/5-20, 240, 230, true, paint2, canvas);
        drawTriangle(screen_width/2-105, screen_height/5-15, 210, 210, true, new Paint(), canvas);
        drawTriangle(screen_width/2-90, screen_height/5, 180, 180, true, paint2, canvas);

        // draw rectangle at bottom
        canvas.drawRect(screen_width/2-45, 2*screen_height/5+screen_width/2-256, screen_width/2+45, 2*screen_height/5+screen_width/2-24, paint2);
        canvas.drawRect(screen_width/2-40, 2*screen_height/5+screen_width/2-255, screen_width/2+40, 2*screen_height/5+screen_width/2-25, new Paint());
        canvas.drawRect(screen_width/2-35, 2*screen_height/5+screen_width/2-245, screen_width/2+35, 2*screen_height/5+screen_width/2-35, paint2);

        // draw rectangle at 9 o'clock position
        canvas.drawRect(23, 2*screen_height/5 - 45, 257, 2*screen_height/5 + 45, paint2);
        canvas.drawRect(30, 2*screen_height/5 - 40, 250, 2*screen_height/5 + 40, new Paint());
        canvas.drawRect(35, 2*screen_height/5 - 35, 245, 2*screen_height/5 + 35, paint2);

        // draw rectangle at 3 o'clock position
        canvas.drawRect(screen_width-273, 2*screen_height/5 - 73, screen_width-87, 2*screen_height/5 + 73, paint2);
        canvas.drawRect(screen_width-270, 2*screen_height/5 - 70, screen_width-90, 2*screen_height/5 + 70, new Paint());
        canvas.drawRect(screen_width-265, 2*screen_height/5 - 65, screen_width-95, 2*screen_height/5 + 65, paint2);

        // paint
        Paint paint3 = new Paint();
        paint3.setColor(Color.rgb(210, 203, 189));

        // draw central circle
        canvas.drawCircle(screen_width/2, 2*screen_height/5, screen_width/25 + 15, paint2);
        canvas.drawCircle(screen_width/2, 2*screen_height/5, screen_width/25 + 5, paint3);
        canvas.drawCircle(screen_width/2, 2*screen_height/5, screen_width/25, paint2);

        // draw hand
        drawHand(hours, hourHandRadius, canvas, paint2);
        drawHand(minutes, minuteHandRadius, canvas, paint2);
        drawSecondsHand(seconds, minuteHandRadius+50, canvas, paint2);

        // guidelines
        // drawGuidelines(canvas, true, true, false);

        int[] arr = getSecondsMinutesHours();
        seconds = arr[0];
        minutes = arr[1];
        hours = arr[2];

        //System.out.println(seconds);

        seconds = seconds*6;
        seconds = 90-seconds;

        minutes = minutes*6-seconds/60;
        minutes = 90-minutes;

        hours = hours*6-minutes/60;
        hours = 270-hours;

        invalidate();
    }

    private void drawTriangle(int x, int y, int width, int height, boolean inverted, Paint paint, Canvas canvas){

        Point p1 = new Point(x,y);
        int pointX = x + width/2;
        int pointY = inverted?  y + height : y - height;

        Point p2 = new Point(pointX,pointY);
        Point p3 = new Point(x+width,y);


        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(p1.x,p1.y);
        path.lineTo(p2.x,p2.y);
        path.lineTo(p3.x,p3.y);
        path.close();

        canvas.drawPath(path, paint);
    }

    private void drawGuidelines(Canvas canvas, boolean vert, boolean watch, boolean screen) {
        // vertical guideline
        if (vert) {
            canvas.drawRect(screen_width/2-5, 0, screen_width/2+5, screen_height, new Paint());
        }

        // middle of screen
        if(screen) {
            canvas.drawRect(0, screen_height/2 - 5, screen_width, screen_height/2 + 5, new Paint());
        }

        // middle of watch
        if(watch) {
            canvas.drawRect(0, 2 * screen_height / 5 - 5, screen_width, 2 * screen_height / 5 + 5, new Paint());
            canvas.drawRect(0, 2 * screen_height / 5 - 5, screen_width, 2 * screen_height / 5 + 5, new Paint());
        }

    }

    private void drawHand(double degrees, int radius, Canvas canvas, Paint paint1) {
        double angleRadians = Math.toRadians(degrees);
        int distance = radius/10;

        Point center = new Point(screen_width/2, 2*screen_height/5);
        Point tip = new Point((int) (center.x + radius * Math.cos(angleRadians)), (int) (center.y - radius * Math.sin(angleRadians)));

        degrees = degrees%360;

        Point l, r;
        l = new Point((int) (center.x + (radius - distance) * Math.cos(angleRadians+Math.PI/36)), (int) (center.y - (radius - distance) * Math.sin(angleRadians+Math.PI/36)));
        r = new Point((int) (center.x + (radius - distance) * Math.cos(angleRadians-Math.PI/36)), (int) (center.y - (radius - distance) * Math.sin(angleRadians-Math.PI/36)));

        paint1.setStrokeWidth(25);

        // circles to smooth the edges
        canvas.drawCircle(tip.x, tip.y, 15, paint1);
        canvas.drawCircle(l.x, l.y, 15, paint1);
        canvas.drawCircle(r.x, r.y, 15, paint1);

        // draw lines
        canvas.drawLine(center.x, center.y,l.x, l.y, paint1);
        canvas.drawLine(l.x, l.y, tip.x, tip.y, paint1);
        canvas.drawLine(tip.x, tip.y,r.x, r.y, paint1);
        canvas.drawLine(r.x, r.y, center.x, center.y, paint1);

    }

    private void drawSecondsHand(double degrees, int radius, Canvas canvas, Paint paint) {
        double angleRadians = Math.toRadians(degrees);

        Point center = new Point(screen_width/2, 2*screen_height/5);
        Point tip = new Point((int) (center.x + radius * Math.cos(angleRadians)), (int) (center.y - radius * Math.sin(angleRadians)));
        Point tip2 = new Point((int) (center.x - 200 * Math.cos(angleRadians)), (int) (center.y + 200 * Math.sin(angleRadians)));

        canvas.drawLine(tip2.x, tip2.y, tip.x, tip.y, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int[] getSecondsMinutesHours() {
        LocalTime localTime = LocalTime.now();

        int[] arr = {localTime.getSecond()%60, localTime.getMinute()%60, localTime.getHour()%12};
        return arr;
    }

    private void drawDiamonds(Canvas canvas) {

    }
}
