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

package io.github.lxgaming.overwolf.api;

import io.github.lxgaming.overwolf.api.entity.Category;
import io.github.lxgaming.overwolf.api.entity.Event;
import io.github.lxgaming.overwolf.api.entity.Key;
import io.github.lxgaming.overwolf.api.entity.Status;
import io.github.lxgaming.overwolf.internal.OverwolfImpl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

public interface Overwolf {
    
    @NonNull
    static Overwolf of(long gameId, @NonNull Category... categories) {
        Overwolf overwolf = new OverwolfImpl(gameId);
        overwolf.register(categories);
        return overwolf;
    }
    
    void register(@NonNull Category... categories);
    
    @Nullable Status connect();
    
    @Nullable Status disconnect();
    
    @Nullable Status set(@NonNull Key key, @NonNull String value);
    
    boolean set(@NonNull Map<Key, String> map);
    
    @Nullable Status triggerEvent(@NonNull Event event);
}