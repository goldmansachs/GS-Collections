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

package com.gs.collections.impl.block.procedure;

import java.util.Comparator;

import com.gs.collections.api.block.procedure.Procedure;

/**
 * Implementation of {@link Procedure} that holds on to the minimum element seen so far,
 * determined by the {@link Comparator}
 */
public class MinComparatorProcedure<T> extends ComparatorProcedure<T>
{
    private static final long serialVersionUID = 1L;

    public MinComparatorProcedure(Comparator<? super T> comparator)
    {
        super(comparator);
    }

    public void value(T each)
    {
        if (!this.visitedAtLeastOnce)
        {
            this.visitedAtLeastOnce = true;
            this.result = each;
        }
        else if (this.comparator.compare(each, this.result) < 0)
        {
            this.result = each;
        }
    }
}
