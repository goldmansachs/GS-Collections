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

package com.gs.collections.impl.tuple;

import java.util.Map;

import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;

/**
 * A PairImpl is a container that holds two related objects.  It is the equivalent of an Association in Smalltalk, or an
 * implementation of Map.Entry in the JDK.
 */
class PairImpl<T1, T2>
        implements Pair<T1, T2>
{
    private static final long serialVersionUID = 1L;

    private final T1 one;
    private final T2 two;

    PairImpl(T1 newOne, T2 newTwo)
    {
        this.one = newOne;
        this.two = newTwo;
    }

    public T1 getOne()
    {
        return this.one;
    }

    public T2 getTwo()
    {
        return this.two;
    }

    public void put(Map<T1, T2> map)
    {
        map.put(this.one, this.two);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Pair))
        {
            return false;
        }

        Pair<?, ?> that = (Pair<?, ?>) o;

        return Comparators.nullSafeEquals(this.one, that.getOne())
                && Comparators.nullSafeEquals(this.two, that.getTwo());
    }

    @Override
    public int hashCode()
    {
        int result = this.one == null ? 0 : this.one.hashCode();
        result = 29 * result + (this.two == null ? 0 : this.two.hashCode());
        return result;
    }

    @Override
    public String toString()
    {
        return this.one + ":" + this.two;
    }

    public Map.Entry<T1, T2> toEntry()
    {
        return ImmutableEntry.of(this.one, this.two);
    }

    public int compareTo(Pair<T1, T2> other)
    {
        int i = ((Comparable<T1>) this.one).compareTo(other.getOne());
        if (i != 0)
        {
            return i;
        }
        return ((Comparable<T2>) this.two).compareTo(other.getTwo());
    }
}
