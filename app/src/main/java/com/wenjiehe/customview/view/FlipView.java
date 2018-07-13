package com.wenjiehe.customview.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.wenjiehe.customview.R;

public class FlipView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();
    //Y轴方向旋转角度

    private float degreeY;

    //不变的那一半，Y轴方向旋转角度

    private float fixDegreeY;

    //Z轴方向（平面内）旋转的角度

    private float degreeZ;


    public AnimatorSet set;

    public FlipView(Context context) {
        super(context);
    }

    public FlipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = -displayMetrics.density * 6;
        camera.setLocation(0, 0, newZ);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, "degreeY", 0, -45);
        animator1.setDuration(1000);
        animator1.setStartDelay(500);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "degreeZ", 0, 270);
        animator2.setDuration(800);
        animator2.setStartDelay(500);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(this, "fixDegreeY", 0, 30);
        animator3.setDuration(500);
        animator3.setStartDelay(500);

        set = new AnimatorSet();
        set.playSequentially(animator1, animator2, animator3);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        set.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(set.isRunning()){
            set.end();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;

        //画变换的一半

        //先旋转，再裁切，再使用camera执行3D动效,**然后保存camera效果**,最后再旋转回来
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degreeZ);
        camera.rotateY(degreeY);
        camera.applyToCanvas(canvas);

        //计算裁切参数时清注意，此时的canvas的坐标系已经移动
        canvas.clipRect(0, -centerY, centerX, centerY);
        canvas.rotate(degreeZ);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        //画不变换的另一半
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degreeZ);
        //计算裁切参数时清注意，此时的canvas的坐标系已经移动
        canvas.clipRect(-centerX, -centerY, 0, centerY);
        //此时的canvas的坐标系已经旋转，所以这里是rotateY
        camera.rotateY(fixDegreeY);
        camera.applyToCanvas(canvas);
        canvas.rotate(degreeZ);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
    }

    public void setFixDegreeY(float fixDegreeY) {
        this.fixDegreeY = fixDegreeY;
        invalidate();
    }

    public void setDegreeY(float degreeY) {
        this.degreeY = degreeY;
        invalidate();
    }

    public void setDegreeZ(float degreeZ) {
        this.degreeZ = degreeZ;
        invalidate();
    }

    public float getDegreeY() {
        return degreeY;
    }

    public float getFixDegreeY() {
        return fixDegreeY;
    }

    public float getDegreeZ() {
        return degreeZ;
    }

    public void start() {
        degreeY = 0;
        fixDegreeY = 0;
        degreeZ = 0;
        invalidate();
        set.start();
    }
}
