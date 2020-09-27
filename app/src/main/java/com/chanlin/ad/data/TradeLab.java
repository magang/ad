package com.chanlin.ad.data;

import android.content.Context;
import android.util.Log;

import com.chanlin.ad.config.PushConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import cn.leancloud.AVQuery;

/**
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

    public List<Trade> findTrades() {
        Log.i(TAG, "findTrades...");

        if (!mPush.isRefreshNeeded()) {
            return getCachedTrades();
        }

        mTrades = findGlobalAds(15, 1);
        if (!mTrades.isEmpty()) {
            return mTrades;
        }

        switch (mIndex) {
            case 0:
                mTrades = findHotTrades(100, 1);
                break;
            case 1:
            default:
                mTrades = findNewTrades(100, 1);
                break;
        }

        mIndex++;
        mIndex = mIndex % 2;

        return mTrades;

//        List<Trade> items = new ArrayList<>();
//        items.addAll(findGlobalAds(100, 100));
//        items.addAll(findHotTrades(100, 5));
//        items.addAll(findNewTrades(100, 5));

//        LinkedHashSet<Trade> hashSet = new LinkedHashSet<>(items);
//        mTrades = new ArrayList<>(hashSet);
//        mTrades = items.stream().distinct().collect(Collectors.toList());
//        return mTrades;
    }

    public List<Trade> getCachedTrades() {
        Log.i(TAG, "getCachedTrades...");
        Log.i(TAG, "items found: " + mTrades.size());
        return mTrades;
    }

    public List<Trade> findNewTrades(int poolSize, int num) {
        Log.i(TAG, "findNewTrades...");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.whereEqualTo("user", "15539509926");
        query.orderByDescending("createdAt");
        query.limit(poolSize);

        return getRandomItems(query, num);
    }

    public List<Trade> findHotTrades(int poolSize, int num) {
        Log.i(TAG, "findHotTrades...");

        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);
        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "trade");
        query.whereEqualTo("online", "online");
        query.whereEqualTo("user", "15539509926");
        query.addDescendingOrder("totalVotes");
        query.limit(poolSize);

        return getRandomItems(query, num);
    }

    private List<Trade> getRandomItems(AVQuery<Trade> query, int num) {
        try {
            List<Trade> items = query.find();
            Log.i(TAG, "items found: " + items.size());
            Collections.shuffle(items);
            num = Math.min(items.size(), num);
            return items.subList(0, num);
        } catch (Exception exception) {
            Log.e(TAG, "getRandomItems failed." + exception);
            return Collections.emptyList();
        }
    }

    public List<Trade> findGlobalAds(int poolSize, int num) {
        AVQuery<Trade> query = AVQuery.getQuery(Trade.TRADE_CLASS);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(Calendar.getInstance().getTime());

        query.whereEqualTo("status", "on");
        query.whereEqualTo("type", "ad");
        query.whereEqualTo("range", "global");
        query.whereEqualTo("online", "online");
        query.whereLessThanOrEqualTo("adStartDate", today);
        query.whereNotContainedIn("objectId", mPush.getGlobalAdIgnores());
        query.orderByDescending("createdAt");
        query.limit(poolSize);

        List<Trade> items = getRandomItems(query, num);
        for (Trade item : items) {
            mPush.addGlobalAdIgnores(item.getObjectId());
        }

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