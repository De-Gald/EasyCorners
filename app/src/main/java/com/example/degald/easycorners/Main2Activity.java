package com.example.degald.easycorners;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.example.degald.easycorners.data.CornersDbHelper;
import com.example.degald.easycorners.data.TestUtil;
import com.example.degald.easycorners.data.CornersContract;


public class Main2Activity extends AppCompatActivity implements CornersAdapter.CornersOnClickHandler {

    private CornersAdapter mAdapter;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RecyclerView cornersRecyclerView;

        cornersRecyclerView = (RecyclerView) this.findViewById(R.id.corners_list_view);

        cornersRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        CornersDbHelper dbHelper = new CornersDbHelper(this);

        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = getAllCornersData();

        mAdapter = new CornersAdapter(this, cursor, this);

        cornersRecyclerView.setAdapter(mAdapter);

        String path_to_Screenshot = getIntent().getStringExtra(FullscreenActivity.PATH_TO_FILE);
        String team = getIntent().getStringExtra(MainActivity.TITLE_ID);
        addNewRecord(team, path_to_Screenshot);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long id = (long) viewHolder.itemView.getTag();
                removeCornersRecord(id);
                mAdapter.swapCursor(getAllCornersData());
            }

        }).attachToRecyclerView(cornersRecyclerView);
//        mDb.delete(CornersContract.WaitlistEntry.TABLE_NAME, null, null);

//        TestUtil.insertFakeData(mDb);

    }


    private Cursor getAllCornersData() {
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

    @Override
    public void onClick(String pathToFile) {
        Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
        intent.putExtra(FullscreenActivity.PATH_TO_FILE, pathToFile);
        startActivity(intent);
    }
}