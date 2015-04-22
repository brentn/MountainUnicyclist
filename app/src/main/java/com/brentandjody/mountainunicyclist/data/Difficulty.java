package com.brentandjody.mountainunicyclist.data;

import com.brentandjody.mountainunicyclist.R;

import static com.brentandjody.mountainunicyclist.R.drawable.ic_difficult;
import static com.brentandjody.mountainunicyclist.R.drawable.ic_easy;
import static com.brentandjody.mountainunicyclist.R.drawable.ic_expert;
import static com.brentandjody.mountainunicyclist.R.drawable.ic_medium;

/**
 * Created by brent on 18/04/15.
 */
public class Difficulty {
    private static enum LEVEL {NOT_SET, EASY, MEDIUM, DIFFICULT, EXPERT}
    private static final int DEFAULT=1;

    private LEVEL mDifficulty = LEVEL.MEDIUM;

    public Difficulty() {}
    public Difficulty(int difficulty_int) { mDifficulty = LEVEL.values()[difficulty_int];}

    public int toInt() { return mDifficulty.ordinal();}

    public int Resource() {
        switch (mDifficulty) {
            case NOT_SET: return R.drawable.transparent_pixel;
            case EASY:  return R.drawable.ic_easy;
            case MEDIUM:  return R.drawable.ic_medium;
            case DIFFICULT: return R.drawable.ic_difficult;
            case EXPERT:  return R.drawable.ic_expert;
        }
        return R.drawable.transparent_pixel;
    }
    public static Difficulty fromResource(int resourceId) {
        switch (resourceId) {
            case R.id.easy: return new Difficulty(0);
            case R.id.medium: return new Difficulty(1);
            case R.id.difficult: return new Difficulty(2);
            case R.id.expert: return new Difficulty(3);
            default:return new Difficulty(DEFAULT);
        }
    }

}
