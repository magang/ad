package com.chanlin.ad.data;

import android.content.Context;
import android.util.Log;

import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.util.MyTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.annotation.AVClassName;
import cn.leancloud.types.AVGeoPoint;

@AVClassName(Trade.TRADE_CLASS)
public class Trade extends AVObject {

    private AVObject mTrade;

    public static final String TAG = "Trade";
    public static final String TRADE_CLASS = "Trade";
    private static final String ID_KEY = "objectId";
    private static final String STATUS_KEY = "status";
    private static final String MODE_KEY = "mode";
    private static final String ONLINE_KEY = "online";
    private static final String TAG_KEY = "tag";
    private static final String LINK_KEY = "link";
    private static final String TITLE_KEY = "title";
    private static final String VIP_KEY = "vip";
    private static final String SHOP_KEY = "shop";
    private static final String LEADER_KEY = "leader";
    private static final String RANK_KEY = "rank";
    private static final String CREDIT_KEY = "credit";
    private static final String AD_RANK_KEY = "adRank";
    private static final String AD_START_DATE_KEY = "adStartDate";
    private static final String AD_END_DATE_KEY = "adEndDate";
    private static final String AD_STATUS_KEY = "adStatus";
    private static final String IS_SHOP_HOME_PAGE_KEY = "isShopHomePage";
    private static final String TYPE_KEY = "type";
    private static final String RANGE_KEY = "range";
    private static final String LIKE_COUNT_KEY = "like";
    private static final String AWARD_COUNT_KEY = "award";
    private static final String VOTE_COUNT_KEY = "totalVotes";
    private static final String SCORE_KEY = "avgScore";
    private static final String COMMENT_COUNT_KEY = "comment";
    private static final String USER_LIKE_COUNT_KEY = "userLike";
    private static final String USER_AWARD_COUNT_KEY = "userAward";
    private static final String USER_COMMENT_COUNT_KEY = "userComment";
    private static final String NAV_ENABLED_KEY = "navEnabled";
    private static final String DETAILS_KEY = "details";
    private static final String PRICE_KEY = "price";
    private static final String CONTACT_KEY = "contact";
    private static final String USER_KEY = "user";
    private static final String POSITION_KEY = "position";
    private static final String LOCATION_KEY = "location";
    private static final String CREATED_KEY = "createdAt";
    private static final String UPDATED_KEY = "updatedAt";
    private static final String IMAGE_KEY = "image";
    private static final String IMAGE_COUNT_KEY = "imageCount";
    private static final String USER_IMAGE_KEY = "userImage";
    private static final String USER_NAME_KEY = "userName";
    private static final String USER_CREDIT_KEY = "userCredit";

    public Trade() {
    }

    public void save() {
        mTrade.saveInBackground();
//        mTrade.saveInBackground(new SaveCallback() {
////            public void done(AVException e) {
////                if (e == null) {
////                    Log.d(TAG, "save successfully.");
////                } else {
////                    Log.e(TAG, "save failed:" + e.getMessage());
////                }
////            }
////        });
    }

    public String getImageUrl() {
        AVFile avFile = this.getAVFile(IMAGE_KEY);
        if (avFile == null) {
            return null;
        } else {
            return avFile.getUrl();
        }
    }

    public String getImageThumbnailUrlBase(Context ctx, int position, int width, int height) {
        String imageName;
        if (position == -1) {
            imageName = "image";
        } else {
            imageName = "image" + position;
        }
        AVFile avFile = this.getAVFile(imageName);
        if (avFile == null) {
            return null;
        } else {
            return avFile.getThumbnailUrl(true, // boolean scaleToFit
                    width,
                    height,
                    100,
                    "jpg");
        }
    }

    public String getImageThumbnailUrl(Context ctx, int position) {
        return getImageThumbnailUrlBase(ctx, position,
                PushConfig.getWidth(), PushConfig.getHeight());
    }

    public String getImageThumbnailUrlSmall(Context ctx, int position) {
        return getImageThumbnailUrlBase(ctx, position,
                200, 200);
    }

    public String getImageThumbnailUrlMedium(Context ctx, int position) {
        return getImageThumbnailUrlBase(ctx, position,
                PushConfig.getWidth(), 400);
    }

    public AVFile getAVFile() {
        return this.getAVFile(IMAGE_KEY);
    }

    public String getStatus() {
        return this.getString(STATUS_KEY);
    }

