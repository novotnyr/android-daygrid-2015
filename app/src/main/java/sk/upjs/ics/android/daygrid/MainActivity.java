package sk.upjs.ics.android.daygrid;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private static final int WEEK_COUNT = 6;
    private static final int DAY_COUNT = 7;
    public static final int LOADER_ID = 0;
    public static final int NO_FLAGS = 0;

    public static final int INSERT_COLORED_DAY_ASYNC_TOKEN = 0;
    public static final Object NO_COOKIE = null;

    private GridView gridView;
    private SimpleCursorAdapter adapter;
    private int currentYear = 2015;
    private int currentMonth = 3;

    private AsyncQueryHandler queryHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queryHandler = new BasicAsyncQueryHandler(getContentResolver());

        initializeGridAdapter();
        initializeLayout();

        getLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, this);
    }

    private void initializeLayout() {
        gridView = new GridView(this);
        gridView.setNumColumns(DAY_COUNT);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setAdapter(this.adapter);
        gridView.setOnItemClickListener(this);
        setContentView(gridView);
    }

    public void initializeGridAdapter() {
        String[] from = { Database.ColoredDay.DAY };
        int[] to = { android.R.id.text1 };
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, NO_FLAGS);
        adapter.setViewBinder(new ColoredDayViewBinder());
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        CursorLoader loader = new CursorLoader(this);
        Uri uri = ColoredDayContentProvider.CONTENT_URI
                .buildUpon()
                .appendPath(String.valueOf(currentYear))
                .appendPath(String.valueOf(currentMonth))
                .build();
        loader.setUri(uri);
        return loader;
    }


    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        this.adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        this.adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TextView textView = (TextView) view;

        Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
        int selectedDay = cursor.getInt(cursor.getColumnIndex(Database.ColoredDay.DAY));

        Uri uri = ColoredDayContentProvider
                .CONTENT_URI
                .buildUpon()
                .appendPath("2015")
                .appendPath("3")
                .appendPath(String.valueOf(selectedDay))
                .build();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.ColoredDay.COLOR, Color.RED);

        queryHandler.startInsert(INSERT_COLORED_DAY_ASYNC_TOKEN, NO_COOKIE, uri, contentValues);
    }

    private static class ColoredDayViewBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int i) {
            if(view instanceof TextView) {
                TextView textView = (TextView) view;

                int day = cursor.getInt(cursor.getColumnIndex(Database.ColoredDay.DAY));
                int color = cursor.getInt(cursor.getColumnIndex(Database.ColoredDay.COLOR));

                if(day != ColoredDay.UNKNOWN_DAY) {
                    textView.setText("" + day);
                    textView.setBackgroundColor(color);
                    textView.setGravity(Gravity.CENTER);
                } else {
                    textView.setEnabled(false);
                }
                return true;
            }
            return false;
        }
    }

    private static class BasicAsyncQueryHandler extends AsyncQueryHandler {
        public BasicAsyncQueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }
    }
}
