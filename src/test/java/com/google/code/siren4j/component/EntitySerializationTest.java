package com.google.code.siren4j.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.siren4j.component.builder.ActionBuilder;
import com.google.code.siren4j.component.builder.EntityBuilder;
import com.google.code.siren4j.component.builder.FieldBuilder;
import com.google.code.siren4j.component.builder.LinkBuilder;
import com.google.code.siren4j.component.impl.ActionImpl.Method;
import com.google.code.siren4j.meta.FieldType;

public class EntitySerializationTest {

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testEntityToJsonSerialization() throws Exception {
	Entity orders = createTestEntity();
	ObjectMapper mapper = new ObjectMapper();
	System.out.println(mapper.writeValueAsString(orders));
    }
    
    private Entity createTestEntity() {
	
	//List item 1
	List<Entity> embedded1 = new ArrayList<Entity>();
	Map<String, Object> props1 = new HashMap<String, Object>();
	List<Link> links1 = new ArrayList<Link>();
	embedded1.add(createEmbeddedEntity("/baskets/98712", "basket"));
	embedded1.add(createEmbeddedEntity("/customer/7809", "basket"));	
	props1.put("total", 30.00f);
	props1.put("currency", "USD");
	props1.put("status", "shipped");
	links1.add(createLink("/orders/123", Link.RELATIONSHIP_SELF));
	
	Entity listItem1 = createEntity(
		new String[]{"order", "list-item"},
		new String[]{"order"},
		props1,
		embedded1,
		links1,
		null		
		);
	
	//List item 2
	List<Entity> embedded2 = new ArrayList<Entity>();
	Map<String, Object> props2 = new HashMap<String, Object>();
	List<Link> links2 = new ArrayList<Link>();
	embedded2.add(createEmbeddedEntity("/baskets/98713", "basket"));
	embedded2.add(createEmbeddedEntity("/customer/12369", "basket"));
	props2.put("total", 20.00f);
	props2.put("currency", "USD");
	props2.put("status", "processing");
	links2.add(createLink("/orders/124", Link.RELATIONSHIP_SELF));
	
	Entity listItem2 = createEntity(
		new String[]{"order", "list-item"},
		new String[]{"order"},
		props2,
		embedded2,
		links2,
		null		
		);
	
	List<Action> orderActions = new ArrayList<Action>();
	List<Entity> orderEntities = new ArrayList<Entity>();
	List<Link> orderLinks = new ArrayList<Link>();
	Map<String, Object> orderProps = new HashMap<String, Object>();
	List<Field> actionFields = new ArrayList<Field>();
	
	actionFields.add(createField("id", FieldType.NUMBER, null));
	
	orderActions.add(createAction(
		"find-order",
		new String[] {"find-order"},
		(String)null,
		(Method)null,
		"/orders",
		(String)null,
		actionFields
		));
	
	orderEntities.add(listItem1);
	orderEntities.add(listItem2);
	
	orderLinks.add(createLink("/orders", Link.RELATIONSHIP_SELF));
	orderLinks.add(createLink("/orders?page=2", Link.RELATIONSHIP_NEXT));
	
	orderProps.put("currentlyProcessing", 14);
	orderProps.put("shippedToday", 20);
	
	Entity orders = createEntity(new String[]{"orders"}, null, orderProps, orderEntities, orderLinks, orderActions);
	return orders;
    }
    
    private Entity createEmbeddedEntity(String href, String... rel) {
	EntityBuilder builder = EntityBuilder.newInstance();
	return builder.setRelationship(rel).setHref(href).build();
    }
    
    private Entity createEntity(String[] entityClass, String[] rel, Map<String, Object> props,
	    List<Entity> entities, List<Link> links, List<Action> actions) {
	EntityBuilder builder = EntityBuilder.newInstance();
	if(!ArrayUtils.isEmpty(entityClass)) {
	    builder.setEntityClass(entityClass);
	}
	if(!ArrayUtils.isEmpty(rel)) {
	    builder.setRelationship(rel);
	}
	if(!MapUtils.isEmpty(props)) {
	    builder.addProperties(props);
	}
	if(!CollectionUtils.isEmpty(entities)) {
	    builder.addSubEntities(entities);
	}
	if(!CollectionUtils.isEmpty(links)) {
	    builder.addLinks(links);
	}
	if(!CollectionUtils.isEmpty(actions)) {
	    builder.addActions(actions);
	}
	
	return builder.build();
    }
    
    private Link createLink(String href, String... rel) {
	LinkBuilder builder = LinkBuilder.newInstance();
	return builder.setRelationship(rel).setHref(href).build();
    }
    
    private Field createField(String name, FieldType type, String value) {
	FieldBuilder builder = FieldBuilder.newInstance();
	builder.setName(name)
	    .setType(type);
	if(StringUtils.isNotBlank(value)) {
	    builder.setValue(value);
	}
	return builder.build();
	
    }
    
    private Action createAction(String name, String[] actionClass, String title, Method method, String href, String type, List<Field> fields) {
	ActionBuilder builder = ActionBuilder.newInstance();
	
	builder.setName(name)
	    .setHref(href);
	if(StringUtils.isNotBlank(title)) {
	    builder.setTitle(title);
	}
	if(method != null) {
	    builder.setMethod(method);
	}
	if(StringUtils.isNotBlank(type)) {
	    builder.setType(type);
	}
	if(!CollectionUtils.isEmpty(fields)) {
	    builder.addFields(fields);
	}
	if(ArrayUtils.isEmpty(actionClass)) {
	    builder.setActionClass(actionClass);
	}
	return builder.build();
	
	
    }
    
   

}
