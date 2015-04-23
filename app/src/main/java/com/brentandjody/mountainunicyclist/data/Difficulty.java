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

    public int RadioButton() {
        switch (mDifficulty) {
            case NOT_SET: return -1;
            case EASY:  return R.id.easy;
            case MEDIUM:  return R.id.medium;
            case DIFFICULT: return R.id.difficult;
            case EXPERT:  return R.id.expert;
            default: return -1;
        }
    }
    public void setFromRadioButton(int resourceId) {
        switch (resourceId) {
            case R.id.easy: mDifficulty=LEVEL.EASY; break;
            case R.id.medium: mDifficulty=LEVEL.MEDIUM; break;
            case R.id.difficult: mDifficulty=LEVEL.DIFFICULT; break;
            case R.id.expert: mDifficulty=LEVEL.EXPERT; break;
        }
    }
    public int Icon() {
        switch (mDifficulty) {
            case NOT_SET: return R.drawable.transparent_pixel;
            case EASY:  return R.drawable.ic_easy;
            case MEDIUM:  return R.drawable.ic_medium;
            case DIFFICULT: return R.drawable.ic_difficult;
            case EXPERT:  return R.drawable.ic_expert;
            default: return R.drawable.transparent_pixel;
        }
    }

}
