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

package com.gs.collections.impl.utility;

import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.internal.IterableIterate;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for the null handling behavior of {@link Iterate}, {@link ArrayIterate}, {@link ArrayListIterate},
 * {@link ListIterate}, {@link IterableIterate}.
 */
public class IterateNullTest
{
    // Iterate

    @Test
    public void collect()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collect(null, Functions.getPassThru());
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.collect(null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void collectIf()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collectIf(null, null, Functions.getPassThru());
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.collectIf(null, null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void collectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collectWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.collectWith(null, null, null);
            }
        });
    }

    @Test
    public void select()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.select(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.select(null, null);
            }
        });
    }

    @Test
    public void selectAndRejectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.selectAndRejectWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.selectAndRejectWith(null, null, null);
            }
        });
    }

    @Test
    public void partition()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.partition(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.partition(null, null);
            }
        });
    }

    @Test
    public void selectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.selectWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.selectWith(null, null, null);
            }
        });
    }

    @Test
    public void detect()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.detect(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.detect(null, null);
            }
        });
    }

    @Test
    public void detectIfNone()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.detectIfNone(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.detectIfNone(null, null, null);
            }
        });
    }

    @Test
    public void detectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.detectWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.detectWith(null, null, null);
            }
        });
    }

    @Test
    public void detectWithIfNone()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.detectWithIfNone(null, null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.detectWithIfNone(null, null, null, null);
            }
        });
    }

    @Test
    public void detectIndex()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.detectIndex(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.detectIndex(null, null);
            }
        });
    }

    @Test
    public void detectIndexWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.detectIndexWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.detectIndexWith(null, null, null);
            }
        });
    }

    @Test
    public void reject()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.reject(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.reject(null, null);
            }
        });
    }

    @Test
    public void rejectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.rejectWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.rejectWith(null, null, null);
            }
        });
    }

    @Test
    public void injectInto()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.injectInto(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.injectInto(null, null, null);
            }
        });
    }

    @Test
    public void injectIntoWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.injectIntoWith(null, null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.injectIntoWith(null, null, null, null);
            }
        });
    }

    @Test
    public void forEach()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.forEach(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.forEach(null, null);
            }
        });
    }

    @Test
    public void forEachWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.forEachWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.forEachWith(null, null, null);
            }
        });
    }

    @Test
    public void forEachWithIndex()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.forEachWithIndex((Iterable<?>) null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.forEachWithIndex(null, null);
            }
        });
    }

    @Test
    public void anySatisfy()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.anySatisfy(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.anySatisfy(null, null);
            }
        });
    }

    @Test
    public void anySatisfyWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.anySatisfyWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.anySatisfyWith(null, null, null);
            }
        });
    }

    @Test
    public void allSatisfy()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.allSatisfy(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.allSatisfy(null, null);
            }
        });
    }

    @Test
    public void allSatisfyWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.allSatisfyWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.allSatisfyWith(null, null, null);
            }
        });
    }

    // Others

    @Test
    public void collectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.collect(null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void collectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.collect(null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void collectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.collect(null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void selectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.select(null, null);
            }
        });
    }

    @Test
    public void selectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.select(null, null);
            }
        });
    }

    @Test
    public void selectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.select(null, null);
            }
        });
    }

    @Test
    public void detectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(ArrayListIterate.detect(null, null));
            }
        });
    }

    @Test
    public void detectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(ListIterate.detect(null, null));
            }
        });
    }

    @Test
    public void detectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(IterableIterate.detect(null, null));
            }
        });
    }

    @Test
    public void rejectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.reject(null, null);
            }
        });
    }

    @Test
    public void rejectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.reject(null, null);
            }
        });
    }

    @Test
    public void rejectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.reject(null, null);
            }
        });
    }

    @Test
    public void injectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(ArrayListIterate.injectInto(null, null, null));
            }
        });
    }

    @Test
    public void injectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(ListIterate.injectInto(null, null, null));
            }
        });
    }

    @Test
    public void injectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(IterableIterate.injectInto(null, null, null));
            }
        });
    }

    @Test
    public void forEachArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEach(null, null);
            }
        });
    }

    @Test
    public void forEachList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.forEach(null, null);
            }
        });
    }

    @Test
    public void forEachIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.forEach(null, null);
            }
        });
    }

    @Test
    public void takeArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.take(null, 0);
            }
        });
    }

    @Test
    public void takeList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.take(null, 0);
            }
        });
    }

    @Test
    public void takeIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.take(null, 0);
            }
        });
    }

    @Test
    public void dropArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.drop(null, 0);
            }
        });
    }

    @Test
    public void dropList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.drop(null, 0);
            }
        });
    }

    @Test
    public void dropIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.drop(null, 0);
            }
        });
    }
}
