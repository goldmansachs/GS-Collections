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

package com.webguys.ponzu.impl.partition.set;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.partition.set.PartitionMutableSet;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.impl.set.mutable.UnifiedSet;

public class PartitionUnifiedSet<T> extends AbstractPartitionMutableSet<T>
{
    private final MutableSet<T> selected = UnifiedSet.newSet();
    private final MutableSet<T> rejected = UnifiedSet.newSet();

    public PartitionUnifiedSet(Predicate<? super T> predicate)
    {
        super(predicate);
    }

    public static <V> PartitionMutableSet<V> of(RichIterable<V> iterable, Predicate<? super V> predicate)
    {
        return partition(iterable, new PartitionUnifiedSet<V>(predicate));
    }

    public MutableSet<T> getSelected()
    {
        return this.selected;
    }

    public MutableSet<T> getRejected()
    {
        return this.rejected;
    }
}
