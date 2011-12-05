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

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.math.SumProcedure;
import com.gs.collections.impl.utility.LazyIterate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DropIterableTest extends AbstractLazyIterableTestCase
{
    private DropIterable<Integer> dropIterable;
    private DropIterable<Integer> emptyListDropIterable;
    private DropIterable<Integer> zeroCountDropIterable;
    private DropIterable<Integer> nearCountDropIterable;
    private DropIterable<Integer> sameCountDropIterable;
    private DropIterable<Integer> higherCountDropIterable;

    @Before
    public void setUp()
    {
        this.dropIterable = new DropIterable<Integer>(Interval.oneTo(5), 2);
        this.emptyListDropIterable = new DropIterable<Integer>(FastList.<Integer>newList(), 2);
        this.zeroCountDropIterable = new DropIterable<Integer>(Interval.oneTo(5), 0);
        this.nearCountDropIterable = new DropIterable<Integer>(Interval.oneTo(5), 4);
        this.sameCountDropIterable = new DropIterable<Integer>(Interval.oneTo(5), 5);
        this.higherCountDropIterable = new DropIterable<Integer>(Interval.oneTo(5), 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negative_throws()
    {
        new DropIterable<Integer>(Interval.oneTo(5), -1);
    }

    @Test
    public void basic()
    {
        Assert.assertEquals(3, this.dropIterable.size());
        Assert.assertEquals(FastList.newListWith(3, 4, 5), this.dropIterable.toList());

        Assert.assertEquals(0, this.emptyListDropIterable.size());
        Assert.assertEquals(5, this.zeroCountDropIterable.size());
        Assert.assertEquals(1, this.nearCountDropIterable.size());
        Assert.assertEquals(0, this.sameCountDropIterable.size());
        Assert.assertEquals(0, this.higherCountDropIterable.size());
    }

    @Test
    public void forEach()
    {
        Sum sum1 = new IntegerSum(0);
        this.dropIterable.forEach(new SumProcedure<Integer>(sum1));
        Assert.assertEquals(12, sum1.getValue().intValue());

        Sum sum2 = new IntegerSum(0);
        this.emptyListDropIterable.forEach(new SumProcedure<Integer>(sum2));
        Assert.assertEquals(0, sum2.getValue().intValue());

        Sum sum3 = new IntegerSum(0);
        this.zeroCountDropIterable.forEach(new SumProcedure<Integer>(sum3));
        Assert.assertEquals(15, sum3.getValue().intValue());

        Sum sum5 = new IntegerSum(0);
        this.nearCountDropIterable.forEach(new SumProcedure<Integer>(sum5));
        Assert.assertEquals(5, sum5.getValue().intValue());

        Sum sum6 = new IntegerSum(0);
        this.sameCountDropIterable.forEach(new SumProcedure<Integer>(sum6));
        Assert.assertEquals(0, sum6.getValue().intValue());

        Sum sum7 = new IntegerSum(0);
        this.higherCountDropIterable.forEach(new SumProcedure<Integer>(sum7));
        Assert.assertEquals(0, sum7.getValue().intValue());
    }

    @Test
    public void forEachWithIndex()
    {
        final Sum sum = new IntegerSum(0);
        final FastList<Integer> indices = FastList.newList(5);
        ObjectIntProcedure<Integer> indexRecordingAndSumProcedure = new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                indices.add(index);
                sum.add(each);
            }
        };

        this.dropIterable.forEachWithIndex(indexRecordingAndSumProcedure);
        Assert.assertEquals(FastList.newListWith(0, 1, 2), indices);
        Assert.assertEquals(12, sum.getValue().intValue());

        indices.clear();
        sum.add(sum.getValue().intValue() * -1);
        this.emptyListDropIterable.forEachWithIndex(indexRecordingAndSumProcedure);
        Assert.assertEquals(0, indices.size());

        indices.clear();
        sum.add(sum.getValue().intValue() * -1);
        this.zeroCountDropIterable.forEachWithIndex(indexRecordingAndSumProcedure);
        Assert.assertEquals(FastList.newListWith(0, 1, 2, 3, 4), indices);
        Assert.assertEquals(15, sum.getValue().intValue());

        indices.clear();
        sum.add(sum.getValue().intValue() * -1);
        this.nearCountDropIterable.forEachWithIndex(indexRecordingAndSumProcedure);
        Assert.assertEquals(FastList.newListWith(0), indices);
        Assert.assertEquals(5, sum.getValue().intValue());

        indices.clear();
        sum.add(sum.getValue().intValue() * -1);
        this.sameCountDropIterable.forEachWithIndex(indexRecordingAndSumProcedure);
        Assert.assertEquals(0, indices.size());

        indices.clear();
        sum.add(sum.getValue().intValue() * -1);
        this.higherCountDropIterable.forEachWithIndex(indexRecordingAndSumProcedure);
        Assert.assertEquals(0, indices.size());
    }

    @Test
    public void forEachWith()
    {
        Procedure2<Integer, Sum> sumAdditionProcedure = new Procedure2<Integer, Sum>()
        {
            public void value(Integer each, Sum sum)
            {
                sum.add(each);
            }
        };

        Sum sum1 = new IntegerSum(0);
        this.dropIterable.forEachWith(sumAdditionProcedure, sum1);
        Assert.assertEquals(12, sum1.getValue().intValue());

        Sum sum2 = new IntegerSum(0);
        this.emptyListDropIterable.forEachWith(sumAdditionProcedure, sum2);
        Assert.assertEquals(0, sum2.getValue().intValue());

        Sum sum3 = new IntegerSum(0);
        this.zeroCountDropIterable.forEachWith(sumAdditionProcedure, sum3);
        Assert.assertEquals(15, sum3.getValue().intValue());

        Sum sum5 = new IntegerSum(0);
        this.nearCountDropIterable.forEachWith(sumAdditionProcedure, sum5);
        Assert.assertEquals(5, sum5.getValue().intValue());

        Sum sum6 = new IntegerSum(0);
        this.sameCountDropIterable.forEachWith(sumAdditionProcedure, sum6);
        Assert.assertEquals(0, sum6.getValue().intValue());

        Sum sum7 = new IntegerSum(0);
        this.higherCountDropIterable.forEachWith(sumAdditionProcedure, sum7);
        Assert.assertEquals(0, sum7.getValue().intValue());
    }

    @Override
    @Test
    public void iterator()
    {
        Sum sum1 = new IntegerSum(0);
        for (Integer each : this.dropIterable)
        {
            sum1.add(each);
        }
        Assert.assertEquals(12, sum1.getValue().intValue());

        Sum sum2 = new IntegerSum(0);
        for (Integer each : this.emptyListDropIterable)
        {
            sum2.add(each);
        }
        Assert.assertEquals(0, sum2.getValue().intValue());

        Sum sum3 = new IntegerSum(0);
        for (Integer each : this.zeroCountDropIterable)
        {
            sum3.add(each);
        }
        Assert.assertEquals(15, sum3.getValue().intValue());

        Sum sum5 = new IntegerSum(0);
        for (Integer each : this.nearCountDropIterable)
        {
            sum5.add(each);
        }
        Assert.assertEquals(5, sum5.getValue().intValue());

        Sum sum6 = new IntegerSum(0);
        for (Integer each : this.sameCountDropIterable)
        {
            sum6.add(each);
        }
        Assert.assertEquals(0, sum6.getValue().intValue());

        Sum sum7 = new IntegerSum(0);
        for (Integer each : this.higherCountDropIterable)
        {
            sum7.add(each);
        }
        Assert.assertEquals(0, sum7.getValue().intValue());
    }

    @Override
    protected LazyIterable<Integer> newWith(Integer... integers)
    {
        return LazyIterate.drop(FastList.newListWith(integers), 0);
    }
}
