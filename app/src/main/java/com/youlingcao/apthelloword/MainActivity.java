package com.youlingcao.apthelloword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.youlingcao.annotation.BindView;
import com.youlingcao.annotation.api.LCJViewBinder;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LCJViewBinder.bind(this);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LCJViewBinder.unBind(this);
    }

}
