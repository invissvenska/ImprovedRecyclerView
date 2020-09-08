package nl.invissvenska.improvedrecyclerview.sample;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.invissvenska.improvedrecyclerview.ImprovedRecyclerView;
import nl.invissvenska.improvedrecyclerview.sample.adapters.SimpleAdapter;

public class SimpleFragment extends Fragment implements SimpleAdapter.OnClickListener<String> {

    private static final List<String> ITEMS = Collections.unmodifiableList(Arrays.asList(
            "Apple",
            "Peach",
            "Banana",
            "Mango",
            "Strawberry",
            "Berry",
            "Carrot",
            "Basil",
            "Apricot",
            "Grape",
            "Almond",
            "Pear",
            "Raspberry",
            "Lemon",
            "Orange",
            "Sweet Pepper",
            "Pumpkin",
            "Olive",
            "Blueberry",
            "Parsley"
    ));

    private ImprovedRecyclerView recyclerView;
    private View emptyView;
    private SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEmptyView(emptyView);

        adapter = new SimpleAdapter(getContext());
        adapter.setOnClickListener(this);

        adapter.setHeader(R.layout.item_header);
        adapter.setFooter(R.layout.item_footer);

        recyclerView.setAdapter(adapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isRemoving()) {
                    adapter.addAll(ITEMS);
                }
            }
        }, 3000);
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