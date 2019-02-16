package com.bluestone.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluestone.rest.entity.CommonHoliday;
import com.bluestone.rest.service.HolidayProvider;

@RestController
public class holidayController {

	@Autowired
	private HolidayProvider holidayProvider;

	@RequestMapping(value = "/holidayprovider", method = RequestMethod.GET)
	@GetMapping(produces = "application/json")
	public CommonHoliday findCommonHolidays(@RequestParam("country1") String country1,
			@RequestParam("country2") String country2, @RequestParam("date") String date) throws Exception {
		return holidayProvider.getNearestCommonHoliday(date, country1, country2);
	}
}
