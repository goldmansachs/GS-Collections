/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.block.function.primitive;

import java.io.Serializable;

/**
 * A CharFunction can be used to convert one character to another.
 */
public interface CodePointFunction
        extends Serializable
{
    CodePointFunction TO_UPPERCASE = new CodePointFunction()
    {
        private static final long serialVersionUID = 1L;

        public int valueOf(int codePoint)
        {
            return Character.toUpperCase(codePoint);
        }
    };

    CodePointFunction TO_LOWERCASE = new CodePointFunction()
    {
        private static final long serialVersionUID = 1L;

        public int valueOf(int codePoint)
        {
            return Character.toLowerCase(codePoint);
        }
    };

    int valueOf(int codePoint);
}
