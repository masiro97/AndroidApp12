package com.example.user.proj12;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by User on 2017-05-18.
 */

public class myCanvas extends View {
    Bitmap mBitmap;
    Canvas mCanvas;
    Paint mPaint = new Paint();
    String operation = "";
    String command = "";
    String path = "";
    String option = "";
    Boolean IsChecked = false;
    int width = 0, height = 0;
    Boolean IsSelected = false;


    public myCanvas(Context context) {
        super(context);
    }

    public myCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(IsChecked) {
            if(!IsSelected){
                drawStamp();
                operation="";
            mCanvas.restore();}
            else{
                IsSelected = false;

            }
        }

        if(command.equals("clear")) {
            mBitmap.eraseColor(Color.parseColor("#fdf39a"));
            command = "";
        }

        if(command.equals("save")){

            Save(path + "img.jpg");
            command = "";
            Toast.makeText(getContext(),"SAVE",Toast.LENGTH_SHORT).show();
        }

        if(command.equals("open")){
            Toast.makeText(getContext(),"OPEN",Toast.LENGTH_SHORT).show();
            Bitmap storedImg = BitmapFactory.decodeFile("/data/data/com.example.user.proj12/filesimg.jpg");

            if(storedImg !=null){
                mBitmap.eraseColor(Color.parseColor("#fdf39a"));
                int width = storedImg.getWidth();
                int height = storedImg.getHeight();

                Bitmap sBitmap = Bitmap.createScaledBitmap(storedImg,width/2,
                height/2,false);
                int x1 = mCanvas.getWidth() /2 - sBitmap.getWidth()/2;
                int y1 = mCanvas.getHeight()/2 - sBitmap.getHeight()/2;
                mCanvas.drawBitmap(sBitmap,x1,y1,null);
            }
            else Toast.makeText(getContext(),"저장된 파일이 없습니다",Toast.LENGTH_SHORT).show();

            command = "";
        }

        if(option.equals("bluring")){
            BlurMaskFilter blur = new BlurMaskFilter(100,
                    BlurMaskFilter.Blur.INNER);
            mPaint.setMaskFilter(blur);
        }
        else if(option.equals("coloring")){
            BlurMaskFilter blur = new BlurMaskFilter(100,
                    BlurMaskFilter.Blur.OUTER);
            mPaint.setMaskFilter(blur);
        }
        else if(option.equals("nofilter")){
            mPaint.reset();
        }
        else if(option.equals("big")) mPaint.setStrokeWidth(5);
        else if(option.equals("small")) mPaint.setStrokeWidth(3);
        else if(option.equals("red")) mPaint.setColor(Color.RED);
        else if(option.equals("blue")) mPaint.setColor(Color.BLUE);

        canvas.drawBitmap(mBitmap,0,0,null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mPaint.setStrokeWidth(3);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);

    }

    private void drawStamp(){
        Bitmap img = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        width = img.getWidth();
        height = img.getHeight();
        mCanvas.save();
        if(operation.equals("rotate"))
            mCanvas.rotate(30,mCanvas.getWidth()/2,mCanvas.getHeight()/2);
        else if(operation.equals("move"))
            mCanvas.translate(10,10);
        else if(operation.equals("scale"))
            mCanvas.scale(1.5f,1.5f);
        else if(operation.equals("skew"))
            mCanvas.skew(0.2f,0.2f);
        mCanvas.drawBitmap(img,sx,sy,mPaint);
}
    public void setOperation(String op){

        if(op.equals("save")) this.command = op;
        else if(op.equals("clear")) this.command = op;
        else if(op.equals("open")) this.command = op;
        else if(op.equals("bluring")) this.option = op;
        else if(op.equals("coloring")) this.option = op;
        else if(op.equals("nofilter")) this.option = op;
        else if(op.equals("big")) this.option =op;
        else if(op.equals("small")) this.option =op;
        else if(op.equals("blue")) this.option = op;
        else if(op.equals("red")) this.option = op;
        else {this.operation = op; IsSelected = true;}
        invalidate();

    }

    public boolean Save(String file_name){
        try{
            FileOutputStream out = new FileOutputStream(file_name);
            mBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException",e.getMessage());
        } catch (IOException e) {
            Log.e("IOException",e.getMessage());
        }
        return false;
    }

    int oldx = -1, oldy = -1, sx=-1, sy=-1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!IsChecked){
            int x = (int)event.getX();
            int y = (int)event.getY();

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                oldx =x; oldy = y;
            }
            else if(event.getAction() == MotionEvent.ACTION_MOVE){
                if (oldx != -1){
                    mCanvas.drawLine(oldx,oldy,x,y,mPaint);
                    invalidate();
                    oldx = x; oldy = y;
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                if (oldx != -1){
                    mCanvas.drawLine(oldx,oldy,x,y,mPaint);
                    invalidate();
                }
                oldx = -1; oldy = -1;
            }
        }
        else{
            int x = (int)event.getX();
            int y = (int)event.getY();
            if(event.getAction() == MotionEvent.ACTION_DOWN){
               sx=x - (width/2) ;sy=y - (height/2);
                invalidate();
            }
        }
        return true;
    }
}
