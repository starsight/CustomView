package com.wenjiehe.customview;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import com.wenjiehe.customview.view.FlipView;
import com.wenjiehe.customview.view.JiKeThumbUpView;
import com.wenjiehe.customview.view.ruler.BooheeRuler;
import com.wenjiehe.customview.view.ruler.KgNumberLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    private BooheeRuler mBooheeRuler;
    private KgNumberLayout mKgNumberLayout;

    private EditText editText;
    private Button buttonNum;
    private JiKeThumbUpView jiKeThumbUpView;

    private FlipView flipview;

    @LayoutRes
    int sampleLayoutRes;

    public static PageFragment newInstance(@LayoutRes int sampleLayoutRes) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt("sampleLayoutRes", sampleLayoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        ViewStub sampleStub = (ViewStub) view.findViewById(R.id.sampleStub);
        sampleStub.setLayoutResource(sampleLayoutRes);
        sampleStub.inflate();

        if(sampleLayoutRes==R.layout.layout_ruler){
            mBooheeRuler =  view.findViewById(R.id.br);
            mKgNumberLayout = view.findViewById(R.id.knl);
            mKgNumberLayout.bindRuler(mBooheeRuler);
        }else if(sampleLayoutRes == R.layout.layout_jikethumbupview){
            editText = view.findViewById(R.id.edit_text);
            buttonNum = view.findViewById(R.id.button_num);
            jiKeThumbUpView = view.findViewById(R.id.jikethumbview);
            buttonNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.valueOf(editText.getText().toString().trim());
                    jiKeThumbUpView.setCount(num).setThumbUp(false);
                }
            });
        }else if(sampleLayoutRes == R.layout.layout_flipview){
            flipview = view.findViewById(R.id.flipview);
            flipview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipview.start();
                }
            });
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            sampleLayoutRes = args.getInt("sampleLayoutRes");
        }

    }

}
