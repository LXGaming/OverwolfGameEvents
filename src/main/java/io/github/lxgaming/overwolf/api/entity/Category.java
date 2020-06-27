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

package io.github.lxgaming.overwolf.api.entity;

import io.github.lxgaming.overwolf.internal.entity.CategoryImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface Category {
    
    @NonNull
    static Category of(@NonNull String name, @NonNull Key... keys) {
        Category category = new CategoryImpl(name);
        category.register(keys);
        return category;
    }
    
    void register(@NonNull Key... keys);
    
    @NonNull String getName();
    
    @NonNull Collection<Key> getKeys();
}