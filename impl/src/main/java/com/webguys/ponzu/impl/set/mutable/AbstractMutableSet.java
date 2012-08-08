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

package com.webguys.ponzu.impl.set.mutable;

import java.util.Set;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.partition.set.PartitionMutableSet;
import com.webguys.ponzu.api.set.ImmutableSet;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.set.SetIterable;
import com.webguys.ponzu.api.set.UnsortedSetIterable;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.collection.mutable.AbstractMutableCollection;
import com.webguys.ponzu.impl.factory.Sets;
import com.webguys.ponzu.impl.multimap.set.UnifiedSetMultimap;
import com.webguys.ponzu.impl.partition.set.PartitionUnifiedSet;
import com.webguys.ponzu.impl.utility.internal.SetIterables;
import com.webguys.ponzu.impl.utility.internal.SetIterate;

public abstract class AbstractMutableSet<T>
        extends AbstractMutableCollection<T>
        implements MutableSet<T>
{
    @Override
    public MutableSet<T> clone()
    {
        try
        {
            return (MutableSet<T>) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }

    @Override
    public MutableSet<T> newEmpty()
    {
        return UnifiedSet.newSet();
    }

    protected <K> MutableSet<K> newEmptySameSize()
    {
        return UnifiedSet.newSet(this.size());
    }

    @Override
    public MutableSet<T> filter(Predicate<? super T> predicate)
    {
        return this.filter(predicate, this.newEmpty());
    }

    @Override
    public <P> MutableSet<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.filterWith(predicate, parameter, this.<T>newEmptySameSize());
    }

    @Override
    public MutableSet<T> filterNot(Predicate<? super T> predicate)
    {
        return this.filterNot(predicate, this.<T>newEmptySameSize());
    }

    @Override
    public <P> MutableSet<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.filterNotWith(predicate, parameter, this.<T>newEmptySameSize());
    }

    public PartitionMutableSet<T> partition(Predicate<? super T> predicate)
    {
        return PartitionUnifiedSet.of(this, predicate);
    }

    @Override
    public <V> MutableSet<V> transform(Function<? super T, ? extends V> function)
    {
        return this.transform(function, this.<V>newEmptySameSize());
    }

    @Override
    public <V> MutableSet<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatTransform(function, this.<V>newEmptySameSize());
    }

    @Override
    public <P, V> MutableSet<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.transformWith(function, parameter, this.<V>newEmptySameSize());
    }

    @Override
    public <V> MutableSet<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.transformIf(predicate, function, this.<V>newEmptySameSize());
    }

    public <V> UnifiedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, UnifiedSetMultimap.<V, T>newMultimap());
    }

    public <V> UnifiedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, UnifiedSetMultimap.<V, T>newMultimap());
    }

    @Override
    public MutableSet<T> asUnmodifiable()
    {
        return UnmodifiableMutableSet.of(this);
    }

    @Override
    public MutableSet<T> asSynchronized()
    {
        return SynchronizedMutableSet.of(this);
    }

    public ImmutableSet<T> toImmutable()
    {
        return Sets.immutable.ofAll(this);
    }

    public <S> MutableSet<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, this.<Pair<T, S>>newEmptySameSize());
    }

    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(this.<Pair<T, Integer>>newEmptySameSize());
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        return SetIterate.removeAllIterable(this, iterable);
    }

    public MutableSet<T> union(SetIterable<? extends T> set)
    {
        return SetIterables.union(this, set);
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public MutableSet<T> intersect(SetIterable<? extends T> set)
    {
        return SetIterables.intersect(this, set);
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public MutableSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return SetIterables.difference(this, subtrahendSet);
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public MutableSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return SetIterables.symmetricDifference(this, setB);
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

    public MutableSet<UnsortedSetIterable<T>> powerSet()
    {
        return (MutableSet<UnsortedSetIterable<T>>) (MutableSet<?>) SetIterables.powerSet(this);
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }
}
