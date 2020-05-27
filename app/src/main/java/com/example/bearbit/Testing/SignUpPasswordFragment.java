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
//public class SignUpPasswordFragment extends Fragment {
//    private static final String TAG = "SignUpPasswordFragment";
//
//    private Button passwordNextButton;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.signup_password_fragment_layout, container, false);
//
//
//        passwordNextButton = (Button) view.findViewById(R.id.signInPasswordNextButton);
//
//        passwordNextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((SignUpName)getActivity()).setViewPager(3);
//            }
//        });
//
//        return view;
//
//    }
//}
