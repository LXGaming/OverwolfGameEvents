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

package io.github.lxgaming.overwolf.internal.util.mapper;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;
import io.github.lxgaming.overwolf.internal.util.StringUtils;

import java.lang.reflect.Method;

public class FunctionMapperImpl implements FunctionMapper {
    
    @Override
    public String getFunctionName(NativeLibrary library, Method method) {
        Mapping[] mappings = method.getDeclaredAnnotationsByType(Mapping.class);
        if (mappings.length == 0) {
            return method.getName();
        }
        
        Mapping mapping = mappings[0];
        if (StringUtils.isBlank(mapping.value())) {
            return method.getName();
        }
        
        return mapping.value();
    }
}