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

package io.github.lxgaming.overwolf.internal;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import io.github.lxgaming.overwolf.internal.structure.CategoryStructure;
import io.github.lxgaming.overwolf.internal.structure.EventStructure;
import io.github.lxgaming.overwolf.internal.util.UnsignedInteger;
import io.github.lxgaming.overwolf.internal.util.mapper.Mapping;

interface GameEvents extends Library {
    
    @Mapping("owgame_events_create")
    int create(UnsignedInteger gameId, UnsignedInteger categoriesLength, CategoryStructure[] categories, PointerByReference handle);
    
    @Mapping("owgame_events_set_info_key")
    int set(Pointer handle, Pointer categoryName, Pointer keyName, UnsignedInteger valueLength, Pointer value);
    
    @Mapping("owgame_events_trigger_event")
    int triggerEvent(Pointer handle, EventStructure event);
    
    @Mapping("owgame_events_begin_info_transaction")
    int beginTransaction(Pointer handle);
    
    @Mapping("owgame_events_commit_info_transaction")
    int commitTransaction(Pointer handle);
    
    @Mapping("owgame_events_close")
    int close(Pointer handle);
    
    @Mapping("owgame_events_turn_on_logger")
    int logger(Pointer filename);
}