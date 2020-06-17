package roadfriend.app.ui.add.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager,val mFragments:ArrayList<Fragment>,val titleList:ArrayList<String>) : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
    override fun getCount(): Int {
        return mFragments.size
    }
    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }
}