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

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public class CaseProcedureTest
{
    @Test
    public void procedure()
    {
        MutableList<String> ifOneList = Lists.mutable.of();
        MutableList<String> defaultList = Lists.mutable.of();
        Procedure<String> ifOneProcedure = CollectionAddProcedure.on(ifOneList);
        Procedure<String> defaultProcedure = CollectionAddProcedure.on(defaultList);
        MutableList<String> list = FastList.newListWith("1", "2");
        Iterate.forEach(list, new CaseProcedure<String>(defaultProcedure).addCase(Predicates.equal("1"), ifOneProcedure));
        Assert.assertEquals(FastList.newListWith("1"), ifOneList);
        Assert.assertEquals(FastList.newListWith("2"), defaultList);
    }
}
