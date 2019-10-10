package com.matrix.yukun.matrix.tool_module.weather.activity;


import com.matrix.yukun.matrix.main_module.entity.SortModel;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinCityComparator implements Comparator<CitySortBean> {

	public int compare(CitySortBean o1, CitySortBean o2) {
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
