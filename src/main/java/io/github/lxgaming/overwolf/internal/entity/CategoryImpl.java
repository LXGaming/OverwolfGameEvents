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

import io.github.lxgaming.overwolf.api.entity.Category;
import io.github.lxgaming.overwolf.api.entity.Key;
import io.github.lxgaming.overwolf.internal.structure.CategoryStructure;
import io.github.lxgaming.overwolf.internal.structure.KeyStructure;
import io.github.lxgaming.overwolf.internal.util.Preconditions;
import io.github.lxgaming.overwolf.internal.util.Toolbox;
import io.github.lxgaming.overwolf.internal.util.UnsignedInteger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CategoryImpl implements Category {
    
    protected final String name;
    protected final List<KeyImpl> keys;
    protected CategoryStructure categoryStructure;
    
    public CategoryImpl(@NonNull String name) {
        this.name = name;
        this.keys = new ArrayList<>();
    }
    
    @Override
    public void register(@NonNull Key... keys) {
        for (Key key : keys) {
            Preconditions.checkArgument(key instanceof KeyImpl, "%s is not supported", Toolbox.getClassSimpleName(key.getClass()));
            register((KeyImpl) key);
        }
    }
    
    protected void register(@NonNull KeyImpl key) {
        Preconditions.checkState(key.getCategory() == null, "Key '%s' is already registered", key.getName());
        Preconditions.checkState(!keys.contains(key), "Duplicate Key '%s'", key.getName());
        key.setCategory(this);
        keys.add(key);
    }
    
    public void populate(@NonNull CategoryStructure categoryStructure) {
        Preconditions.checkArgument(
                categoryStructure.name == null && (categoryStructure.items == null || categoryStructure.items.longValue() == 0L) && categoryStructure.keys == null,
                "CategoryStructure is already populated");
        KeyStructure.ByReference[] keyStructures = (KeyStructure.ByReference[]) new KeyStructure.ByReference().toArray(keys.size());
        for (int index = 0; index < keyStructures.length; index++) {
            keys.get(index).populate(keyStructures[index]);
        }
        
        categoryStructure.name = Toolbox.allocateString(getName());
        categoryStructure.items = new UnsignedInteger(keyStructures.length);
        categoryStructure.keys = Toolbox.allocateStructure(keyStructures);
        setCategoryStructure(categoryStructure);
    }
    
    @NonNull
    @Override
    public String getName() {
        return name;
    }
    
    @NonNull
    @Override
    public Collection<Key> getKeys() {
        return Collections.unmodifiableList(keys);
    }
    
    @Nullable
    public CategoryStructure getCategoryStructure() {
        return categoryStructure;
    }
    
    protected void setCategoryStructure(@NonNull CategoryStructure categoryStructure) {
        this.categoryStructure = categoryStructure;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        CategoryImpl category = (CategoryImpl) obj;
        return Objects.equals(getName(), category.getName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}