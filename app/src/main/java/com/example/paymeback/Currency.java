package com.example.paymeback;

/**
 *
 * @author Anna
 */

public enum Currency {
    Euro, SGD, USD, Pound, TRY;

    public static Currency contains(String currency) {
        for (Currency c : Currency.values()) {
            if (c.name().equals(currency)) {
                return c;
            }
        }
        return null;
    }
}
