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

import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIntProceduresTest
{
    @Test
    public void fromProcedureWithInt()
    {
        CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(FastList.<Integer>newList());
        ObjectIntProcedure<Integer> objectIntProcedure = ObjectIntProcedures.fromProcedure(procedure);
        objectIntProcedure.value(1, 0);
        Assert.assertEquals(FastList.newListWith(1), procedure.getResult());
    }
}
