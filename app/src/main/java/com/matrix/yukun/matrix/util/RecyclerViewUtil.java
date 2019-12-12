package com.matrix.yukun.matrix.util;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView工具类
 *
 */
public class RecyclerViewUtil {

    /**
     * 判断RecyclerView是否显示最后一条消息
     *
     * @param recyclerView
     *
     * @return
     */
    public static boolean isLastMessageVisible(RecyclerView recyclerView) {
        if (null == recyclerView || null == recyclerView.getAdapter()) {
            return false;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        return lastVisiblePosition >= itemCount - 2;
    }

    /**
     * 判断RecyclerView是否开启滚动到最后一条消息
     *
     * @param recyclerView
     *
     * @return
     */
    public static boolean isScrollToBottom(RecyclerView recyclerView) {
        if (null == recyclerView || null == recyclerView.getAdapter()) {
            return false;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        int itemCount = recyclerView.getAdapter().getItemCount();
//        LogUtil.i("111111111111111111111", "itemCount=" + itemCount + " ;lastVisiblePosition=" + lastVisiblePosition);
        if (itemCount - lastVisiblePosition < 5) {
            scrollToBottom(recyclerView);
            return true;
        }
        return false;
    }

    /**
     * 判断RecyclerView滚动到底部
     * @param recyclerView
     * @return
     */
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
            >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    /**
     * 设置RecyclerView滚动到底部
     *
     * @param recyclerView
     */
    public static void scrollToBottom(RecyclerView recyclerView) {
        if (null == recyclerView) {
            throw new IllegalArgumentException("RecyclerView can't be null");
        }
        int lastPosition = recyclerView.getAdapter().getItemCount() - 1;
        scrollToPosition(recyclerView, lastPosition);
    }

    /**
     * 设置RecyclerView滚动到指定的位置
     *
     * @param position
     */
    public static void scrollToPosition(final RecyclerView recyclerView, final int position) {
        if (null == recyclerView) {
            throw new IllegalArgumentException("RecyclerView can't be null");
        }
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(position);
            }
        });
    }

    /**
     * 设置RecyclerView平滑滚动到指定的位置
     *
     * @param position
     */
    public static void smoothScrollToPosition(final RecyclerView recyclerView, final int position) {
        if (null == recyclerView) {
            throw new IllegalArgumentException("RecyclerView can't be null");
        }
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    recyclerView.smoothScrollToPosition(position);
                }catch (IllegalArgumentException e){}
            }
        });
    }
}
