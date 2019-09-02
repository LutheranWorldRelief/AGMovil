package com.cacaomovil.app.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerOpciones extends FragmentPagerAdapter {

    int numberOfTabs;

    public PagerOpciones(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListadoApps tab1 = new ListadoApps();
                return tab1;
            case 1:
                ListadoBiblioteca tab2 = new ListadoBiblioteca();
                return tab2;
            case 2:
                ListadoOtro tab3 = new ListadoOtro();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
