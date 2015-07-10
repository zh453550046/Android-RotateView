package com.example.zhouwn.circlemenu;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by jacob-wj on 2015/4/4.
 */
@SuppressLint("DrawAllocation")
public class CircleMenuView extends ViewGroup {

    /**
     * 中心点的坐标
     */
    private float mCenterX;
    private float mCenterY;
    /**
     * 半径
     */
    private float mRadius;

    private int line;
    private int mLine;


    /**
     * 所有子菜单的图片和文案
     */
    private int[] mDrawable;
    private int[] mTitles;
    private int[] yin;

    /**
     * 每个菜单项目所分得的角度
     */
    private float sPerAngle;

    private float mAngle = 90;

    private long startTime;
    private long endTime;

    private int width;
    private int height;
    private float starX;
    private float startY;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private static final int FLINGABLE_VALUE = 300;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private int mFlingableValue = FLINGABLE_VALUE;
    private boolean isFling = false;
    private OnMenuClickListener mMenuListener;

    private List<Bitmap> listmap = new ArrayList<Bitmap>();

    private int defaultCount = 10;

    private float actualAngle;
    private boolean canClic, layoutFirsht = false;

    public CircleMenuView(Context context) {
        this(context, null);
    }

    public CircleMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            width = getSuggestedMinimumWidth();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            width = width == 0 ? getDefaultWidth() : width;
        } else {
            width = widthSize;
        }

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode != MeasureSpec.EXACTLY) {
            height = getSuggestedMinimumHeight();
            height = height == 0 ? getDefaultHeight() : height;
        } else {
            height = heightSize;
        }

        int layoutSize = Math.min(width, height);
        mCenterX = width / 10f;
        mCenterY = height / 2.f;
        mRadius = layoutSize / 1.8f;

        setMeasuredDimension(layoutSize, height);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float d) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, d, dm);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        for (int i = 0; i < count; i++) {


            View child = getChildAt(i);

            int childW = child.getMeasuredWidth();
            int childH = child.getMeasuredHeight();

            ImageView imageView = (ImageView) child.findViewById(R.id.image_view_menu);
            TextView textView = (TextView) child.findViewById(R.id.text_view_menu_yin);

            childW = imageView.getWidth();
            childH = imageView.getHeight();
            LinearLayout ll_text = (LinearLayout) child.findViewById(R.id.ll_text);


            // 显示 隐藏
            if (layoutFirsht) {
                if (mAngle >= actualAngle + 90) {
                    mAngle = mAngle - actualAngle;
                }
                layoutFirsht = false;
            } else {
                if (mAngle >= actualAngle) {
                    mAngle = mAngle - actualAngle;
                }
                if (mAngle < 0) {
                    mAngle = actualAngle + mAngle;
                }
            }
            if (mAngle > 180 + sPerAngle / 3) {
                child.setVisibility(View.GONE);
            } else {
                child.setVisibility(View.VISIBLE);
            }

//            int left, top;
//            if (mAngle < 180) {
//                left = (int) (mCenterX + mRadius * Math.sin(mAngle * Math.PI / 180f) - childW / 2);
//                top = (int) (mCenterY - mRadius * Math.cos(mAngle * Math.PI / 180f) - childH / 2);
//                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getHeight());
//                Log.e("mAngle+left + top", "mAngele:"+mAngle+",left:" + left + ",top:" + top);
//                child.setVisibility(VISIBLE);
//                if (i == 0 && mLine == 0) {
//                    mLine = (int) (left - 2 * mCenterX);
//                    line = mLine;
//                }
//            } else {
//                left = (int) (-line * moveScale - child.getMeasuredWidth() / 2);
//                top = (int) (mCenterY + mRadius - child.getMeasuredHeight() / 2);
//                if (left <= (getChildCount() - 5) * -2 * (mLine + mCenterX) + 2 * mLine + 3 * mCenterX) {
//                    left = 0;
//                    top = (int) (mCenterY - mRadius);
//                    mAngle = sPerAngle;
//                }
//                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
//                child.setVisibility(GONE);
//                line += 2 * (mLine + mCenterX);
//            }
            //圆形
            if (type == CIRCLE) {
                int left = (int) (mCenterX + mRadius * Math.sin(mAngle * Math.PI / 180f) - childW / 2);
                int top = (int) (mCenterY - mRadius * Math.cos(mAngle * Math.PI / 180f) - childH / 2);
                child.layout(left, top, left + child.getMeasuredWidth(),
                        top + child.getMeasuredHeight());

                float currentAngle = mAngle % actualAngle;
                // 放大 缩小
                if (currentAngle > 90 - sPerAngle && currentAngle < 90 + sPerAngle) {

                    float per = (float) Math.abs(currentAngle - 90) / sPerAngle;
                    Log.e("", "per = " + per);
                    float scale = (float) ((1 - per) * 0.5);
                    imageView.layout(0, 0, (int) (width * (1 + scale)), (int) (height * (1 + scale)));
                } else {
                    imageView.layout(0, 0, width, height);
                }
            } else
                //正六边形
                if (type == HEXAGON) {
                    int left = 0, top = 0;
                    float currentAngle = mAngle % 360;
                    float currentScale;
                    if (currentAngle >= 0 && currentAngle < 45) {
                        currentScale = currentAngle / 45;
                        left = (int) (mCenterX + currentScale * Math.sqrt(2) / 2 * mRadius);
                        top = (int) (mCenterY - mRadius + currentScale * (2 - Math.sqrt(2)) / 2 * mRadius);
                    } else if (currentAngle >= 45 && currentAngle < 135) {
                        currentScale = (currentAngle - 45) / 90;
                        left = (int) (mCenterX + Math.sqrt(2) / 2 * mRadius);
                        top = (int) (mCenterY - Math.sqrt(2) / 2 * mRadius + currentScale * Math.sqrt(2) * mRadius);
                    } else if (currentAngle >= 135 && currentAngle < 180) {
                        currentScale = (currentAngle - 135) / 45;
                        left = (int) (mCenterX + Math.sqrt(2) / 2 * mRadius - currentScale * Math.sqrt(2) / 2 * mRadius);
                        top = (int) (mCenterY + Math.sqrt(2) / 2 * mRadius + currentScale * (2 - Math.sqrt(2)) / 2 * mRadius);
                    } else if (currentAngle >= 180 && currentAngle < 225) {
                        currentScale = (currentAngle - 180) / 45;
                        left = (int) (mCenterX - currentScale * Math.sqrt(2) / 2 * mRadius);
                        top = (int) (mCenterY + mRadius - currentScale * (2 - Math.sqrt(2)) / 2 * mRadius);
                    } else if (currentAngle >= 225 && currentAngle < 315) {
                        currentScale = (currentAngle - 225) / 90;
                        left = (int) (mCenterX - Math.sqrt(2) / 2 * mRadius);
                        top = (int) (mCenterY + Math.sqrt(2) / 2 * mRadius - currentScale * Math.sqrt(2) * mRadius);
                    } else if (currentAngle >= 315 && currentAngle < 360) {
                        currentScale = (currentAngle - 315) / 45;
                        left = (int) (mCenterX - Math.sqrt(2) / 2 * mRadius + currentScale * Math.sqrt(2) / 2 * mRadius);
                        top = (int) (mCenterY - Math.sqrt(2) / 2 * mRadius - currentScale * (2 - Math.sqrt(2)) / 2 * mRadius);
                    }
                    child.layout(left - childW / 2, top - childH / 2, left + child.getMeasuredWidth(),
                            top + child.getMeasuredHeight());
                    // 放大 缩小
                    if (currentAngle >= 0 && currentAngle < sPerAngle) {
                        float per = (float) Math.abs(currentAngle - 0) / sPerAngle;
                        float scale = (float) ((1 - per) * 0.5);
                        imageView.layout(0, 0, (int) (width * (1 + scale)), (int) (height * (1 + scale)));
                    } else if (currentAngle > 135 && currentAngle <= 225) {
                        float per = (float) Math.abs(currentAngle - 180) / sPerAngle;
                        float scale = (float) ((1 - per) * 0.5);
                        imageView.layout(0, 0, (int) (width * (1 + scale)), (int) (height * (1 + scale)));
                    } else if (currentAngle > 315 && currentAngle <= 360) {
                        float per = (float) Math.abs(currentAngle - 360) / sPerAngle;
                        float scale = (float) ((1 - per) * 0.5);
                        imageView.layout(0, 0, (int) (width * (1 + scale)), (int) (height * (1 + scale)));
                    } else {
                        imageView.layout(0, 0, width, height);
                    }
//                    if ((currentAngle >= 45 && currentAngle < 135) || (currentAngle >= 225 && currentAngle < 315)) {
//                        mAngle += 2 * sPerAngle;
//                    } else {
//                        mAngle += sPerAngle;
//                    }

                }

            // 绘制底线
            View view = (View) child.findViewById(R.id.line);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(textView.getWidth()
                    + imageView.getWidth() / 2 + dip2px(getContext(), 15), dip2px(getContext(), 1));
            lp.setMargins(imageView.getWidth() / 2,
                    imageView.getHeight() - dip2px(getContext(), 1.8f), 0, 0);
            view.setLayoutParams(lp);
            mAngle += sPerAngle;
        }
        line = mLine;
    }

    private float mLastX;
    private float mLastY;
    private float mTempAngle;
    private AutoFlingRunnable mAutoRunnable;
    private View child;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int cutx = 0;
        int cuty = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                starX = event.getX();
                startY = event.getY();
                startTime = System.currentTimeMillis();
                mTempAngle = 0;
                if (isFling) {
                    removeCallbacks(mAutoRunnable);
                    isFling = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float startAngle = getAngle(mLastX, mLastY);
                float endAngle = getAngle(x, y);
                int quadrant = getQuadrant(x, y);
                switch (quadrant) {
                    case 4:
                    case 1:
                        mAngle += endAngle - startAngle;
                        mTempAngle += endAngle - startAngle;
                        break;
                    case 3:
                    case 2:
                        mAngle += startAngle - endAngle;
                        mTempAngle += startAngle - endAngle;
                        break;
                }

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                endTime = System.currentTimeMillis();
                float anglePerSecond = mTempAngle * 1000f / (endTime - startTime);
                if (anglePerSecond > mFlingableValue) {
                    isFling = true;
                    mAutoRunnable = new AutoFlingRunnable(anglePerSecond);
                    post(mAutoRunnable);
                }
                cutx = (int) Math.abs(event.getX() - starX);
                cuty = (int) Math.abs(event.getY() - startY);
                if (cutx > 10 || cuty > 10) {
                    canClic = true;

                } else {

                    canClic = false;
                }

                animationTrans();

                break;
        }
        requestLayout();
        return super.dispatchTouchEvent(event);
    }

    public void animationTrans() {

//        float unitAngle = (float) 360 / defaultCount;
        float unitAngle;
        if (type == CIRCLE) {
            unitAngle = (float) 360 / defaultCount;
        } else {
            unitAngle = 45f;
        }
        final float oldAngle = mAngle;
        float nAngle = mAngle % actualAngle;
        float gapAngle = (float) (nAngle / unitAngle) - (int) (nAngle / unitAngle);

        final float willAngle;
        if (type == CIRCLE) {
            willAngle = (float) ((0.5 - gapAngle) * unitAngle);
        } else {
            if (gapAngle < 0.5) {
                willAngle = (float) ((0 - gapAngle) * unitAngle);
            } else {
                willAngle = (float) ((1 - gapAngle) * unitAngle);
            }
        }
        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                mAngle = oldAngle + willAngle * interpolatedTime;

                requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        startAnimation(a);
    }

    private class AutoFlingRunnable implements Runnable {

        private float angelPerSecond;

        private AutoFlingRunnable(float mCurrentSpeed) {
            this.angelPerSecond = mCurrentSpeed;
        }

        @Override
        public void run() {
            if (Math.abs(angelPerSecond) < 20) {
                isFling = false;
                removeCallbacks(this);
                return;
            }
            isFling = true;
            mAngle += (angelPerSecond / 30);
            angelPerSecond = angelPerSecond / 1.066f;
            requestLayout();
        }
    }

    /**
     * 根据当前的触摸点获取所在的象限
     */
    private int getQuadrant(float touchX, float touchY) {
        float deltaX = touchX - mCenterX;
        float deltaY = touchY - mCenterY;
        if (deltaX > 0) {
            return deltaY > 0 ? 4 : 1;
        } else {
            return deltaY > 0 ? 3 : 2;
        }
    }

    /**
     * 根据当前的触摸点获取角度
     */
    private float getAngle(float touchX, float touchY) {
        float deltaX = touchX - mCenterX;
        float deltaY = touchY - mCenterY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return (float) (Math.asin(deltaY / distance) * 180 / Math.PI);
    }

    /**
     * 外部调用，用来传入菜单的资源文件
     */
    public void setMenuResource(int[] drawables, int[] titles, int[] yin, int type) {
        if (drawables.length != titles.length && drawables.length != yin.length) {
            throw new IllegalArgumentException("资源文件和文案没有对应");
        }
        if (type == CIRCLE) {
            sPerAngle = 360.0f / defaultCount;
            layoutFirsht = true;
        }
        if (type == HEXAGON) {
            sPerAngle = 45;
            mAngle = 0;
        }
        mDrawable = drawables;
        mTitles = titles;
        actualAngle = sPerAngle * drawables.length;
        this.yin = yin;
        this.type = type;

        setMenuChildView(titles.length);
    }

    private int drawable;
    private int type;

    public static final int
            CIRCLE = 0x1001,//圆形
            HEXAGON = 0x1002;//六边形

    public void setMenuResource(int drawable, int[] titles, int[] yin, int type) {
        if (type == CIRCLE) {
            sPerAngle = 360.0f / defaultCount;
            layoutFirsht = true;
        }
        if (type == HEXAGON) {
            sPerAngle = 45f;
            mAngle = 0;
//            if (titles.length != 0) {
//                int yu = (titles.length - 1) % 6;
//                int shang = (titles.length - 1) / 6;
//                if (yu >= 2 && yu <= 4) {
//                    actualAngle = 360 * shang + yu * sPerAngle + sPerAngle;
//                } else if (yu == 5) {
//                    actualAngle = 360 * shang + yu * sPerAngle + 2 * sPerAngle;
//                } else {
//                    actualAngle = 360 * shang + yu * sPerAngle;
//                }
//            }

        }
        this.drawable = drawable;
        this.yin = yin;
        this.mTitles = titles;
        actualAngle = titles.length * sPerAngle;
        this.type = type;
        setMenuChildViewSingel(titles.length);
    }

    private void setMenuChildViewSingel(int size) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int count = size;
        for (int i = 0; i < count; i++) {
            final int index = i;
            this.child = inflater.inflate(R.layout.layout_menu_item, this, false);
            ImageView imageView = (ImageView) child.findViewById(R.id.image_view_menu);
            TextView textView = (TextView) child.findViewById(R.id.text_view_menu);
            TextView text_view_menu_yin = (TextView) child.findViewById(R.id.text_view_menu_yin);
            imageView.setImageResource(drawable);
            textView.setText(mTitles[i]);
            text_view_menu_yin.setText(yin[i]);
            child.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mMenuListener != null && !isFling && !canClic) {
                        mMenuListener.onMenuClick(index);
                    }
                }
            });
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            addView(child);

            listmap.add(bitmap);
        }
    }

    private void setMenuChildView(int size) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int count = size;
        for (int i = 0; i < count; i++) {
            final int index = i;
            this.child = inflater.inflate(R.layout.layout_menu_item, this, false);
            ImageView imageView = (ImageView) child.findViewById(R.id.image_view_menu);
            TextView textView = (TextView) child.findViewById(R.id.text_view_menu);
            TextView text_view_menu_yin = (TextView) child.findViewById(R.id.text_view_menu_yin);
            imageView.setImageResource(mDrawable[i]);
            textView.setText(mTitles[i]);
            text_view_menu_yin.setText(yin[i]);
            child.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mMenuListener != null && !isFling && !canClic) {
                        mMenuListener.onMenuClick(index);
                    }
                }
            });
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            addView(child);

            listmap.add(bitmap);
        }
    }

    public View getChild() {

        return child;

    }

    /**
     * 获得默认该layout的尺寸
     */
    private int getDefaultWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    private int getDefaultHeight() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public interface OnMenuClickListener {

        void onMenuClick(int position);
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        mMenuListener = listener;
    }

}
