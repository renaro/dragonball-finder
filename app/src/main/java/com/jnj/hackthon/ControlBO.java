package com.jnj.hackthon;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by renarosantos on 09/12/16.
 */
public class ControlBO {


    private static final int UP_ANGLE = 270;
    private static final int DOWN_ANGLE = 90;
    private static final int RIGHT_ANGLE = 0;
    private static final int LEFT_ANGLE = 180;
    private static final int[] ANGLES = new int[]{RIGHT_ANGLE, LEFT_ANGLE, UP_ANGLE, DOWN_ANGLE};
    private final Service mService;
    private final int MAX_BALLS = 7;
    private final ArrayList<Integer> mVisiteds = new ArrayList<>();
    private long[] lastDistance = new long[]{Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE};
    private int balls = 0;
    private long mLastY;
    private long mLastX;
    private long mCurrentDistance = Long.MAX_VALUE;
    private long mMinDistance = Long.MAX_VALUE;
    private int mNextDirection = 0;
    private long mLastSeenDistance = 0;
    private int mCurrentAngleIndex = 0;


    public ControlBO() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
        mService = retrofit.create(Service.class);
    }

    public void move(int move, final int angle) {
        try {
            mService.control("6aa9df2ee5f5d7759d039c7c0d6dfaa0", "" + move, "" + angle).execute();
        } catch (IOException e) {
            Log.e("Error", "move", e);
        }
    }

    public RadarResponse radar() {
        try {
            final RadarResponse result = mService.radar("6aa9df2ee5f5d7759d039c7c0d6dfaa0").execute().body();
            mLastX = result.cordX;
            mLastY = result.cordY;
            mCurrentDistance = result.radar;
            compute();
            return result;
        } catch (IOException e) {
            Log.e("Error", "move", e);
            return null;
        }
    }

    public void compute() {

        if (mLastSeenDistance != mCurrentDistance) {
            mLastSeenDistance = mCurrentDistance;

            if (mCurrentDistance != Long.MAX_VALUE && (mCurrentDistance <= 25)) {
                balls++;
                return;
            }

            if (mCurrentDistance <= mMinDistance) {
                mVisiteds.clear();
//                updatePositions();
                mMinDistance = mCurrentDistance;
            } else {
                mVisiteds.add(mNextDirection);
                move(1, ANGLES[getOposit(mNextDirection)]);
                move(0, ANGLES[getOposit(mNextDirection)]);
                mNextDirection = getDirections();
                move(1, mNextDirection);
            }
        }
    }

    private void updatePositions() {
        long lastMin = mMinDistance;
        switch (mNextDirection) {
            case RIGHT_ANGLE:
                lastDistance[0] = mCurrentDistance;
                lastDistance[1] = lastMin;
                break;
            case LEFT_ANGLE:
                lastDistance[1] = mCurrentDistance;
                lastDistance[0] = lastMin;
                break;
            case UP_ANGLE:
                lastDistance[2] = mCurrentAngleIndex;
                lastDistance[3] = lastMin;
                break;
            case DOWN_ANGLE:
                lastDistance[3] = mCurrentAngleIndex;
                lastDistance[2] = lastMin;
                break;
        }
    }


    private int getNextBasedOnIndex(final Integer remove, final int newIndex) {
        mVisiteds.remove(remove);
        mVisiteds.add(newIndex);
        return ANGLES[newIndex];
    }


    private int getDirections() {
        for (int i = 0; i < ANGLES.length; i++) {
            if (!mVisiteds.contains(i)) {
                mVisiteds.add(i);
                return ANGLES[i];
            }
        }
        return ANGLES[0];
    }


//        int nextToVisit = ANGLES[currentAngle % ANGLES.length];
//        if (mNextDirection == RIGHT_ANGLE) {
//            if (!mVisiteds.contains(UP_ANGLE)) {
//                nextToVisit = getNextBasedOnIndex(1, 2);
//            } else if (!mVisiteds.contains(ANGLES[1])) {
//                nextToVisit = getNextBasedOnIndex(2, 1);
//            }
//        } else if (mNextDirection == UP_ANGLE) {
//            if (!mVisiteds.contains(LEFT_ANGLE)) {
//                nextToVisit = getNextBasedOnIndex(3, 1);
//            } else if (!mVisiteds.contains(ANGLES[3])) {
//                nextToVisit = getNextBasedOnIndex(1, 3);
//            }
//        } else if (mNextDirection == LEFT_ANGLE) {
//            if (!mVisiteds.contains(DOWN_ANGLE)) {
//                nextToVisit = getNextBasedOnIndex(1, 3);
//            } else if (!mVisiteds.contains(RIGHT_ANGLE)) {
//                nextToVisit = getNextBasedOnIndex(3, 0);
//            }
//        } else if (mNextDirection == DOWN_ANGLE) {
//            if (!mVisiteds.contains(RIGHT_ANGLE)) {
//                nextToVisit = getNextBasedOnIndex(2, 0);
//            } else if (!mVisiteds.contains(UP_ANGLE)) {
//                nextToVisit = getNextBasedOnIndex(0, 2);
//            }
//        }
//
//        return nextToVisit;


    private int getOposit(final int bestIndex) {
        if (bestIndex == 0) return 1;
        if (bestIndex == 1) return 0;
        if (bestIndex == 2) return 3;
        else return 2;
    }


}
