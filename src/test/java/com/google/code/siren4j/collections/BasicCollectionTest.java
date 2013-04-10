package com.google.code.siren4j.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.siren4j.component.Entity;

public class BasicCollectionTest {

    @Test
    public void testToEntity() throws Exception{
	Collection<Float> list = new ArrayList<Float>();
	list.add(34f);
	list.add(64f);
	list.add(123f);
	list.add(4f);
	
	BasicCollection bcoll = new BasicCollection("testCollection",list);
	Entity entity = bcoll.toEntity();
	ObjectMapper mapper = new ObjectMapper();
	System.out.println(mapper.writeValueAsString(entity));
    }
    
}
