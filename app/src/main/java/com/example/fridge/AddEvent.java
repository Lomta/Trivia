package com.example.fridge;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEvent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEvent newInstance(String param1, String param2) {
        AddEvent fragment = new AddEvent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText eventName = getView().findViewById(R.id.eventName);
        EditText eventDate = getView().findViewById(R.id.eventDate);
        EditText bandName = getView().findViewById(R.id.bandName);
//        String quizId = String.valueOf((eventDate.getText().toString() + eventName.getText().toString()).hashCode());
//        Events event = new Events(eventDate.getText().toString(), bandName.getText().toString(), quizId, eventName.getText().toString());
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        FirebaseAuth myAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = myAuth.getCurrentUser();
//        String path = "Events/" + user.getEmail().hashCode();
//        DatabaseReference ref = database.getReference(path);
//        ref.child(quizId).setValue(event);

        Button button = getView().findViewById(R.id.generateQuiz);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quizId = String.valueOf((eventDate.getText().toString() + eventName.getText().toString()).hashCode());
                //Events event = new Events(eventDate.getText().toString(), bandName.getText().toString(), quizId, eventName.getText().toString());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseAuth myAuth = FirebaseAuth.getInstance();
                FirebaseUser user = myAuth.getCurrentUser();
                String path = "Events/" + user.getEmail().hashCode();
                DatabaseReference ref = database.getReference(path);
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("EventName", eventName.getText().toString());
                hashMap.put("EventDate", eventDate.getText().toString());
                hashMap.put("BandName", bandName.getText().toString());
                hashMap.put("quizId", quizId);
                ref.child(quizId).setValue(hashMap);

                //ref.child(quizId).setValue( new Events(eventDate.getText().toString(), bandName.getText().toString(), quizId, eventName.getText().toString()));
                System.out.println(ref);
            }
        });

    }
}