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
package com.google.code.siren4j.component.testpojos;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.resource.BaseResource;

/**
 * BlogEntry
 */
@Siren4JEntity(entityClass = {"blog"}, uri = "/blogs/{id}")
public class BlogEntry extends BaseResource {
    private String title;
    private String message;

    public BlogEntry() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
