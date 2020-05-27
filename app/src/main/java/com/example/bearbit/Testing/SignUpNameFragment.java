//package com.example.bearbit;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//public class SignUpNameFragment extends Fragment {
//    private static final String TAG = "SignUpNameFragment";
//
//    private Button nameNextButton;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.signup_name_fragment_layout, container, false);
//
//        nameNextButton = (Button) view.findViewById(R.id.signInNameNextButton);
//
//        nameNextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((SignUpName)getActivity()).setViewPager(1);
//            }
//        });
//
//        return view;
//
//
//
//    }
//}
