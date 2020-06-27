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

import io.github.lxgaming.overwolf.api.entity.Event;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EventImpl implements Event {
    
    protected final String name;
    protected final String data;
    
    public EventImpl(@NonNull String name, @NonNull String data) {
        this.name = name;
        this.data = data;
    }
    
    @NonNull
    @Override
    public String getName() {
        return name;
    }
    
    @NonNull
    @Override
    public String getData() {
        return data;
    }
}