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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Creates a material themed progressbar for API 11 and above
 *
 * @author Akhil S Pillai
 */

public class ProgressBarMaterial extends View implements Animator.AnimatorListener {
    private static final int DEFAULT_PROGRESS_WIDTH = 5;
    private static final int DEFAULT_ANIM_DURATION = 1200;
    private static final int[] DEFAULT_COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE};
    private Paint mProgressPaint;
    private RectF mProgressRect;
    private float mProgressStrokeWidth, mStartAngle = 0, mSweepAngle = 0, mActualCoveredAngle = 0;
    private AnimatorSet mAnimator;
    private int[] mProgressSwapColors;
    private int mColorArrayPointer = 0;
    private long mProgressCycleDuration;

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
        mProgressStrokeWidth = DEFAULT_PROGRESS_WIDTH
                * getResources().getDisplayMetrics().density;
        mProgressSwapColors = DEFAULT_COLORS;
        mProgressCycleDuration = DEFAULT_ANIM_DURATION;

        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.ProgressBarMaterial, defStyle, 0);

            mProgressStrokeWidth = a.getDimension(
                    R.styleable.ProgressBarMaterial_progressStrokeWidth,
                    mProgressStrokeWidth);

            int colorResId = a.getResourceId(R.styleable.ProgressBarMaterial_progressSwapColors, -1);
            if (colorResId != -1) {
                mProgressSwapColors = getResources().getIntArray(colorResId);
            }

            mProgressCycleDuration = a.getInt(
                    R.styleable.ProgressBarMaterial_progressCycleDuration,
                    DEFAULT_ANIM_DURATION);

            a.recycle();
        }
        mProgressPaint = new Paint();
        mProgressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
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
            mSweepAnimator2.setInterpolator(new DecelerateInterpolator());
            mSweepAnimator2.addListener(this);

            ObjectAnimator mStartAnimator2 = ObjectAnimator.ofFloat(this, "startAngle", 0, 360);
            mStartAnimator2.setInterpolator(new DecelerateInterpolator());

            AnimatorSet mAnimator2 = new AnimatorSet();
            mAnimator2.playTogether(mStartAnimator2, mSweepAnimator2);

            mAnimator = new AnimatorSet();
            mAnimator.playSequentially(mAnimator1, mAnimator2);
            mAnimator.addListener(this);
            mAnimator.setDuration(mProgressCycleDuration/2);
            mAnimator.start();
        }
        mProgressPaint.setColor(mProgressSwapColors[mColorArrayPointer]);
        if (isInEditMode()) {
            canvas.drawArc(mProgressRect, 100, 300, false, mProgressPaint);
        } else {
            canvas.drawArc(mProgressRect, mStartAngle, mSweepAngle, false, mProgressPaint);
        }

    }

    /**
     * This function is used to create the animation for the progressbar.
     * It represents the sweep angle of the arc at a particular time.
     *
     * @param baseValue The example color attribute value to use.
     */
    public void setSweepAngle(float baseValue) {
        mSweepAngle = baseValue;
        invalidate();
    }

    /**
     * This function is used to create the animation for the progressbar.
     * It represents the start angle of the arc at a particular time.
     *
     * @param baseValue The example color attribute value to use.
     */
    public void setStartAngle(float baseValue) {
        mStartAngle = mActualCoveredAngle + baseValue;
        if (mStartAngle > 360) {
            mStartAngle %= 360;
        }

    }

    /**
     * Gets the colors attribute value of the progressbar.
     *
     * @return The progressbar colors attribute value.
     */
    public int[] getProgressSwapColors() {
        return mProgressSwapColors;
    }

    /**
     * Sets the progressbar's colors attribute value.
     *
     * @param progressSwapColors The progressbar colors attribute value to use.
     */
    public void setProgressSwapColors(int[] progressSwapColors) {
        mProgressSwapColors = progressSwapColors;
        invalidate();
    }

    /**
     * Gets the progressbar's stroke width attribute value.
     *
     * @return The stroke width attribute value.
     */
    public float getProgressStrokeWidth() {
        return mProgressStrokeWidth;
    }

    /**
     * Sets the progressbar's stroke width attribute value.
     *
     * @param progressStrokeWidth The stroke width attribute value to use.
     */
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
            mColorArrayPointer++;
            if (mColorArrayPointer == mProgressSwapColors.length) {
                mColorArrayPointer = 0;
            }
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
