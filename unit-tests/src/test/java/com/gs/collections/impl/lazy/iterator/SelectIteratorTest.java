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

package com.gs.collections.impl.lazy.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class SelectIteratorTest
{
    private static final Predicates<Object> PREDICATE = Predicates.equal(Boolean.TRUE);

    @Test
    public void iterator()
    {
        MutableList<Boolean> list = FastList.newListWith(
                Boolean.TRUE,
                Boolean.FALSE,
                Boolean.TRUE,
                Boolean.TRUE,
                Boolean.FALSE,
                null,
                null,
                Boolean.FALSE,
                Boolean.TRUE,
                null);
        this.assertElements(new SelectIterator<Boolean>(list.iterator(), PREDICATE));
        this.assertElements(new SelectIterator<Boolean>(list, PREDICATE));
    }

    private void assertElements(Iterator<Boolean> newIterator)
    {
        for (int i = 0; i < 4; i++)
        {
            Assert.assertTrue(newIterator.hasNext());
            Assert.assertEquals(Boolean.TRUE, newIterator.next());
        }
        Assert.assertFalse(newIterator.hasNext());
    }

    @Test
    public void noSuchElementException()
    {
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                new SelectIterator<Object>(Lists.fixedSize.of(), Predicates.alwaysTrue()).next();
            }
        });
    }

    @Test
    public void remove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                new SelectIterator<Object>(Lists.fixedSize.of(), Predicates.alwaysTrue()).remove();
            }
        });
    }
}
