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

    private LEVEL mDifficulty = LEVEL.MEDIUM;

    public Difficulty() {}
    public Difficulty(int difficulty_int) { mDifficulty = LEVEL.values()[difficulty_int];}

    public int toInt() { return mDifficulty.ordinal();}
    public int Resource() {
        switch (mDifficulty) {
            case NOT_SET: return -1;
            case EASY:  return R.id.easy;
            case MEDIUM:  return R.id.medium;
            case DIFFICULT: return R.id.difficult;
            case EXPERT:  return R.id.expert;
        }
        return R.id.medium;
    }
    public int Icon() {
        switch (mDifficulty) {
            case NOT_SET:return -1;
            case EASY: return ic_easy;
            case MEDIUM: return ic_medium;
            case DIFFICULT: return ic_difficult;
            case EXPERT: return ic_expert;
        }
        return -1;
    }

}
