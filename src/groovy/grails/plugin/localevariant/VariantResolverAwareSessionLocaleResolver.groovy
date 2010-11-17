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

import org.springframework.web.servlet.i18n.SessionLocaleResolver

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.beans.factory.InitializingBean

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.slf4j.LoggerFactory

import grails.util.Environment

class VariantResolverAwareSessionLocaleResolver extends SessionLocaleResolver implements ApplicationContextAware, InitializingBean {

	private static final log = LoggerFactory.getLogger('grails.plugin.localevariant.VariantResolverAwareSessionLocaleResolver')
	
	ApplicationContext applicationContext
	LocaleVariantResolver variantResolver
	
	void afterPropertiesSet() {
		if (!Environment.current.reloadEnabled) {
			variantResolver = findLocaleVariantResolver()
		}
	}
	
	protected LocaleVariantResolver findLocaleVariantResolver() {
		def resolvers = applicationContext.getBeansOfType(LocaleVariantResolver)
		if (resolvers.size() == 0) {
			log.warn("no LocaleVariantResolver implementations found in application context, locales will not have variants")
			null
		} else {
			def firstResolverEntry = resolvers.entrySet().toList().first()
			
			if (resolvers.size() > 1) {
				log.warn("multiple LocaleVariantResolver implementations found (${resolvers.keySet()})")
			}
			
			log.debug("LocaleVariantResolver set to '$firstResolverEntry.key' (type: ${firstResolverEntry.value.class})")
			
			firstResolverEntry.value
		}
	}
	
	protected Locale addVariant(Locale locale, HttpServletRequest request) {
		if (Environment.current.reloadEnabled) {
			variantResolver = findLocaleVariantResolver()
		}
		
		if (variantResolver) {
			def variant = variantResolver.resolveLocaleVariant(locale, request)
			if (variant == null) {
				log.debug("variant not added to locale ${locale}")
				locale
			} else {
				def newLocale = new Locale(locale.language, locale.country, variant)
				log.debug("variant added to ${locale}, making it ${newLocale}")
				newLocale
			}
		} else {
			locale
		}
	}
	
	protected Locale determineDefaultLocale(HttpServletRequest request) {
		addVariant(super.determineDefaultLocale(request), request)
	}

	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		super.setLocale(request, response, addVariant(locale, request))
	}

}