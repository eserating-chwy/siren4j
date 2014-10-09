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
 * ResourceA
 */
@Siren4JEntity(entityClass = "ResourceA")
public class ResourceA extends BaseResource {
    private String prop1;
    private String prop2;

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }
}
