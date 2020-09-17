package com.chanlin.ad.config;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.chanlin.ad.R;
import com.chanlin.ad.data.Trade;
import com.chanlin.ad.data.TradeLab;
import com.chanlin.ad.data.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.leancloud.AVInstallation;
import cn.leancloud.types.AVGeoPoint;

public class PushConfig {
    private static final String TAG = "PushConfig";
    private static final String LOCATION_KEY = "location";
    private static final String STATUS_KEY = "status";
    private static final String USER_TAG_KEY = "userTag";
    private static final String USER_ADDRESS_KEY = "userAddress";
    private static final String USER_PHONE_KEY = "phone";
    private static final String LIKE_KEY = "like";
    private static final String FAVORITE_KEY = "favorite";
    private static final String GLOBAL_AD_IGNORE_KEY = "globalAdIgnore";
    private static final String LOCAL_AD_IGNORE_KEY = "localAdIgnore";
    private static final String KEYWORD_KEY = "keyword";
    private static final String PASSWORD_KEY = "password";
    private static final String FOLLOW_KEY = "follow";
    private static final String LIKE_COUNT_KEY = "likeCount";
    private static final String AWARD_COUNT_KEY = "awardCount";
    private static final String COMMENT_COUNT_KEY = "commentCount";
    private static final String RANGE_KEY = "range";
    private static final String SIGNATURE_KEY = "signature";
    private static final String IMAGE_QUALITY_KEY = "imageQuality";
    private static final String VERIFIED_PHONE_KEY = "verifiedPhone";
    private static final String VERIFIED_COUNT_KEY = "verifiedCount";
    private static final String HELP_READED_KEY = "helpReaded";
    private static final String PULL_TO_REFRESH_NOTICE_ENABLED_KEY = "pullToRefreshEnabled";
    private static final String PUSH_ENABLED_KEY = "pushEnabled";
    private static final String ROAM_ENABLED_KEY = "roamEnabled";
    private static final String SIGNATURE_ENABLED_KEY = "signatureEnabled";
    private static final String NO_IMAGE_MODE_KEY = "noImageMode";
    private static final String FILTER_ENABLED_KEY = "filterEnabled";
    private static final String FILTER_KEY = "filter";
    private static final String SEARCH_RECORD_ENABLED_KEY = "searchRecordEnabled";
    private static final String TRADE_TITLE_KEY = "tradeTitle";
    private static final String TRADE_DETAILS_KEY = "tradeDetails";
    private static final String TRADE_POSITION_KEY = "tradePosition";
    private static final String TRADE_TAG_KEY = "tradeTag";
    private static final String ITEM_ID_KEY = "itemId";
    private static final String ITEM_POSITION_KEY = "itemPosition";
    private static final String WELCOME_MSG_KEY = "welcomeMsg";
    private static final String LAST_LOGIN_DATE_KEY = "lastLoginDate";
    private static final String LAST_MSG_DATE_KEY = "lastMsgDate";
    private static final String LAST_MSG_TIME_KEY = "lastMsgTime";
    private static final String TODAY_MSG_COUNT_KEY = "todayMsgCount";
    private static final String LAST_COMMENT_DATE_KEY = "lastCommentDate";
    private static final String LAST_COMMENT_TIME_KEY = "lastCommentTime";
    private static final String TODAY_COMMENT_COUNT_KEY = "todayCommentCount";
    private static final String LAST_ACTION_DATE_KEY = "lastActionDate";
    private static final String LAST_ACTION_TIME_KEY = "lastActionTime";
    private static final String TODAY_ACTION_COUNT_KEY = "todayActionCount";
    private static final String LAST_LIKE_DATE_KEY = "lastLikeDate";
    private static final String LAST_LIKE_TIME_KEY = "lastLikeTime";
    private static final String TODAY_LIKE_COUNT_KEY = "todayLikeCount";
    private static final String LAST_REPORT_DATE_KEY = "lastReportDate";
    private static final String LAST_REPORT_TIME_KEY = "lastReportTime";
    private static final String TODAY_REPORT_COUNT_KEY = "todayReportCount";
    private static final String SHOP_HOME_PAGE_STATE_KEY = "shopHomePageState";
    private static final String SHOP_HOME_PAGE_COUNT_KEY = "shopHomePageCount";
    private static final String AD_PRICE_KEY = "adPrice";
    private static final String AD_STATUS_KEY = "adStatus";
    private static final String AD_FLAG_KEY = "adFlag";
    private static final String AD_TEXT_KEY = "adText";
    private static final String AD_PERIOD_KEY = "adPeriod";
    private static final String BAIDU_NAV_STATUS_KEY = "baiduNavStatus";
    private static final String SIDE_MENU_KEY = "sideMenu";
    private static final String VIP_MULTIPLIER_KEY = "vipMultiplier";
    private static final String COINS_PER_DAY_KEY = "coinsPerDay";
    private static final String TICKETS_PER_DAY_KEY = "ticketsPerDay";
    private static final String XJC_PER_DAY_KEY = "xjcPerDay";
    private static final String ORDER_TAG_KEY = "orderTag";
    private static final String SAVED_TAG_KEY = "savedTag";
    private static final String PUSH_DISTANCE_KEY = "pushDistance";
    private static final String LOCAL_AD_RANGE_KEY = "localAdRange";
    private static final String MIN_REPORT_LEVEL_KEY = "minReportLevel";
    private static PushConfig sPush;
    private static Context mAppContext;
    private boolean mIsRongYunConnected = false;
    private boolean mIsRefreshNeeded = true;
    private boolean mIsPushEnabled = false;
    private boolean mIsFilterEnabled = false;
    private boolean mIsWelcomeMsgSent = false;
    private boolean mIsRoamEnabled = false;
    private boolean mIsSignatureEnabled = false;
    private boolean mIsNoImageMode = false;
    private boolean mIsHelpReaded = false;
    private boolean mIsPullToRefreshNoticeEnabled = true;
    private boolean mIsSearchRecordEnabled = true;
    private boolean mIsSameAsLastTag = false;
    private boolean mTmpNavStatus = false;
    private String mTmpSignatureStatus = "null";
    private Trade mEditTrade = null;
    private String mUserTag = "";
    private String mLastUserTag = "";
    private String mUserAddress = "";
    private String mUserPhone = "";
    private String mTradeTitle = "";
    private String mTradeDetails = "";
    private String mTradePosition = "";
    private String mTradeTag = "";
    private String mItemId = "";
    private int mItemPosition = 0;
    private int mHomeItemPosition = 0;
    private String mLastItemId = "";
    private String mPeer = "";
    private String mTmpMsg = "";
    private String mFilter = "";
    private String mPassword = "";
    private String mVerifiedPhone = "";
    private String mSignature = "";
    private int mVerifiedCount = 0;
    private String mRange = "10000";
    private String mImageQuality = "90";
    private int mLikeLimit = 20;
    private int mLikeCount = 0;
    private int mAwardCount = 0;
    private int mCommentCount = 0;
    List<String> mLikes = new ArrayList<String>();
    List<String> mImageUrls = new ArrayList<String>();
    private String mLikesStr = "";
    private int mKeywordsLimit = 3;
    List<String> mKeywords = new ArrayList<String>();
    private String mKeywordsStr = "";
    private int mFollowLimit = 50;
    List<String> mFollows = new ArrayList<String>();
    private String mFollowsStr = "";
    private int mFavoriteLimit = 100;
    List<String> mFavorites = new ArrayList<String>();
    private String mFavoritesStr = "";
    private int mGlobalAdIgnoreLimit = 10;
    List<String> mGlobalAdIgnores = new ArrayList<String>();
    private String mGlobalAdIgnoresStr = "";
    private int mLocalAdIgnoreLimit = 10;
    List<String> mLocalAdIgnores = new ArrayList<String>();
    private String mLocalAdIgnoresStr = "";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String mLastLoginDate = "2000/01/01";
    private String mLastMsgTime = "2000/01/01 00:00:01";
    private String mLastMsgDate = "2000/01/01";
    private String mLastCommentTime = "2000/01/01 00:00:01";
    private String mLastCommentDate = "2000/01/01";
    private String mLastActionTime = "2000/01/01 00:00:01";
    private String mLastActionDate = "2000/01/01";
    private String mLastLikeTime = "2000/01/01 00:00:01";
    private String mLastLikeDate = "2000/01/01";
    private String mLastReportTime = "2000/01/01 00:00:01";
    private String mLastReportDate = "2000/01/01";
    private int mMaxVoteOneTime = 10;
    private int mTodayMsgCount = 0;
    private int mMaxMsgCountPerDay = 3;
    private int mMinMsgPeroid = 15;
    private int mMsgPeroid = 0;
    private int mTodayCommentCount = 0;
    private int mMaxCommentCountPerDay = 3;
    private int mMinCommentPeroid = 15;
    private int mCommentPeroid = 0;
    private int mTodayActionCount = 0;
    private int mMaxActionCountPerDay = 3;
    private int mMinActionPeroid = 15;
    private int mActionPeroid = 0;
    private int mTodayLikeCount = 0;
    private int mMaxLikeCountPerDay = 3;
    private int mMinLikePeroid = 15;
    private int mReportPeroid = 0;
    private int mTodayReportCount = 0;
    private int mMaxReportCountPerDay = 3;
    private int mMinReportPeroid = 15;
    private int mLikePeroid = 0;
    private int mMsgsPerScreen = 100;
    private int mImagePosition = 0;
    private int mMaxSearchResultsCount = 30;
    private int mWifiImageQuality = 60;
    private String mShopId = "";
    private String mShopTitle = "";
    private String mShopHomePageState = "off";
    private int mShopHomePageCount = 0;
    private String mSideMenu = "";
    private int mAdPrice = 200;
    private int mAdPeriod = 1;
    private int mVipMultiplier = 1;
    private int mXjcPerDay = 0;
    private int mCoinsPerDay = 1;
    private int mTicketsPerDay = 1;
    private int mPushDistance = 2;
    private int mLocalAdRange = 1;
    private int mMinReportLevel = 9;
    private String mPostsUserId;
    private String mCurrentSearchTag;
    private String mOrderTag = "";
    private String mSavedTag = "";
    private String mAdStatus = "off";
    private String mAdFlag = "off";
    private String mVoteMultiTimesFlag = "off";
    private String mAdText = "";
    private String mPicCompress = "off_600_800";
    private String mStatusPlaceholder = "发布消息...";
    private String mCommentPlaceholder = "发布评论...";
    private String mSearchPlaceholder = "搜索消息...";
    private String mIntroduction = "想在附近买东西？卖东西？上寻街就够了！APP下载链接： https://fir.im/xunjie";
    private String mBaiduNavStatus = "off";
    private String mSelfCertificatedShopStatus = "off";

