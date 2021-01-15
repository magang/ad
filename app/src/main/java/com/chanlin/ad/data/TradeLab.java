package com.chanlin.ad.data;

import android.content.Context;
import android.util.Log;

import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.util.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.leancloud.AVQuery;

/**
 * 数据仓库
 *
 * @author dustforest
 */
public class TradeLab {
    private static final String TAG = TradeLab.class.getName();

    private static TradeLab sTradeLab;
    private Context mAppContext;
    private List<Trade> mTrades;
    private List<Trade> mSearchResults = new ArrayList<>();
    private int mLimit = 100;
    private PushConfig mPush;
    private int mIndex = 0;
    private String latestDay = "2000-01-01";
    private int latestNewsIndex = 0;
    private final String tradesStartDate = "2021-01-01";

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

    private void checkNewDay() {
        String today = CommonUtils.formatDate(new Date());
        if (!today.equalsIgnoreCase(latestDay)) {
            latestDay = today;
            latestNewsIndex = 0;
        }
    }

    public List<Trade> searchTrades() {
        int mSearchLimit = mPush.getMaxSearchResultsCount();
        String userTag = mPush.getTag().trim();

        Log.i(TAG, "find remote searches");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        mSearchResults = new ArrayList<>();
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
        query.addDescendingOrder("totalVotes");
        query.addDescendingOrder("createdAt");
        query.limit(mSearchLimit);

        try {
            List<Trade> mRemoteTrades = query.find();
            if (mSearchResults != null) {
                mRemoteTrades.addAll(mSearchResults);
            }
            mSearchResults = mRemoteTrades;
            if (mSearchResults.size() > mSearchLimit) {
                mSearchResults = mSearchResults.subList(0, mSearchLimit);
            }

            return mSearchResults;
        } catch (Exception exception) {
            Log.e(TAG, "Query searches failed." + exception);
            return Collections.emptyList();
        }
    }

    /**
     * 查询消息
     *
     * @return
     */
    public List<Trade> findTrades() {
        logTraceInfo(Thread.currentThread().getStackTrace()[2].getMethodName());

        if (!mPush.isRefreshNeeded()) {
            return getCachedTrades();
        }

        checkNewDay();
        mTrades = findTodayTrades(30, 1);
        if (!mTrades.isEmpty()) {
            return mTrades;
        }

        int categoryNum = 7;
        switch (mIndex) {
            case 0:
                mTrades = findGlobalAds(15, 1);
                break;
            case 1:
            case 2:
            case 3:
                mTrades = findHotestTrades(100, 1, tradesStartDate);
                break;
            case 4:
            case 5:
            case 6:
                mTrades = findLatestTrades(100, 1, tradesStartDate);
                break;
            default:
                break;
        }
        mIndex++;
        mIndex = mIndex % categoryNum;

        return mTrades;
    }

    public List<Trade> getCachedTrades() {
        logTraceInfo(Thread.currentThread().getStackTrace()[2].getMethodName());
        Log.i(TAG, "items found: " + mTrades.size());
        return mTrades;
    }

    public List<Trade> findLatestTrades(int poolSize, int num, String startDate) {
        logTraceInfo(Thread.currentThread().getStackTrace()[2].getMethodName());

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.whereEqualTo("user", "15539509926");
        query.whereGreaterThanOrEqualTo("createdAt", CommonUtils.getDateWithDateString(startDate));
        query.orderByDescending("createdAt");
        query.limit(poolSize);

        return getRandomItems(query, num);
    }

    public List<Trade> findHotestTrades(int poolSize, int num, String startDate) {
        logTraceInfo(Thread.currentThread().getStackTrace()[2].getMethodName());

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.whereEqualTo("user", "15539509926");
        query.whereGreaterThanOrEqualTo("createdAt", CommonUtils.getDateWithDateString(startDate));
        query.addDescendingOrder("totalVotes");
        query.limit(poolSize);

        return getRandomItems(query, num);
    }

    private List<Trade> getRandomItems(AVQuery<Trade> query, int num) {
        return getItems(query, num, true);
    }

    private List<Trade> getItems(AVQuery<Trade> query, int num, boolean isRandom) {
        try {
            List<Trade> items = query.find();
            Log.i(TAG, "items found: " + items.size());
            if (isRandom) {
                Collections.shuffle(items);
            }
            num = Math.min(items.size(), num);
            return items.subList(0, num);
        } catch (Exception exception) {
            Log.e(TAG, "getItems error: " + exception);
            return Collections.emptyList();
        }
    }

    private void logTraceInfo(String msg) {
        Log.d(TAG, "========== " + msg + " ==========");
    }

    /**
     * 查询当天发布的消息
     *
     * @param num
     * @return
     */
    public List<Trade> findTodayTrades(int poolSize, int num) {
        logTraceInfo(Thread.currentThread().getStackTrace()[2].getMethodName());

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.whereGreaterThanOrEqualTo("createdAt", CommonUtils.getDateWithDateString(CommonUtils.formatDate(new Date())));
        query.orderByAscending("createdAt");
        query.limit(poolSize);

        try {
            List<Trade> items = query.find();
            Log.i(TAG, "items found: " + items.size());
            items = items.subList(latestNewsIndex, latestNewsIndex + num);
            latestNewsIndex += num;
            return items;
        } catch (Exception exception) {
            Log.e(TAG, "getItems error: " + exception);
            return Collections.emptyList();
        }
    }

    /**
     * 查询置顶消息
     *
     * @param poolSize
     * @param num
     * @return
     */
    public List<Trade> findGlobalAds(int poolSize, int num) {
        logTraceInfo(Thread.currentThread().getStackTrace()[2].getMethodName());

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "ad");
        query.whereEqualTo("range", "global");
        query.whereEqualTo("online", "online");
        query.whereLessThanOrEqualTo("adStartDate", CommonUtils.formatDate(new Date()));
        query.whereNotContainedIn("objectId", mPush.getGlobalAdIgnores());
        query.orderByDescending("createdAt");
        query.limit(poolSize);
        List<Trade> items = getRandomItems(query, num);

        return items;
    }

    public void removeItemFromTrades(String id) {
        if (mTrades == null) {return;}
        List<Trade> tmp = new ArrayList<>();

        for (Trade c: mTrades) {
            if (!c.getObjectId().equals(id)) {
                tmp.add(c);
            }
        }

        mTrades = tmp;
    }
}