package util;

import android.content.SharedPreferences;

/**
 * Created by Bryce on 2018-03-04.
 */

public class Ranking {
    private SharedPreferences sharedpreferences;
    private Record[] tops;
    private String lastName;


    public Ranking(SharedPreferences sp){
        sharedpreferences = sp;
        lastName = sharedpreferences.getString("lastName", "");

        tops = new Record[5];
        for(int rank=1; rank<=5; rank++){
            String nameTag = "name" + Integer.toString(rank);
            String stepTag = "step" + Integer.toString(rank);
            String timeTag = "time" + Integer.toString(rank);
            String millisecondsTag = "ms" + Integer.toString(rank);

            String name = sharedpreferences.getString(nameTag, null);
            String stepsString = sharedpreferences.getString(stepTag, null);
            String timeString = sharedpreferences.getString(timeTag, null);
            long time = sharedpreferences.getLong(millisecondsTag, 9999999);

            if(timeString != null)
                tops[rank-1] = new Record(name, Integer.valueOf(stepsString), timeString, time);
            else
                break;
        }
    }

    public int getRank(Record record){
        int rank = 1;
        for(Record top: tops){
            if(top == null || top.compareTo(record) > 0)
                break;
            else
                rank++;
        }
        return rank;
    }

    public void addRecord(Record record){
        lastName = record.getName();

        int rank = getRank(record);
        for(int i=4; i>=rank; i--){
            tops[i] = tops[i-1];
        }
        tops[rank-1] = record;
        saveRanking();
    }

    public void clear(){
        lastName = "";
        tops = new Record[5];
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().apply();
    }

    public void saveRanking(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lastName", lastName);

        for(int rank=1; rank<=5; rank++){
            if(tops[rank-1] == null)
                break;
            String nameTag = "name" + Integer.toString(rank);
            String stepTag = "step" + Integer.toString(rank);
            String timeTag = "time" + Integer.toString(rank);
            String millisecondsTag = "ms" + Integer.toString(rank);

            editor.putString(nameTag, tops[rank-1].getName());
            editor.putString(stepTag, tops[rank-1].getStepsString());
            editor.putString(timeTag, tops[rank-1].getTimeString());
            editor.putLong(millisecondsTag, tops[rank-1].getTime());
        }
        editor.apply();
    }

    public String getLastName() {
        return lastName;
    }
}
