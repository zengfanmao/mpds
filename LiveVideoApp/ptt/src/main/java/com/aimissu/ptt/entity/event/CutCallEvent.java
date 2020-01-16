package com.aimissu.ptt.entity.event;

public class CutCallEvent {
   public String pdtNumber;

   public CutCallEvent(String pdtNumber){
       this.pdtNumber=pdtNumber;
   }

    public String getPdtNumber() {
        return pdtNumber;
    }

    public void setPdtNumber(String pdtNumber) {
        this.pdtNumber = pdtNumber;
    }
}
