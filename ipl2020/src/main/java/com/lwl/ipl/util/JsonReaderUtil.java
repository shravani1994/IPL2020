package com.lwl.ipl.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwl.ipl.domain.Team;

public class JsonReaderUtil {

	public static List<Team> readJson(String fileName) {

		List<Team> teams = new ArrayList<Team>();
		ObjectMapper mapper = new ObjectMapper();

		try {
			teams = mapper.readValue(new FileInputStream(fileName), new TypeReference<List<Team>>() {
			});
		} catch (JsonParseException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return teams;
	}
}
