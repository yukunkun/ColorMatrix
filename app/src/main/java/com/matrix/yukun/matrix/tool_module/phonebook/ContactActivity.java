package com.matrix.yukun.matrix.tool_module.phonebook;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.Cn2Spell;
import com.matrix.yukun.matrix.main_module.utils.ISideBarSelectCallBack;
import com.matrix.yukun.matrix.main_module.views.SideBar;
import com.matrix.yukun.matrix.util.PermissionUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactActivity extends BaseActivity implements View.OnClickListener, ISideBarSelectCallBack {

    private ImageView mIvBack;
    private TextView mTvContact;
    private ListView mRvList;
    private List<PhoneNumberBean> mPhoneNumberBeans;
    private List<PhoneBean> mPhoneBeans=new ArrayList<>();
    private RVContactAdapter mRvContactAdapter;
    private SideBar mSideBar;

    @Override
    public int getLayout() {
        return R.layout.activity_contact;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mTvContact = findViewById(R.id.tv_contact);
        mRvList = findViewById(R.id.rv_phone_list);
        mSideBar = findViewById(R.id.sidebar);
        mRvContactAdapter = new RVContactAdapter(this,mPhoneBeans);
        mRvList.setAdapter(mRvContactAdapter);
    }

    @Override
    public void initDate() {
        getPermission();
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mTvContact.setOnClickListener(this);
        mSideBar.setOnStrSelectCallBack(this);
        //dialog
        mRvContactAdapter.setMoreCallBack(new RVContactAdapter.MoreCallBack() {
            @Override
            public void onMoreCallBack(int pos, PhoneNumberBean phoneNumberBean) {
                PhoneDialog phoneDialog= PhoneDialog.getInstance(phoneNumberBean.getPhoneNum(),phoneNumberBean.contactName);
                phoneDialog.show(getSupportFragmentManager(),"");
            }
        });
    }

    private void getPermission() {
        final List<String> permissingList = new ArrayList<String>();
        permissingList.add(Manifest.permission.READ_CONTACTS);
        permissingList.add(Manifest.permission.READ_PHONE_STATE);
        PermissionUtils permissionUtils = PermissionUtils.getInstance();
        permissionUtils.setContext(this);
        List<String> list = permissionUtils.setPermission(permissingList);
        if(list.size()==0){
            mPhoneNumberBeans = queryContactPhoneNumber();
            mPhoneBeans.addAll(getSortModule());
            mRvContactAdapter.notifyDataSetChanged();
        }else {
            permissionUtils.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        LogUtil.i("------ads"+requestCode);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //6.0权限访问
                    mPhoneNumberBeans = queryContactPhoneNumber();
                    mPhoneBeans.addAll(getSortModule());
                    mRvContactAdapter.notifyDataSetChanged();
                } else { //权限被拒绝
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予访问存储的权限，不开启将无法正常工作！且可能被强制退出登录")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onSelectStr(int index, String selectStr) {
        if(mPhoneBeans==null){
            return;
        }
        for (int i = 0; i < mPhoneBeans.size() ; i++){
            if(mPhoneBeans.get(i).getSortLetters().equals(selectStr)){
                mRvList.setSelection(i);
                return;
            }
        }
    }

    @Override
    public void onSelectEnd() {

    }

    @Override
    public void onSelectStart() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_contact:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Contacts.People.CONTENT_URI);
                startActivity(intent);
                break;
        }
    }

    private List<PhoneNumberBean> queryContactPhoneNumber() {
        List<PhoneNumberBean> list=new ArrayList<>();
        String[] cols = {
                ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols, null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(nameFieldColumnIndex);
            String number = cursor.getString(numberFieldColumnIndex);
            PhoneNumberBean phoneNumberBean=new PhoneNumberBean(name,number);
            list.add(phoneNumberBean);
        }
        return list;
    }

    private List<PhoneBean> getSortModule() {
        List<PhoneBean> filterDateList = new ArrayList<>();
        for (int i = 0; i < mPhoneNumberBeans.size(); i++) {
            String pinYinFirstLetter = Cn2Spell.getPinYinFirstLetter(mPhoneNumberBeans.get(i).contactName);
            PhoneBean sortModel = new PhoneBean(mPhoneNumberBeans.get(i),pinYinFirstLetter.toUpperCase().charAt(0) + "");
            filterDateList.add(sortModel);
        }
        Collections.sort(filterDateList, new PinyinPhoneComparator());
        return filterDateList;
    }

}
