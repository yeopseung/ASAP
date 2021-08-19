package com.example.sorting;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        Toast.makeText(mContext.getApplicationContext(), "fromPosition : "+Integer.toString(fromPosition)+" toPosition : "+ Integer.toString(toPosition),Toast.LENGTH_SHORT).show();
        AddressItem text = items.get(fromPosition); //클릭된 위치의 테이블이 text에 저장됨
//        int fromId = text.getId(); //fromId에 text의 id값 저장
//        int toId = items.get(toPosition).getId();


        mDBHelper.swapAddress(items,fromPosition,toPosition);// DB에서 swapping

        items= mDBHelper.getAddressList(); //새로 추가한 부분 : items에 DB가 지속적으로 반영되게 swap끝난 DB 대입

        notifyItemMoved(fromPosition,toPosition); // RecyclerView에 반영



        //Toast.makeText(mContext.getApplicationContext(),items.get(fromPosition).getAddress() + "   "+ items.get(toPosition).getAddress(),Toast.LENGTH_SHORT);


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
        TextView textView;
        private GestureDetector gestureDetector;

        public ViewHolder(View itemView){
            super(itemView);
            textView= itemView.findViewById(R.id.textView);

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
//                    int curPos = getAdapterPosition(); // 현재 클릭한 리스트 아이템 위치
//                    if(curPos!=RecyclerView.NO_POSITION) {
//                        AddressItem addressItem = items.get(curPos);
//                        int id = addressItem.getId();
//                        mDBHelper.deleteAddress(id);
//                        items.remove(curPos);
//                        notifyItemRemoved(curPos);
//                    }
                    removeItem(getAdapterPosition());

                }
            });

        }

        public void setItem(AddressItem item){
            textView.setText(item.getAddress());

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