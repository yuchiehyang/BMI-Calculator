package com.example.finalproject0623;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FamilyMemberFragment extends Fragment {
    private static final String TAG =  "chloe";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    //CHECKBOX: RECYCLERVIEW & FIRESTORE DATABASE
    private CollectionReference memberListRef = db.collection("FamilyMemberBook");
    private MemberListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"oncreate_family");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG,"oncreateview_family");
        return inflater.inflate(R.layout.fragment_family_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG,"onviewcreated_family");
        //ATTENTION : 以AGE大小進行排序
        Query  query = memberListRef.orderBy("age",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Member> options = new FirestoreRecyclerOptions.Builder<Member>()
                .setQuery(query,Member.class)
                .build();

        mAdapter = new MemberListAdapter(options);


        //CHECKBOX :RECYCLERVIEW
        RecyclerView recyclerView = view.findViewById(R.id.memberRecyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(mAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        //ATTENTION : 顯示ID(FIRESTORE DATABASE id)
        mAdapter.setOnItemClickListener(new MemberListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Toast.makeText(getContext(),"Position: "+position+"ID: "+id,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
        Log.v(TAG,"onStop");
    }
}