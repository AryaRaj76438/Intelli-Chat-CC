package com.example.intelli_chat_cc.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class LoginPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> loginFragmentArray = new ArrayList<>();
    private final ArrayList<String> loginFragmentTitle = new ArrayList<>();

    public LoginPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return loginFragmentArray.get(position);
    }

    @Override
    public int getCount() {
        return loginFragmentArray.size();
    }

    public void addFragment(Fragment fragment, String title){
        loginFragmentArray.add(fragment);
        loginFragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return loginFragmentTitle.get(position);
    }
}
