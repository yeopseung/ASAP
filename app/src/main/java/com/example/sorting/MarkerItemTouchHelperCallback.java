package com.example.sorting;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MarkerItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private OnItemTouchListener listener;

    public MarkerItemTouchHelperCallback(OnItemTouchListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.START|ItemTouchHelper.END);
    }

    @Override
    public boolean onMove( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder target) {
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