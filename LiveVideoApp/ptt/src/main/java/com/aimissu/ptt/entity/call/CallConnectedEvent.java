package com.aimissu.ptt.entity.call;

public class CallConnectedEvent {

    public boolean personCall;
    public CallConnectedEvent(){
    }
    public CallConnectedEvent(boolean personCall){
        this.personCall = personCall;
    }

    public boolean isPersonCall() {
        return personCall;
    }

    public void setPersonCall(boolean personCall) {
        this.personCall = personCall;
    }
}
