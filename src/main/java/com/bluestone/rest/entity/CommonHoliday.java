package com.bluestone.rest.entity;

import java.time.LocalDate;

public class CommonHoliday {

	private LocalDate date;
	private String name1;
	private String name2;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}
}
