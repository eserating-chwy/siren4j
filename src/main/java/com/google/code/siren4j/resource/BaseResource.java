package com.google.code.siren4j.resource;

import java.util.Collection;

import com.google.code.siren4j.annotations.SirenPropertyIgnore;
import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Link;

public abstract class BaseResource implements Resource {
    
    @SirenPropertyIgnore
    protected String overrideUri;
    @SirenPropertyIgnore
    protected Boolean overrideEmbeddedLink;
    @SirenPropertyIgnore
    protected Collection<Link> entityLinks;
    @SirenPropertyIgnore
    protected Collection<Action> entityActions;
    

    public void setOverrideUri(String uri) {
	overrideUri = uri;
    }

    public String getOverrideUri() {
	return overrideUri;
    }

    public void setOverrideEmbeddedLink(Boolean isEmbeddedLink) {
	overrideEmbeddedLink = isEmbeddedLink;
    }

    public Boolean getOverrideEmbeddedLink() {
	return overrideEmbeddedLink;
    }

	public Collection<Link> getEntityLinks() {
		return entityLinks;
	}

	public void setEntityLinks(Collection<Link> entityLinks) {
		this.entityLinks = entityLinks;
	}

	public Collection<Action> getEntityActions() {
		return entityActions;
	}

	public void setEntityActions(Collection<Action> entityActions) {
		this.entityActions = entityActions;
	}
    
    

}
