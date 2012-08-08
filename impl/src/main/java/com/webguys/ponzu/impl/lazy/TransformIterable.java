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

package com.webguys.ponzu.impl.lazy;

import java.util.Iterator;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.lazy.iterator.TransformIterator;
import com.webguys.ponzu.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * A CollectIterable is an iterable that transforms a source iterable on a condition as it iterates.
 */
@Immutable
public class TransformIterable<T, V>
        extends AbstractLazyIterable<V>
{
    private final Iterable<T> adapted;
    private final Function<? super T, ? extends V> function;

    public TransformIterable(Iterable<T> newAdapted, Function<? super T, ? extends V> function)
    {
        this.adapted = newAdapted;
        this.function = function;
    }

    public void forEach(Procedure<? super V> procedure)
    {
        Iterate.forEach(this.adapted, Functions.bind(procedure, this.function));
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        Iterate.forEachWithIndex(this.adapted, Functions.bind(objectIntProcedure, this.function));
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith(this.adapted, Functions.bind(procedure, this.function), parameter);
    }

    public Iterator<V> iterator()
    {
        return new TransformIterator<T, V>(this.adapted, this.function);
    }

    @Override
    public int size()
    {
        return Iterate.sizeOf(this.adapted);
    }

    @Override
    public Object[] toArray()
    {
        Object[] array = Iterate.toArray(this.adapted);
        for (int i = 0; i < array.length; i++)
        {
            array[i] = this.function.valueOf((T) array[i]);
        }
        return array;
    }
}
