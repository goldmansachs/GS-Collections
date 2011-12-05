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

package com.gs.collections.impl.set.sorted.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.EmptyIterator;
import com.gs.collections.impl.factory.SortedSets;
import net.jcip.annotations.Immutable;

/**
 * This is a zero element {@link ImmutableSortedSet} which is created by calling the SortedSets.immutable.of() method.
 */
@Immutable
final class ImmutableEmptySortedSet<T>
        extends AbstractImmutableSortedSet<T>
        implements Serializable
{
    static final ImmutableSortedSet<?> INSTANCE = new ImmutableEmptySortedSet();

    private static final long serialVersionUID = 1L;
    private final Comparator<? super T> comparator;

    ImmutableEmptySortedSet()
    {
        this.comparator = null;
    }

    ImmutableEmptySortedSet(Comparator<? super T> comparator)
    {
        this.comparator = comparator;
    }

    private Object readResolve()
    {
        if (this.comparator == null)
        {
            return INSTANCE;
        }
        return this;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        return other instanceof Set && ((Collection<?>) other).isEmpty();
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    @Override
    public ImmutableSortedSet<T> newWith(T element)
    {
        return SortedSets.immutable.of(this.comparator, element);
    }

    @Override
    public ImmutableSortedSet<T> newWithAll(Iterable<? extends T> elements)
    {
        return SortedSets.immutable.ofAll(this.comparator, elements);
    }

    @Override
    public ImmutableSortedSet<T> newWithout(T element)
    {
        return this;
    }

    @Override
    public ImmutableSortedSet<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this;
    }

    public int size()
    {
        return 0;
    }

    @Override
    public boolean contains(Object object)
    {
        return false;
    }

    public void forEach(Procedure<? super T> procedure)
    {
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
    }

    @Override
    public Iterator<T> iterator()
    {
        return EmptyIterator.getInstance();
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T min()
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max()
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return target;
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return target;
    }

    public T first()
    {
        throw new NoSuchElementException();
    }

    public T last()
    {
        throw new NoSuchElementException();
    }

    public Comparator<? super T> comparator()
    {
        return this.comparator;
    }

    public int compareTo(SortedSetIterable<T> o)
    {
        return o.size() * -1;
    }
}
