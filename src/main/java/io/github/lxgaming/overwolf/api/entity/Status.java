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

import org.checkerframework.checker.nullness.qual.Nullable;

public enum Status {
    
    WARNING_UNKNOWN(-1000),
    
    /**
     * couldn't notify Overwolf to connect
     */
    WARNING_FAILED_TO_TRIGGER_CONNECT(-999),
    
    /**
     * couldn't clear memory when shutting down
     */
    WARNING_FAILED_TO_CLEAR_MEM(-998),
    
    SUCCESS_OK(0),
    
    ERROR_UNKNOWN(1000),
    
    /**
     * contact Overwolf...
     */
    ERROR_UNKNOWN_CATASTROPHIC(1001),
    
    ERROR_INVALID_HANDLE(1002),
    
    /**
     * calling a function on an uninitialized handle
     */
    ERROR_NOT_INITIALIZED(1003),
    
    /**
     * currently not possible to receive
     */
    ERROR_ALREADY_INITIALIZED(1004),
    
    /**
     * couldn't start dispatcher thread
     */
    ERROR_GENERAL_INIT_FAILURE(1005),
    
    /**
     * check that you don't already have a handle to this game_id
     */
    ERROR_SHARED_MEM_INIT_FAILURE(1006),
    
    /**
     * passed invalid parameters (value_length &gt; what you passed in create?)
     */
    ERROR_INVALID_PARAMS(1007),
    
    /**
     * failed to dispatch request (might happen when closing handle)
     */
    ERROR_DISPATCHER(1008),
    
    /**
     * we limit amount of events: 10 event per 200 ms
     */
    ERROR_THROTTLING(1009),
    
    /**
     * couldn't allocate memory
     */
    ERROR_OUT_OF_MEM(1010);
    
    private final int id;
    
    Status(int id) {
        this.id = id;
    }
    
    @Nullable
    public static Status getStatus(int id) {
        for (Status status : Status.values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        
        return null;
    }
    
    public boolean isError() {
        return id > 0;
    }
    
    public boolean isSuccess() {
        return id == 0;
    }
    
    public boolean isWarning() {
        return id < 0;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}