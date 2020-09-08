package nl.invissvenska.improvedrecyclerview.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import nl.invissvenska.improvedrecyclerview.ImprovedRecyclerView;
import nl.invissvenska.improvedrecyclerview.sample.adapters.SimpleAdapter;
import nl.invissvenska.improvedrecyclerview.sample.adapters.UpdateAdapter;
import nl.invissvenska.improvedrecyclerview.sample.diffutils.ItemsDiffUtil;
import nl.invissvenska.improvedrecyclerview.sample.models.Item;

public class UpdateFragment extends Fragment {

    private List<Item> items;

    private ImprovedRecyclerView recyclerView;
    private View emptyView;
    private UpdateAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Item> newList = new ArrayList<>();
                newList.add(new Item(1, "Car"));
                newList.add(new Item(2, UUID.randomUUID().toString()));
                newList.add(new Item(3, "Plane"));
                newList.add(new Item(4, "Bicycle"));

                adapter.update(newList, new ItemsDiffUtil(items, newList));
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<>();
        items.add(new Item(1, "Car"));
        items.add(new Item(2, "Plane"));
        items.add(new Item(3, "Train"));
        items.add(new Item(4, "Bicycle"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEmptyView(emptyView);

        adapter = new UpdateAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.addAll(items);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cancel();
    }
}