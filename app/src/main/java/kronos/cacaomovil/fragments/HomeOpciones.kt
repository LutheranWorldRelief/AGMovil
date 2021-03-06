package kronos.cacaomovil.fragments

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import kronos.cacaomovil.R

class HomeOpciones : Fragment(), View.OnClickListener {
    internal var context: Activity? = null
    internal lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_opciones, container, false)
    }

    override fun onActivityCreated(state: Bundle?) {
        super.onActivityCreated(state)
        context = super.getActivity()
        inits()
    }

    fun finishListadoRutas() {
        context!!.finish()
    }

    private fun inits() {
        tabLayout = context!!.findViewById(R.id.tab_layout) as TabLayout
        viewPager = context!!.findViewById(R.id.view_pager) as ViewPager
        viewPager.setOffscreenPageLimit(3); //before setAdapter

        tabLayout.addTab(tabLayout.newTab().setText("BIBLIOTECA"))
        tabLayout.addTab(tabLayout.newTab().setText("APPS"))
        tabLayout.addTab(tabLayout.newTab().setText("AULA VIRTUAL"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = PagerOpciones(childFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        for (i in 0 until tabLayout.tabCount) {

            val tv = LayoutInflater.from(context).inflate(R.layout.items_tab, null) as TextView
            tv.setTextColor(Color.parseColor("#ffffff"))
            //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tabLayout.getTabAt(i)!!.customView = tv
        }


    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }
}
