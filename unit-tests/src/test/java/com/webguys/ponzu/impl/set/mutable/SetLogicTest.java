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

package com.webguys.ponzu.impl.set.mutable;

import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.impl.block.factory.Predicates;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SetLogicTest
{
    private MutableSet<Integer> setA;
    private MutableSet<Integer> setB;

    @Before
    public void setUp()
    {
        this.setA = UnifiedSet.newSetWith(1, 2, 3, 4).asUnmodifiable();
        this.setB = UnifiedSet.newSetWith(3, 4, 5, 6).asUnmodifiable();
    }

    @Test
    public void inOnlyInAMutable()
    {
        MutableSet<Integer> onlyInA = this.setA.filterNot(Predicates.in(this.setB), UnifiedSet.<Integer>newSet());
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2), onlyInA);
    }

    @Test
    public void onlyInAJdkLike()
    {
        MutableSet<Integer> onlyInA = UnifiedSet.newSet(this.setA);
        onlyInA.removeAll(this.setB);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2), onlyInA);
    }

    @Test
    public void inBothAAndBMutable()
    {
        Assert.assertEquals(UnifiedSet.newSetWith(3, 4), this.setA.filter(Predicates.in(this.setB)));
    }

    @Test
    public void inAOrBButNotInBoth()
    {
        MutableSet<Integer> nonOverlappingSet = UnifiedSet.newSet();
        this.setA.filter(Predicates.notIn(this.setB), nonOverlappingSet);
        this.setB.filter(Predicates.notIn(this.setA), nonOverlappingSet);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 5, 6), nonOverlappingSet);
    }
}
