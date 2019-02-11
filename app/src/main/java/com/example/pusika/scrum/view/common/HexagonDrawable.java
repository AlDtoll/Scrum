package com.example.pusika.scrum.view.common;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class HexagonDrawable extends Drawable {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath = new Path();

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        int width = bounds.width();
        int height = bounds.height();
        mPath.reset();
        mPath.moveTo(0, height / 2);
        mPath.lineTo(width / 4, height / 2 - height * 86 / 100 / 2);
        mPath.lineTo(width * 3 / 4, height / 2 - height * 86 / 100 / 2);
        mPath.lineTo(width, height / 2);
        mPath.lineTo(width * 3 / 4, height / 2 + height * 86 / 100 / 2);
        mPath.lineTo(width / 4, height / 2 + height * 86 / 100 / 2);
        mPath.close();
    }

    protected Paint getPaint() {
        return mPaint;
    }

}
