package com.example.finalproject0623;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MemberListAdapter extends FirestoreRecyclerAdapter<Member,MemberListAdapter.MemberListHolder> {

    //CHECKBOX: RECYCLERVIEW
    private OnItemClickListener listener;
    public MemberListAdapter(@NonNull  FirestoreRecyclerOptions<Member> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  MemberListAdapter.MemberListHolder holder, int position, @NonNull  Member model) {
        holder.tvFamilyName.setText(model.getMemberName());
        holder.tvAge.setText(String.valueOf(model.getAge()));
        holder.tvBMI.setText(String.valueOf(model.getBmi()));
    }

    @Override
    public MemberListHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item,parent,false);
        return new MemberListHolder(view);
    }

    //ATTENTION: swipe out data in recyclerView
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class MemberListHolder extends RecyclerView.ViewHolder{

        TextView tvFamilyName,tvAge,tvBMI;
        public MemberListHolder(@NonNull  View itemView) {
            super(itemView);

            tvFamilyName = itemView.findViewById(R.id.tv_memberName);
            tvAge = itemView.findViewById(R.id.tv_age);
            tvBMI = itemView.findViewById(R.id.tv_BMI);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener !=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
