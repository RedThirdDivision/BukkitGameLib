/*
 * Copyright 2020 Jeter
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.jeter.bukkitgamelib.utils;

import java.util.HashMap;
import java.util.Map;

public class LocaleManager {

    private static final HashMap<String, String> help_texts = new HashMap<>();

    public static void load() {
        help_texts.clear();
        for (Locales locale : Locales.values()) {
            if (locale.toString().toLowerCase().contains("help")) {
                help_texts.put(locale.toString(), locale.getString());
            }
        }
    }

    public static void registerHelp(Map<String, String> helpTexts) {
        for (String key : helpTexts.keySet()) {
            if (help_texts.containsKey(key)) {
                help_texts.remove(key);
            }
            help_texts.put(key, helpTexts.get(key));
        }
    }

    public static Map<String, String> getHelpTexts() {
        return help_texts;
    }

}
