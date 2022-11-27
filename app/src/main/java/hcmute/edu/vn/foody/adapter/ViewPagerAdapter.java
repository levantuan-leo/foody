package hcmute.edu.vn.foody.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import hcmute.edu.vn.foody.fragment.AccountFragment;
import hcmute.edu.vn.foody.fragment.FavouriteFragment;
import hcmute.edu.vn.foody.fragment.HomeFragment;
import hcmute.edu.vn.foody.fragment.InvoiceFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 1:
                return new FavouriteFragment();

            case 2:
                return new InvoiceFragment();

            case 3:
                return new AccountFragment();

            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
