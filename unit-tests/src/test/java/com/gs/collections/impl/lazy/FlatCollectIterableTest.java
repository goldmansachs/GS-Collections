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

import java.util.Collection;

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.LazyIterate;
import org.junit.Assert;
import org.junit.Test;

public class FlatCollectIterableTest extends AbstractLazyIterableTestCase
{
    private static final Function<Integer, Collection<Integer>> INT_TO_INTERVAL =
            new Function<Integer, Collection<Integer>>()
            {
                public Collection<Integer> valueOf(Integer object)
                {
                    return Interval.oneTo(object);
                }
            };

    @Override
    protected LazyIterable<Integer> newWith(Integer... integers)
    {
        return LazyIterate.flatCollect(FastList.newListWith(integers), new Function<Integer, Iterable<Integer>>()
        {
            public Iterable<Integer> valueOf(Integer object)
            {
                return FastList.newListWith(object);
            }
        });
    }

    @Test
    public void forEach()
    {
        InternalIterable<Integer> select = new FlatCollectIterable<Integer, Integer>(Interval.oneTo(5), INT_TO_INTERVAL);
        Appendable builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("112123123412345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Integer> select = new FlatCollectIterable<Integer, Integer>(Interval.oneTo(5), INT_TO_INTERVAL);
        final StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                builder.append(object);
                builder.append(index);
            }
        });
        Assert.assertEquals("10112213243516273849110211312413514", builder.toString());
    }

    @Override
    @Test
    public void iterator()
    {
        InternalIterable<Integer> select = new FlatCollectIterable<Integer, Integer>(Interval.oneTo(5), INT_TO_INTERVAL);
        StringBuilder builder = new StringBuilder("");
        for (Integer each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("112123123412345", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Integer> select = new FlatCollectIterable<Integer, Integer>(Interval.oneTo(5), INT_TO_INTERVAL);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith(new Procedure2<Integer, StringBuilder>()
        {
            public void value(Integer each, StringBuilder aBuilder)
            {
                aBuilder.append(each);
            }
        }, builder);
        Assert.assertEquals("112123123412345", builder.toString());
    }
}
