package demo.cjs.csii.com.androidioc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import demo.cjs.csii.com.ioc.InjectUtil;
import demo.cjs.csii.com.ioc.annotation.BindView;
import demo.cjs.csii.com.ioc.annotation.ClickEvent;

/**
 * 描述:
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月21日 20:16
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class SimpleFragment extends Fragment {
    private Activity mActivity;
    private Bundle data;

    @BindView(R.id.tv)
    private TextView tv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.from(mActivity).inflate(R.layout.frag_simple, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        InjectUtil.inject(this, view);
        data = getArguments();
        if (data != null) {
            String text = data.getString("text");
            Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
            tv.setText(text);
        }
    }

    @ClickEvent(R.id.tv)
    public void onTvClick(View v) {
        Toast.makeText(mActivity, "我是TV", Toast.LENGTH_SHORT).show();
    }
}
