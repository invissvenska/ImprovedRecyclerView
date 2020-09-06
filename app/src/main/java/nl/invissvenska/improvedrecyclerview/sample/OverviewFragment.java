package nl.invissvenska.improvedrecyclerview.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class OverviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_simple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(OverviewFragment.this)
                        .navigate(R.id.action_OverviewFragment_to_SimpleFragment);
            }
        });

        view.findViewById(R.id.button_nextpage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(OverviewFragment.this)
                        .navigate(R.id.action_OverviewFragment_to_NextPageFragment);
            }
        });

        view.findViewById(R.id.button_emptyview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(OverviewFragment.this)
                        .navigate(R.id.action_OverviewFragment_to_EmptyViewFragment);
            }
        });

        view.findViewById(R.id.button_grid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(OverviewFragment.this)
                        .navigate(R.id.action_OverviewFragment_to_GridFragment);
            }
        });
    }
}