package nl.invissvenska.improvedrecyclerview;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImprovedHeaderScrollListener<T extends ImprovedRecyclerAdapter> extends RecyclerView.OnScrollListener {
    private final String TAG = getClass().getSimpleName();
    private final int heightCollapsedItem = 0;
    private int heightExpandedItem;
    private int itemToResize;
    private boolean init = false;
    private LinearLayoutManager linearLayoutManager;
    private int dyAbs;
    private T adapter;

    public ImprovedHeaderScrollListener(T adapter, LinearLayoutManager linearLayoutManager) {
        this.adapter = adapter;
        this.linearLayoutManager = linearLayoutManager;
        heightExpandedItem = adapter.getHeight();
    }

    @Override
    public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        try {
            if (adapter.hasHeader() && linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                dyAbs = Math.abs(dy);
                itemToResize = dy > 0 ? 1 : 0;
                initFocusResize(recyclerView);
                calculateScrolledPosition(recyclerView);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Initially set the header view at the big size
     *
     * @param recyclerView
     */
    private void initFocusResize(RecyclerView recyclerView) {
        if (!init) {
            init = true;
            View view = recyclerView.getChildAt(0);
            view.getLayoutParams().height = heightExpandedItem;
            adapter.onItemInit(recyclerView.getChildViewHolder(view));
        }
    }

    private void calculateScrolledPosition(RecyclerView recyclerView) {
        final View view = recyclerView.getChildAt(0);
        if (view != null) {
            if ((recyclerView.getChildViewHolder(view) instanceof ImprovedHeaderFooterViewHolder)) {
                if (0 == itemToResize) {
                    onItemBigResize(view, recyclerView);
                } else {
                    onItemSmallResize(view, recyclerView);
                }
                view.requestLayout();
            }
        }
    }

    private void onItemSmallResize(View view, RecyclerView recyclerView) {
        if (view.getLayoutParams().height - dyAbs <= heightCollapsedItem) {
            view.getLayoutParams().height = heightCollapsedItem;
        } else if (view.getLayoutParams().height >= heightCollapsedItem) {
            view.getLayoutParams().height -= (dyAbs * 2);
        }
        adapter.onItemSmallResize(recyclerView.getChildViewHolder(view), itemToResize, dyAbs);
    }

    private void onItemBigResize(View view, RecyclerView recyclerView) {
        if (view.getLayoutParams().height + dyAbs >= heightExpandedItem) {
            view.getLayoutParams().height = heightExpandedItem;
        } else {
            view.getLayoutParams().height += (dyAbs * 2);
        }
        adapter.onItemBigResize(recyclerView.getChildViewHolder(view), itemToResize, dyAbs);
    }
}