    private PushConfig(Context appContext) {
        mAppContext = appContext;

        try {
            sp = mAppContext.getSharedPreferences("SP", Activity.MODE_PRIVATE);
            editor = sp.edit();

            mUserTag = sp.getString("USER_TAG_KEY", mUserTag);
            mOrderTag = sp.getString("ORDER_TAG_KEY", mOrderTag);
            mSavedTag = sp.getString("Saved_TAG_KEY", mSavedTag);
            mVerifiedPhone = sp.getString("VERIFIED_PHONE_KEY", mVerifiedPhone);
            mVerifiedCount = sp.getInt("VERIFIED_COUNT_KEY", mVerifiedCount);
            mLikeCount = sp.getInt("LIKE_COUNT_KEY", mLikeCount);
            mAwardCount = sp.getInt("AWARD_COUNT_KEY", mAwardCount);
            mAdPrice = sp.getInt("AD_PRICE_KEY", mAdPrice);
            mAdPeriod = sp.getInt("AD_PERIOD_KEY", mAdPeriod);
            mVipMultiplier = sp.getInt("AD_PRICE_KEY", mVipMultiplier);
            mCoinsPerDay = sp.getInt("COINS_PER_DAY_KEY", mCoinsPerDay);
            mXjcPerDay = sp.getInt("XJC_PER_DAY_KEY", mXjcPerDay);
            mTicketsPerDay = sp.getInt("TICKETS_PER_DAY_KEY", mTicketsPerDay);
            mShopHomePageCount = sp.getInt("SHOP_HOME_PAGE_COUNT_KEY", mShopHomePageCount);
            mCommentCount = sp.getInt("COMMENT_COUNT_KEY", mCommentCount);
            mPushDistance = sp.getInt("PUSH_DISTANCE_KEY", mPushDistance);
            mLocalAdRange = sp.getInt("LOCAL_AD_RANGE_KEY", mLocalAdRange);
            mMinReportLevel = sp.getInt("MIN_REPORT_LEVEL_KEY", mMinReportLevel);
            mUserAddress = sp.getString("USER_ADDRESS_KEY", mUserAddress);
            mUserPhone = sp.getString("USER_PHONE_KEY", mUserPhone);
            mTradeTitle = sp.getString("TRADE_TITLE_KEY", mTradeTitle);
            mTradeDetails = sp.getString("TRADE_DETAILS_KEY", mTradeDetails);
            mTradePosition = sp.getString("TRADE_POSITION_KEY", mTradePosition);
            mTradeTag = sp.getString("TRADE_TAG_KEY", mTradeTag);
            mShopHomePageState = sp.getString("SHOP_HOME_PAGE_STATE_KEY", mShopHomePageState);
            mRange = sp.getString("RANGE_KEY", mRange);
            mSignature = sp.getString("SIGNATURE_KEY", mSignature);
            mLastLoginDate = sp.getString("LAST_LOGIN_DATE_KEY", mLastLoginDate);
            mLastMsgDate = sp.getString("LAST_MSG_DATE_KEY", mLastMsgDate);
            mLastMsgTime = sp.getString("LAST_MSG_TIME_KEY", mLastMsgTime);
            mTodayMsgCount = sp.getInt("TODAY_MSG_COUNT_KEY", mTodayMsgCount);
            mLastCommentDate = sp.getString("LAST_COMMENT_DATE_KEY", mLastCommentDate);
            mLastCommentTime = sp.getString("LAST_COMMENT_TIME_KEY", mLastCommentTime);
            mTodayCommentCount = sp.getInt("TODAY_COMMENT_COUNT_KEY", mTodayCommentCount);
            mLastActionDate = sp.getString("LAST_ACTION_DATE_KEY", mLastActionDate);
            mLastActionTime = sp.getString("LAST_ACTION_TIME_KEY", mLastActionTime);
            mTodayActionCount = sp.getInt("TODAY_ACTION_COUNT_KEY", mTodayActionCount);
            mLastLikeDate = sp.getString("LAST_LIKE_DATE_KEY", mLastLikeDate);
            mLastLikeTime = sp.getString("LAST_LIKE_TIME_KEY", mLastLikeTime);
            mTodayLikeCount = sp.getInt("TODAY_LIKE_COUNT_KEY", mTodayLikeCount);
            mLastReportDate = sp.getString("LAST_REPORT_DATE_KEY", mLastReportDate);
            mLastReportTime = sp.getString("LAST_REPORT_TIME_KEY", mLastReportTime);
            mTodayReportCount = sp.getInt("TODAY_REPORT_COUNT_KEY", mTodayReportCount);
            mFilter = sp.getString("FILTER_KEY", mFilter);
            mPassword = sp.getString("PASSWORD_KEY", mPassword);
            mImageQuality = sp.getString("IMAGE_QUALITY_KEY", mImageQuality);
            mIsWelcomeMsgSent = sp.getBoolean("WELCOME_MSG_KEY", mIsWelcomeMsgSent);
            mIsHelpReaded = sp.getBoolean("HELP_READED_KEY", mIsHelpReaded);
            mIsPullToRefreshNoticeEnabled = sp.getBoolean("PULL_TO_REFRESH_NOTICE_ENABLED_KEY", mIsPullToRefreshNoticeEnabled);
            mIsPushEnabled = sp.getBoolean("PUSH_ENABLED_KEY", mIsPushEnabled);
            mIsNoImageMode = sp.getBoolean("NO_IMAGE_MODE_KEY", mIsNoImageMode);
            mIsFilterEnabled = sp.getBoolean("FILTER_ENABLED_KEY", mIsFilterEnabled);
            mIsRoamEnabled = sp.getBoolean("ROAM_ENABLED_KEY", mIsRoamEnabled);
            mIsSignatureEnabled = sp.getBoolean("SIGNATURE_ENABLED_KEY", mIsSignatureEnabled);
            mIsSearchRecordEnabled = sp.getBoolean("SEARCH_RECORD_ENABLED_KEY", mIsSearchRecordEnabled);
            mLikesStr = sp.getString("LIKE_KEY", mLikesStr);
            mFavoritesStr = sp.getString("FAVORITE_KEY", mFavoritesStr);
            mGlobalAdIgnoresStr = sp.getString("GLOBAL_AD_IGNORE_KEY", mGlobalAdIgnoresStr);
            mLocalAdIgnoresStr = sp.getString("LOCAL_AD_IGNORE_KEY", mLocalAdIgnoresStr);
            // convertLikesToList();
            mKeywordsStr = sp.getString("KEYWORD_KEY", mKeywordsStr);
            // convertKeywordsToList();
            mFollowsStr = sp.getString("FOLLOW_KEY", mFollowsStr);
            // convertFollowsToList();
        } catch (Exception exception) {
            Log.e(TAG, "getSharedPreferences failed: " + exception);
        }
    }

