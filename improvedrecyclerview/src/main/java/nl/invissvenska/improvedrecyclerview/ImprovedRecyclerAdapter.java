package nl.invissvenska.improvedrecyclerview;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class ImprovedRecyclerAdapter<E> extends RecyclerView.Adapter<ImprovedViewHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_ITEM = 3;
    protected OnClickListener<E> listener;
    private Context context;
    protected List<E> items;
    protected boolean isLoading = false;
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

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
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
                break;
        }
    }

    public int getCollectionCount() {
        return items.size();
    }

    public interface OnClickListener<E> {
        void onClick(int index, E item);
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

    private int calculateIndex(int index, boolean isViewBinding) {
        if (isViewBinding) {
            if (index >= items.size()) {
                throw new IllegalStateException("Index is defined in wrong range!");
            } else {
                return index;
            }
        } else {
            return index;
        }
    }
}
