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

package io.github.lxgaming.overwolf.internal.util;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Structure;

import java.nio.charset.StandardCharsets;

public class Toolbox {
    
    public static final int DEFAULT_MAX_STRING_SIZE = 65535;
    
    public static Memory allocateString(String string) throws IllegalStateException {
        return allocateString(string, DEFAULT_MAX_STRING_SIZE);
    }
    
    public static Memory allocateString(String string, int maxLength) throws IllegalStateException {
        if (string.length() > maxLength) {
            throw new IllegalStateException(String.format("Got a too-long string (got %s, max %s)", string.length(), maxLength));
        }
        
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        if (data.length > maxLength) {
            throw new IllegalStateException(String.format("Got a too-long string (got %s, max %s)", data.length, maxLength));
        }
        
        Memory memory = new Memory(data.length + 1);
        memory.write(0, data, 0, data.length);
        memory.setByte(data.length, (byte) 0);
        return memory;
    }
    
    public static Memory allocateWideString(String string) {
        Memory memory = new Memory((string.length() + 1L) * Native.WCHAR_SIZE);
        memory.setWideString(0, string);
        return memory;
    }
    
    public static <T extends Structure> Memory allocateStructure(T[] structures) {
        if (structures.length == 0) {
            return null;
        }
        
        int size = structures[0].size();
        Memory memory = new Memory(structures.length * size);
        for (int index = 0; index < structures.length; index++) {
            structures[index].write();
            byte[] bytes = structures[index].getPointer().getByteArray(0, size);
            memory.write(index * size, bytes, 0, bytes.length);
        }
        
        return memory;
    }
    
    public static void write(byte[] bytes, CharSequence charSequence) {
        for (int index = 0; index < charSequence.length(); index++) {
            bytes[index] = (byte) charSequence.charAt(index);
        }
    }
    
    public static String getClassSimpleName(Class<?> type) {
        if (type.getEnclosingClass() != null) {
            return getClassSimpleName(type.getEnclosingClass()) + "." + type.getSimpleName();
        }
        
        return type.getSimpleName();
    }
}