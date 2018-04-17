package com.android.internal.telephony;

/**
 * Created by ndjat on 24/10/2017.
 */

public interface ITelephony {

    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
