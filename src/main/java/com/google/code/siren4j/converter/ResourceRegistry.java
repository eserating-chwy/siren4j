package com.google.code.siren4j.converter;

import java.util.Map;

import com.google.code.siren4j.error.Siren4JException;

/**
 * The resource registry keeps a list of all Siren4J Resources as defined by their
 * {@link Siren4JEntity} annotation. The name is used as the classname. The names therefore must be
 * unique on the classpath.
 *
 */
public interface ResourceRegistry {

    /**
     * Retrieve the class for the specified entity resource name.
     * @param name the entity name, cannot be <code>null</code> or empty.
     * @return the class for the entity name or <code>null</code> if not found.
     */
    public Class<?> getClassByEntityName(String name);

    /**
     * Returns all entries in the registry.
     * @return map of all entries, never <code>null</code>, may be empty.
     */
    public Map<String, Class<?>> getAllEntries();

    /**
     * Determine if an entry exists for the given entity name.
     * @param entityName cannot be <code>null</code> or empty.
     * @return <code>true</code> if an entry was found.
     */
    public boolean containsEntityEntry(String entityName);

    /**
     * Determine if an entry exists for the given class.
     * @param clazz cannot be <code>null</code>.
     * @return <code>true</code> if an entry was found.
     */
    public boolean containsClassEntry(Class<?> clazz);

    /**
     * Puts an entry into the registry. This can also overwrite existing entries if the flag is
     * set. This method is exposed for testing and edge cases and should be used with care.
     * @param entityName cannot be <code>null</code> or empty.
     * @param clazz cannot be <code>null</code>.
     * @param overwrite if <code>true</code> then overwriting of an existing entry will be allowed,
     * if <code>false</code> then an exception will be thrown if attempting to overwrite an existing entry.
     * @throws Siren4JException if an overwrite is attempted and the flag is not <code>true</code>.
     */
    public void putEntry(String entityName, Class<?> clazz, boolean overwrite) throws Siren4JException;

}