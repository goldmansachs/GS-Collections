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

package com.webguys.ponzu.impl.lazy.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.collection.MutableCollection;
import com.webguys.ponzu.impl.factory.Lists;
import net.jcip.annotations.Immutable;

@Immutable
public final class ChunkIterator<T>
        implements Iterator<RichIterable<T>>
{
    private final Iterator<T> iterator;
    private final int size;
    private final Generator<MutableCollection<T>> speciesNewStrategy;

    public ChunkIterator(final Iterable<T> iterable, int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        this.size = size;
        this.iterator = iterable.iterator();

        if (iterable instanceof MutableCollection)
        {
            this.speciesNewStrategy = new Generator<MutableCollection<T>>()
            {
                public MutableCollection<T> value()
                {
                    return ((MutableCollection<T>) iterable).newEmpty();
                }
            };
        }
        else
        {
            this.speciesNewStrategy = new Generator<MutableCollection<T>>()
            {
                public MutableCollection<T> value()
                {
                    return Lists.mutable.of();
                }
            };
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot remove from a chunk iterator");
    }

    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    public RichIterable<T> next()
    {
        if (!this.iterator.hasNext())
        {
            throw new NoSuchElementException();
        }

        int i = this.size;
        MutableCollection<T> result = this.speciesNewStrategy.value();
        while (i > 0 && this.iterator.hasNext())
        {
            result.add(this.iterator.next());
            i -= 1;
        }

        return result;
    }
}
