/*
 * Copyright 2010 Luke Daley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.localevariant;

import org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The method that we override is called very frequently, so needs to be efficent which is why
 * this class is implemented in Java.
 */
class VariantAndPluginAwareResourceBundleMessageSource extends PluginAwareResourceBundleMessageSource {
	
	/**
	 * Copied from ReloadableResourceBundleMessageSource.
	 * 
	 * See the inline comment for the difference.
	 */
	protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
		List<String> result = new ArrayList<String>(3);
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();
		StringBuilder temp = new StringBuilder(basename);
		
		temp.append('_');
		if (language.length() > 0) {
			temp.append(language);
			result.add(0, temp.toString());
		}

		temp.append('_');
		if (country.length() > 0) {
			temp.append(country);
			result.add(0, temp.toString());
		}

		if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
			temp.append('_').append(variant);
			result.add(0, temp.toString());
			
			// This is the only difference from the ReloadableResourceBundleMessageSource impl.
			// We need to add in a language wildcard version because a locale of en_US_v1,
			// should match a file with the extension en__v1.
			if (country.length() > 0) {
				temp.delete(result.get(2).length(), temp.length());
				temp.append("__");
				temp.append(variant);
				result.add(1, temp.toString());
			}
		}

		return result;
	}
}