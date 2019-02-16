package com.bluestone.rest.service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.rest.entity.CommonHoliday;
import com.bluestone.rest.entity.Holiday;
import com.bluestone.rest.holidayAPI.HolidayAPIRepository;

@Service
public class HolidayProvider {

	private final static int quarterOfTheYear = 3;
	private final static int oneYear = 1;

	@Autowired
	private HolidayAPIRepository holidayAPIRepository;
	private List<Holiday> firstCountryHolidays;
	private List<Holiday> secondCountryHolidays;

	public CommonHoliday getNearestCommonHoliday(String date, String country1, String country2) throws Exception {
		LocalDate givenDate = parseDate(date);
		List<LocalDate> commonDates = getNearestHolidaysDatesForOneYear(country1, country2, givenDate);
		LocalDate commonNearestHolidays = getNearestDateOfHolidayAfterGivenDate(commonDates, givenDate);
		return createCommonHoliday(firstCountryHolidays, secondCountryHolidays, commonNearestHolidays);
	}

	private LocalDate parseDate(String dateToParse) {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate parsedDate = LocalDate.parse(dateToParse, dateFormat);
		return parsedDate;
	}

	private List<LocalDate> getNearestHolidaysDatesForOneYear(String country1, String country2, LocalDate baseDate)
			throws ParseException, IOException {
		LocalDate maximumSearchedDate = baseDate.plusYears(oneYear);
		LocalDate currentEndRange = baseDate.plusMonths(quarterOfTheYear);
		LocalDate currentStartRange = baseDate;
		List<LocalDate> commonHolidays = new ArrayList<>();
		while (commonHolidays.isEmpty() && currentStartRange.isBefore(currentEndRange)
				&& !currentEndRange.isEqual(maximumSearchedDate)) {
			firstCountryHolidays = getHolidaysForTimePeriod(country1, currentStartRange, currentEndRange);
			secondCountryHolidays = getHolidaysForTimePeriod(country2, currentStartRange, currentEndRange);
			commonHolidays = getSameDates(getDates(firstCountryHolidays), getDates(secondCountryHolidays));
			currentStartRange = currentEndRange;
			currentEndRange = currentEndRange.plusMonths(quarterOfTheYear);
		}
		return commonHolidays;
	}

	private LocalDate getNearestDateOfHolidayAfterGivenDate(List<LocalDate> commonHolidayDates, LocalDate givenDate)
			throws Exception {
		Collections.sort(commonHolidayDates);
		LocalDate searchedHolidayDate = null;
		if (commonHolidayDates == null)
			throw new Exception("There are no common holidays for these countries");
		for (LocalDate holidayDate : commonHolidayDates) {
			searchedHolidayDate = holidayDate;
			if (!givenDate.isAfter(holidayDate))
				break;
		}
		return searchedHolidayDate;
	}

	private List<LocalDate> getDates(List<Holiday> holidays) {
		return holidays.stream().map(h -> h.getDate()).collect(Collectors.toList());
	}

	private List<LocalDate> getSameDates(List<LocalDate> dates1, List<LocalDate> dates2) {
		List<LocalDate> common = new ArrayList<>(dates1);
		common.retainAll(dates2);
		return common;
	}

	private CommonHoliday createCommonHoliday(List<Holiday> holidaysForFirstCountry,
			List<Holiday> holidaysForSecondCountry, LocalDate givenDate) {
		CommonHoliday commonHoliday = new CommonHoliday();
		commonHoliday.setDate(givenDate);
		commonHoliday.setName1(getHolidayName(getHolidayByDate(holidaysForFirstCountry, givenDate)));
		commonHoliday.setName2(getHolidayName(getHolidayByDate(holidaysForSecondCountry, givenDate)));
		return commonHoliday;
	}

	private List<Holiday> getHolidayByDate(List<Holiday> holidays, LocalDate localDate) {
		return holidays.stream().filter(h -> h.getDate().isEqual(localDate)).collect(Collectors.toList());
	}

	private String getHolidayName(List<Holiday> holidays) {
		return holidays.stream().map(h -> h.getName()).collect(Collectors.joining("; "));
	}

	private List<Holiday> getHolidaysForTimePeriod(String country, LocalDate startDate, LocalDate endDate)
			throws IOException {
		List<Holiday> holidaysInTimePeriod = new ArrayList<>();
		LocalDate currentHolidayDay = endDate;
		while (!startDate.isAfter(currentHolidayDay)) {
			List<Holiday> holidays = new ArrayList<>();
			holidays = holidayAPIRepository.readJsonFromUrl(prepareInputForRequest(country, currentHolidayDay));
			holidaysInTimePeriod.addAll(holidays);
			holidaysInTimePeriod = removeIfNotInRange(holidaysInTimePeriod, startDate, endDate);
			currentHolidayDay = getCurrentHolidayDate(holidays);
		}
		return holidaysInTimePeriod;
	}

	private List<Holiday> removeIfNotInRange(List<Holiday> toCheck, LocalDate startDate, LocalDate endDate) {
		return toCheck.stream().filter(h -> !h.getDate().isBefore(startDate) && !h.getDate().isAfter(endDate))
				.collect(Collectors.toList());
	}

	private String prepareInputForRequest(String country, LocalDate localDate) {
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		return "&country=" + country + "&year=" + year + "&month=" + month + "&day=" + day + "&previous";
	}

	private LocalDate getCurrentHolidayDate(List<Holiday> holidays) {
		return holidays.stream().map(h -> h.getDate()).findFirst().orElse(null);
	}
}
