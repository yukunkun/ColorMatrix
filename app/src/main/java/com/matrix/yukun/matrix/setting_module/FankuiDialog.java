package com.matrix.yukun.matrix.setting_module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;


import com.matrix.yukun.matrix.R;

import org.json.JSONObject;

/**
 * Created by yukun on 16-8-18.
 */
public class FankuiDialog extends DialogFragment {

    private Button buttonCancel;
    private Button buttonSure;
    private long nid;
    private long uid;
    public static FankuiDialog newInstance(long nid) {
        FankuiDialog fragment = new FankuiDialog();
        Bundle bundle=new Bundle();
        bundle.putLong("nid",nid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_notes_dia, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view) {
        buttonCancel = (Button) view.findViewById(R.id.comment_dialog_grade_cancel);
        buttonSure = (Button) view.findViewById(R.id.comment_dialog_grade_ok);

    }

    private void setListener() {

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getDialog().dismiss();
            }
        });

        buttonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

}
