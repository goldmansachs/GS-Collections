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

package com.gs.collections.impl.lazy;

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.math.SumProcedure;
import com.gs.collections.impl.utility.LazyIterate;
import org.junit.Assert;
import org.junit.Test;

public class RejectIterableTest extends AbstractLazyIterableTestCase
{
    @Override
    protected LazyIterable<Integer> newWith(Integer... integers)
    {
        return LazyIterate.reject(FastList.newListWith(integers), Predicates.alwaysFalse());
    }

    @Test
    public void forEach()
    {
        InternalIterable<Integer> select = new RejectIterable<Integer>(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        select.forEach(new SumProcedure<Integer>(sum));
        Assert.assertEquals(5, sum.getValue().intValue());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Integer> select = new RejectIterable<Integer>(Interval.oneTo(5), Predicates.lessThan(2).or(Predicates.greaterThan(3)));
        final Sum sum = new IntegerSum(0);
        select.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                sum.add(object);
                sum.add(index);
            }
        });
        Assert.assertEquals(6, sum.getValue().intValue());
    }

    @Override
    @Test
    public void iterator()
    {
        InternalIterable<Integer> select = new RejectIterable<Integer>(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        for (Integer each : select)
        {
            sum.add(each);
        }
        Assert.assertEquals(5, sum.getValue().intValue());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Integer> select = new RejectIterable<Integer>(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        select.forEachWith(new Procedure2<Integer, Sum>()
        {
            public void value(Integer each, Sum aSum)
            {
                aSum.add(each);
            }
        }, sum);
        Assert.assertEquals(5, sum.getValue().intValue());
    }
}
