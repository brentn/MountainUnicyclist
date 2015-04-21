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
    //public LEVEL level() { return mDifficulty; }
    public int Resource() {
        switch (mDifficulty) {
            case NOT_SET: return R.drawable.abc_item_background_holo_dark;
            case EASY:  return R.drawable.ic_easy;
            case MEDIUM:  return R.drawable.ic_medium;
            case DIFFICULT: return R.drawable.ic_difficult;
            case EXPERT:  return R.drawable.ic_expert;
        }
        return R.id.medium;
    }

}
