package com.trace.progressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class ProgressBarMaterial extends View implements Animator.AnimatorListener {
    private static final int PROGRESS_WIDTH = 5;
    private static final long ANIM_DURATION = 600;

    private int mProgressColor = Color.RED;
    private Paint mProgressPaint;
    private RectF mProgressRect;
    private float mProgressStrokeWidth, mStartAngle = 0, mSweepAngle = 0, mActualCoveredAngle = 0;
    private AnimatorSet mAnimator;

    public ProgressBarMaterial(Context context) {
        super(context);
        init(null, 0);
    }

    public ProgressBarMaterial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ProgressBarMaterial(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ProgressBarMaterial, defStyle, 0);

        mProgressColor = a.getColor(
                R.styleable.ProgressBarMaterial_progressColor,
                mProgressColor);

        a.recycle();
        mProgressStrokeWidth = PROGRESS_WIDTH
                * getResources().getDisplayMetrics().density;
        mProgressPaint = new Paint();
        mProgressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mProgressRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int actualHeight = h - paddingTop - paddingBottom;
        int actualWidth = w - paddingLeft - paddingRight;
        int size = Math.min(actualHeight, actualWidth);

        mProgressRect.set(paddingLeft, paddingTop, size + paddingLeft, size + paddingTop);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mAnimator == null) {
            ObjectAnimator mSweepAnimator1 = ObjectAnimator.ofFloat(this, "sweepAngle", 0, 300);
            mSweepAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
            mSweepAnimator1.addListener(this);

            ObjectAnimator mStartAnimator1 = ObjectAnimator.ofFloat(this, "startAngle", 0, 100);
            mStartAnimator1.setInterpolator(new LinearInterpolator());

            AnimatorSet mAnimator1 = new AnimatorSet();
            mAnimator1.playTogether(mStartAnimator1, mSweepAnimator1);
            mAnimator1.addListener(this);

            ObjectAnimator mSweepAnimator2 = ObjectAnimator.ofFloat(this, "sweepAngle", 300, 0);
            mSweepAnimator2.setInterpolator(new LinearInterpolator());
            mSweepAnimator2.addListener(this);

            ObjectAnimator mStartAnimator2 = ObjectAnimator.ofFloat(this, "startAngle", 0, 360);
            mStartAnimator2.setInterpolator(new LinearInterpolator());

            AnimatorSet mAnimator2 = new AnimatorSet();
            mAnimator2.playTogether(mStartAnimator2, mSweepAnimator2);

            mAnimator = new AnimatorSet();
            mAnimator.playSequentially(mAnimator1, mAnimator2);
            mAnimator.addListener(this);
            mAnimator.setDuration(ANIM_DURATION);
            mAnimator.start();
        }
        if (isInEditMode()) {
            canvas.drawArc(mProgressRect, 100, 300, false, mProgressPaint);
        } else {
            canvas.drawArc(mProgressRect, mStartAngle, mSweepAngle, false, mProgressPaint);
        }

    }


    public void setSweepAngle(float baseValue) {
        mSweepAngle = baseValue;
        invalidate();
    }

    public void setStartAngle(float baseValue) {
        mStartAngle = mActualCoveredAngle + baseValue;
        if (mStartAngle > 360) {
            mStartAngle %= 360;
        }

    }

    /**
     * Gets the color attribute value of the progressbar.
     * @return The example color attribute value.
     */
    public int getProgressColor() {
        return mProgressColor;
    }

    /**
     * Sets the progressbar's color attribute value.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setProgressColor(int exampleColor) {
        mProgressColor = exampleColor;
    }

    public float getProgressStrokeWidth() {
        return mProgressStrokeWidth;
    }

    public void setProgressStrokeWidth(float progressStrokeWidth) {
        this.mProgressStrokeWidth = progressStrokeWidth;
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mActualCoveredAngle = mStartAngle;
        if (animation.equals(mAnimator)) {
            mAnimator.start();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
