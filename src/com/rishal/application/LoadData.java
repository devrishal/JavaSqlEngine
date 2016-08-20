package com.rishal.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.rishal.valueObjects.TableData;

public class LoadData {
	public static String headerString;
	public static ArrayList<TableData> myData = new ArrayList<TableData>();
	public static LoadData singleton = null;

	private LoadData() {
		List<String> result = null;
		try {
			result = Files.readAllLines(Paths.get("sql_engine_dataset.csv"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		String headerarr[] = result.get(0).split(",");
		headerString = result.get(0);
		for (int i = 1; i < result.size(); i++) {
			TableData data = new TableData();
			String[] temp = result.get(i).split(",");
			data.setBrand(Integer.valueOf(temp[1]));
			data.setIn_stock(Boolean.valueOf(temp[4]));
			data.setPrice(Double.valueOf(temp[3]));
			data.setStore(Integer.valueOf(temp[2]));
			data.setTitle(temp[0]);
			myData.add(data);
		}

	}

	public static LoadData getInstance() {
		if (singleton == null) {
			synchronized (LoadData.class) {
				if (singleton == null) {
					singleton = new LoadData();
				}
			}
		}
		return singleton;
	}

}
