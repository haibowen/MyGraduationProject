package com.example.administrator.filemanagementassistant.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecorations extends RecyclerView.ItemDecoration {
    private float mDividerHeight;
    private Paint mPaint;
    public DividerItemDecorations() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
    }
    //这个方法是针对每一个item的
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //第一个itemView上面不需要绘制分割线
        if (parent.getChildAdapterPosition(view)!=0){
            outRect.top=1;
            //记录每一个item的上边距
            mDividerHeight=1;
        }
    }
    //这个是针对整个recyclerview所以每次都要计算每个分割线左上角坐标和右下角的坐标
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount=parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //获取每一个itemView
            View view=parent.getChildAt(i);
            //获取每一个itemView的位置
            int index=parent.getChildAdapterPosition(view);
            //第一个itemview不需要绘制分割线
            if (index==0){
                continue;
            }
            float dividerTop=view.getTop()-mDividerHeight;//计算分割线的距离左上角的Y轴的距离
            float dividerLeft=parent.getPaddingLeft()+48;//计算分割线的距离左上角的X轴的距离
            float dividerBottom=view.getTop();///计算分割线的距离右下角角的Y轴的距离
            float dividerRight=parent.getWidth()-parent.getPaddingRight();//计算分割线的距离右下角角的X轴的距离
            c.drawRect(dividerLeft,dividerTop,dividerRight,dividerBottom,mPaint);//绘制一个矩形根据上面的计算的坐标点绘制
        }
    }
}
