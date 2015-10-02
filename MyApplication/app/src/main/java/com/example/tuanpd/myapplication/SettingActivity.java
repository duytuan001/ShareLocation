package com.example.tuanpd.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingActivity extends AppCompatActivity implements LocationUserAdapter.LocationUserListListener {

    private static final int MODE_JOINT = 0;
    private static final int MODE_LEAVE = 1;
    private static final int MODE_CREATE = 2;
    private int mModeConnect = MODE_JOINT;

    private ToggleButton mSharingTb;
    private Spinner mIntervalPeriodSp;
    private TextView mMemberListIndicatorTv;
    private ListView mMemberLv;
    private ImageButton mCreatingGroupIb;
    private Button mJointBtn;
    private EditText mGroupIdEt;
    private EditText mGroupPwEt;
    private Button mJointNearBtn;

    private LocationGroup mLocationGroup = null;
    private LocationUserAdapter mAdapter = null;
    private boolean mIsConnected = false;
    private MNetwork mNetworkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        mNetworkManager = new MNetwork();
        mLocationGroup = ((MyApplication) getApplication()).getLocationGroup();
        mIsConnected = MPreference.isGroupConnected(this);
        if (mIsConnected) {
            mModeConnect = MODE_LEAVE;
        } else {
            mModeConnect = MODE_JOINT;
        }

        controlView();
        loadData();
        displayData();
    }

    private void initView() {
        mSharingTb = (ToggleButton) findViewById(R.id.settings_sharing_tb);
        mIntervalPeriodSp = (Spinner) findViewById(R.id.settings_interval_period_spinner);
        mMemberLv = (ListView) findViewById(R.id.settings_member_list_lv);
        mMemberLv.setEmptyView(findViewById(R.id.settings_empty_member_tv));
        mMemberListIndicatorTv = (TextView) findViewById(R.id.settings_members_indicator_tv);
        mCreatingGroupIb = (ImageButton) findViewById(R.id.settings_group_create_btn);
        mJointBtn = (Button) findViewById(R.id.settings_group_joint_btn);
        mGroupIdEt = (EditText) findViewById(R.id.settings_group_id_et);
        mGroupPwEt = (EditText) findViewById(R.id.settings_group_pw_et);
        mJointNearBtn = (Button) findViewById(R.id.settings_group_joint_near_btn);
    }

    private void displayData() {
        // Interval period Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arr_interval_period_str, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIntervalPeriodSp.setAdapter(adapter);
        int position = MPreference.getIntervalPeriodIndex(this);
        mIntervalPeriodSp.setSelection(position);

        // Sharing toggle button
        mSharingTb.setChecked(MPreference.isGPSSharing(this));

        // set for id & password group
        if (mIsConnected) {
            mGroupIdEt.setText(MPreference.getCurrentGroupId(this));
            mGroupPwEt.setText(MPreference.getCurrentGroupPw(this));
        }
        // set for buttons
        updateViewsForChangeMode();

        // set for members list
        if (mAdapter == null && mLocationGroup != null) {
            mAdapter = new LocationUserAdapter(this, mLocationGroup.getUserList());
            mAdapter.setListener(this);
            mMemberLv.setAdapter(mAdapter);
        }
    }

    private void controlView() {
        mSharingTb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MPreference.setGPSSharing(SettingActivity.this, isChecked);
                notifySettingChange();
            }
        });
        mIntervalPeriodSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MPreference.setIntervalPeriodIndex(SettingActivity.this, position);
                notifySettingChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mMemberLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    mMemberListIndicatorTv.setText(totalItemCount + "/" + totalItemCount);
                } else {
                    mMemberListIndicatorTv.setText(visibleItemCount + "/" + totalItemCount);
                }
            }
        });
        mMemberLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(SettingActivity.this, MapsActivity.class);
//                startActivity(intent);
            }
        });
        mJointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String id = mGroupIdEt.getText().toString().trim();
                String pw = mGroupPwEt.getText().toString().trim();
                if (mModeConnect == MODE_JOINT) {
                    mNetworkManager.handleGroupRequest(MNetwork.REQ_GROUP_JOINT, new LocationGroup(id, pw));
                } else if (mModeConnect == MODE_LEAVE) {
                    mNetworkManager.handleGroupRequest(MNetwork.REQ_GROUP_LEAVE);
                } else if (mModeConnect == MODE_CREATE) {
                    mNetworkManager.handleGroupRequest(MNetwork.REQ_GROUP_CREATE, new LocationGroup(id, pw));
                }
            }
        });
        mCreatingGroupIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModeConnect == MODE_JOINT) {
                    mCreatingGroupIb.setBackgroundResource(android.R.drawable.ic_menu_revert);
                    mGroupIdEt.setEnabled(false);
                    mGroupIdEt.setText(((MyApplication) getApplication()).getMyLocationUser().getPhoneNumber() + "00001");
                    mJointBtn.setText(R.string.group_create);
                    mModeConnect = MODE_CREATE;
                } else if (mModeConnect == MODE_CREATE) {
                    mGroupIdEt.setEnabled(true);
                    mGroupIdEt.setText("");
                    mGroupPwEt.setText("");
                    mCreatingGroupIb.setBackgroundResource(android.R.drawable.ic_input_add);
                    mJointBtn.setText(R.string.group_joint);
                    mModeConnect = MODE_JOINT;
                }
            }
        });
        mGroupIdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        mGroupPwEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        mJointNearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNetworkManager.handleGroupRequest(MNetwork.REQ_GROUP_JOINT_NEAR);
            }
        });
    }

    private void loadData() {
//        if (mIsConnected) {
//            mNetworkManager.handleGroupRequest(MNetwork.REQ_GROUP_INFO);
//        } else {
//            mLocationGroup = null;
//        }
        mLocationGroup = ((MyApplication) getApplication()).getLocationGroup();
    }

    private void updateViewsForChangeMode() {
        if (mIsConnected) {
            mGroupIdEt.setEnabled(false);
            mGroupPwEt.setEnabled(false);
            mJointBtn.setText(R.string.group_leave);
            mCreatingGroupIb.setEnabled(false);
            mModeConnect = MODE_LEAVE;
        } else {
            mGroupIdEt.setEnabled(true);
            mGroupPwEt.setEnabled(true);
            mGroupIdEt.setText("");
            mGroupPwEt.setText("");
            mJointBtn.setText(R.string.group_joint);
            mCreatingGroupIb.setEnabled(true);
            mModeConnect = MODE_JOINT;
        }
    }

    @Override
    public void onDeleteLocationUser(LocationUser user) {
        mNetworkManager.handleUserRequest(MNetwork.REQ_USER_DELETE, user);
    }

    private void notifySettingChange() {

    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void hideKeyboard() {
        hideKeyboard(mGroupIdEt);
        hideKeyboard(mGroupIdEt);
    }

}
