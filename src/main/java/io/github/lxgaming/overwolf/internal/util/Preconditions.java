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

public class Preconditions {
    
    public static void checkArgument(boolean expression, String format, Object... args) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }
    
    public static <T> T checkNotNull(T instance, String format, Object... args) {
        if (instance == null) {
            throw new NullPointerException(String.format(format, args));
        }
        
        return instance;
    }
    
    public static void checkState(boolean expression, String format, Object... args) {
        if (!expression) {
            throw new IllegalStateException(String.format(format, args));
        }
    }
}