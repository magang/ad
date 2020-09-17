package com.chanlin.ad.fragment.home;

import android.content.Context;

import com.chanlin.ad.manager.QDDataManager;


/**
 * @author dustforest
 */
public class HomeCloudController extends HomeController {

    public HomeCloudController(Context context) {
        super(context);
    }

    @Override
    protected String getTitle() {
        return "信息";
    }

    @Override
    protected ItemAdapter getItemAdapter() {
        return new ItemAdapter(getContext(), QDDataManager.getInstance().getComponentsDescriptions());
    }
}
