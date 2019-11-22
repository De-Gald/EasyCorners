package com.example.degald.easycorners;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.degald.easycorners.data.CornersContract;


public class CornersAdapter extends RecyclerView.Adapter<CornersAdapter.GuestViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public CornersAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.corners_list_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;

        String team = mCursor.getString(mCursor.getColumnIndex(CornersContract.WaitlistEntry.COLUMN_TEAM_NAME));
        String date = mCursor.getString(mCursor.getColumnIndex(CornersContract.WaitlistEntry.COLUMN_TIMESTAMP)).split(" ")[0];
        long id = mCursor.getLong(mCursor.getColumnIndex(CornersContract.WaitlistEntry._ID));

        holder.teamName.setText(team + " - " + date);
        holder.itemView.setTag(id);
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    class GuestViewHolder extends RecyclerView.ViewHolder {

        TextView teamName;

        public GuestViewHolder(View itemView) {
            super(itemView);
            teamName = (TextView) itemView.findViewById(R.id.team_data_text_view);
        }

    }
}