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

public class StringUtils {
    
    public static boolean isBlank(CharSequence charSequence) {
        int length;
        if (charSequence == null || (length = charSequence.length()) == 0) {
            return true;
        }
        
        for (int index = 0; index < length; index++) {
            if (!Character.isWhitespace(charSequence.charAt(index))) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean isNotBlank(CharSequence charSequence) {
        return !isBlank(charSequence);
    }
    
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }
    
    public static String rightPad(String string, int length, char character) {
        if (string == null) {
            return null;
        }
        
        if (string.length() >= length) {
            return string;
        }
        
        StringBuilder stringBuilder = new StringBuilder(string);
        while (stringBuilder.length() < string.length()) {
            stringBuilder.append(character);
        }
        
        return stringBuilder.toString();
    }
}