package com.brentandjody.mountainunicyclist.data;

/**
 * A class to hold riders and/or program users
 * Created by brentn on 29/04/15.
 */

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Observable;

@ParseClassName("Person")
public class Person extends ParseObject{
    private static final String NICKNAME = "nickname";

    private static final PersonObservable mObservable = new PersonObservable();

    public Person() {}
    public static ParseQuery<Person> getQuery() {return ParseQuery.getQuery(Person.class);}

    public String ID() {return getObjectId();}
    public String Nickname() {return getString(NICKNAME);}

    public void setNickname(String nickname) {
        put(NICKNAME, nickname);
        mObservable.setChanged();
    }


    public boolean isDeleted() {
        return Flags.isDeleted(getObjectId());
    }
    public void Delete() {
        Flags.Delete(getObjectId());
        force_update();
    }


    // **Observers
    public void notify_observers() {
        mObservable.notifyObservers(this);
    }
    private void force_update() {
        mObservable.setChanged();
        mObservable.notifyObservers(this);
    }

    private static class PersonObservable extends Observable {
        @Override
        public void setChanged() {
            super.setChanged();
        }
    }
}

