package com.rogervinas.testwebapp.model.mem;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.model.ActiveRecord;

public class GenericMemDb<T extends ActiveRecord<T>>
{
	private static final Logger logger = LoggerFactory.getLogger(GenericMemDb.class);
	
	private final Map<String, T> elems = new HashMap<String, T>();
	
	@SuppressWarnings("unchecked")
	public T load(String id)
	{
		T elem = elems.get(id);
		return elem == null ? null : (T) elem.clone();
	}

	@SuppressWarnings("unchecked")
	public void save(T elem)
	{
		elem = (T) elem.clone();
		logger.info(String.format("Save %s %s", elem.getClass().getSimpleName(), elem.getId()));
		elems.put(elem.getId(), elem);
	}

	public void delete(T elem)
	{
		logger.info(String.format("Delete %s %s", elem.getClass().getSimpleName(), elem.getId()));
		elems.remove(elem.getId());
	}
}
