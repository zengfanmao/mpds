/*******************************************************************************
 * Copyright (C) 2009-2010 eoeMobile. 
 * All rights reserved.
 * http://www.eoeMobile.com/
 *
 * CHANGE LOG:
 *  DATE			AUTHOR			COMMENTS
 * =============================================================================
 *  2010MAY11		Waznheng Ma		Refine for Constructor and error handler.
 *
 *******************************************************************************/

package com.aimissu.ptt.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public final class MD5Utils {
    private static final String LOG_TAG = "MD5Utils";
    private static final String ALGORITHM = "MD5";

    private static char sHexDigits[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    private static MessageDigest sDigest;

    static {
        try {
            sDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Get MD5Utils Digest failed.");
        }
    }

    private MD5Utils() {
    }


    final public static String encode(String source) {
        byte[] btyes = source.getBytes();
        byte[] encodedBytes = sDigest.digest(btyes);

        return hexString(encodedBytes);
    }

    public static String hexString(byte[] source) {

        if (source == null || source.length <= 0) {
            return "";
        }

        final int size = source.length;
        final char str[] = new char[size * 2];
        int index = 0;
        byte b;
        for (int i = 0; i < size; i++) {
            b = source[i];
            str[index++] = sHexDigits[b >>> 4 & 0xf];
            str[index++] = sHexDigits[b & 0xf];
        }
        return new String(str);
    }
}
