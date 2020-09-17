package com.chanlin.ad.data;

import android.content.Context;
import android.util.Log;

import com.chanlin.ad.config.PushConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;


public class TradeLab {

    private static final String TAG = TradeLab.class.getName();

    private static TradeLab sTradeLab;
    private Context mAppContext;
    private List<Trade> mTrades;
    private List<Trade> mPosts;
    private List<Trade> mFollows;
    private List<Trade> mFavorites;
    private List<Trade> mUserPosts;
    private List<Trade> mSearchs = new ArrayList<>();
    private Date mUpdatedAt;
    private Date mGlobalAdUpdatedAt;
    private Date mLocalAdUpdatedAt;
    private boolean mIsNeedUpdate = true;
    private boolean mIsSameAsLastSearch = false;
    private String mLastSearchKey = "";
    private int mLimit = 100;
    private int mKilometers = 2;
    private PushConfig mPush;

    private TradeLab(Context appContext) {
        mAppContext = appContext;
        mPush = PushConfig.get(appContext);
    }

    public static TradeLab get(Context c) {
        if (sTradeLab == null) {
            sTradeLab = new TradeLab(c.getApplicationContext());
        }
        return sTradeLab;
    }

    public void setUpdateFlag(boolean flag) {
        mIsNeedUpdate = flag;
    }

    public boolean isNeedUpdate() {
        return mIsNeedUpdate;
    }

    public List<Trade> findPosts() {

        if (mPosts != null) {
            return mPosts;
        }

        Log.i(TAG, "find remote posts");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        AVUser currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            query.whereEqualTo("user", currUser.getString("mobilePhoneNumber"));
        } else {
            return Collections.emptyList();
        }

        query.orderByDescending("createdAt");

        try {
            List<Trade> mRemoteTrades = query.find();
            if (mPosts != null) {
                mRemoteTrades.addAll(mPosts);
            }
            mPosts = mRemoteTrades;
            orderPosts();
            return mPosts;
        } catch (Exception exception) {
            Log.e(TAG, "Query likes failed." + exception);
            return Collections.emptyList();
        }
    }

    public List<Trade> findUserPosts() {
        Log.i(TAG, "find remote user posts");

        mUserPosts = new ArrayList<Trade>();

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("user", mPush.getPostsUserId());
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.orderByDescending("createdAt");

        try {
            List<Trade> mRemoteTrades = query.find();
            if (mUserPosts != null) {
                mRemoteTrades.addAll(mUserPosts);
            }
            mUserPosts = mRemoteTrades;
            return mUserPosts;
        } catch (Exception exception) {
            Log.e(TAG, "Query likes failed." + exception);
            return Collections.emptyList();
        }
    }

    public List<Trade> findFavorites() {

        if (mFavorites != null) {
            return shopMsgFilter(mFavorites);
        }

        Log.i(TAG, "find remote likes");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereContainedIn("objectId", mPush.getFavorites());

        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.orderByDescending("createdAt");

        try {
            List<Trade> mRemoteTrades = query.find();
            if (mFavorites != null) {
                mRemoteTrades.addAll(mFavorites);
            }
            mFavorites = mRemoteTrades;
            return shopMsgFilter(mFavorites);
        } catch (Exception exception) {
            Log.e(TAG, "Query likes failed." + exception);
            return Collections.emptyList();
        }
    }

    public List<Trade> findFollows() {
        mFollows = new ArrayList<Trade>();

        Log.i(TAG, "find remote follows");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereContainedIn("user", mPush.getFollows());

        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.orderByDescending("createdAt");

        try {
            List<Trade> mRemoteTrades = query.find();
            Log.i(TAG, "Query follows count: " + mRemoteTrades.size());
            if (mFollows != null) {
                mRemoteTrades.addAll(mFollows);
            }
            mFollows = mRemoteTrades;
            return shopMsgFilter(mFollows);
        } catch (Exception exception) {
            Log.e(TAG, "Query follows failed." + exception);
            return Collections.emptyList();
        }
    }

    public List<Trade> findSearchs() {
        int mSearchLimit = mPush.getMaxSearchResultsCount();
        String userTag = mPush.getTag().trim();

        Log.i(TAG, "find remote searches");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        mSearchs = new ArrayList<Trade>();
        if (userTag != null && !userTag.equals("")) {

            String pattern = ".*(";
            String[] tag = userTag.split(" ");
            for (int i = 0; i < tag.length; i++) {
                String s = tag[i].trim();
                if(!s.equals("")) {
                    pattern += s + "|";
                }
            }

            int len = pattern.length() - 1;
            pattern = pattern.substring(0, len) + ").*";

            String orPattern = pattern;
            String andPattern = "";

            for (int i = 0; i < tag.length; i++) {
                String s = tag[i].trim();
                if(!s.equals("")) {
                    andPattern += pattern;
                }
            }

            Log.i(TAG, "Pattern: " + andPattern);
            Log.i(TAG, "userTag:" + userTag + ":");
            query.whereMatches("tag", andPattern);
        }

        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        //query.addDescendingOrder("avgScore");
        query.addDescendingOrder("totalVotes");
        query.addDescendingOrder("createdAt");

        // query.addDescendingOrder();
        // query.addAscendingOrder();

//        if (!mPush.isRoamEnabled()) {
//            mKilometers = Integer.parseInt(mPush.getRange());
//            try {
//                AVGeoPoint point = new AVGeoPoint(mLocManager.getCurrLatitude(), mLocManager.getCurrLongitude());
//                query.whereWithinKilometers("location", point, mKilometers);
//            } catch (NullPointerException exception) {
//                Log.e(TAG, "new AVGeoPoint failed:" + exception);
//            }
//        }

        query.limit(mSearchLimit);
        try {
            List<Trade> mRemoteTrades = query.find();
            if (mSearchs != null) {
                mRemoteTrades.addAll(mSearchs);
            }
            mSearchs = mRemoteTrades;
            if (mSearchs.size() > mSearchLimit) {
                mSearchs = mSearchs.subList(0, mSearchLimit);
            }

            return shopMsgFilter(mSearchs);
        } catch (Exception exception) {
            Log.e(TAG, "Query searches failed." + exception);
            return Collections.emptyList();
        }
    }

    public List<Trade> findTrades() {
        Log.i(TAG, "query remote trades...");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);

        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");

