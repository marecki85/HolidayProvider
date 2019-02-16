package com.bluestone.rest.holidayAPI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bluestone.rest.entity.Holiday;
import com.bluestone.rest.loader.PropertiesLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class HolidayAPIRepository {

	public List<Holiday> readJsonFromUrl(String additionalParameters) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(new URL(getCompleteUrl(additionalParameters)));
		JsonNode holidaysNode = root.path("holidays");
		return getHolidaysFromNode(holidaysNode, objectMapper);
	}

	private List<Holiday> getHolidaysFromNode(JsonNode holidaysNode, ObjectMapper objectMapper) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Holiday> holidays = new ArrayList();
		for (JsonNode node : holidaysNode) {
			Holiday holiday = objectMapper.convertValue(node, Holiday.class);
			holidays.add(holiday);
		}
		return holidays;
	}

	private String getCompleteUrl(String additionalParameters) {
		return getPrefixFromUrl() + additionalParameters;
	}

	private String getPrefixFromUrl() {
		return PropertiesLoader.getproperties("holidayAPIurl") + "?key=" + PropertiesLoader.getproperties("key");
	}
}