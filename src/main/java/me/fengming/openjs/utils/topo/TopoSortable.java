package me.fengming.openjs.utils.topo;

import java.util.Collection;

/**
 * @author ZZZank
 */
public interface TopoSortable<T extends TopoSortable<T>> {

    Collection<T> getDependencies();
}
