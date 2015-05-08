package com.brentandjody.mountainunicyclist.data;

import java.util.Comparator;

/**
 * Created by brentn on 06/05/15.
 */
public class ListItem {
    private String label;
    private String value;

    public ListItem(String l, String v) {
        label = l;
        value = v;
    }

    public String Value() {return value;}
    @Override
    public String toString() {
        return label;
    }
    public boolean equals(ListItem that) { return this.value.equals(that.value);}
}
