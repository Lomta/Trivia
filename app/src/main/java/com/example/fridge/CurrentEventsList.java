package com.example.fridge;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridge.dummy.DummyContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class CurrentEventsList extends Fragment {
    List<Events> ITEMS;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CurrentEventsList() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CurrentEventsList newInstance(long columnCount) {
        CurrentEventsList fragment = new CurrentEventsList();
        Bundle args = new Bundle();
        args.putLong(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_events_list_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //
            ITEMS = new ArrayList<Events>();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            FirebaseAuth myAuth = FirebaseAuth.getInstance();
            FirebaseUser user = myAuth.getCurrentUser();
            String path = "Events/" + user.getEmail().hashCode();
            DatabaseReference ref = database.getReference(path);
            System.out.println(ref);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ITEMS = new ArrayList<Events>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Events musicEvents = dataSnapshot.getValue(Events.class);
                        ITEMS.add(musicEvents);
                    }
                    updateView(recyclerView, ITEMS);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(ITEMS));
            System.out.println(ITEMS);
        }
        return view;
    }

    public void updateView(RecyclerView recyclerView, List <Events> list) {
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(list));
    }
}