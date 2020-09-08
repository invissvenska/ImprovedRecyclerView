package nl.invissvenska.improvedrecyclerview.sample.diffutils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import nl.invissvenska.improvedrecyclerview.sample.models.Item;

public class ItemsDiffUtil extends DiffUtil.Callback {
    public static final String EXTRA_ITEM_DESCRIPTION = "EXTRA_ITEM_DESCRIPTION";

    private List<Item> oldList;
    private List<Item> newList;

    public ItemsDiffUtil(List<Item> oldList, List<Item> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ITEM_DESCRIPTION, newList.get(newItemPosition).getName());
        return bundle;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName());
    }
}
