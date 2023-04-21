package com.example.lab08;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {

    // 3120 x 1440 pixel screen for pixel 6 pro
    final int screen_width = 1440;
    final int screen_height = 3120;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int royalBlue = Color.rgb(18, 61, 129);
        int blueWhite = Color.rgb(213, 227, 247);
        int offWhite = Color.rgb(235, 240, 234);

        // drawing
        Paint paint1 = new Paint();
        paint1.setShader(new LinearGradient(screen_width/4, screen_width/2, screen_width, getHeight(), royalBlue, blueWhite, Shader.TileMode.MIRROR));


        // watch face
        canvas.drawCircle(screen_width/2, 2*screen_height/5, screen_width/2, paint1);

        // triangle at top
        Paint paint2 = new Paint();
        paint2.setColor(offWhite);
        drawTriangle(screen_width/2-100, screen_height/5, 200, 200, true, new Paint(), canvas);
        drawTriangle(screen_width/2-95, screen_height/5, 190, 190, true, paint2, canvas);

        // draw rectangle at bottom
        canvas.drawRect(screen_width/2-40, 2*screen_height/5+screen_width/2-200, screen_width/2+40, 2*screen_height/5+screen_width/2-50, paint2);
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
}
