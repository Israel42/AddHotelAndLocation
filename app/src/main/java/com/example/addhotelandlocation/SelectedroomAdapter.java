package com.example.addhotelandlocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectedroomAdapter extends RecyclerView.Adapter<SelectedroomAdapter.viewadapter> {
    List<RoomDetail> roomDetails;
    Context context;
    String hotelname;

    public SelectedroomAdapter(List<RoomDetail> roomDetails, Context context, String hotelname) {
        this.roomDetails = roomDetails;
        this.context = context;
        this.hotelname = hotelname;
    }

    @NonNull
    @Override
    public viewadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_select_layout,parent,false);
        return new viewadapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewadapter holder, final int position) {
        holder.roomtype.setText(roomDetails.get(position).getRoomtype());
     holder.price.setText(String.format("%sETB", String.valueOf(roomDetails.get(position).getRoomprice())));
        Picasso.get().load(roomDetails.get(position).getRoomimage()).fit().into(holder.hotelimage);
     String roomAvaliable=roomDetails.get(position).roomsize;
     if (roomAvaliable.equalsIgnoreCase("0")){
         holder.roomavalibilty.setText("NOT AVALIABLE AT THE MOMENT");
         holder.roomavalibilty.setTextColor(Color.RED);
     }
     if (roomAvaliable.equalsIgnoreCase("NOT AVALIABLE AT THE MOMENT")){
         holder.book.setClickable(false);
     }else{
         holder.book.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String roomtypename=holder.roomtype.getText().toString();
                 String roomprice=String.valueOf(roomDetails.get(position).getRoomprice());
                 Intent intent=new Intent(v.getContext(),FinalPayment.class);
                 intent.putExtra("hotelname",hotelname);
                 intent.putExtra("roomtype",roomtypename);
                 intent.putExtra("roomprice",roomprice);
                 v.getContext().startActivity(intent);
             }
         });

     }


    }

    @Override
    public int getItemCount() {
        return roomDetails.size();
    }

    public class viewadapter extends RecyclerView.ViewHolder {
        Button book;
        TextView roomtype,roomavalibilty,price;
        ImageView hotelimage;
        public viewadapter(@NonNull View itemView) {
            super(itemView);
            book=itemView.findViewById(R.id.Book1);
            roomtype=itemView.findViewById(R.id.selectedroom);
            roomavalibilty=itemView.findViewById(R.id.Avalabile);
            price=itemView.findViewById(R.id.priceofroom);
            hotelimage=itemView.findViewById(R.id.imageroom);
        }
    }
}
