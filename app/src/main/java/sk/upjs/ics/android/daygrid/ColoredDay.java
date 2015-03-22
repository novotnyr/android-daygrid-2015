package sk.upjs.ics.android.daygrid;

import android.graphics.Color;

public class ColoredDay {
    public static final int UNKNOWN_YEAR = -1;
    public static final int UNKNOWN_MONTH = -1;
    public static final int UNKNOWN_DAY = -1;

    public static final ColoredDay EMPTY = new ColoredDay(ColoredDay.UNKNOWN_YEAR, ColoredDay.UNKNOWN_MONTH, ColoredDay.UNKNOWN_DAY);

    private int year;

    private int month;

    private int day;

    private int color;

    public ColoredDay(int year, int month, int day) {
        this(year, month, day, Color.TRANSPARENT);
    }

    public ColoredDay(int year, int month, int day, int color) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColoredDay that = (ColoredDay) o;

        if (color != that.color) return false;
        if (day != that.day) return false;
        if (month != that.month) return false;
        if (year != that.year) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        result = 31 * result + color;
        return result;
    }
}
