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

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.impl.lazy.iterator.ChunkIterator;
import com.webguys.ponzu.impl.utility.internal.IterableIterate;
import net.jcip.annotations.Immutable;

/**
 * A CollectIterable is an iterable that transforms a source iterable on a condition as it iterates.
 */
@Immutable
public class ChunkIterable<T>
        extends AbstractLazyIterable<RichIterable<T>>
{
    private final Iterable<T> iterable;
    private final int size;

    public ChunkIterable(Iterable<T> iterable, int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        this.iterable = iterable;
        this.size = size;
    }

    public Iterator<RichIterable<T>> iterator()
    {
        return new ChunkIterator<T>(this.iterable, this.size);
    }

    public void forEach(Procedure<? super RichIterable<T>> procedure)
    {
        IterableIterate.forEach(this, procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super RichIterable<T>> objectIntProcedure)
    {
        IterableIterate.forEachWithIndex(this, objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super RichIterable<T>, ? super P> procedure, P parameter)
    {
        IterableIterate.forEachWith(this, procedure, parameter);
    }
}
