package com.google.code.siren4j.converter;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.error.Siren4JException;

/**
 * The resource registry keeps a list of all Siren4J Resources as defined by their
 * {@link Siren4JEntity} annotation. The name is used as the classname. The names therefore must be
 * unique on the classpath. If two of the same named resources are found then an exception will be thrown during
 * classpath scanning. This is required for the
 * {@link ReflectingConverter#toObject(com.google.code.siren4j.component.Entity)} method so
 * it can figure out which classes to reconstitute.
 */
public class ResourceRegistryImpl implements ResourceRegistry {

    private Map<String, Class<?>> entries = new HashMap<String, Class<?>>();

    private static Logger LOG = LoggerFactory.getLogger(ResourceRegistryImpl.class);

    private ResourceRegistryImpl(String... packages) throws Siren4JException {
        init(packages);
    }

    /**
     * Retrieve a new resource registry instance.
     *
     * @param packages array of package pattern strings that will be searched
     * for resources.
     * @return the instance, never <code>null</code>.
     * @throws Siren4JException
     */
    public static ResourceRegistry newInstance(String... packages) throws Siren4JException {
        return new ResourceRegistryImpl(packages);
    }

    /* (non-Javadoc)
     * @see com.google.code.siren4j.converter.ResourceRegistry#getClassByEntityName(java.lang.String)
     */
    public Class<?> getClassByEntityName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be null or empty.");
        }
        return entries.get(name);
    }

    /* (non-Javadoc)
     * @see com.google.code.siren4j.converter.ResourceRegistry#getAllEntries()
     */
    @SuppressWarnings("unchecked")
    public Map<String, Class<?>> getAllEntries() {
        return (Map<String, Class<?>>) MapUtils.unmodifiableMap(entries);
    }

    /* (non-Javadoc)
     * @see com.google.code.siren4j.converter.ResourceRegistry#containsEntityEntry(java.lang.String)
     */
    public boolean containsEntityEntry(String entityName) {
        if (StringUtils.isBlank(entityName)) {
            throw new IllegalArgumentException("entityName cannot be null or empty.");
        }
        return entries.containsKey(entityName);
    }

    /* (non-Javadoc)
     * @see com.google.code.siren4j.converter.ResourceRegistry#containsClassEntry(java.lang.Class)
     */
    public boolean containsClassEntry(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null.");
        }
        return entries.containsValue(clazz);
    }

    /* (non-Javadoc)
     * @see com.google.code.siren4j.converter.ResourceRegistry#putEntry(java.lang.String, java.lang.Class, boolean)
     */
    public void putEntry(String entityName, Class<?> clazz, boolean overwrite) throws Siren4JException {
        if (StringUtils.isBlank(entityName)) {
            throw new IllegalArgumentException("entityName cannot be null or empty.");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null.");
        }
        if (!overwrite && containsEntityEntry(entityName)) {
            String newline = "\n";
            StringBuilder msg = new StringBuilder("Attempted to add a resource with duplicate name to the registry: ");
            msg.append(newline);
            msg.append("Entity Name: ");
            msg.append(entityName);
            msg.append(newline);
            msg.append("Existing class: ");
            msg.append(getClassByEntityName(entityName).getName());
            msg.append(newline);
            msg.append("Other class: ");
            msg.append(clazz.getName());
            LOG.error(msg.toString());
            throw new Siren4JException(msg.toString());
        }
        LOG.info("Found Siren4J resource: [name: "
                + entityName + "] [class: " + clazz.getName() + "]");
        entries.put(entityName, clazz);

    }

    /**
     * Scans the classpath for resources.
     *
     * @param packages
     * @throws Siren4JException
     */
    @SuppressWarnings("deprecation")
    private void init(String... packages) throws Siren4JException {
        LOG.info("Siren4J scanning classpath for resource entries...");
        Reflections reflections = null;
        ConfigurationBuilder builder = new ConfigurationBuilder();
        Collection<URL> urls = new HashSet<URL>();
        if (packages != null && packages.length > 0) {
            //limit scan to packages            
            for (String pkg : packages) {
                urls.addAll(ClasspathHelper.forPackage(pkg));
            }

        } else {
            urls.addAll(ClasspathHelper.forPackage("com."));
            urls.addAll(ClasspathHelper.forPackage("org."));
            urls.addAll(ClasspathHelper.forPackage("net."));
        }
        //Always add Siren4J Resources by default, this will add the collection resource
        //and what ever other resource gets added to this package and has the entity annotation.
        urls.addAll(ClasspathHelper.forPackage("com.google.code.siren4j.resource"));
        builder.setUrls(urls);
        reflections = new Reflections(builder);

        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Siren4JEntity.class);
        for (Class<?> c : types) {
            Siren4JEntity anno = c.getAnnotation(Siren4JEntity.class);
            String name = StringUtils
                    .defaultIfBlank(anno.name(), anno.entityClass().length > 0 ? anno.entityClass()[0] : c.getName());
            putEntry(StringUtils.defaultIfEmpty(name, c.getName()), c, false);
            // Always add the class name as an entry in the index if it does not already exist.
            if (!containsEntityEntry(c.getName())) {
                putEntry(StringUtils.defaultIfEmpty(c.getName(), c.getName()), c, false);
            }
        }
    }


}
