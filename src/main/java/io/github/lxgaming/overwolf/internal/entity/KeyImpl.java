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

package io.github.lxgaming.overwolf.internal.entity;

import com.sun.jna.Memory;
import io.github.lxgaming.overwolf.api.entity.Key;
import io.github.lxgaming.overwolf.internal.structure.KeyStructure;
import io.github.lxgaming.overwolf.internal.util.Preconditions;
import io.github.lxgaming.overwolf.internal.util.Toolbox;
import io.github.lxgaming.overwolf.internal.util.UnsignedInteger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class KeyImpl implements Key {
    
    protected final String name;
    protected final int size;
    protected CategoryImpl category;
    protected KeyStructure keyStructure;
    protected Memory value;
    
    public KeyImpl(@NonNull String name, int size) {
        this.name = name;
        this.size = size;
    }
    
    public void populate(@NonNull KeyStructure keyStructure) {
        Preconditions.checkArgument(keyStructure.key == null && (keyStructure.size == null || keyStructure.size.longValue() == 0L), "KeyStructure is already populated");
        Preconditions.checkState(getCategory() != null, "Key '%s' has not been registered", getName());
        Preconditions.checkState(getKeyStructure() == null, "Key '%s' is already populated", getSize());
        keyStructure.key = Toolbox.allocateString(getName());
        keyStructure.size = new UnsignedInteger(getSize());
        setKeyStructure(keyStructure);
    }
    
    @NonNull
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getSize() {
        return size;
    }
    
    @Nullable
    public CategoryImpl getCategory() {
        return category;
    }
    
    protected void setCategory(@NonNull CategoryImpl category) {
        this.category = category;
    }
    
    @Nullable
    public KeyStructure getKeyStructure() {
        return keyStructure;
    }
    
    protected void setKeyStructure(@NonNull KeyStructure keyStructure) {
        this.keyStructure = keyStructure;
    }
    
    @Nullable
    public Memory getValue() {
        return value;
    }
    
    public void setValue(@NonNull Memory value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        KeyImpl key = (KeyImpl) obj;
        return Objects.equals(getName(), key.getName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}