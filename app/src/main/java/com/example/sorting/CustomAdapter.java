package com.example.sorting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.skt.Tmap.TMapTapi;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements MarkerItemTouchHelperCallback.OnItemTouchListener
{

    private ArrayList<AddressItem> mAddressItems;
    private Context mContext;
    private DBHelper mDBHelper;

    public CustomAdapter(ArrayList<AddressItem> addressItems, Context mContext) {
        this.mAddressItems = addressItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_number.setText(mAddressItems.get(position).getNumber());
        holder.tv_address.setText(mAddressItems.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return mAddressItems.size();
    } //DB에 저장된 table 개수 리턴하는 메소드

    @Override
    public boolean moveItem(int fromPosition, int toPosition) { //리사이클러 뷰에서 터치한 부분을 이동시키는 메소드
        AddressItem text = mAddressItems.get(fromPosition);
        mAddressItems.remove(fromPosition);
        mAddressItems.add(toPosition, text);
        notifyItemMoved(fromPosition,toPosition);
        return true;
    }

    @Override
    public void removeItem(int position) { //리사이클러 뷰에서 터치한 부분을 삭제하는 메소드
        mAddressItems.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_number;
        private TextView tv_address;
        TMapTapi tMapTapi = new TMapTapi(mContext);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_number=itemView.findViewById(R.id.tv_number);
            tv_address=itemView.findViewById(R.id.tv_address);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int curPos = getAdapterPosition(); // 현재 클릭한 리스트 아이템 위치
                    AddressItem addressItem = mAddressItems.get(curPos);
                    String[] strChoiceItems = {"안내하기","삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);  // Dialog 생성
                    builder.setTitle("원하는 작업을 선택 해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position == 0){ // 안내하기를 눌렀을 경우 tMap 연동
                                tMapTapi.invokeNavigate("", (float)addressItem.getLongitude(), (float)addressItem.getLatitude(), 0, true);
                                // tMap 연동하기
                            }
                            else if(position == 1) { // 삭제하기를 눌렀을 경우 해당 위치 부분을 삭제하고 toast 메세지 출력
                                int id=addressItem.getId();
                                mDBHelper.deleteAddress(id);
                                mAddressItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext,"목록이 제거 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else {;}
                        }
                    });


                    builder.create();
                    builder.show();
                }
            });
        }
    }

    //액티비티에서 호출되는 함수이며, 현재 어댑터에 새로운 게시글 아이템을 전달받아 추가하는 목적이다.
    public void addItem(AddressItem _item){
        mAddressItems.add(_item);
    }
}