package com.example.pusika.scrum.view.common;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Rect;
import android.graphics.Shader;

public class BitmapHexagonDrawable extends HexagonDrawable {

    private Bitmap mOriginBitmap;

    public BitmapHexagonDrawable(Bitmap bitmap) {
        mOriginBitmap = bitmap;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        Bitmap bitmap = Bitmap.createScaledBitmap(mOriginBitmap, bounds.width(), bounds.height(), true);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        getPaint().setShader(shader);
    }

}
