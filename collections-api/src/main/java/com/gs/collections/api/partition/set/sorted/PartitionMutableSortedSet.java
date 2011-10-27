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

package com.gs.collections.api.partition.set.sorted;

import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.set.sorted.MutableSortedSet;

public interface PartitionMutableSortedSet<T> extends PartitionMutableCollection<T>, PartitionSortedSet<T>
{
    MutableSortedSet<T> getSelected();

    MutableSortedSet<T> getRejected();

    PartitionImmutableSortedSet<T> toImmutable();
}
