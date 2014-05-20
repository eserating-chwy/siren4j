/*
******************************************************************************
* (c) Copyright 2002-2014 ADP (Automatic Data Processing), Inc. All rights reserved. Information
* in this publication is subject to change without notice. No part of this
* publication may be reproduced in any form without prior written permission
* of ADP Inc. ADP is a registered trademark of ADP Inc.
* Other products mentioned in this publication may be registered trademarks,
* trademarks, or service marks of their respective manufacturers, companies,
* or organizations.
******************************************************************************
*/
package com.google.code.siren4j.converter;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.testpojos.BlogEntry;
import com.google.code.siren4j.component.testpojos.Comment;
import com.google.code.siren4j.resource.CollectionResource;
import com.google.code.siren4j.util.ComponentUtils;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * ReflectingConverterDefectCoverageTest
 */
public class ReflectingConverterDefectCoverageTest {

    private ReflectingConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new ReflectingConverter();
    }

    /**
     * Coverage for defect https://code.google.com/p/siren4j/issues/detail?id=12.
     * Where setting value on resources overrideURI method is also overidding the href for any
     * Link or Action on the same resource during conversion to Siren.
     *
     * @throws Exception
     */
    @Test
    public void testOverrideURINotSettingOtherURIs() throws Exception {

        Comment comment = new Comment();
        comment.setCommentText("some text");
        comment.setId("6767");
        comment.setCourseid("888");
        comment.setCreatedate(new Date());
        comment.setStatus(Comment.Status.APPROVED);
        comment.setUserid("abe1");

        String overrideURI = "/override";
        comment.setOverrideUri(overrideURI);

        Entity entity = converter.toEntity(comment);
        System.out.println(entity.toString());
        assertNotNull(entity);
        assertEquals(overrideURI, ComponentUtils.getLinkByRel(entity, "self").getHref());
        assertNotEquals(overrideURI, ComponentUtils.getLinkByRel(entity, "course").getHref());
        assertNotEquals(overrideURI, ComponentUtils.getLinkByRel(entity, "replies").getHref());


    }

    /**
     * Coverage for defect https://code.google.com/p/siren4j/issues/detail?id=6.
     * Registry is not indexing by entityClass and therefore unable to find the class type
     * to reconstitute when doing toObject().
     *
     * @throws Exception
     */
    @Test
    public void testRegistryLookupByEntityClass() throws Exception {
        BlogEntry entry = new BlogEntry();
        entry.setTitle("hey");
        entry.setMessage("Some message.");

        Entity entity = converter.toEntity(entry);
        BlogEntry deserializedEntry = (BlogEntry) converter.toObject(entity);
        assertNotNull(deserializedEntry);
        assertEquals("hey", deserializedEntry.getTitle());

    }


    @Test
    public void testSimpleNumberConversion() throws Exception {
        CollectionResource<String> resource = new CollectionResource<String>();
        resource.add("test1");
        resource.add("test2");
        resource.setTotal(2L);

        Entity entity = converter.toEntity(resource);

        CollectionResource<String> deserializedCollection = (CollectionResource<String>) converter.toObject(entity);
        System.out.println(entity.toString());
        ObjectMapper mapper = new ObjectMapper();
        Entity ent = mapper.readValue(entity.toString(), Entity.class);
        assertNotNull(deserializedCollection);
    }
}
