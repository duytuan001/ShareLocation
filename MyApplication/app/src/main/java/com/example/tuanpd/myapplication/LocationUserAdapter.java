package com.example.tuanpd.myapplication;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LocationUserAdapter extends BaseAdapter {
    public interface LocationUserListListener {
        public void onDeleteLocationUser(LocationUser user);
    }

    private LocationUserListListener mListener;
    private List<LocationUser> mList;
    private Context mContext;

    private final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("HH:mm MM/dd/yy");
    private final int[] mColorList = {R.color.HUE_AZURE, R.color.HUE_BLUE, R.color.HUE_CYAN,
            R.color.HUE_GREEN, R.color.HUE_MAGENTA, R.color.HUE_ORANGE,
            R.color.HUE_ROSE, R.color.HUE_VIOLET, R.color.HUE_YELLOW};

    public LocationUserAdapter(Context context, List<LocationUser> list) {
        mList = list;
        mContext = context;
    }

    public void setLocationUserList(List<LocationUser> list) {
        mList = list;
    }

    public void setListener(LocationUserListListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_members_list_item, null);
            holder = new ViewHolder();
            holder.colorIv = (ImageView) convertView.findViewById(R.id.members_list_color_iv);
            holder.deleteIv = (ImageView) convertView.findViewById(R.id.members_list_delete_iv);
            holder.statusIv = (ImageView) convertView.findViewById(R.id.members_list_presence_iv);
            holder.usernameTv = (TextView) convertView.findViewById(R.id.members_list_username_tv);
            holder.updatedDateTimeTv = (TextView) convertView.findViewById(R.id.members_list_last_time_tv);

            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDeleteLocationUser(mList.get(position));
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LocationUser user = mList.get(position);

        if (!TextUtils.isEmpty(user.getUsername())) {
            holder.usernameTv.setText(user.getUsername());
        } else {
            holder.usernameTv.setText(user.getPhoneNumber());
        }

        String updatedDate = mDateTimeFormat.format(new Date(user.getUpdatedTime()));
        holder.updatedDateTimeTv.setText(updatedDate);

        holder.colorIv.setBackgroundResource(mColorList[position % mColorList.length]);

        long wait = (System.currentTimeMillis() - user.getUpdatedTime()) / 1000;
        long interval = AppUtils.getIntervalPeriodTime(mContext) + 60;
        if (wait < interval) {
            holder.statusIv.setImageResource(android.R.drawable.presence_online);
        } else if (wait < 2 * interval) {
            holder.statusIv.setImageResource(android.R.drawable.presence_away);
        } else {
            holder.statusIv.setImageResource(android.R.drawable.presence_invisible);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView usernameTv;
        ImageView statusIv;
        TextView updatedDateTimeTv;
        ImageView deleteIv;
        ImageView colorIv;
    }
}
