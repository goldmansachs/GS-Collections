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

package com.gs.collections.impl.block.function;

import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Functions0;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class Functions0Test
{
    @Test
    public void newFastList()
    {
        Assert.assertEquals(Lists.mutable.of(), Functions0.newFastList().value());
        Verify.assertInstanceOf(FastList.class, Functions0.newFastList().value());
    }

    @Test
    public void newUnifiedSet()
    {
        Assert.assertEquals(UnifiedSet.newSet(), Functions0.newUnifiedSet().value());
        Verify.assertInstanceOf(UnifiedSet.class, Functions0.newUnifiedSet().value());
    }

    @Test
    public void newHashBag()
    {
        Assert.assertEquals(Bags.mutable.of(), Functions0.newHashBag().value());
        Verify.assertInstanceOf(HashBag.class, Functions0.newHashBag().value());
    }

    @Test
    public void newUnifiedMap()
    {
        Assert.assertEquals(UnifiedMap.newMap(), Functions0.newUnifiedMap().value());
        Verify.assertInstanceOf(UnifiedMap.class, Functions0.newUnifiedMap().value());
    }
}
