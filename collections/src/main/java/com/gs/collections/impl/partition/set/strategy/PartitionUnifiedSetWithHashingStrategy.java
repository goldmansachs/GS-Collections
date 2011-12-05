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

package com.gs.collections.impl.partition.set.strategy;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.partition.set.AbstractPartitionMutableSet;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;

public class PartitionUnifiedSetWithHashingStrategy<T>
        extends AbstractPartitionMutableSet<T>
{
    private final MutableSet<T> selected;
    private final MutableSet<T> rejected;

    public PartitionUnifiedSetWithHashingStrategy(
            HashingStrategy<? super T> hashingStrategy,
            Predicate<? super T> predicate)
    {
        super(predicate);
        this.selected = UnifiedSetWithHashingStrategy.newSet(hashingStrategy);
        this.rejected = UnifiedSetWithHashingStrategy.newSet(hashingStrategy);
    }

    public static <T> PartitionMutableSet<T> of(
            HashingStrategy<? super T> hashingStrategy,
            RichIterable<T> iterable,
            Predicate<? super T> predicate)
    {
        return partition(iterable, new PartitionUnifiedSetWithHashingStrategy<T>(hashingStrategy, predicate));
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
