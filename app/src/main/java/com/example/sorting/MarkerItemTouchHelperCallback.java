package com.example.sorting;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class MarkerItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private OnItemTouchListener listener;

    public MarkerItemTouchHelperCallback(OnItemTouchListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NotNull RecyclerView recyclerView, RecyclerView.@NotNull ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.START|ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return listener.moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
    }

    @Override
    public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
        listener.removeItem(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public interface OnItemTouchListener{
        boolean moveItem(int fromPosition, int toPosition);
        void removeItem(int position);
    }
}