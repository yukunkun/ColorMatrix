package com.ykk.pluglin_video.entity;

public class SortModel {

	private String name;
	private String sortLetters;
	private String year;
	private int month;
	private int day;
	private String title;
	private int type;

	public SortModel(String name, String sortLetters, String year, int month, int day) {
		this.name = name;
		this.sortLetters = sortLetters;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	@Override
	public String toString() {
		return "SortModel{" +
				"name='" + name + '\'' +
				", sortLetters='" + sortLetters + '\'' +
				", year='" + year + '\'' +
				", month=" + month +
				", day=" + day +
				", title='" + title + '\'' +
				", type=" + type +
				'}';
	}
}
