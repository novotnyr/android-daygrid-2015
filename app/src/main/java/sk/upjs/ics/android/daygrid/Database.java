package sk.upjs.ics.android.daygrid;

import android.provider.BaseColumns;

public class Database {
    public static class ColoredDay implements BaseColumns {
        /**
         * Represents a day in month, 1-based.
         * Type: INTEGER (int)
         */
        public static final String DAY = "day";
        /**
         * Represents a month in year, 1-based.
         * Type: INTEGER (int)
         */
        public static final String MONTH = "month";
        /**
         * Represents a year in the YYYY format, such as 2015.
         * Type: INTEGER (int).
         */
        public static final String YEAR = "year";
        /**
         * Represents a color, represented as {@link android.graphics.Color}.
         * Type: INTEGER (int).
         */
        public static final String COLOR = "color";
    }
}
