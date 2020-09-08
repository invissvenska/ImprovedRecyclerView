package nl.invissvenska.improvedrecyclerview.sample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import nl.invissvenska.improvedrecyclerview.ImprovedRecyclerAdapter;
import nl.invissvenska.improvedrecyclerview.ImprovedViewHolder;
import nl.invissvenska.improvedrecyclerview.sample.R;

public class SimpleAdapter extends ImprovedRecyclerAdapter<String> {

    public SimpleAdapter(Context context) {
        super(context, Collections.<String>emptyList());
    }

    public SimpleAdapter(Context context, int height) {
        super(context, Collections.<String>emptyList(), height);
    }

    @Override
    protected ImprovedViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImprovedViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
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

    public class TestViewHolder extends ImprovedViewHolder<String> {
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
        protected void bind(final String item, final int position, List<Object> payloads) {
            tvPosition.setText(String.valueOf(position));
            tvText.setText(item);

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
