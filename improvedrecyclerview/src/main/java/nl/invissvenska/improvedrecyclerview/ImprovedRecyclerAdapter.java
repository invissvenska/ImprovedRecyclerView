package nl.invissvenska.improvedrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ImprovedRecyclerAdapter<E> extends RecyclerView.Adapter<ImprovedViewHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_ITEM = 3;
    protected OnClickListener<E> listener;
    protected OnNextPageListener nextPageListener;
    private Context context;
    protected List<E> items;
    private Handler handler = new Handler(Looper.getMainLooper());
    protected boolean isCancelled = false;
    protected boolean isLoading = false;
    protected Queue<Collection<E>> pendingUpdates = new ArrayDeque<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private int nextPageOffset = 1;
    private View footerView;
    private View headerView;
    private RecyclerView.LayoutManager layoutManager;

    public ImprovedRecyclerAdapter(Context context, Collection<E> list) {
        this.context = context;
        this.items = new ArrayList<>(list);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @NonNull
    @Override
    public ImprovedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return onCreateHeaderViewHolder();
            case TYPE_FOOTER:
                return onCreateFooterViewHolder();
            default:
                return onCreateItemViewHolder(parent, viewType);
        }
    }

    protected ImprovedViewHolder onCreateHeaderViewHolder() {
        return new ImprovedHeaderFooterViewHolder(headerView);
    }

    protected ImprovedViewHolder onCreateFooterViewHolder() {
        return new ImprovedHeaderFooterViewHolder(footerView);
    }

    protected abstract ImprovedViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        int itemCount = items != null ? items.size() : 0;

        if (hasFooter()) {
            itemCount++;
        }

        if (hasHeader()) {
            itemCount++;
        }

        return itemCount;
    }

    @Override
    public void onBindViewHolder(@NonNull ImprovedViewHolder holder, int position) {
        onBindViewHolder(holder, position, Collections.emptyList());
    }

    @Override
    public void onBindViewHolder(@NonNull ImprovedViewHolder holder, int position, @NonNull List<Object> payloads) {
        //check what type of view our position is
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
            case TYPE_FOOTER:
                //Nothing, for now.
                break;
            default:
                position = calculateIndex(position, true);
                E item = items.get(position);
                holder.bind(item, position, payloads);

                if (nextPageListener != null && !isLoading && position >= getCollectionCount() - getNextPageOffset() && !isCancelled) {
                    isLoading = true;

                    // If RecyclerView is currently computing a layout, it's in a lockdown state and any
                    // attempt to update adapter contents will result in an exception. In these cases, we need to postpone the change
                    // using a Handler.
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            nextPageListener.onScrolledToNextPage();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        } else if (isFooter(position)) {
            return TYPE_FOOTER;
        }

        return getAdditionalItemViewType(position, calculateIndex(position, true));
    }

    public int getCollectionCount() {
        return items.size();
    }

    public interface OnClickListener<E> {
        void onClick(int index, E item);
    }

    public interface OnNextPageListener {
        void onScrolledToNextPage();
    }

    public void setOnClickListener(OnClickListener<E> listener) {
        this.listener = listener;
    }

    public void setOnNextPageListener(OnNextPageListener listener, int nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
        setOnNextPageListener(listener);
    }

    public void setOnNextPageListener(OnNextPageListener nextPageListener) {
        this.nextPageListener = nextPageListener;
    }

    public void cancel() {
        isCancelled = true;
    }

    public void reset() {
        isCancelled = false;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void addAll(Collection<E> collection) {
        int position = items.size();
        items.addAll(collection);
        notifyItemRangeInserted(calculateIndex(position, false), collection.size());
    }

    /**
     * Clears current items.
     */
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    /**
     * Update the current adapter state. If {@param callback} is provided, an updated data set is calculated with DiffUtil, otherwise
     * current data set is clear and {@param newItems} are added to the internal items collection.
     *
     * @param newItems Collection of new items, which are added to adapter.
     * @param callback DiffUtil callback, which is used to update the items.
     */
    public void update(Collection<E> newItems, @Nullable DiffUtil.Callback callback) {
        if (callback != null) {
            pendingUpdates.add(newItems);
            if (pendingUpdates.size() == 1) {
                updateData(newItems, callback);
            }
        } else {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }
    }

    /**
     * Calculates provided {@param callback} DiffResult by using DiffUtils.
     *
     * @param newItems Collection of new items, with which our current items collection is updated.
     * @param callback DiffUtil.Callback on which DiffResult is calculated.
     */
    private void updateData(final Collection<E> newItems, final DiffUtil.Callback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
                postDiffResults(newItems, diffResult, callback);
            }
        });
    }

    /**
     * Dispatched {@param diffResult} DiffResults to the adapter if adapter has not been cancelled. If there are any queued pending updates,
     * it will peek the latest new items collection and once again update the adapter content.
     *
     * @param newItems   Collection of new items, with which our current items collection is updated.
     * @param diffResult DiffUtil.DiffResult which was calculated for {@param callback}.
     * @param callback   DiffUtil.Callback on which DiffResult was calculated.
     */
    private void postDiffResults(final Collection<E> newItems, final DiffUtil.DiffResult diffResult, final DiffUtil.Callback callback) {
        if (!isCancelled) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pendingUpdates.remove();
                    diffResult.dispatchUpdatesTo(ImprovedRecyclerAdapter.this);
                    items.clear();
                    items.addAll(newItems);

                    if (pendingUpdates.size() > 0) {
                        updateData(pendingUpdates.peek(), callback);
                    }
                }
            });
        }
    }

    private int calculateIndex(int index, boolean isViewBinding) {
        if (isViewBinding) {
            index = index - (hasHeader() ? 1 : 0);
            if (index >= items.size()) {
                throw new IllegalStateException("Index is defined in wrong range!");
            } else {
                return index;
            }
        } else {
            index = index + (hasHeader() ? 1 : 0);
            return index;
        }
    }

    public boolean hasFooter() {
        return footerView != null;
    }

    public boolean hasHeader() {
        return headerView != null;
    }

    protected boolean isFooter(int position) {
        return hasFooter() && position == getItemCount() - 1;
    }

    protected boolean isHeader(int position) {
        return hasHeader() && position == 0;
    }

    public int getNextPageOffset() {
        return nextPageOffset;
    }

    public void setFooter(@LayoutRes int footerViewId) {
        setFooter(LayoutInflater.from(context).inflate(footerViewId, (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content), false));
    }

    public void setFooter(View footerView) {
        boolean hadFooterBefore = hasFooter();

        int position = getCollectionCount() + (hasHeader() ? 1 : 0);
        this.footerView = footerView;
        setDefaultLayoutParams(this.footerView);

        if (hadFooterBefore) {
            notifyItemChanged(position);
        } else {
            notifyItemInserted(position);
        }
    }

    public void setHeader(@LayoutRes int headerViewId) {
        setHeader(LayoutInflater.from(context).inflate(headerViewId, (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content), false));
    }

    public void setHeader(View headerView) {
        boolean hadHeaderBefore = hasHeader();

        this.headerView = headerView;
        setDefaultLayoutParams(this.headerView);

        if (hadHeaderBefore) {
            notifyItemChanged(0);
        } else {
            notifyItemInserted(0);
        }
    }

    protected int getAdditionalItemViewType(int adapterPosition, int itemPosition) {
        return TYPE_ITEM;
    }

    private void setDefaultLayoutParams(View view) {
        if (getLayoutManager() != null && getLayoutManager() instanceof LinearLayoutManager) {
            RecyclerView.LayoutParams layoutParams;

            if (((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(layoutParams);
            } else {
                layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                        RecyclerView.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(layoutParams);
            }
        }
    }

    public void setParallaxHeader(final View header, final RecyclerView view) {
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO complete this for a parallax effect
                Log.d("SCROLL", "" + view.computeVerticalScrollOffset());
            }
        });
    }
}
