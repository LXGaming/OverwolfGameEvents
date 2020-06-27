/*
 * Copyright 2020 Alex Thomson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lxgaming.overwolf.internal.structure;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import io.github.lxgaming.overwolf.internal.util.Toolbox;
import io.github.lxgaming.overwolf.internal.util.UnsignedInteger;

import java.util.List;

public class KeyStructure extends Structure {
    
    public Pointer key;
    public UnsignedInteger size;
    
    public KeyStructure() {
        this(null, null);
    }
    
    public KeyStructure(Pointer key, UnsignedInteger size) {
        super();
        this.key = key;
        this.size = size;
    }
    
    @Override
    protected List<String> getFieldOrder() {
        return Toolbox.newArrayList("key", "size");
    }
    
    public static class ByReference extends KeyStructure implements Structure.ByReference {
        
        public ByReference() {
            super();
        }
        
        public ByReference(Pointer key, UnsignedInteger size) {
            super(key, size);
        }
    }
}