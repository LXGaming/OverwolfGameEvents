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

import com.sun.jna.Structure;
import io.github.lxgaming.overwolf.internal.util.Toolbox;

import java.util.List;

public class EventStructure extends Structure {
    
    public static final int MAX_NAME_LENGTH = 255;
    public static final int MAX_DATA_LENGTH = 1024 * 63;
    
    public byte[] name;
    public byte[] data;
    
    public EventStructure() {
        this(new byte[MAX_NAME_LENGTH], new byte[MAX_DATA_LENGTH]);
    }
    
    public EventStructure(byte[] name, byte[] data) {
        super();
        this.name = name;
        this.data = data;
    }
    
    @Override
    protected List<String> getFieldOrder() {
        return Toolbox.newArrayList("name", "data");
    }
}