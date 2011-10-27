/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.partition.set;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.partition.set.PartitionImmutableSet;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.impl.partition.AbstractPartitionMutableCollection;

public abstract class AbstractPartitionMutableSet<T>
        extends AbstractPartitionMutableCollection<T> implements PartitionMutableSet<T>
{
    protected AbstractPartitionMutableSet(Predicate<? super T> predicate)
    {
        super(predicate);
    }

    public PartitionImmutableSet<T> toImmutable()
    {
        return new PartitionImmutableSetImpl<T>(this);
    }
}
