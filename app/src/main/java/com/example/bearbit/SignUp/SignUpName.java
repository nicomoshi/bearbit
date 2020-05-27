package com.example.bearbit.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bearbit.R;

public class SignUpName extends AppCompatActivity {

//    private SectionStatePagerAdapter mSectionStatePagerAdapter;
//    private CustomViewPager mViewPager;

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_name_fragment_layout);

        nextButton = findViewById(R.id.signInNameNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get name
                Intent intent = new Intent(SignUpName.this, SignUpEmail.class);
                SignUpName.this.startActivity(intent);
            }
        });

//        CREATE A NEW FRAGMENT  WITH VIEWPAGER
//        mSectionStatePagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());
//
//        mViewPager = findViewById(R.id.container);
//
//        mViewPager.setPagingEnabled(false);
//        //Setup the pager
//        setupViewPager(mViewPager);
//
//        //Set a new fragment
//        mViewPager.setCurrentItem(1);

    }

    // SETUP VIEWPAGER
//    private void setupViewPager(ViewPager viewPager) {
//        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new SignUpNameFragment(), "SignUpNameFragment");
//        adapter.addFragment(new SignUpEmailFragment(), "SignUpEmailFragment");
//        adapter.addFragment(new SignUpPasswordFragment(), "SignUpPasswordFragment");
//        adapter.addFragment(new SignUpAgeFragment(), "SignUpAgeFragment");
//
//        viewPager.setAdapter(adapter);
//
//
//    }
//
//    public void setViewPager(int fragmentNumber) {
//        mViewPager.setCurrentItem(fragmentNumber);
//    }
//      THIS TRIES TO GO THROUGH FRAGMENTS WHEN USING THE BACK BUTTON
//    @Override
//    public void onBackPressed() {
//        FragmentManager fm = getFragmentManager();
//        if (fm.getBackStackEntryCount() > 0) {
//            Log.i("MainActivity", "popping backstack");
//            fm.popBackStack();
//        } else {
//            Log.i("MainActivity", "nothing on backstack, calling super");
//            super.onBackPressed();
//        }
//    }
}
