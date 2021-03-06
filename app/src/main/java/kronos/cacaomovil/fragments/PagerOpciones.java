package kronos.cacaomovil.fragments;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
                ListadoBiblioteca tab1 = new ListadoBiblioteca();
                return tab1;
            case 1:
                ListadoApps tab2 = new ListadoApps();
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
