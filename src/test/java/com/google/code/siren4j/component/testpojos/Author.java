package com.google.code.siren4j.component.testpojos;

import com.google.code.siren4j.annotations.SirenEntity;

@SirenEntity(name = "author", uri = "/authors/{id}")
public class Author extends BasePojo {

    private String firstname;
    private String middlename;
    private String lastname;
    
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getMiddlename() {
        return middlename;
    }
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    

}
