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

package com.gs.collections.impl.set.sorted.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.set.sorted.PartitionMutableSortedSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure;
import com.gs.collections.impl.collection.mutable.AbstractMutableCollection;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.set.sorted.TreeSortedSetMultimap;
import com.gs.collections.impl.partition.set.sorted.PartitionTreeSortedSet;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.internal.SetIterables;
import com.gs.collections.impl.utility.internal.SetIterate;
import com.gs.collections.impl.utility.internal.SortedSetIterables;

public final class TreeSortedSet<T>
        extends AbstractMutableCollection<T>
        implements Externalizable, MutableSortedSet<T>
{
    private static final long serialVersionUID = 1L;
    private TreeSet<T> treeSet;

    public TreeSortedSet()
    {
        this.treeSet = new TreeSet<T>();
    }

    public TreeSortedSet(Iterable<? extends T> iterable)
    {
        this.treeSet = new TreeSet<T>();
        this.addAllIterable(iterable);
    }

    public TreeSortedSet(Comparator<? super T> comparator)
    {
        this.treeSet = new TreeSet<T>(comparator);
    }

    public TreeSortedSet(SortedSet<T> set)
    {
        this.treeSet = new TreeSet<T>(set);
    }

    public TreeSortedSet(Comparator<? super T> comparator, Iterable<? extends T> iterable)
    {
        this(comparator);
        this.addAllIterable(iterable);
    }

    public static <T> TreeSortedSet<T> newSet()
    {
        return new TreeSortedSet<T>();
    }

    public static <T> TreeSortedSet<T> newSet(Comparator<? super T> comparator)
    {
        return new TreeSortedSet<T>(comparator);
    }

    public static <T> TreeSortedSet<T> newSet(Iterable<? extends T> source)
    {
        if (source instanceof SortedSet<?>)
        {
            return new TreeSortedSet<T>((SortedSet<T>) source);
        }
        TreeSortedSet<T> sortedSet = TreeSortedSet.newSet();
        Iterate.forEach(source, CollectionAddProcedure.on(sortedSet));
        return sortedSet;
    }

    public static <T> TreeSortedSet<T> newSet(Comparator<? super T> comparator, Iterable<? extends T> iterable)
    {
        return new TreeSortedSet<T>(comparator, iterable);
    }

    public static <T> TreeSortedSet<T> newSetWith(T... elements)
    {
        return new TreeSortedSet<T>().with(elements);
    }

    public static <T> TreeSortedSet<T> newSetWith(Comparator<? super T> comparator, T... elements)
    {
        return new TreeSortedSet<T>(comparator).with(elements);
    }

    @Override
    public MutableSortedSet<T> asUnmodifiable()
    {
        return UnmodifiableSortedSet.of(this);
    }

    @Override
    public MutableSortedSet<T> asSynchronized()
    {
        return SynchronizedSortedSet.of(this);
    }

    public ImmutableSortedSet<T> toImmutable()
    {
        return SortedSets.immutable.ofSortedSet(this);
    }

    @Override
    public boolean add(T element)
    {
        return this.treeSet.add(element);
    }

    @Override
    public boolean remove(Object element)
    {
        return this.treeSet.remove(element);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection)
    {
        return this.treeSet.addAll(collection);
    }

    @Override
    public boolean contains(Object o)
    {
        return this.treeSet.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return this.treeSet.containsAll(collection);
    }

    @Override
    public void clear()
    {
        this.treeSet.clear();
    }

    @Override
    public TreeSortedSet<T> clone()
    {
        try
        {
            TreeSortedSet<T> clone = (TreeSortedSet<T>) super.clone();
            clone.treeSet = (TreeSet<T>) this.treeSet.clone();
            return clone;
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object object)
    {
        return this.treeSet.equals(object);
    }

    @Override
    public int hashCode()
    {
        return this.treeSet.hashCode();
    }

    public TreeSortedSet<T> with(T element)
    {
        this.treeSet.add(element);
        return this;
    }

    public TreeSortedSet<T> with(T element1, T element2)
    {
        this.treeSet.add(element1);
        this.treeSet.add(element2);
        return this;
    }

    public TreeSortedSet<T> with(T element1, T element2, T element3)
    {
        this.treeSet.add(element1);
        this.treeSet.add(element2);
        this.treeSet.add(element3);
        return this;
    }

    public TreeSortedSet<T> with(T... elements)
    {
        ArrayIterate.forEach(elements, CollectionAddProcedure.on(this.treeSet));
        return this;
    }

    public TreeSortedSet<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public TreeSortedSet<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public TreeSortedSet<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    @Override
    public TreeSortedSet<T> newEmpty()
    {
        return TreeSortedSet.newSet(this.treeSet.comparator());
    }

    @Override
    public TreeSortedSet<T> select(Predicate<? super T> predicate)
    {
        return Iterate.select(this.treeSet, predicate, this.newEmpty());
    }

    @Override
    public TreeSortedSet<T> reject(Predicate<? super T> predicate)
    {
        return Iterate.reject(this.treeSet, predicate, this.newEmpty());
    }

    public PartitionMutableSortedSet<T> partition(Predicate<? super T> predicate)
    {
        return PartitionTreeSortedSet.of(this, predicate);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return Iterate.collect(this.treeSet, function, FastList.<V>newList());
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.flatCollect(this.treeSet, function, FastList.<V>newList());
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return Iterate.collectIf(this.treeSet, predicate, function, FastList.<V>newList());
    }

    public <V> TreeSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return Iterate.groupBy(this.treeSet, function, TreeSortedSetMultimap.<V, T>newMultimap(this.comparator()));
    }

    public <V> TreeSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.groupByEach(this.treeSet, function, TreeSortedSetMultimap.<V, T>newMultimap(this.comparator()));
    }

    @Override
    public <P> TreeSortedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.selectWith(this.treeSet, predicate, parameter, this.newEmpty());
    }

    @Override
    public <P> TreeSortedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.rejectWith(this.treeSet, predicate, parameter, this.newEmpty());
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return Iterate.collectWith(this.treeSet, function, parameter, FastList.<V>newList());
    }

    public <S> TreeSortedSet<Pair<T, S>> zip(Iterable<S> that)
    {
        Comparator<? super T> comparator = this.comparator();
        if (comparator == null)
        {
            TreeSortedSet<Pair<T, S>> pairs = TreeSortedSet.newSet(Comparators.<Pair<T, S>, T>byFunction(Functions.<T>firstOfPair(), Comparators.<T>naturalOrder()));
            return Iterate.zip(this, that, pairs);
        }
        return Iterate.zip(this, that, TreeSortedSet.<Pair<T, S>>newSet(Comparators.<T>byFirstOfPair(comparator)));
    }

    public TreeSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        Comparator<? super T> comparator = this.comparator();
        if (comparator == null)
        {
            TreeSortedSet<Pair<T, Integer>> pairs = TreeSortedSet.newSet(Comparators.<Pair<T, Integer>, T>byFunction(Functions.<T>firstOfPair(), Comparators.<T>naturalOrder()));
            return Iterate.zipWithIndex(this, pairs);
        }
        return Iterate.zipWithIndex(this, TreeSortedSet.<Pair<T, Integer>>newSet(Comparators.<T>byFirstOfPair(comparator)));
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        return SetIterate.removeAllIterable(this.treeSet, iterable);
    }

    public Comparator<? super T> comparator()
    {
        return this.treeSet.comparator();
    }

    public MutableSortedSet<T> subSet(T fromElement, T toElement)
    {
        return SortedSetAdapter.adapt(this.treeSet.subSet(fromElement, toElement));
    }

    public MutableSortedSet<T> headSet(T toElement)
    {
        return SortedSetAdapter.adapt(this.treeSet.headSet(toElement));
    }

    public MutableSortedSet<T> tailSet(T fromElement)
    {
        return SortedSetAdapter.adapt(this.treeSet.tailSet(fromElement));
    }

    public T first()
    {
        return this.treeSet.first();
    }

    public T last()
    {
        return this.treeSet.last();
    }

    @Override
    public T getFirst()
    {
        return this.first();
    }

    @Override
    public T getLast()
    {
        return this.last();
    }

    public MutableSortedSet<SortedSetIterable<T>> powerSet()
    {
        return (MutableSortedSet<SortedSetIterable<T>>) (MutableSortedSet<?>) SortedSetIterables.powerSet(this);
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }

    public TreeSortedSet<T> union(SetIterable<? extends T> set)
    {
        return SetIterables.unionInto(this, set, this.newEmpty());
    }

    public TreeSortedSet<T> intersect(SetIterable<? extends T> set)
    {
        return SetIterables.intersectInto(this, set, this.newEmpty());
    }

    public TreeSortedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, this.newEmpty());
    }

    public MutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return SetIterables.symmetricDifferenceInto(this, setB, this.newEmpty());
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.symmetricDifferenceInto(this, set, targetSet);
    }

    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isSubsetOf(this, candidateSuperset);
    }

    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isProperSubsetOf(this, candidateSuperset);
    }

    @Override
    public Iterator<T> iterator()
    {
        return this.treeSet.iterator();
    }

    @Override
    public int size()
    {
        return this.treeSet.size();
    }

    public void writeExternal(final ObjectOutput out) throws IOException
    {
        out.writeObject(this.comparator());
        out.writeInt(this.size());
        this.forEach(new CheckedProcedure<T>()
        {
            @Override
            public void safeValue(T each) throws Exception
            {
                out.writeObject(each);
            }
        });
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.treeSet = new TreeSet<T>((Comparator<T>) in.readObject());
        int size = in.readInt();
        for (int i = 0; i < size; i++)
        {
            this.add((T) in.readObject());
        }
    }

    public int compareTo(SortedSetIterable<T> otherSet)
    {
        return SortedSetIterables.compare(this, otherSet);
    }
}
