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
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import io.github.lxgaming.overwolf.api.Overwolf;
import io.github.lxgaming.overwolf.api.entity.Category;
import io.github.lxgaming.overwolf.api.entity.Event;
import io.github.lxgaming.overwolf.api.entity.Key;
import io.github.lxgaming.overwolf.api.entity.Status;
import io.github.lxgaming.overwolf.internal.entity.CategoryImpl;
import io.github.lxgaming.overwolf.internal.entity.KeyImpl;
import io.github.lxgaming.overwolf.internal.structure.CategoryStructure;
import io.github.lxgaming.overwolf.internal.structure.EventStructure;
import io.github.lxgaming.overwolf.internal.util.Preconditions;
import io.github.lxgaming.overwolf.internal.util.StringUtils;
import io.github.lxgaming.overwolf.internal.util.Toolbox;
import io.github.lxgaming.overwolf.internal.util.UnsignedInteger;
import io.github.lxgaming.overwolf.internal.util.mapper.FunctionMapperImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class OverwolfImpl implements Overwolf {
    
    protected static final GameEvents GAME_EVENTS = loadGameEvents();
    protected final long gameId;
    protected final PointerByReference handle;
    protected final List<CategoryImpl> categories;
    protected final AtomicBoolean transaction = new AtomicBoolean(false);
    
    public OverwolfImpl(long gameId) {
        this.gameId = gameId;
        this.handle = new PointerByReference();
        this.categories = new ArrayList<>();
    }
    
    @SuppressWarnings("deprecation")
    protected static GameEvents loadGameEvents() {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put(Library.OPTION_FUNCTION_MAPPER, new FunctionMapperImpl());
            return Native.loadLibrary("libowgameevents", GameEvents.class, options);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
    
    public void register(@NonNull Category... categories) {
        Preconditions.checkState(handle.getValue() == null, "Can not register after connection has been established");
        for (Category category : categories) {
            Preconditions.checkArgument(category instanceof CategoryImpl, "%s is not supported", Toolbox.getClassSimpleName(category.getClass()));
            register((CategoryImpl) category);
        }
    }
    
    protected void register(CategoryImpl category) {
        Preconditions.checkState(!categories.contains(category), "Duplicate Category '%s'", category.getName());
        categories.add(category);
    }
    
    @Override
    public Status connect() {
        Preconditions.checkState(handle.getValue() == null, "Already connected");
        CategoryStructure[] categoryStructures = (CategoryStructure[]) new CategoryStructure().toArray(categories.size());
        for (int index = 0; index < categoryStructures.length; index++) {
            categories.get(index).populate(categoryStructures[index]);
        }
        
        int statusId = GAME_EVENTS.create(
                new UnsignedInteger(gameId),
                new UnsignedInteger(categoryStructures.length),
                categoryStructures,
                handle
        );
        
        return Status.getStatus(statusId);
    }
    
    @Override
    public Status disconnect() {
        Preconditions.checkState(handle.getValue() != null, "Not connected");
        int statusId = GAME_EVENTS.close(handle.getValue());
        return Status.getStatus(statusId);
    }
    
    @Override
    public Status set(@NonNull Key key, @NonNull String value) {
        Preconditions.checkArgument(key instanceof KeyImpl, "%s is not supported", Toolbox.getClassSimpleName(key.getClass()));
        Preconditions.checkState(handle.getValue() != null, "Not connected");
        return set((KeyImpl) key, value);
    }
    
    protected Status set(@NonNull KeyImpl key, @NonNull String value) {
        Preconditions.checkState(key.getCategory() != null, "Key '%s' has not been registered", key.getName());
        Preconditions.checkState(key.getKeyStructure() != null, "Key '%s' has not been populated", key.getName());
        Preconditions.checkState(key.getCategory().getCategoryStructure() != null, "Category '%s' has not been populated", key.getCategory().getName());
        
        Memory memory = Toolbox.allocateString(value, key.getSize());
        
        int statusId = GAME_EVENTS.set(
                handle.getValue(),
                key.getCategory().getCategoryStructure().name,
                key.getKeyStructure().key,
                new UnsignedInteger(memory.size()),
                memory);
        
        Status status = Status.getStatus(statusId);
        if (status != null && status.isSuccess()) {
            key.setValue(memory);
        }
        
        return status;
    }
    
    @Override
    public synchronized boolean set(@NonNull Map<Key, String> map) {
        Status status = beginTransaction();
        if (status == null || !status.isSuccess()) {
            return false;
        }
        
        try {
            for (Map.Entry<Key, String> entry : map.entrySet()) {
                status = set(entry.getKey(), entry.getValue());
                if (status == null || !status.isSuccess()) {
                    commitTransaction();
                    return false;
                }
            }
            
            status = commitTransaction();
            return status != null && status.isSuccess();
        } catch (Exception ex) {
            commitTransaction();
            return false;
        }
    }
    
    @Override
    public Status triggerEvent(@NonNull Event event) {
        Preconditions.checkArgument(event.getName().length() <= EventStructure.MAX_NAME_LENGTH, "Name exceeds maximum length");
        Preconditions.checkArgument(event.getData().length() <= EventStructure.MAX_DATA_LENGTH, "Data exceeds maximum length");
        Preconditions.checkState(handle.getValue() != null, "Not connected");
        
        EventStructure eventStructure = new EventStructure();
        Toolbox.write(eventStructure.name, StringUtils.rightPad(event.getName(), EventStructure.MAX_NAME_LENGTH, '\0'));
        Toolbox.write(eventStructure.data, StringUtils.rightPad(event.getData(), EventStructure.MAX_DATA_LENGTH, '\0'));
        
        int statusId = GAME_EVENTS.triggerEvent(handle.getValue(), eventStructure);
        return Status.getStatus(statusId);
    }
    
    protected Status beginTransaction() {
        Preconditions.checkState(!transaction.get(), "Transaction already in progress");
        int statusId = GAME_EVENTS.beginTransaction(handle.getValue());
        Status status = Status.getStatus(statusId);
        if (status != null && status.isSuccess()) {
            Preconditions.checkState(transaction.compareAndSet(false, true), "Transaction already in progress");
        }
        
        return status;
    }
    
    protected Status commitTransaction() {
        Preconditions.checkState(transaction.get(), "Transaction not in progress");
        int statusId = GAME_EVENTS.commitTransaction(handle.getValue());
        Status status = Status.getStatus(statusId);
        if (status != null && status.isSuccess()) {
            Preconditions.checkState(transaction.compareAndSet(true, false), "Transaction not in progress");
        }
        
        return status;
    }
}