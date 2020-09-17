package com.chanlin.ad.fragment.home;

import android.content.Context;

import com.chanlin.ad.manager.QDDataManager;

/**
 * @author dustforest
 */

public class HomeInfoController extends HomeController {

    public HomeInfoController(Context context) {
        super(context);
    }

    @Override
    protected String getTitle() {
        return "我";
    }

    @Override
    protected ItemAdapter getItemAdapter() {
        return new ItemAdapter(getContext(), QDDataManager.getInstance().getComponentsDescriptions());
    }
}
