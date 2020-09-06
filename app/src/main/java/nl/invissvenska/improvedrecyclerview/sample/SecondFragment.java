package nl.invissvenska.improvedrecyclerview.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.invissvenska.improvedrecyclerview.ImprovedRecyclerView;
import nl.invissvenska.improvedrecyclerview.sample.adapters.SimpleAdapter;

public class SecondFragment extends Fragment {

    private static final List<String> ITEMS = Collections.unmodifiableList(Arrays.asList(
            "One",
            "Two",
            "Three asdf asdfasd fasdfasdf asdfdasdf",
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
            "Twenty"
    ));

    ImprovedRecyclerView recyclerView;
    View emptyView;
    SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEmptyView(emptyView);

        adapter = new SimpleAdapter(getContext());

        adapter.setHeader(R.layout.item_header);
        adapter.setFooter(R.layout.item_footer);

        recyclerView.setAdapter(adapter);
        adapter.addAll(ITEMS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}