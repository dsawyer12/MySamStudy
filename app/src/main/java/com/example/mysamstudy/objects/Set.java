package com.example.mysamstudy.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class Set implements Parcelable{
    private static final String TAG = "TAG";
    private String setName;
    private int setId, FK;
    private int setSize = 0;
    private boolean show_answers, loop_set, share;
    private ArrayList<Card> cards;

    public Set() {}

    public Set(String setName, int FK) {
        this.setName = setName;
        this.FK = FK;
        cards = null;
    }

    public Set(int id, String setName, int setSize, boolean show_answers, boolean loop_set, boolean share, int FK) {
        this.setId = id;
        this.setName = setName;
        this.setSize = setSize;
        this.show_answers = show_answers;
        this.loop_set = loop_set;
        this.share = share;
        this.FK = FK;
    }

    protected Set(Parcel in) {
        setName = in.readString();
        setId = in.readInt();
        FK = in.readInt();
        setSize = in.readInt();
        show_answers = in.readByte() != 0;
        loop_set = in.readByte() != 0;
        share = in.readByte() != 0;
        cards = in.createTypedArrayList(Card.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(setName);
        dest.writeInt(setId);
        dest.writeInt(FK);
        dest.writeInt(setSize);
        dest.writeByte((byte) (show_answers ? 1 : 0));
        dest.writeByte((byte) (loop_set ? 1 : 0));
        dest.writeByte((byte) (share ? 1 : 0));
        dest.writeTypedList(cards);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Set> CREATOR = new Creator<Set>() {
        @Override
        public Set createFromParcel(Parcel in) {
            return new Set(in);
        }

        @Override
        public Set[] newArray(int size) {
            return new Set[size];
        }
    };

    public int getFK() {
        return FK;
    }

    public void setFK(int FK) {
        this.FK = FK;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean isShow_answers() {
        return show_answers;
    }

    public void setShow_answers(boolean show_answers) {
        this.show_answers = show_answers;
    }

    public void setAnswerAlwaysOn(boolean value){
        for(int i = 0; i < cards.size(); i++){
            cards.get(i).setAlwaysShowAnswer(value);
        }
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public int getSetSize() {
        return setSize;
    }

    public void setSetSize(int setSize) {
        setSetSize(setSize);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public boolean isLoop_set() {
        return loop_set;
    }

    public void setLoop_set(boolean loop_set) {
        this.loop_set = loop_set;
    }
}