    public static PushConfig get(Context c) {
        if (sPush == null) {
            sPush = new PushConfig(c.getApplicationContext());
        }
        return sPush;
    }

    public void convertData() {
        convertLikesToList();
        convertKeywordsToList();
        convertFollowsToList();
        convertFavoritesToList();
        convertGlobalAdIgnoresToList();
        convertLocalAdIgnoresToList();
    }

    public void setCurrentInstallationLocation(double latitude, double longitude) {
        AVGeoPoint point = new AVGeoPoint(latitude, longitude);

        AVInstallation installation = AVInstallation.getCurrentInstallation();
        installation.put(LOCATION_KEY, point);
        installation.saveInBackground();
//        installation.saveInBackground(new SaveCallback() {
//            public void done(AVException e) {
//                if (e == null) {
//                    // String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
//                    Log.d(TAG, "setCurrentInstallationLocation successfully.");
//                } else {
//                    Log.e(TAG, "setCurrentInstallationLocation failed:" + e.getMessage());
//                }
//            }
//        });
    }

    public void setNullInstallationLocation() {
        AVGeoPoint point = new AVGeoPoint(0.0, 0.0);

        AVInstallation installation = AVInstallation.getCurrentInstallation();
        installation.put(LOCATION_KEY, point);
        installation.saveInBackground();
//        installation.saveInBackground(new SaveCallback() {
//            public void done(AVException e) {
//                if (e == null) {
//                    // String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
//                    Log.d(TAG, "setNullInstallationLocation successfully.");
//                } else {
//                    Log.e(TAG, "setNullInstallationLocation failed:" + e.getMessage());
//                }
//            }
//        });
    }

    public void convertGlobalAdIgnoresToString() {
        mGlobalAdIgnoresStr = "";

        if (mGlobalAdIgnores != null && mGlobalAdIgnores.size() > 0) {
            for (String a: mGlobalAdIgnores) {
                mGlobalAdIgnoresStr += a + " ";
            }

            mGlobalAdIgnoresStr = mGlobalAdIgnoresStr.trim();
            Log.d(TAG, "convertGlobalAdIgnoresToString: " + "<" + mGlobalAdIgnoresStr + ">");
        }
    }

    public void convertGlobalAdIgnoresToList() {
        String[] aa = mGlobalAdIgnoresStr.split(" ");

        for (int i = 0 ; i < aa.length ; i++ ) {
            if (!aa[i].trim().equals("")) {
                Log.d(TAG, "convertGlobalAdIgnoresToList: " + "<" + aa[i].trim() + ">");
                addGlobalAdIgnores(aa[i].trim());
            }
        }
    }

