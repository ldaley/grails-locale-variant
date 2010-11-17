package grails.plugin.localevariant

import grails.plugin.spock.*
import spock.lang.*

import org.springframework.web.context.request.RequestContextHolder

class MessageResolutionSpec extends IntegrationSpec {

	def testLocaleVariantResolverService
	def localeResolver
	def messageSource
	
	@Unroll("#language - #country - #variant = #message")
	def "test1"() {
		given:
		def request = RequestContextHolder.requestAttributes.currentRequest
		def response = RequestContextHolder.requestAttributes.currentResponse
		def locale = new Locale(language, country, "")
		
		when:
		testLocaleVariantResolverService.variant = variant
		localeResolver.setLocale(request, response, locale)
		
		and:
		def actualMessage = messageSource.getMessage("m1", [] as Object[], localeResolver.resolveLocale(request))
		
		then:
		message == actualMessage
		
		where:
		language | country | variant | message
		""       | ""      | ""      | "default"
		"en"     | ""      | ""      | "en"   
		"en"     | "US"    | ""      | "en"
		"en"     | "AU"    | ""      | "en_AU"
		"en"     | "AU"    | "v1"    | "en_AU_v1"
		"en"     | "AU"    | "v2"    | "en__v2"
		"en"     | "US"    | "v1"    | "en__v1"
		"en"     | "US"    | "v2"    | "en__v2"
		"en"     | ""      | "v1"    | "en__v1"
		"en"     | ""      | "v2"    | "en__v2"
		"en"     | ""      | "v3"    | "en"
		"xx"     | "US"    | "v2"    | "default"
	}
	

}