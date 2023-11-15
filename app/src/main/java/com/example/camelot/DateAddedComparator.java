package com.example.camelot;
import java.util.Comparator;

public class DateAddedComparator implements Comparator<AudioModel> {
    @Override
    public int compare(AudioModel o1, AudioModel o2) {
        int dateComparison = o2.getDateAdded().compareTo(o1.getDateAdded());

        // Если даты добавления одинаковы, используем вторичную сортировку по имени файла
        if (dateComparison == 0) {
            return o1.getTitle().compareTo(o2.getTitle());
        }

        return dateComparison;
    }
}