    public String getUserName() {
        String name = this.getString(USER_NAME_KEY);
        if (name == null || name.equals("")) {
            name = "匿名用户";
        }

        return name;
    }

    public int getUserCreditInt() {
        return this.getInt(USER_CREDIT_KEY);
    }

    public String getUserCreditStr() {
        return "L" + Integer.toString(this.getInt(USER_CREDIT_KEY));
    }

    public String getScoreStr() {
        return Double.toString(this.getDouble(SCORE_KEY));
    }

    public double getScore() {
        return this.getDouble(SCORE_KEY);
    }

    public String getUserNameRaw() {
        return this.getString(USER_NAME_KEY);
    }

    public void setStatus(String status) {
        this.put(STATUS_KEY, status);
    }

    public String getMode() {
        return this.getString(MODE_KEY);
    }

    public void setMode(String mode) {
        this.put(MODE_KEY, mode);
    }

    public void setOnline(String status) {
        this.put(ONLINE_KEY, status);
    }

    public String getType() {
        return this.getString(TYPE_KEY);
    }

    public void setType(String type) {
        this.put(TYPE_KEY, type);
    }

    public String getRange() {
        return this.getString(RANGE_KEY);
    }

    public void setRange(String type) {
        this.put(RANGE_KEY, type);
    }

    public int getAdRank() {
        return this.getInt(AD_RANK_KEY);
    }

    public void setAdRank(int rank) {
        this.put(AD_RANK_KEY, rank);
    }

    public String getAdEndDate() {
        return this.getString(AD_END_DATE_KEY);
    }

    public String getAdStartDate() {
        return this.getString(AD_START_DATE_KEY);
    }

    public void setAdEndDate(String date) {
        this.put(AD_END_DATE_KEY, date);
    }

    public String getTitle() {
        return this.getString(TITLE_KEY);
    }

    public void setTitle(String title) {
        this.put(TITLE_KEY, title);
    }

    public String getDetails() {
        return this.getString(DETAILS_KEY);
    }

    public String getForwardDetails() {
        return "#转自 " + getUserName() +"# " + getDetails();
    }

    public String getShortDetails(int max) {
        String details = this.getString(DETAILS_KEY);
        int len = details.length();
        if (len > max) {
            len = max;
        }

        return details.substring(0, len).trim() + "...";
    }

    public void setDetails(String details) {
        this.put(DETAILS_KEY, details);
    }

    public String getPrice() {
        return this.getString(PRICE_KEY);
    }

    public void setPrice(String price) {
        this.put(PRICE_KEY, price);
    }

    public String getLink() {
        return this.getString(LINK_KEY);
    }

    public String getContact() {
        return this.getString(CONTACT_KEY);
    }

    public int getImageCount() {
        return this.getInt(IMAGE_COUNT_KEY);
    }

    public int getLikeCount() {
        return this.getInt(LIKE_COUNT_KEY);
    }

    public int getUserLikeCount() {
        return this.getInt(USER_LIKE_COUNT_KEY);
    }

    public int getRank() {
        return this.getInt(RANK_KEY);
    }

    public int getCredit() {
        return this.getInt(CREDIT_KEY);
    }

