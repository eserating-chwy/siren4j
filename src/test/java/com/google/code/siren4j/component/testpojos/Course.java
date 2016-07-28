package com.google.code.siren4j.component.testpojos;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JActionField;
import com.google.code.siren4j.annotations.Siren4JCondition;
import com.google.code.siren4j.annotations.Siren4JCondition.Is;
import com.google.code.siren4j.annotations.Siren4JCondition.Type;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JFieldOption;
import com.google.code.siren4j.annotations.Siren4JInclude;
import com.google.code.siren4j.annotations.Siren4JInclude.Include;
import com.google.code.siren4j.annotations.Siren4JLink;
import com.google.code.siren4j.annotations.Siren4JMetaData;
import com.google.code.siren4j.annotations.Siren4JOptionData;
import com.google.code.siren4j.annotations.Siren4JSubEntity;
import com.google.code.siren4j.component.impl.ActionImpl.Method;

@Siren4JInclude(Include.NON_NULL)
@Siren4JEntity(entityClass = {"course", "test"}, uri = "/courses/{courseid}", links = { @Siren4JLink(rel = "reviews",
    href = "/courseReviews/course/{courseid}") }, actions = { @Siren4JAction(name = "addReview", method = Method.POST,
       href = "/courseReviews/course/{courseid}", 
       condition = @Siren4JCondition(name = "getDescription", logic = Is.NOTEMPTY, type = Type.METHOD), 
       fields = {
        @Siren4JActionField(name = "userid", type = "text", required = true,
                fieldClass = {"classifyMe", "classifyMeAgain"},
                metaData = {
                        @Siren4JMetaData(key = "extraStuff1", value = "hello"),
                        @Siren4JMetaData(key = "extraStuff2", value = "more stuff")
                }),
        @Siren4JActionField(name = "hasoptions", type = "checkbox", required = true,
                options = {
                        @Siren4JFieldOption(title = "option1", value = "foo"),
                        @Siren4JFieldOption(title = "option2", value = "foo2", optionDefault = true,
                           data = {
                                   @Siren4JOptionData(key = "key1", value = "value1"),
                                   @Siren4JOptionData(key = "key2", value = "{courseid}")
                           }
                        )
                }),
        @Siren4JActionField(name = "body", type = "text", required = true, value="{myenum}", maxLength = 250) }) })
public class Course extends BasePojo {

    public Course() {

    }

    private String courseid;
    private String title;
    private String description;
    private String type;
    private BigDecimal cost;
    private MYENUM myenum = MYENUM.ENUM_VALUE2;

    @Siren4JSubEntity(rel = "lastComment")
    private Comment lastComment;

    @Siren4JSubEntity(links = { @Siren4JLink(rel = "course", href = "/courses/{courseid}/overridden"),
        @Siren4JLink(rel = "foo", href = "/foo/{title}") }, actions = {
           @Siren4JAction(name = "Delete", method = Method.DELETE, href = "/comments/{id}/overridden"),
           @Siren4JAction(name = "Reject", method = Method.PUT, href = "/comments/{id}/reject",
               condition = @Siren4JCondition(name = "parent.getDescription", logic = Is.NOTEMPTY, type = Type.METHOD)
               ) })
    private Comment firstComment;

    @Siren4JSubEntity()
    private Comment nullComment;
    
    @Siren4JSubEntity(rel = "embedComment", embeddedLink = true)
    private Comment embedComment;

    private Date createdate;

    @Siren4JSubEntity(rel = "authors", uri = "/authors?courseid={parent.courseid}/{offset}")
    private Collection<Author> authors;

    @Siren4JSubEntity(uri = "/authors?courseid={parent.courseid}/{[offset]}")
    private Collection<Author> authors2;
    
    private String[] allowedTypes = new String[]{"Online", "Classroom", "External"};
    
    private Collection<Integer> basicCollection;

    private SortedSet<String> setOfStrings;
    
    private Map<String, Boolean> boolMap;

    public Map<String, Boolean> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<String, Boolean> boolMap) {
        this.boolMap = boolMap;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Collection<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<Author> authors) {
        this.authors = authors;
    }

    public Comment getLastComment() {
        return lastComment;
    }

    public void setLastComment(Comment lastComment) {
        this.lastComment = lastComment;
    }

    public Comment getFirstComment() {
        return firstComment;
    }

    public void setFirstComment(Comment firstComment) {
        this.firstComment = firstComment;
    }

    public Comment getNullComment() {
        return nullComment;
    }

    public Collection<Author> getAuthors2() {
        return authors2;
    }

    public void setAuthors2(Collection<Author> authors2) {
        this.authors2 = authors2;
    }

    public Comment getEmbedComment() {
        return embedComment;
    }

    public void setEmbedComment(Comment embedComment) {
        this.embedComment = embedComment;
    }

    public String[] getAllowedTypes() {
        return allowedTypes;
    }

    public void setAllowedTypes(String[] allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    public Collection<Integer> getBasicCollection() {
        return basicCollection;
    }

    public void setBasicCollection(Collection<Integer> basicCollection) {
        this.basicCollection = basicCollection;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public MYENUM getMyenum() {
        return myenum;
    }

    public void setMyenum(MYENUM myenum) {
        this.myenum = myenum;
    }

    public enum MYENUM {
        ENUM_VALUE1,
        ENUM_VALUE2
    }

	public SortedSet<String> getSetOfStrings() {
		return setOfStrings;
	}

	public void setSetOfStrings(SortedSet<String> setOfStrings) {
		this.setOfStrings = setOfStrings;
	}
    

}
