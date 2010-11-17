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

package grails.plugin.localevariant.test

import javax.servlet.http.HttpServletRequest
import grails.plugin.localevariant.LocaleVariantResolver
import org.springframework.web.context.request.RequestContextHolder

class TestLocaleVariantResolverService implements LocaleVariantResolver {

	String variant
	
	String resolveLocaleVariant(Locale locale, HttpServletRequest request) {
		RequestContextHolder.requestAttributes.params.variant ?: variant
	}

}