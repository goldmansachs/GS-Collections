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
import com.gs.collections.impl.factory.Lists;
import net.jcip.annotations.Immutable;

/**
 * This is a seven element immutable List which is created by calling
 * Immutable.newListWith(one, two, three, four, five, six, seven) method.
 */
@Immutable
final class ImmutableSeptupletonList<T>
        extends AbstractImmutableList<T>
        implements Serializable, RandomAccess
{
    private static final long serialVersionUID = 1L;

    private final T element1;
    private final T element2;
    private final T element3;
    private final T element4;
    private final T element5;
    private final T element6;
    private final T element7;

    ImmutableSeptupletonList(T obj1, T obj2, T obj3, T obj4, T obj5, T obj6, T obj7)
    {
        this.element1 = obj1;
        this.element2 = obj2;
        this.element3 = obj3;
        this.element4 = obj4;
        this.element5 = obj5;
        this.element6 = obj6;
        this.element7 = obj7;
    }

    public int size()
    {
        return 7;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        procedure.value(this.element1);
        procedure.value(this.element2);
        procedure.value(this.element3);
        procedure.value(this.element4);
        procedure.value(this.element5);
        procedure.value(this.element6);
        procedure.value(this.element7);
    }

    @Override
    public T get(int index)
    {
        switch (index)
        {
            case 0:
                return this.element1;
            case 1:
                return this.element2;
            case 2:
                return this.element3;
            case 3:
                return this.element4;
            case 4:
                return this.element5;
            case 5:
                return this.element6;
            case 6:
                return this.element7;
            default:
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
        }
    }

    public ImmutableList<T> newWith(T newItem)
    {
        return Lists.immutable.of(
                this.get(0),
                this.get(1),
                this.get(2),
                this.get(3),
                this.get(4),
                this.get(5),
                this.get(6),
                newItem);
    }
}
