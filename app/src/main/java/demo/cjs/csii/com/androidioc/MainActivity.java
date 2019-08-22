package demo.cjs.csii.com.androidioc;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import demo.cjs.csii.com.ioc.InjectUtil;
import demo.cjs.csii.com.ioc.annotation.BindView;
import demo.cjs.csii.com.ioc.annotation.ClickEvent;
import demo.cjs.csii.com.ioc.annotation.ContentViewId;
import demo.cjs.csii.com.ioc.annotation.LongClickEvent;

@ContentViewId(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    private TextView tv;
    @BindView(R.id.ed)
    private EditText ed;
    @BindView(R.id.tv2)
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        InjectUtil.inject(this);
        tv.setText("点我呀");
        tv2.setText("HHHHHHH");
        ed.setHint("输入文字");

        SimpleFragment fragment=new SimpleFragment();
        Bundle b=new Bundle();
        b.putString("text","hello");
        fragment.setArguments(b);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content,fragment,"Simple");
        transaction.commit();
    }

    @ClickEvent({R.id.tv, R.id.tv2})
    @LongClickEvent(value = R.id.tv2, consume = true)
    @Deprecated
    @JavascriptInterface
    public void onC(View v) {
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
    }

}
