package nl.invissvenska.improvedrecyclerview.sample;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.invissvenska.improvedrecyclerview.ImprovedRecyclerView;
import nl.invissvenska.improvedrecyclerview.sample.adapters.SimpleAdapter;

public class NextPageFragment extends Fragment implements SimpleAdapter.OnClickListener<String>, SimpleAdapter.OnNextPageListener {

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
            "Twenty"
    ));

    private ImprovedRecyclerView recyclerView;
    private View emptyView;
    private SimpleAdapter adapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nextpage, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEmptyView(emptyView);

        adapter = new SimpleAdapter(getContext());
        adapter.setOnClickListener(this);

        recyclerView.setAdapter(adapter);
        adapter.addAll(ITEMS);
        adapter.setOnNextPageListener(this);
    }

    @Override
    public void onClick(int index, String item) {
        Toast.makeText(this.getContext(), item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrolledToNextPage() {
        // Simulate network call by showing progressbar and adding data to adapter with some delay.
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isRemoving()) {
                    adapter.addAll(ITEMS);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cancel();
    }


}
