package nl.invissvenska.improvedrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ImprovedRecyclerView extends RecyclerView {

    private View emptyView;

    private boolean showEmptyViewIfAdapterNotSet = false;

    public ImprovedRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ImprovedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImprovedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (getAdapter() != null && getAdapter() instanceof ImprovedRecyclerAdapter
                && ((ImprovedRecyclerAdapter) getAdapter()).getLayoutManager() == null) {
            ((ImprovedRecyclerAdapter) getAdapter()).setLayoutManager(getLayoutManager());
        }
    }

    /**
     * Sets the RecyclerView's adapter and registers adapter data observer, which is used to update empty view's visibility and loading
     * state of the adapter.
     */
    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        if (adapter instanceof ImprovedRecyclerAdapter && ((ImprovedRecyclerAdapter) getAdapter()).getLayoutManager() == null) {
            ((ImprovedRecyclerAdapter) getAdapter()).setLayoutManager(getLayoutManager());
        }

        checkIfEmpty();
    }

    /**
     * Custom AdapterDataObserver observer, which has two functions:
     * 1. Update the empty view visibility depending on adapter's content.
     * 2. Update the loading state of adapter, depending on whether a change of adapter's content has been detected.
     */
    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            updateState();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateState();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateState();
        }

        private void updateState() {
            checkIfEmpty();
            ((ImprovedRecyclerAdapter) getAdapter()).setLoading(false);

        }
    };

    /**
     * Checks if the adapter collection is empty. If it is, hide the RecyclerView content and show the empty view.
     */
    private void checkIfEmpty() {
        if (getAdapter() == null && showEmptyViewIfAdapterNotSet) {
            emptyView.setVisibility(View.VISIBLE);
            setVisibility(View.GONE);
        } else if (getAdapter() != null) {
            final boolean emptyViewVisible = ((ImprovedRecyclerAdapter) getAdapter()).getCollectionCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    /**
     * Sets the empty view. RecyclerView can have only one empty view at the time.
     *
     * @param emptyView view which is used as RecyclerView's empty view.
     */
    public void setEmptyView(View emptyView) {
        setEmptyView(emptyView, false);
    }

    /**
     * Sets the empty view to the RecyclerView. {@param showIfAdapterNotSet} determines should we show the empty view if adapter is
     * still not set to the RecyclerView. Default value is set to false.
     *
     * @param emptyView           view which is used as RecyclerView's empty view.
     * @param showIfAdapterNotSet determines should we show empty view if adapter is still not set to the RecyclerView.
     */
    public void setEmptyView(View emptyView, boolean showIfAdapterNotSet) {
        this.emptyView = emptyView;
        this.showEmptyViewIfAdapterNotSet = showIfAdapterNotSet;
        checkIfEmpty();
    }

    /**
     * Checks whether the RecyclerView is showing empty view.
     *
     * @return true is empty view is shown, false otherwise.
     */
    public boolean isEmptyViewShown() {
        return emptyView != null && emptyView.getVisibility() == VISIBLE;
    }
}
