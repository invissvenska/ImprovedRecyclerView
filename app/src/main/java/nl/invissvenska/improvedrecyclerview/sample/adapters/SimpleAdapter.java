package nl.invissvenska.improvedrecyclerview.sample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import nl.invissvenska.improvedrecyclerview.ImprovedRecyclerAdapter;
import nl.invissvenska.improvedrecyclerview.ImprovedViewHolder;
import nl.invissvenska.improvedrecyclerview.sample.R;

public class SimpleAdapter extends ImprovedRecyclerAdapter<String> {

    public SimpleAdapter(Context context) {
        super(context, Collections.<String>emptyList());
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
    public class TestViewHolder extends ImprovedViewHolder<String> {
        TextView tvPosition;
        TextView tvText;
        View rootView;

        public TestViewHolder(View itemView) {
            super(itemView);

            tvPosition = itemView.findViewById(R.id.tv_position);
            tvText = itemView.findViewById(R.id.tv_text);
            rootView = itemView.findViewById(R.id.root_view);
        }

        @Override
        protected void bind(final String item, final int position, List<Object> payloads) {
            tvPosition.setText(String.valueOf(position).concat("."));
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
