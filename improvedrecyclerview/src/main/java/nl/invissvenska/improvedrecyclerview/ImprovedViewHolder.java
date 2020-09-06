package nl.invissvenska.improvedrecyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class ImprovedViewHolder<E> extends RecyclerView.ViewHolder {

    public ImprovedViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract void bind(E item, int position, List<Object> payloads);
}
