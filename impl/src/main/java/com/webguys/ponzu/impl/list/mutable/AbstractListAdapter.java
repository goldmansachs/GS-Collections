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

package com.webguys.ponzu.impl.list.mutable;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.multimap.list.MutableListMultimap;
import com.webguys.ponzu.api.partition.list.PartitionMutableList;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.collection.mutable.AbstractCollectionAdapter;

@SuppressWarnings("AbstractMethodOverridesConcreteMethod")
public abstract class AbstractListAdapter<T>
        extends AbstractCollectionAdapter<T>
        implements MutableList<T>
{
    @Override
    public MutableList<T> clone()
    {
        try
        {
            return (MutableList<T>) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }

    @Override
    protected abstract List<T> getDelegate();

    public boolean addAll(int index, Collection<? extends T> collection)
    {
        return this.getDelegate().addAll(index, collection);
    }

    public MutableList<T> toReversed()
    {
        return FastList.newList(this).reverseThis();
    }

    public MutableList<T> reverseThis()
    {
        int mid = this.size() >> 1;
        int j = this.size() - 1;
        for (int i = 0; i < mid; i++, j--)
        {
            this.swapElements(i, j);
        }
        return (MutableList<T>) this;
    }

    protected void swapElements(int i, int j)
    {
        this.set(i, this.set(j, this.get(i)));
    }

    public T get(int index)
    {
        return this.getDelegate().get(index);
    }

    public T set(int index, T element)
    {
        return this.getDelegate().set(index, element);
    }

    public void add(int index, T element)
    {
        this.getDelegate().add(index, element);
    }

    public T remove(int index)
    {
        return this.getDelegate().remove(index);
    }

    public int indexOf(Object o)
    {
        return this.getDelegate().indexOf(o);
    }

    public int lastIndexOf(Object o)
    {
        return this.getDelegate().lastIndexOf(o);
    }

    public ListIterator<T> listIterator()
    {
        return this.getDelegate().listIterator();
    }

    public ListIterator<T> listIterator(int index)
    {
        return this.getDelegate().listIterator(index);
    }

    public MutableList<T> subList(int fromIndex, int toIndex)
    {
        return ListAdapter.adapt(this.getDelegate().subList(fromIndex, toIndex));
    }

    @Override
    public boolean equals(Object o)
    {
        return this.getDelegate().equals(o);
    }

    @Override
    public int hashCode()
    {
        return this.getDelegate().hashCode();
    }

    @Override
    public abstract <V> MutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    @Override
    public abstract <V> MutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    @Override
    public abstract MutableList<T> filter(Predicate<? super T> predicate);

    @Override
    public abstract MutableList<T> filterNot(Predicate<? super T> predicate);

    @Override
    public abstract <V> MutableList<V> transform(Function<? super T, ? extends V> function);

    @Override
    public abstract <V> MutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    @Override
    public abstract <V> MutableList<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    @Override
    public abstract PartitionMutableList<T> partition(Predicate<? super T> predicate);

    @Override
    public abstract <P> MutableList<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    public abstract <P> MutableList<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    public abstract <P, V> MutableList<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    @Override
    public abstract <S> MutableList<Pair<T, S>> zip(Iterable<S> that);

    @Override
    public abstract MutableList<Pair<T, Integer>> zipWithIndex();
}