    public boolean addGlobalAdIgnores(String s) {
        boolean added = false;

        if (mGlobalAdIgnores != null && mGlobalAdIgnores.size() > 0) {
            boolean unique = true;

            for (String a: mGlobalAdIgnores) {
                if(a.equals(s)) {
                  unique = false;
                  break;
                }
            }

            if (unique) {
                List<String> tmp = new ArrayList<String>();
                tmp.add(s);
                tmp.addAll(mGlobalAdIgnores);
                mGlobalAdIgnores = tmp;
                // mGlobalAdIgnores.add(s);
                added = true;
            }
        } else {
            mGlobalAdIgnores.add(s);
            added = true;
        }

        if (mGlobalAdIgnores.size() > mGlobalAdIgnoreLimit) {
            mGlobalAdIgnores = mGlobalAdIgnores.subList(0, mGlobalAdIgnoreLimit);
        }

        try {
            convertGlobalAdIgnoresToString();
            editor.putString("GLOBAL_AD_IGNORE_KEY", mGlobalAdIgnoresStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit GLOBAL_AD_IGNORE_KEY failed: " + exception);
        }

        return added;
    }

    public int getGlobalAdIgnoresCount() {
        return mGlobalAdIgnores.size();
    }

    public void delGlobalAdIgnores(String s) {
        mGlobalAdIgnores.remove(s);

        try {
            convertGlobalAdIgnoresToString();
            editor.putString("GLOBAL_AD_IGNORE_KEY", mGlobalAdIgnoresStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit GLOBAL_AD_IGNORE_KEY failed: " + exception);
        }

    }

    public List<String> getGlobalAdIgnores() {
        return mGlobalAdIgnores;
    }

    public boolean isGlobalAdIgnored(String s) {
        return mGlobalAdIgnores.contains(s);
    }

    public void convertLocalAdIgnoresToString() {
        mLocalAdIgnoresStr = "";

        if (mLocalAdIgnores != null && mLocalAdIgnores.size() > 0) {
            for (String a: mLocalAdIgnores) {
                mLocalAdIgnoresStr += a + " ";
            }

            mLocalAdIgnoresStr = mLocalAdIgnoresStr.trim();
            Log.d(TAG, "convertLocalAdIgnoresToString: " + "<" + mLocalAdIgnoresStr + ">");
        }
    }

    public void convertLocalAdIgnoresToList() {
        String[] aa = mLocalAdIgnoresStr.split(" ");

        for (int i = 0 ; i < aa.length ; i++ ) {
            if (!aa[i].trim().equals("")) {
                Log.d(TAG, "convertLocalAdIgnoresToList: " + "<" + aa[i].trim() + ">");
                addLocalAdIgnores(aa[i].trim());
            }
        }
    }

    public boolean addLocalAdIgnores(String s) {
        boolean added = false;

        if (mLocalAdIgnores != null && mLocalAdIgnores.size() > 0) {
            boolean unique = true;

            for (String a: mLocalAdIgnores) {
                if(a.equals(s)) {
                  unique = false;
                  break;
                }
            }

            if (unique) {
                List<String> tmp = new ArrayList<String>();
                tmp.add(s);
                tmp.addAll(mLocalAdIgnores);
                mLocalAdIgnores = tmp;
                // mLocalAdIgnores.add(s);
                added = true;
            }
        } else {
            mLocalAdIgnores.add(s);
            added = true;
        }

        if (mLocalAdIgnores.size() > mLocalAdIgnoreLimit) {
            mLocalAdIgnores = mLocalAdIgnores.subList(0, mLocalAdIgnoreLimit);
        }

        try {
            convertLocalAdIgnoresToString();
            editor.putString("LOCAL_AD_IGNORE_KEY", mLocalAdIgnoresStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LOCAL_AD_IGNORE_KEY failed: " + exception);
        }

        return added;
    }

    public int getLocalAdIgnoresCount() {
        return mLocalAdIgnores.size();
    }

    public void delLocalAdIgnores(String s) {
        mLocalAdIgnores.remove(s);

        try {
            convertLocalAdIgnoresToString();
            editor.putString("LOCAL_AD_IGNORE_KEY", mLocalAdIgnoresStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LOCAL_AD_IGNORE_KEY failed: " + exception);
        }

    }

    public List<String> getLocalAdIgnores() {
        return mLocalAdIgnores;
    }

    public boolean isLocalAdIgnored(String s) {
        return mLocalAdIgnores.contains(s);
    }

    public void convertFavoritesToString() {
        mFavoritesStr = "";

        if (mFavorites != null && mFavorites.size() > 0) {
            for (String a: mFavorites) {
                mFavoritesStr += a + " ";
            }

            mFavoritesStr = mFavoritesStr.trim();
            Log.d(TAG, "convertFavoritesToString: " + "<" + mFavoritesStr + ">");
        }
    }

    public void convertFavoritesToList() {
        String[] aa = mFavoritesStr.split(" ");

        for (int i = 0 ; i < aa.length ; i++ ) {
            if (!aa[i].trim().equals("")) {
                Log.d(TAG, "convertFavoritesToList: " + "<" + aa[i].trim() + ">");
                addFavorites(aa[i].trim());
            }
        }
    }

    public boolean addFavorites(String s) {
        boolean added = false;

        if (mFavorites != null && mFavorites.size() > 0) {
            boolean unique = true;

            for (String a: mFavorites) {
                if(a.equals(s)) {
                  unique = false;
                  break;
                }
            }

            if (unique) {
                List<String> tmp = new ArrayList<String>();
                tmp.add(s);
                tmp.addAll(mFavorites);
                mFavorites = tmp;
                // mFavorites.add(s);
                added = true;
            }
        } else {
            mFavorites.add(s);
            added = true;
        }

        if (mFavorites.size() > mFavoriteLimit) {
            mFavorites = mFavorites.subList(0, mFavoriteLimit);
        }

        try {
            convertFavoritesToString();
            editor.putString("FAVORITE_KEY", mFavoritesStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit FAVORITE_KEY failed: " + exception);
        }

        return added;
    }

    public int getFavoritesCount() {
        return mFavorites.size();
    }

    public void delFavorites(String s) {
        mFavorites.remove(s);

        try {
            convertFavoritesToString();
            editor.putString("FAVORITE_KEY", mFavoritesStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit FAVORITE_KEY failed: " + exception);
        }

    }

    public List<String> getFavorites() {
        return mFavorites;
    }

    public boolean isFavorited(String s) {
        return mFavorites.contains(s);
    }

    public void convertLikesToString() {
        mLikesStr = "";

        if (mLikes != null && mLikes.size() > 0) {
            for (String a: mLikes) {
                mLikesStr += a + " ";
            }

            mLikesStr = mLikesStr.trim();
            Log.d(TAG, "convertLikesToString: " + "<" + mLikesStr + ">");
        }
    }

    public void convertLikesToList() {
        String[] aa = mLikesStr.split(" ");

        for (int i = 0 ; i < aa.length ; i++ ) {
            if (!aa[i].trim().equals("")) {
                Log.d(TAG, "convertLikesToList: " + "<" + aa[i].trim() + ">");
                addLikes(aa[i].trim());
            }
        }
    }

    public boolean addLikes(String s) {
        boolean added = false;

        if (mLikes != null && mLikes.size() > 0) {
            boolean unique = true;

            for (String a: mLikes) {
                if(a.equals(s)) {
                  unique = false;
                  break;
                }
            }

            if (unique) {
                List<String> tmp = new ArrayList<String>();
                tmp.add(s);
                tmp.addAll(mLikes);
                mLikes = tmp;
                // mLikes.add(s);
                added = true;
            }
        } else {
            mLikes.add(s);
            added = true;
        }

        if (mLikes.size() > mLikeLimit) {
            mLikes = mLikes.subList(0, mLikeLimit);
        }

        try {
            convertLikesToString();
            editor.putString("LIKE_KEY", mLikesStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LIKE_KEY failed: " + exception);
        }

        return added;
    }

    public int getLikesCount() {
        return mLikes.size();
    }

    public void delLikes(String s) {
        mLikes.remove(s);

        try {
            convertLikesToString();
            editor.putString("LIKE_KEY", mLikesStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LIKE_KEY failed: " + exception);
        }

    }

    public List<String> getLikes() {
        return mLikes;
    }

    public boolean isLiked(String s) {
        return mLikes.contains(s);
    }

    public void convertKeywordsToString() {
        mKeywordsStr = "";

        if (mKeywords != null && mKeywords.size() > 0) {
            for (String a: mKeywords) {
                mKeywordsStr += a + ",";
            }

            mKeywordsStr = mKeywordsStr.trim();
            mKeywordsStr = mKeywordsStr.substring(0, mKeywordsStr.length()-1);
            Log.d(TAG, "convertKeywordsToString: " + "<" + mKeywordsStr + ">");
        }
    }

    public void convertKeywordsToList() {
        String[] aa = mKeywordsStr.split(",");

        for (int i = 0 ; i < aa.length ; i++ ) {
            if (!aa[i].trim().equals("")) {
                Log.d(TAG, "convertKeywordsToList: " + "<" + aa[i].trim() + ">");
                addKeywords(aa[i].trim());
            }
        }
    }

    public boolean addKeywords(String s) {

        if (!mIsSearchRecordEnabled) {
            return false;
        }

        boolean added = false;

        if (mKeywords != null && mKeywords.size() > 0) {
            boolean unique = true;

            for (String a: mKeywords) {
                if(a.equals(s)) {
                  unique = false;
                  break;
                }
            }

            if (unique) {
                added = true;
            } else {
                mKeywords.remove(s);
            }

            List<String> tmp = new ArrayList<String>();
            tmp.add(s);
            tmp.addAll(mKeywords);
            mKeywords = tmp;
        } else {
            mKeywords.add(s);
            added = true;
        }

        if (mKeywords.size() > mKeywordsLimit) {
            mKeywords = mKeywords.subList(0, mKeywordsLimit);
        }

        try {
            convertKeywordsToString();
            editor.putString("KEYWORD_KEY", mKeywordsStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit KEYWORD_KEY failed: " + exception);
        }

        return added;
    }

    public void clearKeywords() {
        mKeywords.clear();

        try {
            convertKeywordsToString();
            editor.putString("KEYWORD_KEY", mKeywordsStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit KEYWORD_KEY failed: " + exception);
        }
    }

    public int getKeywordsCount() {
        return mKeywords.size();
    }

    public void delKeywords(String s) {
        mKeywords.remove(s);

        try {
            convertKeywordsToString();
            editor.putString("KEYWORD_KEY", mKeywordsStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit KEYWORD_KEY failed: " + exception);
        }

    }

    public List<String> getKeywords() {
        if (mKeywords == null) {
            return Collections.emptyList();
        } else {
            return mKeywords;
        }
    }

    public boolean isKeyworded(String s) {
        return mKeywords.contains(s);
    }

    public void convertFollowsToString() {
        mFollowsStr = "";

        if (mFollows != null && mFollows.size() > 0) {
            for (String a: mFollows) {
                mFollowsStr += a + " ";
            }

            mFollowsStr = mFollowsStr.trim();
            Log.d(TAG, "convertFollowsToString: " + "<" + mFollowsStr + ">");
        }
    }

    public void convertFollowsToList() {
        String[] aa = mFollowsStr.split(" ");

        for (int i = 0 ; i < aa.length ; i++ ) {
            if (!aa[i].trim().equals("")) {
                Log.d(TAG, "convertFollowsToList: " + "<" + aa[i].trim() + ">");
                addFollows(aa[i].trim());
            }
        }
    }

    public boolean addFollows(String s) {
        boolean added = false;

        if (mFollows != null && mFollows.size() > 0) {
            boolean unique = true;

            for (String a: mFollows) {
                if(a.equals(s)) {
                  unique = false;
                  break;
                }
            }

            if (unique) {
                // mFollows.add(s);
                List<String> tmp = new ArrayList<String>();
                tmp.add(s);
                tmp.addAll(mFollows);
                mFollows = tmp;
                added = true;
            }
        } else {
            mFollows.add(s);
            added = true;
        }

        if (mFollows.size() > mFollowLimit) {
            mFollows = mFollows.subList(0, mFollowLimit);
        }

        try {
            convertFollowsToString();
            editor.putString("FOLLOW_KEY", mFollowsStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit FOLLOW_KEY failed: " + exception);
        }

        return added;
    }

    public int getFollowsCount() {
        return mFollows.size();
    }

    public void delFollows(String s) {
        mFollows.remove(s);

        try {
            convertFollowsToString();
            editor.putString("FOLLOW_KEY", mFollowsStr);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit FOLLOW_KEY failed: " + exception);
        }

    }

    public List<String> getFollows() {
        return mFollows;
    }

    public boolean isFollowed(String s) {
        return mFollows.contains(s);
    }

    public boolean isWelcomeMsgSent() {
        return mIsWelcomeMsgSent;
    }

    public void setWelcomeMsgSent(boolean status) {
        mIsWelcomeMsgSent = status;

        try {
            editor.putBoolean("WELCOME_MSG_KEY", mIsWelcomeMsgSent);
            editor.commit();
            Log.d(TAG, "editor.commit WELCOME_MSG_KEY succeed: " + String.valueOf(mIsWelcomeMsgSent));
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit WELCOME_MSG_KEY failed: " + exception);
        }
    }

    public boolean isRoamEnabled() {
        return mIsRoamEnabled;
    }

    public void setRoamEnabled(boolean status) {
        mIsRoamEnabled = status;
        TradeLab.get(mAppContext).resetTrades();

        try {
            editor.putBoolean("ROAM_ENABLED_KEY", mIsRoamEnabled);
            editor.commit();
            Log.d(TAG, "editor.commit ROAM_ENABLED_KEY succeed: " + String.valueOf(mIsRoamEnabled));
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit ROAM_ENABLED_KEY failed: " + exception);
        }
    }

    public boolean isAdEnabled() {
        if (mAdStatus.equals("off") || mAdStatus.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public String getAdStatus() {
        return mAdStatus;
    }

    public void setAdStatus(String status) {
        mAdStatus = status;
    }

    public boolean isAdFlagEnabled() {
        if (mAdFlag.equals("off") || mAdFlag.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isVoteMultiTimesEnabled() {
        if (mVoteMultiTimesFlag.equals("off") || mVoteMultiTimesFlag.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public String getVoteMultiTimesFlag() {
        return mVoteMultiTimesFlag;
    }

    public void setVoteMultiTimesFlag(String flag) {
        mVoteMultiTimesFlag = flag;
    }

    public String getAdFlag() {
        return mAdFlag;
    }

    public void setAdFlag(String flag) {
        mAdFlag = flag;
    }

    public String getAdText() {
        return mAdText;
    }

    public void setAdText(String text) {
        mAdText = text;
    }

    public boolean isPicCompressEnabled() {
        String[] data = mPicCompress.split("_");
        String status = data[0];
        if (status.equals("off") || status.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public int getPicCompressWidth() {
        String[] data = mPicCompress.split("_");
        return Integer.parseInt(data[1]);
    }

    public int getPicCompressHeight() {
        String[] data = mPicCompress.split("_");
        return Integer.parseInt(data[2]);
    }

    public String getPicCompress() {
        return mPicCompress;
    }

    public void setPicCompress(String flag) {
        mPicCompress = flag;
    }

    public String getStatusPlaceholder() {
        return mStatusPlaceholder;
    }

    public void setStatusPlaceholder(String text) {
        mStatusPlaceholder = text;
    }

    public String getSearchPlaceholder() {
        return mSearchPlaceholder;
    }

    public void setSearchPlaceholder(String text) {
        mSearchPlaceholder = text;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String text) {
        mIntroduction = text;
    }

    public String getCommentPlaceholder() {
        return mCommentPlaceholder;
    }

    public void setCommentPlaceholder(String text) {
        mCommentPlaceholder = text;
    }

    public boolean isBaiduNavEnabled() {
        if (mBaiduNavStatus.equals("on")) {
            return true;
        } else {
            return false;
        }
    }

    public String getBaiduNavStatus() {
        return mBaiduNavStatus;
    }

    public void setBaiduNavStatus(String status) {
        mBaiduNavStatus = status;
    }

    public boolean isSelfCertificatedShopEnabled() {
        if (mSelfCertificatedShopStatus.equals("on")) {
            return true;
        } else {
            return false;
        }
    }

    public String getSelfCertificatedShopStatus() {
        return mSelfCertificatedShopStatus;
    }

    public void setSelfCertificatedShopStatus(String status) {
        mSelfCertificatedShopStatus = status;
    }

    public boolean isSignatureEnabled() {
        return mIsSignatureEnabled;
    }

    public void setSignatureEnabled(boolean status) {
        mIsSignatureEnabled = status;

        try {
            editor.putBoolean("SIGNATURE_ENABLED_KEY", mIsSignatureEnabled);
            editor.commit();
            Log.d(TAG, "editor.commit SIGNATURE_ENABLED_KEY succeed: " + String.valueOf(mIsSignatureEnabled));
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit SIGNATURE_ENABLED_KEY failed: " + exception);
        }
    }

    public boolean isNoImageMode() {
        return mIsNoImageMode;
    }

    public void setNoImageMode(boolean status) {
        mIsNoImageMode = status;
        TradeLab.get(mAppContext).resetTrades();

        try {
            editor.putBoolean("NO_IMAGE_MODE_KEY", mIsNoImageMode);
            editor.commit();
            Log.d(TAG, "editor.commit NO_IMAGE_MODE_KEY succeed: " + String.valueOf(mIsNoImageMode));
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit NO_IMAGE_MODE_KEY failed: " + exception);
        }
    }

    public boolean isFilterEnabled() {
        return mIsFilterEnabled;
    }

    public void setFilterEnabled(boolean status) {
        mIsFilterEnabled = status;
        TradeLab.get(mAppContext).resetTrades();

        try {
            editor.putBoolean("FILTER_ENABLED_KEY", mIsFilterEnabled);
            editor.commit();
            Log.d(TAG, "editor.commit FILTER_ENABLED_KEY succeed: " + String.valueOf(mIsFilterEnabled));
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit FILTER_ENABLED_KEY failed: " + exception);
        }
    }

    public boolean isRongYunConnected() {
        return mIsRongYunConnected;
    }

    public void setRongYunConnected(boolean status) {
        mIsRongYunConnected = status;
    }

    public boolean isSearchRecordEnabled() {
        return mIsSearchRecordEnabled;
    }

    public void setSearchRecordEnabled(boolean status) {
        mIsSearchRecordEnabled = status;

        if (!mIsSearchRecordEnabled) {
            mKeywords = new ArrayList<String>();

            try {
                convertKeywordsToString();
                editor.putString("KEYWORD_KEY", mKeywordsStr);
                editor.commit();
            } catch (Exception exception) {
                Log.e(TAG, "editor.commit KEYWORD_KEY failed: " + exception);
            }
        }

        try {
            editor.putBoolean("SEARCH_RECORD_ENABLED_KEY", mIsSearchRecordEnabled);
            editor.commit();
            Log.d(TAG, "editor.commit SEARCH_RECORD_ENABLED_KEY succeed: " + String.valueOf(mIsSearchRecordEnabled));
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit SEARCH_RECORD_ENABLED_KEY failed: " + exception);
        }

    }

    public boolean isEnabled() {
        return mIsPushEnabled;
    }

    public void setEnabled(boolean status) {
        mIsPushEnabled = status;

        try {
            editor.putBoolean("PUSH_ENABLED_KEY", mIsPushEnabled);
            editor.commit();
            Log.d(TAG, "editor.commit PUSH_ENABLED_KEY succeed: " + String.valueOf(mIsPushEnabled));
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit PUSH_ENABLED_KEY failed: " + exception);
        }

//        if (mIsPushEnabled) {
//            PushService.subscribe(mAppContext, "public", MainTabActivity.class);
//        } else {
//            PushService.unsubscribe(mAppContext, "public");
//        }

        AVInstallation.getCurrentInstallation().saveInBackground();
    }

    public boolean isRefreshNeeded() {
        return mIsRefreshNeeded;
    }

    public void needRefresh(boolean status) {
        mIsRefreshNeeded = status;
    }

    public void setVipMultiplier(int val) {
        mVipMultiplier = val;

        try {
            editor.putInt("VIP_MULTIPLIER_KEY", mVipMultiplier);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit VIP_MULTIPLIER_KEY failed: " + exception);
        }
    }

    public boolean isHelpReaded() {
        return mIsHelpReaded;
    }

    public void setHelpReaded(boolean status) {
        mIsHelpReaded = status;

        try {
            editor.putBoolean("HELP_READED_KEY", mIsHelpReaded);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit HELP_READED_KEY failed: " + exception);
        }
    }

    public boolean isPullToRefreshNoticeEnabled() {
        return mIsPullToRefreshNoticeEnabled;
    }

    public void setPullToRefreshNoticeEnabled(boolean status) {
        mIsPullToRefreshNoticeEnabled = status;

        try {
            editor.putBoolean("PULL_TO_REFRESH_NOTICE_ENABLED_KEY", mIsPullToRefreshNoticeEnabled);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit PULL_TO_REFRESH_NOTICE_ENABLED_KEY failed: " + exception);
        }
    }

    public void setTag(String tag) {
        mUserTag = tag;
        if (!mUserTag.equals(mLastUserTag)) {
            setItemPosition(0);
            mIsSameAsLastTag = false;
        } else {
            mIsSameAsLastTag = true;
        }
        mLastUserTag = mUserTag;
        // TradeLab.get(mAppContext).resetTrades();

        try {
            editor.putString("USER_TAG_KEY", mUserTag);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit USER_TAG_KEY failed: " + exception);
        }
    }

    public String getTag() {
        return mUserTag;
    }

    public void setOrderTag(String tag) {
        mOrderTag = tag;

        try {
            editor.putString("ORDER_TAG_KEY", mOrderTag);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit ORDER_TAG_KEY failed: " + exception);
        }
    }

    public String getOrderTag() {
        return mOrderTag;
    }

    public void setSavedTag(String tag) {
        mSavedTag = tag;

        try {
            editor.putString("SAVED_TAG_KEY", mSavedTag);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit SAVED_TAG_KEY failed: " + exception);
        }
    }

    public String getSavedTag() {
        return mSavedTag;
    }

    public boolean isTimeOrder() {
//        return mOrderTag.equals(mAppContext.getString(R.string.trade_newest));
        return true;
    }

    public boolean isHotOrder() {
//        return mOrderTag.equals(mAppContext.getString(R.string.trade_hotest));
        return true;
    }

    public boolean isPhoneTag() {
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(mUserTag);
        return m.matches();
    }

    //public boolean isHomeTag() {
    //    return mUserTag.equals("");
    //}

    public boolean isShoppingListTag() {
        return mUserTag.equals(mAppContext.getString(R.string.action_shopping));
    }

    public boolean isMyPostsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.action_post));
    }

    public boolean isUserPostsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.action_user_post));
    }

    public boolean isMyInfoTag() {
        return mUserTag.equals(mAppContext.getString(R.string.action_info));
    }

    public boolean isCreateTag() {
        return mUserTag.equals(mAppContext.getString(R.string.action_create));
    }

    public boolean isMySearchsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.action_search));
    }

    public boolean isMyFollowsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.action_follow));
    }

    public boolean isMyFavoritesTag() {
        return mUserTag.equals(mAppContext.getString(R.string.action_favorite));
    }

    public boolean isTradesTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_nearby_msgs));
    }

    public boolean isTopicsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_topics));
    }

    public boolean isNewsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_news));
    }

    public boolean isReadsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_reads));
    }

    public boolean isDiscoversTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_discovers));
    }

    public boolean isProductsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_products));
    }

    public boolean isScenesTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_scenes));
    }

    public boolean isFoodsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_foods));
    }

    public boolean isLostsTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_losts));
    }

    public boolean isCarTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_car));
    }

    public boolean isHouseTag() {
        return mUserTag.equals(mAppContext.getString(R.string.trade_house));
    }

    public boolean isHomePageTag() {
        if (isTradesTag()
            || isTopicsTag()
            || isNewsTag()
            || isReadsTag() || isDiscoversTag()
            || isProductsTag()
            || isScenesTag()
            || isFoodsTag()
            || isLostsTag()
                || isCarTag()
                || isHouseTag()
            ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSearchTag() {
        if (isPhoneTag() || isShoppingListTag() || isMyPostsTag()
            || isMyFollowsTag() || isMyFavoritesTag() || isMySearchsTag()
            || isHomePageTag() || isUserPostsTag()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isSameAsLastTag() {
        return mIsSameAsLastTag;
    }

    public void setShopHomePageState(String state) {
        mShopHomePageState = state;

        try {
            editor.putString("SHOP_HOME_PAGE_STATE_KEY", mShopHomePageState);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit SHOP_HOME_PAGE_STATE_KEY failed: " + exception);
        }
    }

    public boolean isShopHomePageEnabled() {
        return mShopHomePageState.equals("on");
    }

    public void setImagePosition(int position) {
        mImagePosition = position;
    }

    public int getImagePosition() {
        return mImagePosition;
    }

    public void setTmpMsg(String msg) {
        mTmpMsg = msg;
    }

    public String getTmpMsg() {
        return mTmpMsg;
    }

    public void setTmpNavStatus(boolean status) {
        mTmpNavStatus = status;
    }

    public boolean getTmpNavStatus() {
        return mTmpNavStatus;
    }

    public void setTmpSignatureStatus(String status) {
        mTmpSignatureStatus = status;
    }

    public String getTmpSignatureStatus() {
        return mTmpSignatureStatus;
    }

    public boolean isTmpSignatureNull() {
        if (mTmpSignatureStatus.equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTmpSignatureOn() {
        if (mTmpSignatureStatus.equals("on")) {
            return true;
        } else {
            return false;
        }
    }

    public void setMaxSearchResultsCount(int count) {
        mMaxSearchResultsCount = count;
    }

    public int getMaxSearchResultsCount() {
        return mMaxSearchResultsCount;
    }

    public void setWifiImageQuality(int quality) {
        mWifiImageQuality = quality;
    }

    public int getWifiImageQuality() {
        return mWifiImageQuality;
    }

    public void setMsgsPerScreen(int count) {
        mMsgsPerScreen = count;
    }

    public int getMsgsPerScreen() {
        return mMsgsPerScreen;
    }

    public void setMaxVoteOneTime(int count) {
        mMaxVoteOneTime = count;
    }

    public int getMaxVoteOneTime() {
        return mMaxVoteOneTime;
    }

    public void setMaxMsgCount(int count) {
        mMaxMsgCountPerDay = count;
    }

    public void setMinMsgPeriod(int period) {
        mMinMsgPeroid = period;
    }

    public void setLastLoginDate(String date) {
        mLastLoginDate = date;
    }

    public boolean isNewLogin() {
        if (!User.isLoggedIn()) {
            return false;
        }

//        if (!Network.isConnected(mAppContext)) {
//            return false;
//        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (mLastLoginDate.equals(date)) {
            return false;
        } else {
            mLastLoginDate = date;
            User.updateLastLoginDate(date);
            try {
                editor.putString("LAST_LOGIN_DATE_KEY", mLastLoginDate);
                editor.commit();
            } catch (Exception exception) {
                Log.e(TAG, "editor.commit LAST_LOGIN_DATE_KEY failed: " + exception);
            }

            return true;
        }
    }

    public void setLastMsgDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (mLastMsgDate.equals(date)) {
            mTodayMsgCount ++;
        } else {
            mLastMsgDate = date;
            mTodayMsgCount = 1;
        }

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = sdf.format(new Date());

        mLastMsgTime = date;

        try {
            editor.putString("LAST_MSG_DATE_KEY", mLastMsgDate);
            editor.putString("LAST_MSG_TIME_KEY", mLastMsgTime);
            editor.putInt("TODAY_MSG_COUNT_KEY", mTodayMsgCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LAST_MSG_DATE_KEY failed: " + exception);
        }
    }

    public boolean isMsgsTooMany() {
        if (User.isAdmin()) {return false;}

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (!mLastMsgDate.equals(date)) {
            mTodayMsgCount = 0;
        }

        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }
        
        if (mTodayMsgCount >= mMaxMsgCountPerDay * multiplier) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMsgsTooFrequent() {
        return false;
//    	int mins;
//
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date last = sdf.parse(mLastMsgTime);
//            Date now = new Date();
//
//            Interval interval = new Interval(last.getTime(), now.getTime());
//            Period period = interval.toPeriod();
//            mins = period.getMinutes();
//
//            if (mins >= mMinMsgPeroid) {
//                return false;
//            } else {
//                mMsgPeroid = mins;
//                return true;
//            }
//
//        } catch (Exception exception) {
//            return false;
//        }

    }

    public int getMsgPeriod() {
        return mMsgPeroid;
    }

    public int getMaxMsgCount() {
        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        return mMaxMsgCountPerDay * multiplier;
    }

    public int getMinMsgPeriod() {
        return mMinMsgPeroid;
    }

    public void setMaxCommentCount(int count) {
        mMaxCommentCountPerDay = count;
    }

    public void setMinCommentPeriod(int period) {
        mMinCommentPeroid = period;
    }

    public void setLastCommentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (mLastCommentDate.equals(date)) {
            mTodayCommentCount ++;
        } else {
            mLastCommentDate = date;
            mTodayCommentCount = 1;
        }

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = sdf.format(new Date());

        mLastCommentTime = date;

        try {
            editor.putString("LAST_COMMENT_DATE_KEY", mLastCommentDate);
            editor.putString("LAST_COMMENT_TIME_KEY", mLastCommentTime);
            editor.putInt("TODAY_COMMENT_COUNT_KEY", mTodayCommentCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LAST_COMMENT_DATE_KEY failed: " + exception);
        }
    }

    public boolean isCommentsTooMany() {
        if (User.isAdmin()) {return false;}

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (!mLastCommentDate.equals(date)) {
            mTodayCommentCount = 0;
        }

        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        if (mTodayCommentCount >= mMaxCommentCountPerDay * multiplier) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCommentsTooFrequent() {
        return false;
//    	int mins;
//
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date last = sdf.parse(mLastCommentTime);
//            Date now = new Date();
//
//            Interval interval = new Interval(last.getTime(), now.getTime());
//            Period period = interval.toPeriod();
//            mins = period.getMinutes();
//
//            if (mins >= mMinCommentPeroid) {
//                return false;
//            } else {
//                mCommentPeroid = mins;
//                return true;
//            }
//        } catch (Exception exception) {
//            return false;
//        }

    }

    public int getCommentPeriod() {
        return mCommentPeroid;
    }

    public int getMaxCommentCount() {
        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        return mMaxCommentCountPerDay * multiplier;
    }

    public int getMinCommentPeriod() {
        return mMinCommentPeroid;
    }

    public void setMaxLikeCount(int count) {
        mMaxLikeCountPerDay = count;
    }

    public void setMinLikePeriod(int period) {
        mMinLikePeroid = period;
    }

    public void setLastLikeDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (mLastLikeDate.equals(date)) {
            mTodayLikeCount ++;
        } else {
            mLastLikeDate = date;
            mTodayLikeCount = 1;
        }

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = sdf.format(new Date());
        mLastLikeTime = date;

        try {
            editor.putString("LAST_LIKE_DATE_KEY", mLastLikeDate);
            editor.putString("LAST_LIKE_TIME_KEY", mLastLikeTime);
            editor.putInt("TODAY_LIKE_COUNT_KEY", mTodayLikeCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LAST_LIKE_DATE_KEY failed: " + exception);
        }
    }

    public boolean isLikesTooMany() {
        if (User.isAdmin()) {return false;}

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (!mLastLikeDate.equals(date)) {
            mTodayLikeCount = 0;
        }

        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        if (mTodayLikeCount >= mMaxLikeCountPerDay * multiplier) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLikesTooFrequent() {
        return false;
//    	int mins;
//
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date last = sdf.parse(mLastLikeTime);
//            Date now = new Date();
//
//            Interval interval = new Interval(last.getTime(), now.getTime());
//            Period period = interval.toPeriod();
//            mins = period.getMinutes();
//
//            if (mins >= mMinLikePeroid) {
//                return false;
//            } else {
//                mLikePeroid = mins;
//                return true;
//            }
//        } catch (Exception exception) {
//            return false;
//        }

    }

    public int getLikePeriod() {
        return mLikePeroid;
    }

    public int getMaxLikeCount() {
        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        return mMaxLikeCountPerDay * multiplier;
    }

    public int getMinLikePeriod() {
        return mMinLikePeroid;
    }

    public void setMaxReportCount(int count) {
        mMaxReportCountPerDay = count;
    }

    public void setMinReportPeriod(int period) {
        mMinReportPeroid = period;
    }

    public void setLastReportDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (mLastReportDate.equals(date)) {
            mTodayReportCount ++;
        } else {
            mLastReportDate = date;
            mTodayReportCount = 1;
        }

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = sdf.format(new Date());
        mLastReportTime = date;

        try {
            editor.putString("LAST_REPORT_DATE_KEY", mLastReportDate);
            editor.putString("LAST_REPORT_TIME_KEY", mLastReportTime);
            editor.putInt("TODAY_REPORT_COUNT_KEY", mTodayReportCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LAST_REPORT_DATE_KEY failed: " + exception);
        }
    }

    public boolean isReportsTooMany() {
        if (User.isReportUnlimitedRank()) {
           return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (!mLastReportDate.equals(date)) {
            mTodayReportCount = 0;
        }

        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        if (mTodayReportCount >= mMaxReportCountPerDay * multiplier) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isReportsTooFrequent() {
        return false;
//    	int mins;
//
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date last = sdf.parse(mLastReportTime);
//            Date now = new Date();
//
//            Interval interval = new Interval(last.getTime(), now.getTime());
//            Period period = interval.toPeriod();
//            mins = period.getMinutes();
//
//            if (mins >= mMinReportPeroid) {
//                return false;
//            } else {
//                mReportPeroid = mins;
//                return true;
//            }
//        } catch (Exception exception) {
//            return false;
//        }

    }

    public int getReportPeriod() {
        return mReportPeroid;
    }

    public int getMaxReportCount() {
        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        return mMaxReportCountPerDay * multiplier;
    }

    public int getMinReportPeriod() {
        return mMinReportPeroid;
    }

    public void setMaxActionCount(int count) {
        mMaxActionCountPerDay = count;
    }

    public void setMinActionPeriod(int period) {
        mMinActionPeroid = period;
    }

    public void setLastActionDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (mLastActionDate.equals(date)) {
            mTodayActionCount ++;
        } else {
            mLastActionDate = date;
            mTodayActionCount = 1;
        }

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = sdf.format(new Date());
        mLastActionTime = date;

        try {
            editor.putString("LAST_ACTION_DATE_KEY", mLastActionDate);
            editor.putString("LAST_ACTION_TIME_KEY", mLastActionTime);
            editor.putInt("TODAY_ACTION_COUNT_KEY", mTodayActionCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LAST_ACTION_DATE_KEY failed: " + exception);
        }
    }

    public boolean isActionsTooMany() {
        if (User.isAdmin()) {return false;}

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        if (!mLastActionDate.equals(date)) {
            mTodayActionCount = 0;
        }

        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        if (mTodayActionCount >= mMaxActionCountPerDay * multiplier) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isActionsTooFrequent() {
        return false;
//    	int mins;
//
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date last = sdf.parse(mLastActionTime);
//            Date now = new Date();
//
//            Interval interval = new Interval(last.getTime(), now.getTime());
//            Period period = interval.toPeriod();
//            mins = period.getMinutes();
//
//            if (mins >= mMinActionPeroid) {
//                return false;
//            } else {
//                mActionPeroid = mins;
//                return true;
//            }
//        } catch (Exception exception) {
//            return false;
//        }

    }

    public int getActionPeriod() {
        return mActionPeroid;
    }

    public int getMaxActionCount() {
        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }

        return mMaxActionCountPerDay * multiplier;
    }

    public int getMinActionPeriod() {
        return mMinActionPeroid;
    }
    
    public int getRankMultiplier() {
        int multiplier = 1;
        if (User.isHighRank()) {
            multiplier = (User.getRealRank() - 3) * mVipMultiplier;
        }     
        return  multiplier;       
    }    

    public void setItemId(String itemId) {
        mItemId = itemId;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemPosition(int pos) {
        mItemPosition = pos;
    }

    public int getItemPosition() {
        return mItemPosition;
    }

    public void setHomeItemPosition(int pos) {
        mHomeItemPosition = pos;
    }

    public int getHomeItemPosition() {
        return mHomeItemPosition;
    }

    public void setLastItemId(String itemId) {
        mLastItemId = itemId;
    }

    public String getLastItemId() {
        return mLastItemId;
    }

    public void setPhone(String phone) {
        mUserPhone = phone;

        try {
            editor.putString("USER_PHONE_KEY", mUserPhone);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit USER_PHONE_KEY failed: " + exception);
        }

    }

    public String getPhone() {
        return mUserPhone;
    }

    public void incrShopHomePageCount() {
        mShopHomePageCount++;

        try {
            editor.putInt("SHOP_HOME_PAGE_COUNT_KEY", mShopHomePageCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit SHOP_HOME_PAGE_COUNT_KEY failed: " + exception);
        }

    }

    public void decrShopHomePageCount() {
        mShopHomePageCount--;
        if (mShopHomePageCount < 0) {mShopHomePageCount = 0;}

        try {
            editor.putInt("SHOP_HOME_PAGE_COUNT_KEY", mShopHomePageCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit SHOP_HOME_PAGE_COUNT_KEY failed: " + exception);
        }

    }

    public boolean isShopHomePageExist() {
        if (mShopHomePageCount != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setVerifiedPhone(String verifiedPhone) {
        mVerifiedPhone = verifiedPhone;

        try {
            editor.putString("VERIFIED_PHONE_KEY", mVerifiedPhone);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit VERIFIED_PHONE_KEY failed: " + exception);
        }

    }

    public String getVerifiedPhone() {
        return mVerifiedPhone;
    }

    public void incrVerifiedCount() {
        mVerifiedCount += 1;

        try {
            editor.putInt("VERIFIED_COUNT_KEY", mVerifiedCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit VERIFIED_COUNT_KEY failed: " + exception);
        }

    }

    public int getVerifiedCount() {
        return mVerifiedCount;
    }

    public void setAwardCount(int count) {
        mAwardCount = count;

        try {
            editor.putInt("AWARD_COUNT_KEY", mAwardCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit AWARD_COUNT_KEY failed: " + exception);
        }

    }

    public int getAwardCount() {
        return mAwardCount;
    }

    public void setAdPrice(int price) {
        mAdPrice = price;

        try {
            editor.putInt("AD_PRICE_KEY", mAdPrice);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit AD_PRICE_KEY failed: " + exception);
        }
    }

    public int getAdPrice() {
        return mAdPrice;
    }

    public void setCoinsPerDay(int val) {
        mCoinsPerDay = val;

        try {
            editor.putInt("COINS_PER_DAY_KEY", mCoinsPerDay);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit COINS_PER_DAY_KEY failed: " + exception);
        }
    }

    public int getCoinsPerDay() {
        return mCoinsPerDay;
    }

    public void setTicketsPerDay(int val) {
        mTicketsPerDay = val;

        try {
            editor.putInt("TICKETS_PER_DAY_KEY", mTicketsPerDay);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit TICKETS_PER_DAY_KEY failed: " + exception);
        }
    }

    public int getXjcPerDay() {
        return mXjcPerDay;
    }

    public void setXjcPerDay(int val) {
        mXjcPerDay = val;

        try {
            editor.putInt("XJC_PER_DAY_KEY", mXjcPerDay);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit XJC_PER_DAY_KEY failed: " + exception);
        }
    }

    public int getTicketsPerDay() {
        return mTicketsPerDay;
    }

    public void setPushDistance(int val) {
        mPushDistance = val;

        try {
            editor.putInt("PUSH_DISTANCE_KEY", mPushDistance);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit PUSH_DISTANCE_KEY failed: " + exception);
        }
    }

    public int getPushDistance() {
        return mPushDistance;
    }

    public void setLocalAdRange(int val) {
        mLocalAdRange = val;

        try {
            editor.putInt("LOCAL_AD_RANGE_KEY", mLocalAdRange);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LOCAL_AD_RANGE_KEY failed: " + exception);
        }
    }

    public int getLocalAdRange() {
        return mLocalAdRange;
    }

    public void setMinReportLevel(int val) {
        mMinReportLevel = val;

        try {
            editor.putInt("MIN_REPORT_LEVEL_KEY", mMinReportLevel);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit MIN_REPORT_LEVEL_KEY failed: " + exception);
        }
    }

    public int getMinReportLevel() {
        return mMinReportLevel;
    }

    public void setAdPeriod(int period) {
        mAdPeriod = period;

        try {
            editor.putInt("AD_PERIOD_KEY", mAdPeriod);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit AD_PERIOD_KEY failed: " + exception);
        }
    }

    public int getAdPeriod() {
        return mAdPeriod;
    }

    public void setLikeCount(int count) {
        mLikeCount = count;

        try {
            editor.putInt("LIKE_COUNT_KEY", mLikeCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit LIKE_COUNT_KEY failed: " + exception);
        }

    }

    public int getLikeCount() {
        return mLikeCount;
    }

    public void setCommentCount(int count) {
        mCommentCount = count;

        try {
            editor.putInt("COMMENT_COUNT_KEY", mCommentCount);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit COMMENT_COUNT_KEY failed: " + exception);
        }
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setAddress(String address) {
        mUserAddress = address;

        try {
            editor.putString("USER_ADDRESS_KEY", mUserAddress);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit USER_ADDRESS_KEY failed: " + exception);
        }

    }

    public String getAddress() {
        return mUserAddress;
    }

    public void setTradeTitle(String tradeTitle) {
        mTradeTitle = tradeTitle;

        try {
            editor.putString("TRADE_TITLE_KEY", mTradeTitle);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit TRADE_TITLE_KEY failed: " + exception);
        }

    }

    public String getTradeTitle() {
        return mTradeTitle;
    }

    public void setTradeDetails(String tradeDetails) {
        mTradeDetails = tradeDetails;

        try {
            editor.putString("TRADE_DETAILS_KEY", mTradeDetails);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit TRADE_DETAILS_KEY failed: " + exception);
        }

    }

    public String getTradeDetails() {
        return mTradeDetails;
    }

    public void setTradePosition(String tradePosition) {
        mTradePosition = tradePosition;

        try {
            editor.putString("TRADE_POSITION_KEY", mTradePosition);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit TRADE_POSITION_KEY failed: " + exception);
        }

    }

    public String getTradePosition() {
        return mTradePosition;
    }

    public void setTradeTag(String tradeTag) {
        mTradeTag = tradeTag;

        try {
            editor.putString("TRADE_TAG_KEY", mTradeTag);
            editor.commit();
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit TRADE_TAG_KEY failed: " + exception);
        }

    }

    public String getTradeTag() {
        return mTradeTag;
    }

    public void setRange(String range) {
        mRange = range;
        TradeLab.get(mAppContext).resetTrades();

        try {
            editor.putString("RANGE_KEY", mRange);
            editor.commit();
            Log.d(TAG, "editor.commit RANGE_KEY succeed: " + mRange);
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit RANGE_KEY failed: " + exception);
        }
    }

    public String getRange() {
        return mRange;
    }

    public void setSignature(String signature) {
        mSignature = signature;

        try {
            editor.putString("SIGNATURE_KEY", mSignature);
            editor.commit();
            Log.d(TAG, "editor.commit SIGNATURE_KEY succeed: " + mSignature);
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit SIGNATURE_KEY failed: " + exception);
        }
    }

    public String getSignature() {
        return mSignature;
    }

    public void setFilter(String filter) {
        mFilter = filter;
        TradeLab.get(mAppContext).resetTrades();

        try {
            editor.putString("FILTER_KEY", mFilter);
            editor.commit();
            Log.d(TAG, "editor.commit FILTER_KEY succeed: " + mFilter);
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit FILTER_KEY failed: " + exception);
        }
    }

    public String getFilter() {
        return mFilter;
    }

    public void setPassword(String password) {
        mPassword = password;

        try {
            editor.putString("PASSWORD_KEY", mPassword);
            editor.commit();
            Log.d(TAG, "editor.commit PASSWORD_KEY succeed: " + mPassword);
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit PASSWORD_KEY failed: " + exception);
        }
    }

    public String getPassword() {
        return mPassword;
    }

    public void setImageQuality(String imageQuality) {
        mImageQuality = imageQuality;

        try {
            editor.putString("IMAGE_QUALITY_KEY", mImageQuality);
            editor.commit();
            Log.d(TAG, "editor.commit IMAGE_QUALITY_KEY succeed: " + mImageQuality);
        } catch (Exception exception) {
            Log.e(TAG, "editor.commit IMAGE_QUALITY_KEY failed: " + exception);
        }
    }

    public int getImageQuality() {
        return Integer.parseInt(mImageQuality);
    }

    public String getImageQualityString() {
        return mImageQuality;
    }

    public void setPeer(String peer) {
        mPeer = peer;
    }

    public String getPeer() {
        return mPeer;
    }

    public void setPostsUserId(String id) {
        mPostsUserId = id;
    }

    public String getPostsUserId() {
        return mPostsUserId;
    }

    public void setCurrentSearchTag(String tag) {
        mCurrentSearchTag = tag;
    }

    public String getCurrentSearchTag() {
        return mCurrentSearchTag;
    }

    public void setImageUrls(List<String> urls) {
        mImageUrls = urls;
    }

    public List<String> getImageUrls() {
        return mImageUrls;
    }

    public void setEditTrade(Trade trade) {
        mEditTrade = trade;
    }

    public Trade getEditTrade() {
        return mEditTrade;
    }

    public void setSideMenu(String menu) {
        mSideMenu = menu;
    }

    public String getSideMenu() {
        return mSideMenu;
    }

    public List<String> getSideMenuList() {
        List<String> mMenu = new ArrayList<String>();
        String[] s = mSideMenu.split(" ");

        for (int i = 0; i < s.length; i++) {
            if (!s[i].trim().equals("")) {
                mMenu.add(s[i].trim());
            }
        }

        return mMenu;
    }

    public void setShopId(String id) {
        mShopId = id;
    }

    public String getShopId() {
        return mShopId;
    }

    public void setShopTitle(String title) {
        mShopTitle = title;
    }

    public String getShopTitle() {
        return mShopTitle;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) public static int getWidth() {
        WindowManager wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) public static int getHeight() {
        WindowManager wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return height;
    }

    public void init() {
        AVInstallation.getCurrentInstallation().saveInBackground();
//        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
//            public void done(AVException e) {
//                if (e == null) {
//                    // String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
//                    Log.d(TAG, "init successfully.");
//                } else {
//                    Log.e(TAG, "init failed:" + e.getMessage());
//                }
//            }
//        });
    }

}