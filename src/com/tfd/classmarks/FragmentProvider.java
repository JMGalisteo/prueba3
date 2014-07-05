package com.tfd.classmarks;

import android.support.v4.app.Fragment;

public interface FragmentProvider {

	public Fragment getFragmentForPosition(int position);
    public int getCount();
	
}
