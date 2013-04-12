package com.google.code.siren4j.resource;

import java.util.Collection;

import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Link;

public interface Resource {
    
    /**
     * Overrides annotation settings for the uri of this resource.
     * The override ONLY occurs if this method is called.
     * @param uri the uri to override with. Cannot be <code>null</code> or empty.
     */
    public void setOverrideUri(String uri);
    
    /**
     * Retrieve the override uri if set.
     * @return override uri or <code>null</code> if not set.
     */
    public String getOverrideUri();
    
    /**
     * Override annotation settings indicating that if this is a sub entity and
     * if it should be an embedded link. The override ONLY occurs if this method is called.
     * @param isEmbeddedLink
     */
    public void setOverrideEmbeddedLink(Boolean isEmbeddedLink);
    
    /**
     * Retrieve the override embedded link setting if set.
     * @return <code>null</code> if not set.
     */
    public Boolean getOverrideEmbeddedLink();
    
    /**
     * Set links for this entity resource. These will be merged with links set by annotation
     * by the reflecting converter..
     * Links set here will override the same named links done in annotations.
     * @param links may be <code>null</code> or empty.
     */
    public void setEntityLinks(Collection<Link> links);
    
    /**
     * Get links set on this resource by the setter method, does not retrieve those set by
     * annotation.
     * @return links for the entity, may be <code>null</code> or empty.
     */
    public Collection<Link> getEntityLinks();
    
    /**
     * Set actions for this entity resource. These will be merged with actions set by annotation
     * by the reflecting converter.
     * Actions set here will override the same named actions done in annotations.
     * @param actions may be <code>null</code> or empty.
     */
    public void setEntityActions(Collection<Action> actions);
    
    /**
     * Get actions set on this resource by the setter method, does not retrieve those set by
     * annotation.
     * @return actions may be <code>null</code> or empty.
     */
    public Collection<Action> getEntityActions();
    

}
