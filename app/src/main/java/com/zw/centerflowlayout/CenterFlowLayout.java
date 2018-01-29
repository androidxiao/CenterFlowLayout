package com.zw.centerflowlayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chawei on 2018/1/19.
 */

public class CenterFlowLayout extends ViewGroup {

    public static final String TAG = "ez";
    private static final int DEFAULT_CHILD_SPACING = 0;
    private static final int DEFAULT_ROW_SPACING = 0;
    private int mChildSpacing = DEFAULT_CHILD_SPACING;
    private int mRowSpacing = DEFAULT_ROW_SPACING;
    private List<Integer> itemLineWidth = new ArrayList<>();
    private List<Integer> itemLineNum = new ArrayList<>();
    private int mRowTotalCount = 0;

    public CenterFlowLayout(Context context) {
        this(context, null);
    }

    public CenterFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CenterFlowLayout, 0, 0);
        mChildSpacing = typedArray.getDimensionPixelSize(R.styleable.CenterFlowLayout_childSpacing, DEFAULT_CHILD_SPACING);
        mRowSpacing = typedArray.getDimensionPixelSize(R.styleable.CenterFlowLayout_rowSpacing, DEFAULT_ROW_SPACING);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childRight = getMeasuredWidth() - getPaddingRight();
        int availableWidth = childRight - childLeft;
        int curLeft;
        int curTop = childTop;
        int maxHeight = 0;
        int childHeight;
        int childWidth;
        int childIndex = 0;
        for (int j = 0; j < mRowTotalCount; j++) {
            Integer childNum = itemLineNum.get(j);
            curLeft = childLeft + (availableWidth - itemLineWidth.get(j)) / 2;
            int verticalMargin = 0;
            for (int i = 0; i < childNum; i++) {
                View child = getChildAt(childIndex++);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();
                MarginLayoutParams params = (CenterLayoutParams) child.getLayoutParams();
                int marginRight = 0, marginTop = 0, marginBottom;
                if (params instanceof MarginLayoutParams) {
                    marginRight = params.rightMargin;
                    marginTop = params.topMargin;
                    marginBottom = params.bottomMargin;
                    if (childNum > 1 && i == 0) {
                        verticalMargin = marginTop + marginBottom;
                    }
                }
                child.layout(curLeft, curTop, curLeft + childWidth, curTop + childHeight);
                if (maxHeight < childHeight) {
                    maxHeight = childHeight;
                }
                curLeft += childWidth + mChildSpacing + marginRight;
            }
            curTop += maxHeight + mRowSpacing + verticalMargin;
            maxHeight = 0;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        itemLineNum.clear();
        itemLineWidth.clear();
        mRowTotalCount = 0;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int rowLength = widthSize - getPaddingLeft() - getPaddingRight();
        int measuredWidth = 0;
        int measuredHeight = 0;
        int maxWidth = 0;
        int maxHeight = 0;
        int rowCount = 0;
        int childCount = getChildCount();
        int rowWidth = 0;
        int childWidth;
        int childHeight;
        int childNumInRow = 0;
        int tempIndex = 0;
        int exceptLastRowNum = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            CenterLayoutParams params = (CenterLayoutParams) child.getLayoutParams();
            int marginRight = 0, marginTop = 0, marginBottom = 0;
            if (params instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, measuredHeight);
                marginRight = params.rightMargin;
                marginTop = params.topMargin;
                marginBottom = params.bottomMargin;
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
            childWidth = child.getMeasuredWidth() + mChildSpacing + marginRight;
            childHeight = child.getMeasuredHeight() + mRowSpacing + marginBottom + marginTop;
            rowWidth += childWidth;
            maxWidth += Math.max(maxWidth, childWidth);
            if (measuredWidth + childWidth > rowLength) {
                tempIndex = i;
                rowWidth = rowWidth - childWidth - mChildSpacing - marginRight;
                itemLineWidth.add(rowWidth);
                rowWidth = childWidth;
                ++rowCount;
                measuredWidth = childWidth;
                maxHeight += childHeight;
                itemLineNum.add(childNumInRow);
                exceptLastRowNum += childNumInRow;
                childNumInRow = 1;
            } else {
                measuredWidth += childWidth;
                ++childNumInRow;
                maxHeight = Math.max(maxHeight, childHeight);
            }

        }

        int lastRowWidth = 0;
        int singleHorizalMargin = 0;
        for (int i = tempIndex; i < childCount; i++) {
            View child = getChildAt(i);
            int horizalMargin = 0;
            CenterLayoutParams params = (CenterLayoutParams) child.getLayoutParams();
            if (params instanceof MarginLayoutParams) {
                singleHorizalMargin = horizalMargin = params.rightMargin;
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
            lastRowWidth += child.getMeasuredWidth() + mChildSpacing + horizalMargin;
        }
        int lastChildCount = childCount - exceptLastRowNum;
        lastRowWidth -= mChildSpacing == 0 ? singleHorizalMargin : mChildSpacing;
        itemLineWidth.add(lastRowWidth);
        itemLineNum.add(lastChildCount);
        mRowTotalCount = rowCount + 1;
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth()) + getPaddingRight() + getPaddingLeft();
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight()) + getPaddingTop() + getPaddingBottom();
        measuredWidth=widthMode== MeasureSpec.EXACTLY?widthSize:maxWidth;
        measuredHeight=heightMode== MeasureSpec.EXACTLY?heightSize:maxHeight;

        setMeasuredDimension(resolveSize(measuredWidth, widthMeasureSpec),
                resolveSize(measuredHeight, heightMeasureSpec));
    }

    public void setChildSpacing(int childSpacing){
        mChildSpacing=childSpacing;
        requestLayout();
    }

    public void setRowSpacing(int rowSpacing){
        mRowSpacing=rowSpacing;
        requestLayout();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new CenterLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CenterLayoutParams(getContext(), attrs);
    }

    /**
     * @param p
     * @return
     */
    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof CenterLayoutParams;
    }

    public static class CenterLayoutParams extends MarginLayoutParams {

        public CenterLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public CenterLayoutParams(LayoutParams source) {
            super(source);
        }

        public CenterLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public CenterLayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
