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
//public class SignUpEmailFragment extends Fragment {
//    private static final String TAG = "SignUpEmailFragment";
//
//    private Button emailNextButton;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.signup_email_fragment_layout, container, false);
//
//        emailNextButton = (Button) view.findViewById(R.id.signInEmailNextButton);
//
//        emailNextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((SignUpName)getActivity()).setViewPager(2);
//            }
//        });
//
//        return view;
//
//    }
//}
