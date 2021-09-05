package com.example.sorting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.ViewHolder> implements MarkerItemTouchHelperCallback.OnItemTouchListener {

    ArrayList<AddressItem> items = new ArrayList<AddressItem>();
    private OnStartDragListener onStartDragListener;
    private DBHelper mDBHelper;
    private Context mContext;
    int temposition=0;



    public MarkerAdapter(ArrayList<AddressItem> addressItems, Context mContext, OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
        this.items = addressItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    public void addItem(AddressItem item){
        items.add(item);
    }

    public void setItems(ArrayList<AddressItem> items){
        this.items = items;
    }

    public AddressItem getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, AddressItem item){
        items.set(position,item);
    }

    //목록 이동
    @Override
    public boolean moveItem(int fromPosition, int toPosition){

        AddressItem text = items.get(fromPosition); //클릭된 위치의 테이블이 text에 저장됨

        mDBHelper.swapAddress(items,fromPosition,toPosition);// DB에서 swapping

        items= mDBHelper.getAddressList(); //새로 추가한 부분 : items에 DB가 지속적으로 반영되게 swap끝난 DB 대입

        notifyItemMoved(fromPosition,toPosition); // RecyclerView에 반영

       return true;
    }


    //목록 삭제
    @Override
    public void removeItem(int position){
        AddressItem addressItem = items.get(position);
        int id=addressItem.getId();
        mDBHelper.deleteAddress(id);
        items.remove(position);
        notifyItemRemoved(position);
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.marker_items,parent,false);


        return  new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AddressItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements GestureDetector.OnGestureListener{
        TextView tv_address;
        TextView tv_order;
        private GestureDetector gestureDetector;

        public ViewHolder(View itemView){
            super(itemView);
            tv_address= itemView.findViewById(R.id.tv_address);
            tv_order= itemView.findViewById(R.id.tv_order);
            gestureDetector = new GestureDetector(itemView.getContext(),this);
            itemView.findViewById(R.id.drag_handle).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return gestureDetector.onTouchEvent(event);

                }
            });
            itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("삭제하시겠습니까?");

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id) { }
                    });

                    builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            removeItem(getAdapterPosition());
                            Toast.makeText(mContext, "삭제 완료", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog alertDialog = builder.create();  //AlertDialog 팝업창 설정
                    alertDialog.show();

                }
            });

        }

        public void setItem(AddressItem item){
            tv_address.setText(item.getAddress());
            tv_order.setText(Integer.toString(items.indexOf(item)+1));
        }

        @Override
        public boolean onDown(MotionEvent e){
            onStartDragListener.onStartDrag(this);
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

    }
    public interface OnStartDragListener{
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }
}