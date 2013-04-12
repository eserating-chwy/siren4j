package com.google.code.siren4j.resource;

import com.google.code.siren4j.annotations.SirenPropertyIgnore;

public class BaseResource implements Resource {
    
    @SirenPropertyIgnore
    protected String overrideUri;
    @SirenPropertyIgnore
    protected Boolean overrideEmbeddedLink;

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

}
