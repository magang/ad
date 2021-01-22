package com.chanlin.ad.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.chanlin.ad.config.PushConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.AVSaveOption;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class User {
    private static final String TAG = User.class.getName();

    private static AVUser currUser;
    private static int userCredit = 0;

    private static int getQuality(Context ctx) {
        return PushConfig.get(ctx).getWifiImageQuality();
    }

    private static String getImageThumbnailUrlBase(Context ctx, int width, int height) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            AVFile avFile = currUser.getAVFile("image");
            if (avFile == null) {
                return null;
            } else {
                return avFile.getThumbnailUrl(true, // boolean scaleToFit
                                              width,
                                              height,
                                              getQuality(ctx),
                                              "jpg");
            }
        } else {
            return null;
        }
    }

    public static String getImageThumbnailUrl(Context ctx) {
        return getImageThumbnailUrlBase(ctx,
            PushConfig.getWidth(), PushConfig.getHeight());
    }

    public static String getImageThumbnailUrlSmall(Context ctx) {
        return getImageThumbnailUrlBase(ctx,
            120, 120);
    }

    public static String getImageThumbnailUrlMedium(Context ctx) {
        return getImageThumbnailUrlBase(ctx,
            PushConfig.getWidth(), 400);
    }

    public static String getIMImageThumbnailUrl(Context ctx, AVUser user) {
        if (user != null) {
            AVFile avFile = user.getAVFile("image");
            if (avFile == null) {
                return null;
            } else {
                return avFile.getThumbnailUrl(true, // boolean scaleToFit
                                              120,
                                              120,
                                              getQuality(ctx),
                                              "jpg");
            }
        } else {
            return null;
        }
    }

    public static String getName(AVUser user) {
        if (user != null) {
            String name = user.getString("nickname");
            if (name == null || name.equals("")) {
                return null;
            }

            return name;
        } else {
            return null;
        }
    }

    public static boolean isLoggedIn() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            return true;
        } else {
            return false;
        }
    }

    public static void addCoin(int val) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            currUser.setFetchWhenSave(true);
            currUser.increment("coin", val);
            save();
        }
    }

    public static void addTicket(int val) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            currUser.setFetchWhenSave(true);
            currUser.increment("ticket", val);
            save();
        }
    }

    public static void addXjc(int val) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            currUser.setFetchWhenSave(true);
            currUser.increment("xjc", val);
            save();
        }
    }

    public static void updateCoin() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            currUser.setFetchWhenSave(true);
            currUser.increment("coin", 0);
            save();
        }
    }

    public static void updateReport() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            currUser.setFetchWhenSave(true);
            currUser.increment("report", 0);
            save();
        }
    }

    public static void updateLastLoginDate(String date) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            currUser.setFetchWhenSave(true);
            currUser.put("lastLoginDate", date);
            save();
        }
    }

    public static void updateXjcAddress(String address) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            currUser.setFetchWhenSave(true);
            currUser.put("xjcAddress", address);
            save();
        }
    }

    private static AVSaveOption getSaveOption() {
        AVSaveOption option = new AVSaveOption();
        option.setFetchWhenSave(true);
        return option;
    }

    public static void syncUser() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            Log.d(TAG, "sync user...");
            Log.d(TAG, "current user:" + currUser.toString());

            currUser.increment("coin", 0.0);
            currUser.increment("xjc", 0.0);
            currUser.increment("ticket", 0.0);
            currUser.increment("report", 0);
            currUser.increment("rank", 0);
            currUser.increment("inviteNum", 0);
            save();
        }
    }

    public static void save() {
        currUser.setFetchWhenSave(true);
        currUser.saveInBackground().subscribe(new Observer<AVObject>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(AVObject avUser) {
                Log.d(TAG, "current user: " + currUser.toString());
            }
            public void onError(Throwable throwable) {
            }
            public void onComplete() {}
        });

    }

    public static void calCredit() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            int score = currUser.getInt("xjc");
            int report = currUser.getInt("report");
            score = (int) (Math.round( score * (1 - report/10.0) ));
            if (score <= 0) {
                userCredit = 0;
            } else if (score <= 100) {
                userCredit = 1;
            } else if (score <= 300) {
                userCredit = 2;
            } else if (score <= 900) {
                userCredit = 3;
            } else if (score <= 1800) {
                userCredit = 4;
            } else if (score <= 3600) {
                userCredit = 5;
            } else if (score <= 7200) {
                userCredit = 6;
            } else if (score <= 10800) {
                userCredit = 7;
            } else if (score <= 14400) {
                userCredit = 8;
            } else if (score <= 18000) {
                userCredit = 9;
            } else{
                userCredit = 9;
            }
        } else {
            userCredit = 0;
        }
    }

    public static int getCredit() {
        calCredit();
        return userCredit;
    }

    public static String getCreditStr() {
        calCredit();
        return "L" + Integer.toString(userCredit);
    }

    public static void syncRank() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            int rank = currUser.getInt("rank");
            if (rank < 4) {
                if (isVip() || isShop()) {
                    currUser.put("vip", false);
                    currUser.put("shop", 0);
                    save();
                }
            } else if (rank == 4) {
                if (!isVip()) {
                    currUser.put("vip", true);
                    currUser.put("shop", 0);
                    save();
                }
            } else if (rank == 5) {
                if (!isShop()) {
                    currUser.put("vip", false);
                    currUser.put("shop", 1);
                    save();
                }
            } else if (isMemberUser()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                int currDate =  Integer.parseInt(sdf.format(new Date()));
                int endDate = currUser.getInt("memberEnd");

                Log.d("syncRank", "currDate: " + currDate);
                Log.d("syncRank", "endDate: " + endDate);

                if (currDate > endDate) {
                    currUser.setFetchWhenSave(true);
                    currUser.put("rank", getRank());
                    currUser.put("memberEnd", 0);
                    save();
                }
            } else if (isAdUser()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                int currDate =  Integer.parseInt(sdf.format(new Date()));
                int endDate = currUser.getInt("adEnd");

                Log.d("syncRank", "currDate: " + currDate);
                Log.d("syncRank", "endDate: " + endDate);

                if (currDate > endDate) {
                    currUser.setFetchWhenSave(true);
                    currUser.put("rank", getRank());
                    currUser.put("adEnd", 0);
                    save();
                }
            } else if (isReportUnlimitedRank()) {
                currUser.put("vip", false);
                currUser.put("shop", 0);
                save();
            }
        }
    }

    public static boolean isBadUser(Context ctx, boolean hasToast) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            if (currUser.getInt("report") >= 10) {
                if (hasToast) {
                    Toast.makeText(ctx, "该账户已被禁言！", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static boolean isVip() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("vip")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isShop() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null ) {
            int shop = currUser.getInt("shop");
            if (shop == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isSelfCertificatedShop() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null ) {
            int shop = currUser.getInt("selfShop");
            if (shop == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static void setSelfCertificatedShop(int val) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            currUser.put("selfShop", val);
            save();
        }
    }

    public static boolean isHighRank() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            if (currUser.getInt("rank") >= 4) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static boolean isReportUnlimitedRank() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            if (currUser.getInt("rank") > 7) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static boolean isAdmin() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            if (currUser.getInt("rank") == 10) {
                return true;
            } else {
                return false;
            }
        }
        
        return false;
    }

    public static boolean isAdUser() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            int rank = currUser.getInt("rank");
            if (rank == 6 || rank == 7) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
    
    public static boolean isMemberUser() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            int rank = currUser.getInt("rank");
            if (rank == 4 || rank == 5 || rank == 6 || rank == 7) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static String getXjcAddress() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            String xjcAddress = currUser.getString("xjcAddress");
            return xjcAddress;
        }

        return "";
    }

    public static int getRealRank() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
            int rank = currUser.getInt("rank");
            return rank;
        }

        return 0;
    }    

    public static int getRank() {
        if (isVip()) {
            return 4;
        } else if (isShop()) {
            return 5;
        } else {
            return 3;
        }
    }

    public static boolean isLeader() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getBoolean("leader")) {
            return true;
        } else {
            return false;
        }
    }

    public static String getPhoneNumber() {
        if (isLoggedIn()) {
            return AVUser.getCurrentUser().getString("mobilePhoneNumber");
        } else {
            return "";
        }
    }

    public static boolean isNameBlank() {
        currUser = AVUser.getCurrentUser();
        if (currUser != null && currUser.getString("nickname") != null
            && !currUser.getString("nickname").equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static String getName() {
        if (isNameBlank()) {
            return "";
        } else {
            return AVUser.getCurrentUser().getString("nickname");
        }
    }

    public static String getAdEnd(String spliter) {
        int intAdEnd = AVUser.getCurrentUser().getInt("adEnd");
        if (intAdEnd == 0) {
            intAdEnd = 20000101;
        }

        String strAdEnd = Integer.toString(intAdEnd);
        String year = strAdEnd.substring(0,4);
        String mouth = strAdEnd.substring(4,6);
        String day = strAdEnd.substring(6,8);
        return year + spliter + mouth + spliter + day;
    }
    
    public static String getMemberEnd(String spliter) {
        int intMemberEnd = AVUser.getCurrentUser().getInt("memberEnd");
        if (intMemberEnd == 0) {
            intMemberEnd = 20000101;
        }

        String strMemberEnd = Integer.toString(intMemberEnd);
        String year = strMemberEnd.substring(0,4);
        String mouth = strMemberEnd.substring(4,6);
        String day = strMemberEnd.substring(6,8);
        return year + spliter + mouth + spliter + day;
    }    

    public static void updateLocalParams(Context ctx) {
        currUser = AVUser.getCurrentUser();
        if (currUser != null) {
            String lastLoginDate = currUser.getString("lastLoginDate");
            if (lastLoginDate != null && !lastLoginDate.equals("")) {
                PushConfig.get(ctx).setLastLoginDate(lastLoginDate);
            }
        }
    }

    public static void logout() {
        AVUser.logOut();
    }
}