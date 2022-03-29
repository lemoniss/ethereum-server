package com.maxlength.spec.enums;

public enum Confirm {
    TRANSFER(0),
    MINT(1),
    BURN(2),
    ADD(3),
    REMOVE(4);

    private final int confirmValue;

    Confirm(int confirmValue) {
        this.confirmValue = confirmValue;
    }

    public int getConfirmValue() {
        return confirmValue;
    }
}
