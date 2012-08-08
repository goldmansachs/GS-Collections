/*
 * Copyright 2011 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webguys.ponzu.api.map.sorted;

import java.util.Collection;
import java.util.SortedMap;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.collection.MutableCollection;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.list.MutableListMultimap;
import com.webguys.ponzu.api.partition.list.PartitionMutableList;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.tuple.Pair;

/**
 * A MutableSortedMap is similar to a JCF Map but adds additional useful internal iterator methods.
 * The MutableSortedMap interface additionally implements some of the methods in the Smalltalk Dictionary protocol.
 */
public interface MutableSortedMap<K, V>
        extends SortedMapIterable<K, V>, SortedMap<K, V>, Cloneable
{
    /**
     * Creates a new instance of the same type with the same internal Comparator.
     */
    MutableSortedMap<K, V> newEmpty();

    /**
     * Adds all the entries derived from <code>collection</code> to <code>this</code>.
     * The key and value for each entry is determined by applying the <code>keyFunction</code> and <code>valueFunction</code> to each item in <code>collection</code>.
     * Any entry in <code>map</code> that has the same key as an entry in <code>this</code> will have it's value replaced by that in <code>map</code>.
     */
    <E> MutableSortedMap<K, V> transformKeysAndValues(
            Collection<E> collection,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction);

    /**
     * Remove an entry from the map at the specified <code>key</code>.
     *
     * @return The value removed from entry at key, or null if not found.
     * @see #remove(Object)
     */
    V removeKey(K key);

    /**
     * Return the value in the Map that corresponds to the specified key, or if there is no value
     * at the key, return the result of evaluating the specified Function0, and put that value in the
     * map at the specified key.
     */
    V getIfAbsentPut(K key, Generator<? extends V> function);

    /**
     * Return the value in the Map that corresponds to the specified key, or if there is no value
     * at the key, return the result of evaluating the specified one argument Function
     * using the specified parameter, and put that value in the map at the specified key.
     */
    <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter);

    MutableSortedMap<K, V> with(Pair<K, V>... pairs);

    /**
     * Returns an unmodifiable view of this map.
     * This method allows modules to provide users with "read-only" access to internal maps.
     * Any query operations on the returned map that "read through" to this map and attempt
     * to modify the returned map, whether direct or via its iterator, result in an
     * {@link UnsupportedOperationException}.
     * <p/>
     * The returned map will be <tt>Serializable</tt> if this map is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this map.
     */
    MutableSortedMap<K, V> asUnmodifiable();

    /**
     * Returns an immutable copy of this map.
     * If the map is immutable, it returns itself.
     * <p/>
     * The returned map will be <tt>Serializable</tt> if this map is <tt>Serializable</tt>.
     */
    ImmutableSortedMap<K, V> toImmutable();

    /**
     * Returns a synchronized (thread-safe) map backed by the specified
     * map.  In order to guarantee serial access, it is critical that
     * <strong>all</strong> access to the backing map is accomplished
     * through the returned map.<p>
     * <p/>
     * It is imperative that the user manually synchronize on the returned
     * map when iterating over any of its collection views:
     * <pre>
     *  MutableMap map = myMutableMap.asSynchronized();
     *      ...
     *  Set set = map.keySet();  // Needn't be in synchronized block
     *      ...
     *  synchronized(map)
     *  {  // Synchronizing on map, not set!
     *      Iterator i = s.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     * <p/>
     * The preferred way of iterating over a synchronized collection is to use the collection.forEach()
     * method which is properly synchronized internally.
     * <pre>
     *  MutableMap map = myMutableMap.asSynchronized();
     *      ...
     *  Set set = map.keySet();  // Needn't be in synchronized block
     *     ...
     *  Iterate.forEach(set, new Procedure()
     *  {
     *      public void value(Object each)
     *      {
     *          ...
     *      }
     *  });
     * </pre>
     * <p/>
     * <p>The returned map will be serializable if the specified map is
     * serializable.
     */
    MutableSortedMap<K, V> asSynchronized();

    MutableSortedMap<K, V> filter(Predicate2<? super K, ? super V> predicate);

    MutableSortedMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate);

    <K2, V2> MutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> MutableSortedMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function);

    <R> MutableList<R> transform(Function<? super V, ? extends R> function);

    <R> MutableList<R> transformIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function);

    <R> MutableList<R> flatTransform(Function<? super V, ? extends Iterable<R>> function);

    MutableList<V> filterNot(Predicate<? super V> predicate);

    MutableList<V> filter(Predicate<? super V> predicate);

    PartitionMutableList<V> partition(Predicate<? super V> predicate);

    <S> MutableList<Pair<V, S>> zip(Iterable<S> that);

    MutableList<Pair<V, Integer>> zipWithIndex();

    MutableSet<Entry<K, V>> entrySet();

    /**
     * The underlying set for the keys is sorted in ascending order according to their natural ordering or a custom comparator.
     * However, Java 5 TreeMap returns a keySet that does not inherit from SortedSet therefore we have decided to
     * return the keySet simply as a MutableSet to maintain Java 5 compatibility.
     */
    //todo: Change return type to MutableSortedSet when we move to Java 6
    MutableSet<K> keySet();

    MutableSortedMap<K, V> headMap(K toKey);

    MutableSortedMap<K, V> tailMap(K fromKey);

    MutableSortedMap<K, V> subMap(K fromKey, K toKey);

    MutableCollection<V> values();

    MutableSortedMap<K, V> clone();

    <VV> MutableListMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> MutableListMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);
}
