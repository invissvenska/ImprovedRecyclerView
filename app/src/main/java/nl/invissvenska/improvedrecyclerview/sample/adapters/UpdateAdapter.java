package nl.invissvenska.improvedrecyclerview.sample.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import nl.invissvenska.improvedrecyclerview.ImprovedRecyclerAdapter;
import nl.invissvenska.improvedrecyclerview.ImprovedViewHolder;
import nl.invissvenska.improvedrecyclerview.sample.R;
import nl.invissvenska.improvedrecyclerview.sample.diffutils.ItemsDiffUtil;
import nl.invissvenska.improvedrecyclerview.sample.models.Item;

public class UpdateAdapter extends ImprovedRecyclerAdapter<Item> {

    public UpdateAdapter(Context context) {
        super(context, Collections.<Item>emptyList());
    }

    @Override
    protected ImprovedViewHolder<Item> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onItemBigResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {

    }

    @Override
    public void onItemSmallResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {

    }

    @Override
    public void onItemInit(RecyclerView.ViewHolder viewHolder) {

    }

    public class TestViewHolder extends ImprovedViewHolder<Item> {

        private TextView tvPosition;
        private TextView tvText;
        private View rootView;

        public TestViewHolder(View itemView) {
            super(itemView);

            tvPosition = itemView.findViewById(R.id.tv_position);
            tvText = itemView.findViewById(R.id.tv_text);
            rootView = itemView.findViewById(R.id.root_view);
        }

        @Override
        protected void bind(final Item item, final int position, List<Object> payloads) {
            if (payloads.isEmpty()) {
                tvPosition.setText(String.valueOf(item.getId()));
                tvText.setText(item.getName());
            } else {
                // only the different items will be updated
                Bundle bundle = (Bundle) payloads.get(0);
                tvText.setText(bundle.getString(ItemsDiffUtil.EXTRA_ITEM_DESCRIPTION));
            }

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(position, item);
                    }
                }
            });
        }
    }
}
