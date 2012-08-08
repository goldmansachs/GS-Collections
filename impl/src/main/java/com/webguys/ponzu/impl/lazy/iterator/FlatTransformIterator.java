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

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.impl.EmptyIterator;

public final class FlatTransformIterator<T, V> implements Iterator<V>
{
    private final Iterator<T> iterator;
    private final Function<? super T, ? extends Iterable<V>> function;
    private Iterator<V> innerIterator = EmptyIterator.getInstance();

    public FlatTransformIterator(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> newFunction)
    {
        this(iterable.iterator(), newFunction);
    }

    public FlatTransformIterator(
            Iterator<T> newIterator,
            Function<? super T, ? extends Iterable<V>> newFunction)
    {
        this.iterator = newIterator;
        this.function = newFunction;
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot remove from a flatTransform iterator");
    }

    public boolean hasNext()
    {
        while (true)
        {
            if (this.innerIterator.hasNext())
            {
                return true;
            }
            if (!this.iterator.hasNext())
            {
                return false;
            }
            this.innerIterator = this.function.valueOf(this.iterator.next()).iterator();
        }
    }

    public V next()
    {
        if (!this.hasNext())
        {
            throw new NoSuchElementException();
        }
        return this.innerIterator.next();
    }
}
