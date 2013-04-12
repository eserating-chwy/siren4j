package com.google.code.siren4j.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.code.siren4j.annotations.SirenSubEntity;

/**
 * Class representing a collection of resources. 
 */
public class CollectionResource<T> extends BaseResource implements Collection<T>{
    @SirenSubEntity
    private Collection<T> items = new ArrayList<T>();
    private long offset;
    private long limit;
    private long total;
    
    public Collection<T> getItems() {
        return items;
    }
    
    public void setItems(Collection<T> items) {
        this.items = items;
    }    
    
    public void clear() {
	if(items != null) {
	    items.clear();
	}
    }
    
    public boolean isEmpty() {
	return items == null || items.isEmpty();
    }
    
    public long getOffset() {
        return offset;
    }
    
    public void setOffset(long offset) {
        this.offset = offset;
    }
    
    public long getLimit() {
        return limit;
    }
    
    public void setLimit(long limit) {
        this.limit = limit;
    }
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }

    public int size() {
	return getCollection().size();
    }

    public boolean contains(Object o) {
	return getCollection().contains(o);
    }

    public Iterator<T> iterator() {
	return getCollection().iterator();
    }

    public Object[] toArray() {
	return getCollection().toArray();
    }

    @SuppressWarnings("hiding")
    public <T> T[] toArray(T[] a) {
	return getCollection().toArray(a);
    }

    public boolean remove(Object o) {
	return getCollection().remove(o);
    }

    public boolean containsAll(Collection<?> c) {
	return getCollection().containsAll(c);
    }    

    public boolean removeAll(Collection<?> c) {
	return getCollection().removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
	return getCollection().retainAll(c);
    }    

    public boolean add(T e) {
	return getCollection().add(e);
    }

    public boolean addAll(Collection<? extends T> c) {
	return getCollection().addAll(c);
    }
    
    private Collection<T> getCollection() {
	if(items == null) {
	    items = new ArrayList<T>();
	}
	return items;
    }
}
