package com.example.degald.easycorners;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.degald.easycorners.data.CornersDbHelper;
import com.example.degald.easycorners.data.TestUtil;
import com.example.degald.easycorners.data.CornersContract;


public class Main2Activity extends AppCompatActivity {

    private CornersAdapter mAdapter;
    private SQLiteDatabase mDb;
    private final static String LOG_TAG = Main2Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RecyclerView cornersRecyclerView;

        cornersRecyclerView = (RecyclerView) this.findViewById(R.id.corners_list_view);

        cornersRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Create a DB helper (this will create the DB if run for the first time)
        CornersDbHelper dbHelper = new CornersDbHelper(this);

        mDb = dbHelper.getWritableDatabase();

        // Get all corners info from the database and save in a cursor
        Cursor cursor = getAllGuests();

        mAdapter = new CornersAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        cornersRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long id = (long) viewHolder.itemView.getTag();
                removeCornersRecord(id);
                mAdapter.swapCursor(getAllGuests());
            }

        }).attachToRecyclerView(cornersRecyclerView);

        TestUtil.insertFakeData(mDb);

    }


    private Cursor getAllGuests() {
        return mDb.query(
                CornersContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CornersContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }

    private long addNewRecord(String team, String pathToFile) {
        ContentValues cv = new ContentValues();
        cv.put(CornersContract.WaitlistEntry.COLUMN_TEAM_NAME, team);
        cv.put(CornersContract.WaitlistEntry.COLUMN_PATH_TO_SCREENSHOT, pathToFile);
        return mDb.insert(CornersContract.WaitlistEntry.TABLE_NAME, null, cv);
    }


    private boolean removeCornersRecord(long id) {
        return mDb.delete(CornersContract.WaitlistEntry.TABLE_NAME, CornersContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }

}