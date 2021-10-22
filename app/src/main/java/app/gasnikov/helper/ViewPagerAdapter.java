package app.gasnikov.helper;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import app.gasnikov.helper.fragments.helper.fragments.Chats;
import app.gasnikov.helper.fragments.helper.fragments.UsersFragment;

public class ViewPagerAdapter extends FragmentStateAdapter{
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       if(position==0){
           return new Chats();
       }
       else
        return new UsersFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
