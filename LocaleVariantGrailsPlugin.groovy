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

import grails.plugin.localevariant.VariantResolverAwareSessionLocaleResolver
import grails.plugin.localevariant.VariantAndPluginAwareResourceBundleMessageSource

import org.springframework.web.servlet.DispatcherServlet

class LocaleVariantGrailsPlugin {
	
	def title = "Locale Variant Plugin"
	def description = 'Allows the determination of locale variants in a configurable manner'
	def documentation = "http://grails.org/plugin/locale-variant"
	
	def author = "Luke Daley"
	def authorEmail = "ld@ldaley.com"
	
	def version = "0.1"
	def grailsVersion = "1.3.5 > *"
	def dependsOn = [:]
	
	def pluginExcludes = [
		"**/grails/plugin/localevariant/test/**/*",
		"grails-app/**/*",
		"web-app"
	]

	def doWithSpring = {
		// Replaces the SessionLocaleResolver defined by the i18n core Grails plugin
		"${DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME}"(VariantResolverAwareSessionLocaleResolver)
		
		// Replace the message source class with ours that handles variants better
		delegate.getBeanDefinition("messageSource").beanClass = VariantAndPluginAwareResourceBundleMessageSource
	}

}
