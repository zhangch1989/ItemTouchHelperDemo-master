package com.zch.tooldemos.doubleseekbar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jaygoo.widget.RangeSeekBar;

import com.zch.tooldemos.R;

/**
 * Created by Zch on 2019/4/17 14:54.
 */

public class Activity_DoubleSeekbar  extends Activity{

    private SeekBarPressure seekBarPressures;
    DoubleHeadedDragonBar bar1;
    RangeSeekBar seekBar;
    TextView tv_min, tv_max;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubleseekbar);

        seekBarPressures = (SeekBarPressure) findViewById(R.id.seekbar);

        seekBarPressures.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {
//                isScreen = true;
            }

            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {

//                editTexts_min.setText((int) progressLow + "");


//                editTexts_max.setText((int) progressHigh + "");

            }

            @Override
            public void onProgressAfter() {
//                isScreen = false;
            }
        });

        bar1 = findViewById(R.id.bar1);
        bar1.setUnit("2019-04-01", "2019-04-31");

        bar1.setMinValue(10);
        bar1.setMaxValue(80);

        seekBar = (RangeSeekBar) findViewById(R.id.seekbar1);
        tv_min = (TextView) findViewById(R.id.tv_min);
        tv_max = (TextView) findViewById(R.id.tv_max);
        seekBar.setRange(1, 31);
        tv_min.setText("2019-04-01");
        tv_max.setText("2019-04-31");
        seekBar.setValue(2,21);
        seekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                tv_min.setText("2019-04-" + (int)min);
                tv_max.setText("2019-04-" + (int)max);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
    }

}
