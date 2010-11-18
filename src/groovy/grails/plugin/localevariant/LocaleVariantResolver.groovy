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

package grails.plugin.localevariant

import javax.servlet.http.HttpServletRequest

/**
 * A locale variant resolver determines the locale variant to use for a particular request.
 * 
 * It is called whenever an explicit locale is set programatically. Or in the absence of a set
 * locale, when resolving the locale based on the user request.
 */
interface LocaleVariantResolver {
	
	/**
	 * For the given request, return the locale variant to use.
	 * 
	 * @param locale The already determined locale for the request, before adding the variant
	 * @param request The request we are determining the locale variant for
	 * @return the locale variant to use, or null if no variant should be used 
	 */
	String resolveLocaleVariant(Locale locale, HttpServletRequest request)
	
}