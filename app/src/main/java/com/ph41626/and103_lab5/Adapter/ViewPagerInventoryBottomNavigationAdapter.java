package com.ph41626.and103_lab5.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ph41626.and103_lab5.Fragment.DistributorFragment;
import com.ph41626.and103_lab5.Fragment.FruitFragment;

public class ViewPagerInventoryBottomNavigationAdapter extends FragmentStateAdapter {

    public ViewPagerInventoryBottomNavigationAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FruitFragment();
            case 1: return new DistributorFragment();
            default:break;
        }
        return new FruitFragment();
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}
