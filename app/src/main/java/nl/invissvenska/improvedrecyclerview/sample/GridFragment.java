package nl.invissvenska.improvedrecyclerview.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.invissvenska.improvedrecyclerview.ImprovedRecyclerView;
import nl.invissvenska.improvedrecyclerview.sample.adapters.SimpleAdapter;

public class GridFragment extends Fragment implements SimpleAdapter.OnClickListener<String> {

    private static final List<String> ITEMS = Collections.unmodifiableList(Arrays.asList(
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten",
            "Eleven",
            "Twelve",
            "Thirteen",
            "Fourteen",
            "Fifteen",
            "Sixteen",
            "Seventeen",
            "Eighteen",
            "Nineteen",
            "Twenty",
            "Twenty one",
            "Twenty two",
            "Twenty three",
            "Twenty four",
            "Twenty five",
            "Twenty six",
            "Twenty seven",
            "Twenty eight",
            "Twenty nine",
            "Thirty"
    ));

    private ImprovedRecyclerView recyclerView;
    private View emptyView;
    private SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)) {
                    case SimpleAdapter.TYPE_HEADER:
                    case SimpleAdapter.TYPE_FOOTER:
                        return layoutManager.getSpanCount();
                    case SimpleAdapter.TYPE_ITEM:
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(emptyView);

        adapter = new SimpleAdapter(getContext());
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.addAll(ITEMS);

        adapter.setHeader(R.layout.item_header);

        View footerView = getLayoutInflater().inflate(R.layout.item_footer, null);
        adapter.setFooter(footerView);
    }

    @Override
    public void onClick(int index, String item) {
        Toast.makeText(this.getContext(), item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cancel();
    }
}