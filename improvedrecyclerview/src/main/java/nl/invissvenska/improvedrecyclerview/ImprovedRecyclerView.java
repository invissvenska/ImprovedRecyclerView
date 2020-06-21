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

    public void setEmptyView(View emptyView) {
        setEmptyView(emptyView, false);
    }

    public void setEmptyView(View emptyView, boolean showIfAdapterNotSet) {
        this.emptyView = emptyView;
        this.showEmptyViewIfAdapterNotSet = showIfAdapterNotSet;
        checkIfEmpty();
    }

    public boolean isEmptyViewShown() {
        return emptyView != null && emptyView.getVisibility() == VISIBLE;
    }


}
