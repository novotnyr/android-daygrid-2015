package sk.upjs.ics.android.daygrid;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.MonthDisplayHelper;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColoredDayContentProvider extends ContentProvider {
    public static final String AUTHORITY = "sk.upjs.ics.android.daygrid.ColoredDayContentProvider";

    public static final String TABLE_NAME = "coloredday";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    private static final String MIME_TYPE_COLORED_DAYS = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + TABLE_NAME;

    private static final int WEEK_COUNT = 6;
    private static final int DAY_COUNT = 7;

    public static final int URI_MATCH_DAY = 0;
    public static final int URI_MATCH_MONTH = 1;

    private Set<ColoredDay> colorings = new HashSet<>();

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String[] COLUMN_NAMES = {
            Database.ColoredDay._ID,
            Database.ColoredDay.YEAR,
            Database.ColoredDay.MONTH,
            Database.ColoredDay.DAY,
            Database.ColoredDay.COLOR
    };

    @Override
    public boolean onCreate() {
        int blueLightColor = getContext().getResources().getColor(android.R.color.holo_blue_light);
        colorings.add(new ColoredDay(2015, 3, 16, blueLightColor));
        colorings.add(new ColoredDay(2015, 3, 22, blueLightColor));
        colorings.add(new ColoredDay(2015, 3, 30, blueLightColor));

        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#/#/#", URI_MATCH_DAY);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#/#", URI_MATCH_MONTH);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch(uriMatcher.match(uri)) {
            case URI_MATCH_MONTH:
                List<String> pathSegments = uri.getPathSegments();
                int year = Integer.parseInt(pathSegments.get(1));
                int month = Integer.parseInt(pathSegments.get(2));

                Cursor cursor = getColoringsCursor(year, month);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                return cursor;
            default:
                return null;
        }
    }

    private Cursor getColoringsCursor(int year, int month) {
        MatrixCursor matrixCursor = new MatrixCursor(COLUMN_NAMES);

        MonthDisplayHelper monthDisplayHelper = new MonthDisplayHelper(year, month - 1, Calendar.MONDAY);

        for (int weekIndex = 0; weekIndex < WEEK_COUNT; weekIndex++) {
            for(int dayIndex = 0; dayIndex < DAY_COUNT; dayIndex++) {
                int day = monthDisplayHelper.getDayAt(weekIndex, dayIndex);
                int id = (weekIndex * DAY_COUNT) + dayIndex;
                if(monthDisplayHelper.isWithinCurrentMonth(weekIndex, dayIndex)) {
                    matrixCursor.addRow(toArray(id, getDayColor(year, month, day)));
                } else {
                    matrixCursor.addRow(toArray(id, ColoredDay.EMPTY));
                }
            }
        }
        return matrixCursor;
    }

    private ColoredDay getDayColor(int year, int month, int day) {
        for (ColoredDay coloring : this.colorings) {
            if((coloring.getYear() == year) && (coloring.getMonth() == month) && (coloring.getDay() == day)) {
                return coloring;
            }
        }
        return new ColoredDay(year, month, day, Color.TRANSPARENT);
    }

    public Object[] toArray(int id, ColoredDay coloredDay) {
        int year = coloredDay.getYear();
        int month = coloredDay.getMonth();
        int day = coloredDay.getDay();

        return new Object[] { id, year, month, day, coloredDay.getColor()} ;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        return MIME_TYPE_COLORED_DAYS;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case URI_MATCH_DAY:
                List<String> pathSegments = uri.getPathSegments();
                int year = Integer.parseInt(pathSegments.get(1));
                int month = Integer.parseInt(pathSegments.get(2));
                int day = Integer.parseInt(pathSegments.get(3));

                int color = values.getAsInteger(Database.ColoredDay.COLOR);
                ColoredDay coloredDay = new ColoredDay(year, month, day, color);
                this.colorings.add(coloredDay);

                Uri monthUri = CONTENT_URI.buildUpon()
                        .appendPath(String.valueOf(year))
                        .appendPath(String.valueOf(month))
                        .build();

                getContext().getContentResolver().notifyChange(monthUri, null, false);

                return uri;
            default: return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

