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

package com.webguys.ponzu.impl.map;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.bag.MutableBag;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.webguys.ponzu.api.block.function.primitive.IntObjectToIntFunction;
import com.webguys.ponzu.api.block.function.primitive.LongObjectToLongFunction;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MapIterable;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.map.sorted.MutableSortedMap;
import com.webguys.ponzu.api.multimap.MutableMultimap;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.set.sorted.MutableSortedSet;
import com.webguys.ponzu.api.tuple.Pair;

public abstract class AbstractMapIterable<K, V> implements MapIterable<K, V>
{
    protected int keyAndValueHashCode(K key, V value)
    {
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    protected boolean keyAndValueEquals(K key, V value, Map<K, V> map)
    {
        if (value == null && !map.containsKey(key))
        {
            return false;
        }

        V oValue = map.get(key);
        return oValue == value || (oValue != null && oValue.equals(value));
    }

    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    public boolean notEmpty()
    {
        return !this.isEmpty();
    }

    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        V result = this.get(key);
        return this.isAbsent(result, key) ? null : function.valueOf(result);
    }

    protected boolean isAbsent(V result, K key)
    {
        return result == null && !this.containsKey(key);
    }

    public V getIfAbsent(K key, Generator<? extends V> function)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            result = function.value();
        }
        return result;
    }

    public <P> V getIfAbsentWith(
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            result = function.valueOf(parameter);
        }
        return result;
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.valuesView().allSatisfy(predicate);
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.valuesView().anySatisfy(predicate);
    }

    public void appendString(Appendable appendable)
    {
        this.valuesView().appendString(appendable);
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.valuesView().appendString(appendable, separator);
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        this.valuesView().appendString(appendable, start, separator, end);
    }

    public MutableBag<V> toBag()
    {
        return this.valuesView().toBag();
    }

    public LazyIterable<V> asLazy()
    {
        return this.valuesView().asLazy();
    }

    public MutableList<V> toList()
    {
        return this.valuesView().toList();
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.valuesView().toMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.valuesView().toSortedMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Comparator<? super NK> comparator,
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.valuesView().toSortedMap(comparator, keyFunction, valueFunction);
    }

    public MutableSet<V> toSet()
    {
        return this.valuesView().toSet();
    }

    public MutableList<V> toSortedList()
    {
        return this.valuesView().toSortedList();
    }

    public MutableList<V> toSortedList(Comparator<? super V> comparator)
    {
        return this.valuesView().toSortedList(comparator);
    }

    public <R extends Comparable<? super R>> MutableList<V> toSortedListBy(Function<? super V, ? extends R> function)
    {
        return this.valuesView().toSortedListBy(function);
    }

    public MutableSortedSet<V> toSortedSet()
    {
        return this.valuesView().toSortedSet();
    }

    public MutableSortedSet<V> toSortedSet(Comparator<? super V> comparator)
    {
        return this.valuesView().toSortedSet(comparator);
    }

    public <R extends Comparable<? super R>> MutableSortedSet<V> toSortedSetBy(Function<? super V, ? extends R> function)
    {
        return this.valuesView().toSortedSetBy(function);
    }

    public RichIterable<RichIterable<V>> chunk(int size)
    {
        return this.valuesView().chunk(size);
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.forEachValue(procedure);
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure2, P parameter)
    {
        this.valuesView().forEachWith(procedure2, parameter);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        this.valuesView().forEachWithIndex(objectIntProcedure);
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        this.keysView().forEach(procedure);
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        this.valuesView().forEach(procedure);
    }

    public <R> RichIterable<R> transform(Function<? super V, ? extends R> function)
    {
        return this.valuesView().transform(function);
    }

    public <R, C extends Collection<R>> C transform(Function<? super V, ? extends R> function, C target)
    {
        return this.valuesView().transform(function, target);
    }

    public <R> RichIterable<R> transformIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.valuesView().transformIf(predicate, function);
    }

    public <R, C extends Collection<R>> C transformIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function, C target)
    {
        return this.valuesView().transformIf(predicate, function, target);
    }

    public <P, R, C extends Collection<R>> C transformWith(Function2<? super V, ? super P, ? extends R> function, P parameter, C targetCollection)
    {
        return this.valuesView().transformWith(function, parameter, targetCollection);
    }

    public boolean contains(Object object)
    {
        return this.containsValue(object);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return this.valuesView().containsAllArguments(elements);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return this.valuesView().containsAllIterable(source);
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.containsAllIterable(source);
    }

    public int count(Predicate<? super V> predicate)
    {
        return this.valuesView().count(predicate);
    }

    public V find(Predicate<? super V> predicate)
    {
        return this.valuesView().find(predicate);
    }

    public V findIfNone(Predicate<? super V> predicate, Generator<? extends V> function)
    {
        return this.valuesView().findIfNone(predicate, function);
    }

    public <R> RichIterable<R> flatTransform(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.valuesView().flatTransform(function);
    }

    public <R, C extends Collection<R>> C flatTransform(Function<? super V, ? extends Iterable<R>> function, C target)
    {
        return this.valuesView().flatTransform(function, target);
    }

    public V getFirst()
    {
        return this.valuesView().getFirst();
    }

    public V getLast()
    {
        return this.valuesView().getLast();
    }

    public <R, C extends MutableMultimap<R, V>> C groupBy(Function<? super V, ? extends R> function, C target)
    {
        return this.valuesView().groupBy(function, target);
    }

    public <R, C extends MutableMultimap<R, V>> C groupByEach(Function<? super V, ? extends Iterable<R>> function, C target)
    {
        return this.valuesView().groupByEach(function, target);
    }

    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super V, ? extends IV> function)
    {
        return this.valuesView().foldLeft(initialValue, function);
    }

    public int foldLeft(int initialValue, IntObjectToIntFunction<? super V> function)
    {
        return this.valuesView().foldLeft(initialValue, function);
    }

    public long foldLeft(long initialValue, LongObjectToLongFunction<? super V> function)
    {
        return this.valuesView().foldLeft(initialValue, function);
    }

    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super V> function)
    {
        return this.valuesView().foldLeft(initialValue, function);
    }

    public String makeString()
    {
        return this.valuesView().makeString();
    }

    public String makeString(String separator)
    {
        return this.valuesView().makeString(separator);
    }

    public String makeString(String start, String separator, String end)
    {
        return this.valuesView().makeString(start, separator, end);
    }

    public V max()
    {
        return this.valuesView().max();
    }

    public V max(Comparator<? super V> comparator)
    {
        return this.valuesView().max(comparator);
    }

    public <R extends Comparable<? super R>> V maxBy(Function<? super V, ? extends R> function)
    {
        return this.valuesView().maxBy(function);
    }

    public V min()
    {
        return this.valuesView().min();
    }

    public V min(Comparator<? super V> comparator)
    {
        return this.valuesView().min(comparator);
    }

    public <R extends Comparable<? super R>> V minBy(Function<? super V, ? extends R> function)
    {
        return this.valuesView().minBy(function);
    }

    public RichIterable<V> filterNot(Predicate<? super V> predicate)
    {
        return this.valuesView().filterNot(predicate);
    }

    public <R extends Collection<V>> R filterNot(Predicate<? super V> predicate, R target)
    {
        return this.valuesView().filterNot(predicate, target);
    }

    public <P, R extends Collection<V>> R filterNotWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.valuesView().filterNotWith(predicate, parameter, targetCollection);
    }

    public RichIterable<V> filter(Predicate<? super V> predicate)
    {
        return this.valuesView().filter(predicate);
    }

    public <R extends Collection<V>> R filter(Predicate<? super V> predicate, R target)
    {
        return this.valuesView().filter(predicate, target);
    }

    public <P, R extends Collection<V>> R filterWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.valuesView().filterWith(predicate, parameter, targetCollection);
    }

    public Object[] toArray()
    {
        return this.valuesView().toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return this.valuesView().toArray(a);
    }

    public <S> RichIterable<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.valuesView().zip(that);
    }

    public <S, R extends Collection<Pair<V, S>>> R zip(Iterable<S> that, R target)
    {
        return this.valuesView().zip(that, target);
    }

    public RichIterable<Pair<V, Integer>> zipWithIndex()
    {
        return this.valuesView().zipWithIndex();
    }

    public <R extends Collection<Pair<V, Integer>>> R zipWithIndex(R target)
    {
        return this.valuesView().zipWithIndex(target);
    }
}
