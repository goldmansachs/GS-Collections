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

package com.gs.collections.impl.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;

/**
 * A synchronized view of a SortedMap.
 *
 * @see SynchronizedMapIterable (java.util.SortedMap)
 */
public abstract class SynchronizedMapIterable<K, V>
        implements MapIterable<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;

    protected final Object lock;
    private final MapIterable<K, V> mapIterable;

    protected SynchronizedMapIterable(MapIterable<K, V> newMap)
    {
        this(newMap, null);
    }

    protected SynchronizedMapIterable(MapIterable<K, V> newMap, Object newLock)
    {
        if (newMap == null)
        {
            throw new IllegalArgumentException("Cannot create a SynchronizedMapIterable on a null map");
        }
        this.mapIterable = newMap;
        this.lock = newLock == null ? this : newLock;
    }

    protected MapIterable<K, V> getMap()
    {
        return this.mapIterable;
    }

    public V get(Object key)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.get(key);
        }
    }

    public boolean containsKey(Object key)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.containsKey(key);
        }
    }

    public boolean containsValue(Object value)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.containsValue(value);
        }
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        synchronized (this.lock)
        {
            this.mapIterable.forEachValue(procedure);
        }
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        synchronized (this.lock)
        {
            this.mapIterable.forEachKey(procedure);
        }
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure2)
    {
        synchronized (this.lock)
        {
            this.mapIterable.forEachKeyValue(procedure2);
        }
    }

    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.getIfAbsent(key, function);
        }
    }

    public <P> V getIfAbsentWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.getIfAbsentWith(key, function, parameter);
        }
    }

    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.ifPresentApply(key, function);
        }
    }

    public abstract RichIterable<K> keysView();

    public abstract RichIterable<V> valuesView();

    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.detect(predicate);
        }
    }

    public int size()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.size();
        }
    }

    public boolean isEmpty()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.isEmpty();
        }
    }

    public boolean notEmpty()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.notEmpty();
        }
    }

    public V getFirst()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.getFirst();
        }
    }

    public V getLast()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.getLast();
        }
    }

    public boolean contains(Object object)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.contains(object);
        }
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.containsAllIterable(source);
        }
    }

    public boolean containsAll(Collection<?> source)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.containsAll(source);
        }
    }

    public boolean containsAllArguments(Object... elements)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.containsAllArguments(elements);
        }
    }

    public <R extends Collection<V>> R select(Predicate<? super V> predicate, R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.select(predicate, target);
        }
    }

    public <P, R extends Collection<V>> R selectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.selectWith(predicate, parameter, targetCollection);
        }
    }

    public <R extends Collection<V>> R reject(Predicate<? super V> predicate, R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.reject(predicate, target);
        }
    }

    public <P, R extends Collection<V>> R rejectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.rejectWith(predicate, parameter, targetCollection);
        }
    }

    public V detect(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.detect(predicate);
        }
    }

    public V detectIfNone(Predicate<? super V> predicate, Function0<? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.detectIfNone(predicate, function);
        }
    }

    public int count(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.count(predicate);
        }
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.anySatisfy(predicate);
        }
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.allSatisfy(predicate);
        }
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super V, ? extends IV> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.injectInto(injectedValue, function);
        }
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super V> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.injectInto(injectedValue, function);
        }
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super V> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.injectInto(injectedValue, function);
        }
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super V> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.injectInto(injectedValue, function);
        }
    }

    public MutableList<V> toList()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toList();
        }
    }

    public MutableList<V> toSortedList()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSortedList();
        }
    }

    public MutableList<V> toSortedList(Comparator<? super V> comparator)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSortedList(comparator);
        }
    }

    public MutableSortedSet<V> toSortedSet()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSortedSet();
        }
    }

    public MutableSortedSet<V> toSortedSet(Comparator<? super V> comparator)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSortedSet(comparator);
        }
    }

    public MutableSet<V> toSet()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSet();
        }
    }

    public MutableBag<V> toBag()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toBag();
        }
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toMap(keyFunction, valueFunction);
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSortedMap(keyFunction, valueFunction);
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Comparator<? super NK> comparator,
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSortedMap(comparator, keyFunction, valueFunction);
        }
    }

    public LazyIterable<V> asLazy()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.asLazy();
        }
    }

    public Object[] toArray()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toArray();
        }
    }

    public <T> T[] toArray(T[] a)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toArray(a);
        }
    }

    public V min(Comparator<? super V> comparator)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.min(comparator);
        }
    }

    public V max(Comparator<? super V> comparator)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.max(comparator);
        }
    }

    public V min()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.min();
        }
    }

    public V max()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.max();
        }
    }

    public String makeString()
    {
        synchronized (this.lock)
        {
            return this.mapIterable.makeString();
        }
    }

    public String makeString(String separator)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.makeString(separator);
        }
    }

    public String makeString(String start, String separator, String end)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.makeString(start, separator, end);
        }
    }

    public void appendString(Appendable appendable)
    {
        synchronized (this.lock)
        {
            this.mapIterable.appendString(appendable);
        }
    }

    public void appendString(Appendable appendable, String separator)
    {
        synchronized (this.lock)
        {
            this.mapIterable.appendString(appendable, separator);
        }
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        synchronized (this.lock)
        {
            this.mapIterable.appendString(appendable, start, separator, end);
        }
    }

    public <S, R extends Collection<Pair<V, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.zip(that, target);
        }
    }

    public <R extends Collection<Pair<V, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.zipWithIndex(target);
        }
    }

    public RichIterable<RichIterable<V>> chunk(int size)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.chunk(size);
        }
    }

    public <KK, R extends MutableMultimap<KK, V>> R groupBy(Function<? super V, ? extends KK> function, R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.groupBy(function, target);
        }
    }

    public <KK, R extends MutableMultimap<KK, V>> R groupByEach(Function<? super V, ? extends Iterable<KK>> function, R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.groupByEach(function, target);
        }
    }

    public <A extends Comparable<? super A>> V minBy(Function<? super V, ? extends A> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.minBy(function);
        }
    }

    public <A extends Comparable<? super A>> V maxBy(Function<? super V, ? extends A> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.maxBy(function);
        }
    }

    public <A extends Comparable<? super A>> MutableSortedSet<V> toSortedSetBy(Function<? super V, ? extends A> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSortedSetBy(function);
        }
    }

    public <A extends Comparable<? super A>> MutableList<V> toSortedListBy(Function<? super V, ? extends A> function)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.toSortedListBy(function);
        }
    }

    public <A, R extends Collection<A>> R flatCollect(Function<? super V, ? extends Iterable<A>> function, R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.flatCollect(function, target);
        }
    }

    public <A, R extends Collection<A>> R collectIf(
            Predicate<? super V> predicate,
            Function<? super V, ? extends A> function,
            R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.collectIf(predicate, function, target);
        }
    }

    public <P, A, R extends Collection<A>> R collectWith(
            Function2<? super V, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.collectWith(function, parameter, targetCollection);
        }
    }

    public <A, R extends Collection<A>> R collect(
            Function<? super V, ? extends A> function,
            R target)
    {
        synchronized (this.lock)
        {
            return this.mapIterable.collect(function, target);
        }
    }

    public void forEach(Procedure<? super V> procedure)
    {
        synchronized (this.lock)
        {
            this.mapIterable.forEach(procedure);
        }
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        synchronized (this.lock)
        {
            this.mapIterable.forEachWithIndex(objectIntProcedure);
        }
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure2, P parameter)
    {
        synchronized (this.lock)
        {
            this.mapIterable.forEachWith(procedure2, parameter);
        }
    }

    public Iterator<V> iterator()
    {
        return this.mapIterable.iterator();
    }
}

