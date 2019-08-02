package com.pollen.community.Manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

public class CutLayout extends FrameLayout {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Xfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private Path path = new Path();

    public CutLayout(Context context) {
        super(context);
    }

    public CutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CutLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);

        paint.setXfermode(pdMode);
        path.reset();
        path.moveTo(0, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics()));
        path.close();
        canvas.drawPath(path, paint);

        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);
    }
}
