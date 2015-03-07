package com.neemre.btcdcli4j.jsonrpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

public class JsonMapper {

	private ObjectMapper rawMapper;
	private ObjectWriter rawWriter;


	public JsonMapper() {
		rawMapper = new ObjectMapper();
		rawWriter = rawMapper.writer().withDefaultPrettyPrinter();
		configureMappingProvider();
	}
	
	private void configureMappingProvider() {
		rawMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		rawMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
		rawMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
	}

	public <T> String mapToJson(T entity) {
		try {
			String entityJson = rawWriter.writeValueAsString(entity);
			return entityJson;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> List<String> mapToJson(List<T> entities) {
		try {
			List<String> entitiesJson = new ArrayList<String>();
			for(T entity : entities) {
				entitiesJson.add(rawWriter.writeValueAsString(entity));
			}
			return entitiesJson;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}


	public <T> T mapToEntity(String entityJson, Class<T> entityClass) {
		try {
			T entity = rawMapper.readValue(entityJson, entityClass);
			return entity;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T, S> Map<T, S> mapToMap(String entitiesJson, Class<T> keyClass, Class<S> valueClass) {
		try {
			MapType mapType = rawMapper.getTypeFactory().constructMapType(HashMap.class, keyClass,
					valueClass);
			Map<T, S> entities = rawMapper.readValue(entitiesJson, mapType);
			return entities;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> List<T> mapToList(String entitiesJson, Class<T> entityClass) {
		try {
			CollectionType listType = rawMapper.getTypeFactory().constructCollectionType(
					ArrayList.class, entityClass);
			List<T> entities = rawMapper.readValue(entitiesJson, listType);
			return entities;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T, S> S mapToNestedLists(int depth, String entitiesJson, Class<T> entityClass) {
		try {
			CollectionType outmostListType = rawMapper.getTypeFactory().constructCollectionType(
					ArrayList.class, entityClass);
			for(int i = 0; i < depth; i++) {
				outmostListType = rawMapper.getTypeFactory().constructCollectionType(ArrayList.class,
						outmostListType);
			}
			S entities = rawMapper.readValue(entitiesJson, outmostListType);
			return entities;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}