    public boolean isHighRank() {
        if (getRank() > 6) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAdmin() {
        if (getRank() == 10) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNavEnabled() {
        return this.getBoolean(NAV_ENABLED_KEY);
    }

    public int getVoteCount() {
        return (int) (this.getDouble(VOTE_COUNT_KEY));
    }

    public int getAwardCount() {
        return this.getInt(AWARD_COUNT_KEY);
    }

    public int getUserAwardCount() {
        return this.getInt(USER_AWARD_COUNT_KEY);
    }

    public int getCommentCount() {
        return this.getInt(COMMENT_COUNT_KEY);
    }

    public void incrCommentCount() {
        int count =  getCommentCount() + 1;
        this.put(COMMENT_COUNT_KEY, count);
    }

    public int getUserCommentCount() {
        return this.getInt(USER_COMMENT_COUNT_KEY);
    }

    public void incrUserCommentCount() {
        int count =  getUserCommentCount() + 1;
        this.put(USER_COMMENT_COUNT_KEY, count);
    }

    public String getUser() {
        return this.getString(USER_KEY);
    }

    public void setContact(String contact) {
        this.put(CONTACT_KEY, contact);
    }

    public String getPosition() {
        return this.getString(POSITION_KEY);
    }

    public void setPosition(String position) {
        this.put(POSITION_KEY, position);
    }

    public boolean isAdOn() {
        String status = this.getString(AD_STATUS_KEY);
        if (status.equals("on")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOn() {
        String status = this.getString(STATUS_KEY);
        if (status.equals("on")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOnline() {
        String online = this.getString(ONLINE_KEY);
        if (online.equals("online")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAd() {
        if (getType().equals("ad")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGlobalAd() {
        if (isAd() && getRange().equals("global")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLocalAd() {
        if (isAd() && getRange().equals("local")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBuy() {
        if (getMode().equals("buy")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isChatRoom() {
        if (getTag().indexOf("社区聊天室") != -1) {
            return true;
        } else {
            return false;
        }
    }

    public String getSourceTag() {
        int val = 3;
        String sourceTag = "";

        String[] aa = getTag().split("__");
        int len = aa.length;

        int index = len;

        if (len > val) {
            index = len - val;
        }
        for (int i = 0; i < index; i++ ) {
            if (!aa[i].trim().equals("")) {
                sourceTag += aa[i].trim() + " ";
            }
        }

        return sourceTag.trim();
    }

    public String getTitleAndPrice() {
        String body = "";

        String[] aa = getDetails().split(" ");
        int len = aa.length;
        int index = 0;

        for (int i = 0; i < len; i++ ) {
            if (index > 2) {break;}
            if (!aa[i].trim().equals("")) {
                body += aa[i].trim() + " ";
                index++;
            }
        }

        return body.trim();
    }

    public String getProductTitle() {
        String body = "";

        String[] aa = getDetails().split(" ");
        int len = aa.length;
        int index = 0;

        for (int i = 0; i < len; i++ ) {
            if (index > 0) {break;}
            if (!aa[i].trim().equals("")) {
                body += aa[i].trim() + " ";
                index++;
            }
        }

        return body.trim();
    }

    public String getTag() {
        String tag = this.getString(TAG_KEY);
        if (tag == null) {
            return "";
        } else {
            return tag;
        }
    }

    public Boolean isVip() {
        Boolean vip = this.getBoolean(VIP_KEY);
        if (vip == null) {
            return false;
        } else {
            return vip;
        }
    }

    public Boolean isLeader() {
        Boolean leader = this.getBoolean(LEADER_KEY);
        if (leader == null) {
            return false;
        } else {
            return leader;
        }
    }

    public Boolean isShop() {
        int shop = this.getInt(SHOP_KEY);
        if (shop == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setShopHomePage(boolean state) {
        this.put(IS_SHOP_HOME_PAGE_KEY, state);
    }

    public Boolean isShopHomePage() {
        Boolean state = this.getBoolean(IS_SHOP_HOME_PAGE_KEY);
        if (state == null) {
            return false;
        } else {
            return state;
        }
    }

    public String getAge() {
        Date startDate = this.getCreatedAt();
        Date endDate = Calendar.getInstance().getTime();
        return MyTime.getAge(startDate, endDate);
    }

    public String calAdEndDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, +days);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(cal.getTime());
    }

    public int calAdLeftDays() {
        int daysLeft = 0;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = Calendar.getInstance().getTime();
            Date endDate = formatter.parse(getAdEndDate());
            long time = endDate.getTime() - date.getTime();

            if (time > 0) {
                daysLeft = (int) (time/(24*60*60*1000));
            }

        } catch (Exception e) {
            Log.e("Trade", "calAdLeftDays failed: " + e.getMessage());
            daysLeft = 0;
        }

        return daysLeft;
    }

    public boolean isAdExpired() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = Calendar.getInstance().getTime();
            Date endDate= formatter.parse(getAdEndDate());
            long time = date.getTime() - endDate.getTime();

            if (time > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Log.e("Trade", "isAdExpired failed: " + e.getMessage());
            return false;
        }
    }

    public boolean isLocalAdExpired() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date now = new Date();
            String nowStr = formatter.format(now);
            Date date= formatter.parse(nowStr);
            Date endDate= formatter.parse(getAdStartDate());
            long time = date.getTime() - endDate.getTime();

            if (time > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("Trade", "isLocalAdExpired failed: " + e.getMessage());
            return false;
        }
    }

    public AVGeoPoint getLocation() {
        return (AVGeoPoint)this.get("location");
    }

    public Double getLatitude() {
        AVGeoPoint location = (AVGeoPoint) this.get("location");
        return location.getLatitude();
    }

    public Double getLongitude() {
        AVGeoPoint location = (AVGeoPoint) this.get("location");
        return location.getLongitude();
    }

}