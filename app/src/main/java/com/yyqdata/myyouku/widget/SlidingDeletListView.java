package com.yyqdata.myyouku.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 自定义的ListView实现了侧滑删除，并提供了点击事件和滑动事件接口；
 * Created by 严雨祺 on 2016/10/25.
 */
public class SlidingDeletListView extends ListView implements AbsListView.OnScrollListener {
    private static int SCROLL_SATE = 0;//静止
    private static int MOVE_SPEED = 5;
    private int speed = MOVE_SPEED;
    private int scrollState = SCROLL_SATE;
    private int width;
    private int itemPosition;
    private float startX;
    private float startY;
    private float tempX;
    private float tempY;
    private ViewGroup childItem;
    private ViewGroup itemView;
    private ViewGroup btnView;
    private View btn;
    private boolean isSliding;
    private boolean isShowBtn;
    private boolean isMoved;
    private boolean isMoving;
    private boolean isScrollVertic;
    private VelocityTracker velocityTracker;
    private RelativeLayout.LayoutParams params;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture backFuture;
    private ScheduledFuture toFuture;
    private OnItemClickListener onItemClickListener;
    private OnScrollListener onScrollListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    backNormal();
                    break;
                case 2:
                    toBtnShow();
                    break;
            }
        }
    };


    public SlidingDeletListView(Context context) {
        this(context, null);
    }

    public SlidingDeletListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setOnScrollListener(this);
    }

    public SlidingDeletListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public OnItemClickListener getOnItemClickListeners() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 点击异步复原
     */
    protected void clickReNormal() {
        backFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 0, 4, TimeUnit.MILLISECONDS);
    }

    /**
     * 自动完成按钮显示
     */
    protected void toBtnShow() {
        params.leftMargin -= speed * 0.5;
        if (params.leftMargin <= -width) {
            params.leftMargin = -width;
            toFuture.cancel(true);
        }
        itemView.setLayoutParams(params);
        itemView.invalidate();
        if (params.leftMargin == -width) {
            isShowBtn = true;
            isMoved = true;
        }
    }

    /**
     * 自动恢复
     */
    protected void backNormal() {
        params.leftMargin += speed * 0.5;
        if (params.leftMargin >= 0) {
            params.leftMargin = 0;
            backFuture.cancel(true);
        }
        itemView.setLayoutParams(params);
        itemView.invalidate();
        if (params.leftMargin == 0) {
            isShowBtn = false;
            isSliding = false;
            isMoving = false;

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isShowBtn) {
            startX = ev.getX();
            startY = ev.getY();
            int itemPositionNew = pointToPosition((int) startX, (int) startY) - getFirstVisiblePosition();
            if (itemPositionNew != itemPosition) {
                return true;
            }
        } else {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int event = ev.getAction();
        switch (event) {
            case MotionEvent.ACTION_DOWN:
                //如果優item显示了按钮，单击屏幕就会恢复
                if (isShowBtn) {
                    if (backFuture == null || (backFuture != null && backFuture.isCancelled())) {
                        clickReNormal();
                    }
                } else {
                    actionDown(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isMoved) {
                    actionMove(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                //按钮完全显示的时候，下面方法不执行
                if (!isShowBtn) {
                    actionUp(ev);
                }
                break;
        }
        if (isMoving) {
            return true;
        }
        return isSliding ? false : super.onTouchEvent(ev);
    }

    /**
     * 处理抬起事件
     *
     * @param ev
     */
    private void actionUp(MotionEvent ev) {

        if (isSliding) {
            if (params.leftMargin >= -width / 2) {
                backFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                }, 0, 4, TimeUnit.MILLISECONDS);
            } else {
                toFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(2);
                    }
                }, 0, 4, TimeUnit.MILLISECONDS);
            }
        }

        if (!isMoved && !isSliding && !isShowBtn && !isScrollVertic && onItemClickListener != null) {
            int position = pointToPosition((int) ev.getX(), (int) ev.getY());
            onItemClickListener.onItemClick(this, childItem, position, 0);
        }

        if (params.leftMargin == 0) {
            isMoving = false;
        }
    }

    /**
     * 处理滑动事件
     *
     * @param ev
     */
    private void actionMove(MotionEvent ev) {
        tempX = ev.getX();
        tempY = ev.getY();
        int disX = (int) Math.abs(startX - tempX);
        int disY = (int) Math.abs(startY - tempY);
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        if (disX - disY > 10) {
            isSliding = true;
            isMoving = true;
            if (tempX <= startX) {
                if (disX > width) {
                    disX = width;
                }
                params.leftMargin = -disX;
                itemView.setLayoutParams(params);
                itemView.invalidate();
            } else {
                final VelocityTracker mVelocityTracker = velocityTracker;
                mVelocityTracker.computeCurrentVelocity(30);
                float speed = Math.abs(mVelocityTracker.getXVelocity());
                params.leftMargin += speed;
                if (params.leftMargin >= 0) {
                    params.leftMargin = 0;
                }
                itemView.setLayoutParams(params);
                itemView.invalidate();
            }
            if (scrollState != SCROLL_SATE) {
                isScrollVertic = true;
            }
        }
    }

    /**
     * 处理点击事件
     *
     * @param ev
     */
    private void actionDown(MotionEvent ev) {
        startX = ev.getX();
        startY = ev.getY();
        itemPosition = pointToPosition((int) startX, (int) startY) - getFirstVisiblePosition();
        childItem = (ViewGroup) getChildAt(itemPosition);
        itemView = (ViewGroup) childItem.getChildAt(1);
        btnView = (ViewGroup) childItem.getChildAt(0);
        btn = btnView.getChildAt(0);
        btn.getMeasuredWidth();
        width = btnView.getMeasuredWidth();
        int parentWidth = childItem.getMeasuredWidth();
        params = (RelativeLayout.LayoutParams) itemView.getLayoutParams();
        params.width = parentWidth;
        isMoved = false;
    }

    public void reNomal() {
        isShowBtn = false;
        isSliding = false;
        params.leftMargin = 0;
        itemView.setLayoutParams(params);
        itemView.invalidate();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        if (scrollState == SCROLL_SATE) {
            isScrollVertic = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