//        if (!mPush.isTradesTag()) {
//            query.whereContains("tag", mPush.getTag());
//        }

//        if (mPush.isTimeOrder()) {
//            query.orderByDescending("createdAt");
//        }
//
//        if (mPush.isHotOrder()) {
//            //query.addDescendingOrder("avgScore");
//            query.addDescendingOrder("totalVotes");
//            query.addDescendingOrder("createdAt");
//        }

//        if (!mPush.isRoamEnabled()) {
//            mKilometers = Integer.parseInt(mPush.getRange());
//            try {
//                AVGeoPoint point = new AVGeoPoint(mLocManager.getCurrLatitude(), mLocManager.getCurrLongitude());
//                query.whereWithinKilometers("location", point, mKilometers);
//            } catch (NullPointerException exception) {
//                Log.e(TAG, "new AVGeoPoint failed:" + exception);
//            }
//        }

        query.limit(mLimit);
        try {
            List<Trade> mRemoteTrades = query.find();
            Log.i(TAG, "Query trades count: " + mRemoteTrades.size());
            if (mRemoteTrades != null && mRemoteTrades.size() > 0) {
                mUpdatedAt = ((Trade)mRemoteTrades.get(0)).getCreatedAt();
            }
            if (mTrades != null) {
                mRemoteTrades.addAll(mTrades);
            }
            mTrades = mRemoteTrades;
            if (mTrades.size() > mLimit) {
                mTrades = mTrades.subList(0, mLimit);
            }

            findLatestUpdatedTrade();
            findLatestCreatedTrade();
            findGlobalAds();
            orderTrades();
            syncPosts();

            return shopMsgFilter(mTrades);
        } catch (Exception exception) {
            Log.e(TAG, "Query trades failed." + exception);
            if (mTrades != null) {
                return shopMsgFilter(mTrades);
            } else {
                return Collections.emptyList();
            }
        }
    }

    public List<Trade> findItems() {
        mLimit = mPush.getMsgsPerScreen();

        if (mPush.isMyFavoritesTag()) {
            return findFavorites();
        } else if (mPush.isMyPostsTag()) {
            return findPosts();
        } else if (mPush.isMyFollowsTag()) {
            return findFollows();
        } else if (mPush.isUserPostsTag()) {
            return findUserPosts();
        }  else if (mPush.isHomePageTag()) {
            return findTrades();
        } else {
            return findSearchs();
        }
    }

    public List<Trade> getCurrentItems() {

        if (mPush.isMyFavoritesTag()) {
            return getFavorites();
        } else if (mPush.isMyPostsTag()) {
            return getPosts();
        } else if (mPush.isMyFollowsTag()) {
            return getFollows();
        } else if (mPush.isUserPostsTag()) {
            return getUserPosts();
        } else if (mPush.isHomePageTag()) {
            return getTrades();
        } else {
            return getSearchs();
        }
    }

    public void findLatestCreatedTrade() {
        Log.i(TAG, "find latest created trade");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.orderByDescending("createdAt");
        query.limit(1);

        if (!mPush.isTradesTag()) {
            query.whereContains("tag", mPush.getTag());
        }

//        if (!mPush.isRoamEnabled()) {
//            mKilometers = Integer.parseInt(mPush.getRange());
//            try {
//                AVGeoPoint point = new AVGeoPoint(mLocManager.getCurrLatitude(), mLocManager.getCurrLongitude());
//                query.whereWithinKilometers("location", point, mKilometers);
//            } catch (NullPointerException exception) {
//                Log.e(TAG, "new AVGeoPoint failed:" + exception);
//            }
//        }

        try {
            List<Trade> mNewTrades = query.find();
            if (mTrades != null) {
                mNewTrades.addAll(mTrades);
            }
            mTrades = mNewTrades;
            if (mTrades.size() > mLimit) {
                mTrades = mTrades.subList(0, mLimit);
            }
        } catch (Exception exception) {
            Log.e(TAG, "Query latest created trade failed." + exception);
        }
    }

    public void findLatestUpdatedTrade() {
        Log.i(TAG, "find latest updated trade");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.orderByDescending("updatedAt");
        query.limit(1);

        if (!mPush.isTradesTag()) {
            query.whereContains("tag", mPush.getTag());
        }

//        if (!mPush.isRoamEnabled()) {
//            mKilometers = Integer.parseInt(mPush.getRange());
//            try {
//                AVGeoPoint point = new AVGeoPoint(mLocManager.getCurrLatitude(), mLocManager.getCurrLongitude());
//                query.whereWithinKilometers("location", point, mKilometers);
//            } catch (NullPointerException exception) {
//                Log.e(TAG, "new AVGeoPoint failed:" + exception);
//            }
//        }

        try {
            List<Trade> mNewTrades = query.find();
            if (mTrades != null) {
                mNewTrades.addAll(mTrades);
            }
            mTrades = mNewTrades;
            if (mTrades.size() > mLimit) {
                mTrades = mTrades.subList(0, mLimit);
            }
        } catch (Exception exception) {
            Log.e(TAG, "Query latest created trade failed." + exception);
        }
    }

    public void findGlobalAds() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(Calendar.getInstance().getTime());

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);

        /*if (mGlobalAdUpdatedAt != null) {
            query.whereGreaterThan("createdAt", mGlobalAdUpdatedAt);
        }*/

        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "ad");
        query.whereEqualTo("range", "global");
        query.whereEqualTo("online", "online");
        query.whereLessThanOrEqualTo("adStartDate", today);
        // query.whereEqualTo("source", "yumi");
        query.whereNotContainedIn("objectId", mPush.getGlobalAdIgnores());
        query.orderByDescending("createdAt");
        query.limit(mLimit);
        try {
            List<Trade> mRemoteAds = query.find();
            if (mRemoteAds != null && mRemoteAds.size() > 0) {
                mGlobalAdUpdatedAt = ((Trade)mRemoteAds.get(0)).getCreatedAt();
            }
            if (mTrades != null) {
                mRemoteAds.addAll(mTrades);
            }
            mTrades = mRemoteAds;
            if (mTrades.size() > mLimit) {
                mTrades = mTrades.subList(0, mLimit);
            }
        } catch (Exception exception) {
            Log.e(TAG, "Query trades failed." + exception);
        }
    }

    private void orderTrades() {
        List<Trade> mGlobalAds = new ArrayList<Trade>();
        List<Trade> mLocalAds = new ArrayList<Trade>();
        List<Trade> mOthers = new ArrayList<Trade>();
        if (mTrades == null) {return;}
        for (Trade c: mTrades) {
            if (c.getType().equals("ad") && c.getRange().equals("global")) {

                if (mGlobalAds != null && mGlobalAds.size() > 0) {
                    boolean unique = true;

                    for (Trade a: mGlobalAds) {
                        if(a.getObjectId().equals(c.getObjectId())) {
                            unique = false;
                            break;
                        }
                    }

                    if (unique) {
                        mGlobalAds.add(c);
                    }
                } else {
                    mGlobalAds.add(c);
                }

            } else if (c.getType().equals("ad") && c.getRange().equals("local")) {

                if (mLocalAds != null && mLocalAds.size() > 0) {
                    boolean unique = true;

                    for (Trade a: mLocalAds) {
                        if(a.getObjectId().equals(c.getObjectId())) {
                            unique = false;
                            break;
                        }
                    }

                    if (unique) {
                        mLocalAds.add(c);
                    }
                } else {
                    mLocalAds.add(c);
                }

            } else {

                if (mOthers != null && mOthers.size() > 0) {
                    boolean unique = true;

                    for (Trade a: mOthers) {
                        if(a.getObjectId().equals(c.getObjectId())) {
                            unique = false;
                            break;
                        }
                    }

                    if (unique) {
                        mOthers.add(c);
                    }
                } else {
                    mOthers.add(c);
                }
            }
        }

        mGlobalAds.addAll(mLocalAds);
        mGlobalAds.addAll(mOthers);
        mTrades = mGlobalAds;
    }

    private void orderPosts() {
        List<Trade> mTmp = new ArrayList<Trade>();
        if (mPosts == null) {return;}

        // unique
        for (Trade c: mPosts) {
            if (mTmp != null && mTmp.size() > 0) {
                boolean unique = true;

                for (Trade a: mTmp) {
                    if(a.getObjectId().equals(c.getObjectId())) {
                        unique = false;
                        break;
                    }
                }

                if (unique) {
                    mTmp.add(c);
                }
            } else {
                mTmp.add(c);
            }
        }

        mPosts = mTmp;
        mTmp = null;

        // offline items first
        List<Trade> tmp = new ArrayList<Trade>();
        List<Trade> tmp1 = new ArrayList<Trade>();

        for (Trade c: mPosts) {
            if (c.isOnline()) {
                tmp.add(c);
            } else {
                tmp1.add(c);
            }
        }

        tmp1.addAll(tmp);
        mPosts = tmp1;

        // shopHomePage items first
        List<Trade> btmp = new ArrayList<Trade>();
        List<Trade> btmp1 = new ArrayList<Trade>();

        for (Trade c: mPosts) {
            if (c.isShopHomePage()) {
                btmp.add(c);
            } else {
                btmp1.add(c);
            }
        }

        btmp.addAll(btmp1);
        mPosts = btmp;

        getLikeLogs();
        getComments();
    }

    private void orderFollows() {
        List<Trade> mTmp = new ArrayList<Trade>();
        if (mFollows == null) {return;}
        for (Trade c: mFollows) {
            if (mTmp != null && mTmp.size() > 0) {
                boolean unique = true;

                for (Trade a: mTmp) {
                    if(a.getObjectId().equals(c.getObjectId())) {
                        unique = false;
                        break;
                    }
                }

                if (unique) {
                    mTmp.add(c);
                }
            } else {
                mTmp.add(c);
            }
        }

        mFollows = mTmp;
    }

    public List<Trade> getTrades() {
        if (mTrades == null) {
            return Collections.emptyList();
        } else {
            return shopMsgFilter(mTrades);
        }
    }

    public List<Trade> getPosts() {
        if (mPosts == null) {
            return Collections.emptyList();
        } else {
            return shopMsgFilter(mPosts);
        }
    }

    public List<Trade> getUserPosts() {
        if (mUserPosts == null) {
            return Collections.emptyList();
        } else {
            return shopMsgFilter(mUserPosts);
        }
    }

    public List<Trade> getFavorites() {
        if (mFavorites == null) {
            return Collections.emptyList();
        } else {
            return shopMsgFilter(mFavorites);
        }
    }

    public List<Trade> getFollows() {
        if (mFollows == null) {
            return Collections.emptyList();
        } else {
            return shopMsgFilter(mFollows);
        }
    }

    public List<Trade> getSearchs() {
        if (mSearchs == null) {
            return Collections.emptyList();
        } else {
            return shopMsgFilter(mSearchs);
        }
    }

    public Trade getTrade(String id) {
        for (Trade c: mTrades) {
            if (c.getObjectId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public Trade getTrade(int index) {
        try {
            return mTrades.get(index);
        } catch (Exception exception) {
            return null;
        }
    }

    public void resetData() {
        resetTrades();
        resetPosts();
        resetFollows();
        resetFavorites();
    }

    public void resetTrades() {
        mPush.setHomeItemPosition(0);
        mTrades = null;
        mUpdatedAt = null;
    }

    public void resetPosts() {
        mPosts = null;
    }

    public void resetFollows() {
        mFollows = null;
    }

    public void resetFavorites() {
        mFavorites = null;
    }

    public void resetUpdatedAt() {
        mUpdatedAt = null;
    }

    public void addItemToTrades(Trade item) {
        if (mTrades == null) {return;}
        mTrades.add(item);
    }

    public void removeItemFromTrades(String id) {
        if (mTrades == null) {return;}
        List<Trade> tmp = new ArrayList<Trade>();

        for (Trade c: mTrades) {
            if (!c.getObjectId().equals(id)) {
                tmp.add(c);
            }
        }

        mTrades = tmp;
    }

    public void addItemToFavorites(Trade item) {
        if (mFavorites == null) {return;}
        mFavorites.add(item);
    }

    public void removeItemFromFavorites(String id) {
        if (mFavorites == null) {return;}
        List<Trade> tmp = new ArrayList<Trade>();

        for (Trade c: mFavorites) {
            if (!c.getObjectId().equals(id)) {
                tmp.add(c);
            }
        }

        mFavorites = tmp;
    }

    public void removeItemFromPosts(String id) {
        if (mPosts == null) {return;}
        List<Trade> tmp = new ArrayList<Trade>();

        for (Trade c: mPosts) {
            if (!c.getObjectId().equals(id)) {
                tmp.add(c);
            }
        }

        mPosts = tmp;
    }

    public void removeItemFromSearchs(String id) {
        if (mSearchs == null) {return;}
        List<Trade> tmp = new ArrayList<Trade>();

        for (Trade c: mSearchs) {
            if (!c.getObjectId().equals(id)) {
                tmp.add(c);
            }
        }

        mSearchs = tmp;
    }

    public void offlineItemFromPosts(String id) {
        if (mPosts == null) {return;}

        for (Trade c: mPosts) {
            if (c.getObjectId().equals(id)) {
                c.setOnline("offline");
            }
        }
    }

    public int getPostsLikeSum() {
        if (mPosts == null) {return 0;}

        int sum = 0;
        for (Trade c: mPosts) {
            sum += c.getUserLikeCount();
        }

        return sum;
    }

    public int getPostsAwardSum() {
        if (mPosts == null) {return 0;}

        int sum = 0;
        for (Trade c: mPosts) {
            //sum += c.getUserAwardCount();
            sum += c.getVoteCount();
        }

        return sum;
    }

    public int getPostsCommentSum() {
        if (mPosts == null) {return 0;}

        int sum = 0;
        for (Trade c: mPosts) {
            sum += c.getUserCommentCount();
        }

        return sum;
    }

    private void getComments(){
        int lastCommentCount;
        int currCommentCount;
        currCommentCount = getPostsCommentSum();
        lastCommentCount = mPush.getCommentCount();
        if (currCommentCount > lastCommentCount) {
//            CommentLab.get(mAppContext).findComments();
        }
    }

    private void getAwards(){
        int lastAwardCount;
        int currAwardCount;
        currAwardCount = getPostsAwardSum();
        lastAwardCount = mPush.getAwardCount();
        if (currAwardCount > lastAwardCount) {
//            AwardLab.get(mAppContext).findAwards();
        }
    }

    private void getLikeLogs(){
        int lastLikeCount;
        int currLikeCount;
        currLikeCount = getPostsLikeSum();
        lastLikeCount = mPush.getLikeCount();
        if (currLikeCount > lastLikeCount) {
//            LikeLab.get(mAppContext).findLikes();
        }
    }

    private List<Trade> shopMsgFilter(List<Trade> items) {
        if (!mPush.isShopHomePageEnabled()) {return items;}

        List<Trade> tmp = new ArrayList<Trade>();
        for (Trade c: items) {
            if (c.isVip() && !c.isHighRank() && !c.isAd() && !c.isShopHomePage()) {
            } else {
                tmp.add(c);
            }
        }
        return tmp;
    }

    public void addPersonToFollows(String user) {
        if (mFollows == null) {return;}
        List<Trade> tmp = new ArrayList<Trade>();
        for (Trade c: mTrades) {
            if (c.getUser().equals(user)) {

                boolean unique = true;

                for (Trade a: mFollows) {
                    if(a.getObjectId().equals(c.getObjectId())) {
                        unique = false;
                        break;
                    }
                }

                if (unique) {
                    tmp.add(c);
                }
            }
        }

        tmp.addAll(mFollows);
        mFollows = tmp;
    }

    public void removePersonFromFollows(String user) {
        if (mFollows == null) {return;}
        List<Trade> tmp = new ArrayList<Trade>();

        for (Trade c: mFollows) {
            if (!c.getUser().equals(user)) {
                tmp.add(c);
            }
        }

        mFollows = tmp;
    }

    public void removeItemFromFollows(String id) {
        if (mFollows == null) {return;}
        List<Trade> tmp = new ArrayList<Trade>();

        for (Trade c: mFollows) {
            if (!c.getObjectId().equals(id)) {
                tmp.add(c);
            }
        }

        mFollows = tmp;
    }

    public void updateItemCommentCount(String id) {
        if (mFollows != null) {
            for (Trade c: mFollows) {
                if (c.getObjectId().equals(id)) {
                    c.incrCommentCount();
                }
            }
        }

        if (mTrades != null) {
            for (Trade c: mTrades) {
                if (c.getObjectId().equals(id)) {
                    c.incrCommentCount();
                }
            }
        }

        if (mPosts != null) {
            for (Trade c: mPosts) {
                if (c.getObjectId().equals(id)) {
                    c.incrCommentCount();
                }
            }
        }
    }

    public void syncFollows() {
        if (mFollows == null) {return;}

        List<Trade> tmp = new ArrayList<Trade>();
        for (Trade c: mTrades) {
            if (mPush.getFollows().contains(c.getUser())) {

                boolean unique = true;

                if (unique) {
                    tmp.add(c);
                }
            }
        }

        tmp.addAll(mFollows);
        mFollows = tmp;

        orderFollows();
    }

    public void syncPosts() {
        if (mPosts == null) {
            findPosts();
            return;
        }

        AVUser currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            String user = currUser.getString("mobilePhoneNumber");

            List<Trade> tmp = new ArrayList<Trade>();
            for (Trade c: mTrades) {
                if (c.getUser().equals(user)) {

                    boolean unique = true;

                    if (unique) {
                        tmp.add(c);
                    }
                }
            }

            tmp.addAll(mPosts);
            mPosts = tmp;
        }

        orderPosts();
    }
}