package com.jc.pda.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonUtils {

	private static Gson mGson = new Gson();

	/**
	 * object2string
	 * 
	 * @param object
	 * @param <T>
	 * @return
	 */
	public static <T> String object2String(T object) {
		return mGson.toJson(object);
	}

	/**
	 *
	 * @param json
	 * @param clz
	 * @param <T>
	 * @return
	 */
	public static <T> T string2Object(String json, Class<T> clz)
			throws JsonSyntaxException {
		return mGson.fromJson(json, clz);
	}

	/**
	 *
	 * @param json
	 * @param clz
	 * @param <T>
	 * @return
	 * @throws JsonSyntaxException
	 */
	public static <T> T jsonobject2Object(JsonObject json, Class<T> clz)
			throws JsonSyntaxException {
		return mGson.fromJson(json, clz);
	}

	/**
	 *
	 * @param json
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static <T> T string2Object(String json, Type type)
			throws JsonSyntaxException {
		return mGson.fromJson(json, type);
	}

	/**
	 *
	 * @param json
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> ArrayList<T> string2ObjectArray(String json,
			Class<T> clazz) {
		Type type = new TypeToken<ArrayList<JsonObject>>() {
		}.getType();
		ArrayList<JsonObject> jsonObjects = mGson.fromJson(json, type);

		ArrayList<T> arrayList = new ArrayList<>();
		for (JsonObject jsonObject : jsonObjects) {
			arrayList.add(mGson.fromJson(jsonObject, clazz));
		}
		return arrayList;
	}

}
