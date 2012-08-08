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

package com.webguys.ponzu.impl.block.procedure;

import java.util.ArrayList;
import java.util.Collection;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.procedure.Procedure;

public final class TransformIfProcedure<T, V>
        implements Procedure<T>
{
    private static final long serialVersionUID = 1L;
    private final Function<? super T, ? extends V> function;
    private final Predicate<? super T> predicate;
    private final Collection<V> collection;

    public TransformIfProcedure(int taskSize, Function<? super T, ? extends V> function,
            Predicate<? super T> predicate)
    {
        this(new ArrayList<V>(taskSize), function, predicate);
    }

    public TransformIfProcedure(Collection<V> targetCollection, Function<? super T, ? extends V> function,
            Predicate<? super T> predicate)
    {
        this.function = function;
        this.predicate = predicate;
        this.collection = targetCollection;
    }

    @Override
    public void value(T object)
    {
        if (this.predicate.accept(object))
        {
            this.collection.add(this.function.valueOf(object));
        }
    }

    public Collection<V> getCollection()
    {
        return this.collection;
    }
}
