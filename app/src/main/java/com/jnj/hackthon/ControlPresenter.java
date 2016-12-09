package com.jnj.hackthon;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jnj.hackthon.task.AppTask;
import com.jnj.hackthon.task.TaskExecutor;

/**
 * Created by renarosantos on 09/12/16.
 */
public class ControlPresenter {

    private static final int UP_ANGLE = 270;
    private static final int DOWN_ANGLE = 90;
    private static final int RIGHT_ANGLE = 0;
    private static final int LEFT_ANGLE = 180;
    private static final int[] ANGLES = new int[]{RIGHT_ANGLE, LEFT_ANGLE, UP_ANGLE, DOWN_ANGLE};
    private final ControlBO mBo;
    private final TaskExecutor mExecutor;
    private final ControlView mView;



    public ControlPresenter(@NonNull final ControlView view, @NonNull final ControlBO bo, @NonNull final TaskExecutor executor) {
        mBo = bo;
        mExecutor = executor;
        mView = view;
    }

    public void onUpClicked() {
        mExecutor.async(new MoveTask(UP_ANGLE));
    }

    public void onRightClicked() {
        mExecutor.async(new MoveTask(RIGHT_ANGLE));
    }

    public void onLeftClicked() {
        mExecutor.async(new MoveTask(LEFT_ANGLE));
    }

    public void onDownClicked() {
        mExecutor.async(new MoveTask(DOWN_ANGLE));
    }

    public void radar() {
        mExecutor.async(new RadarTask());
    }

    public class MoveTask extends AppTask.SimpleAppTask {

        private final double mAngle;

        public MoveTask(final double angle) {
            mAngle = angle;
        }

        @Override
        public void simpleExecute() {
            mBo.move(1, (int) mAngle);
        }

        @Override
        public void onPostExecute() {

        }
    }

    public class RadarTask implements AppTask<RadarResponse> {

        @Override
        public RadarResponse execute() {
            return mBo.radar();
        }

        @Override
        public void onPostExecute(@Nullable final RadarResponse result) {
            if (result != null) {
                mView.updateRadar(result);
            }

        }
    }

}
