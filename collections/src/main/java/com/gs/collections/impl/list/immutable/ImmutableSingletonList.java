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

package com.gs.collections.impl.list.immutable;

import java.io.Serializable;
import java.util.RandomAccess;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
import net.jcip.annotations.Immutable;

/**
 * This is a single element immutable List which is created by calling
 * Immutable.newListWith(one) method.
 */
@Immutable
final class ImmutableSingletonList<T>
        extends AbstractImmutableList<T>
        implements Serializable, RandomAccess
{
    private static final long serialVersionUID = 1L;

    private final T element1;

    ImmutableSingletonList(T obj1)
    {
        this.element1 = obj1;
    }

    public int size()
    {
        return 1;
    }

    @Override
    public boolean contains(Object obj)
    {
        return Comparators.nullSafeEquals(obj, this.element1);
    }

    public void forEach(Procedure<? super T> procedure)
    {
        procedure.value(this.element1);
    }

    @Override
    public T get(int index)
    {
        if (index == 0)
        {
            return this.element1;
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
    }

    @Override
    public T getFirst()
    {
        return this.element1;
    }

    @Override
    public T getLast()
    {
        return this.element1;
    }

    public ImmutableList<T> newWith(T newItem)
    {
        return Lists.immutable.of(this.get(0), newItem);
    }
}
