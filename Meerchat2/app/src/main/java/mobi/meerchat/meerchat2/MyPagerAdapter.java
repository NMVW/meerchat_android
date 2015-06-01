package mobi.meerchat.meerchat2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

//import android.widget.RelativeLayout;
//import android.R;



public class MyPagerAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;
    private CarouselActivity context;
    private FragmentManager fm;
    private float scale;


    //private RelativeLayout lLayout = null;

    public MyPagerAdapter(CarouselActivity context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        // make the first pager bigger than others
        if (position == CarouselActivity.FIRST_PAGE)
            scale = CarouselActivity.BIG_SCALE;
        else
            scale = CarouselActivity.SMALL_SCALE;

//    position = position % MainActivity.PAGES;
        return MyFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount()  {
//    if(MainActivity.fileList.size() <= 0) return 1;
//    if(MainActivity.fileList.size() > 100) return 2;
//    return MainActivity.fileList.size();
        return 100;
    }
//  public int getCount()
//  {
//    return MainActivity.PAGES * MainActivity.LOOPS;
//  }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {

//    lLayout = (RelativeLayout) findViewById(R.layout.main);

//if(position == 1) lLayout.setBackgroundColor(Color.parseColor("#000000"));
//if(position == 2) lLayout.setBackgroundColor(Color.parseColor("#0000ff"));


        if (positionOffset >= 0f && positionOffset <= 1f)
        {
            cur = getRootView(position);
            next = getRootView(position +1);

            cur.setScaleBoth(CarouselActivity.BIG_SCALE - CarouselActivity.DIFF_SCALE * positionOffset);
            next.setScaleBoth(CarouselActivity.SMALL_SCALE + CarouselActivity.DIFF_SCALE * positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    private MyLinearLayout getRootView(int position)
    {
        return (MyLinearLayout)
                fm.findFragmentByTag(this.getFragmentTag(position)).getView().findViewById(R.id.root);
    }

    private String getFragmentTag(int position)
    {
        return "android:switcher:" + context.pager.getId() + ":" + position;
    }
}
