package com.matrix.yukun.matrix.tool_module.phonebook;

import java.util.Comparator;

/**
 * author: kun .
 * date:   On 2018/11/22
 */
public class PinyinPhoneComparator implements Comparator<PhoneBean> {
    @Override
    public int compare(PhoneBean o1, PhoneBean o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
