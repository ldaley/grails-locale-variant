This plugin allows you to programatically decorate the locale with a *variant*. 

## About Locale Variants

From the [`java.util.Locale`](http://download.oracle.com/javase/6/docs/api/java/util/Locale.html "Locale (Java Platform SE 6)") documentation, a *variant* is described as:

> The variant argument is a vendor or browser-specific code. For example, use WIN for Windows, MAC for Macintosh, and POSIX for POSIX. 

Unlike the *language* and *country* components of a locale, the *variant* component is not standardised. This means that we can use it in any manner in our application. For example, you could set the locale variant to the username or id of the logged in user, allowing you to use the in-built i18n message resolution mechanism in Grails (i.e. `<g:message>` and friends) to resolve message codes based on the logged in user.

If you are wondering why you might want to do this, consider an e-commerce engine that is used by multiple affiliates. Using this plugin, you can decorate the user's locale with a variant for each affiliate. This would allow you to *easily* customise the text displayed for each affiliate while still supporting internationalisation.

## Using this plugin

This plugin is extremely simple to use. Simply install the plugin, and provide **exactly one** implementation of the following interface in your application context…

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

On application startup, the application context is scanned for an implementation of this interface. Whenever a locale is in need of decoration with a *variant*, it is used.

### Example Implementation

For sake of example, let's say we want to use the logged in user's id as the variant. A simple way to do this would be to set a request attribute in a before filter, and then refer to that attribute in a `LocaleVariantResolver` implementation.

Our filter might look like…

    class LocaleVariantFilters {
        def filters = {
            initLocaleVariantRequestParam(controller: '*') {
                before = {
                    request.userId = // get the logged in user id somehow
                }
            }
        }
    }

We can use a service for our `LocaleVariantResolver` implementation.

    import grails.plugin.localevariant.LocaleVariantResolver
    import javax.servlet.http.HttpServletRequest
    
    class LocaleVariantService implements LocaleVariantResolver {

        String resolveLocaleVariant(Locale locale, HttpServletRequest request) {
            request.userId
        }
        
    }

Being a service, an instance of this will be in the application context so will be found by this plugin and used to resolve locale variants.

If a user with id `100` accesses our application with their browser's locale set to `en_AU` their locale becomes `en_AU_100` after decoration.

## Message Resolution

Using the above user id variant scheme, let's look at how messages will be resolved.

On our application home page, we want to display a welcome message to the user. To do this, we have a GSP that looks something like…

    <p>Hello user!</p>
    <p><g:message code="welcomemessage" />

In our `grails-app/i18n` directory we have the following files:

##### messages_en.properties

    welcomemessage=this is the default welcome message

##### messages\_en_AU.properties

    welcomemessage=this is the default welcome message for Australians

##### messages\_en\_\_100_.properties

    welcomemessage=this is the welcome message for user 100

##### messages\_en\_AU\_100_.properties

    welcomemessage=this is the welcome message for user 100 in Australia

##### messages\_en\_\_200_.properties

    welcomemessage=Hello user 200!

Notice that locales do not have to have a *country* component. The locale `en__100` effectively means the English messages for user `100`, regardless of their country. 

### Precedence

It is important to consider precedence. You will notice that we have declared messages for the locales `en_AU_100` and `en__100`. If user `100` accesses the application with a locale of `en_AU`, the `en_AU_100` messages will be used by Grails. If they access the application with a locale of `en_US`, the `en__100` messages will be used by Grails. This is because an explicit country match has higher precedence than a *wildcard* country match.

If user `200` accesses the application with a locale set to `en_AU`, the messages from `en__200` will be used. This is because a version with a matching variant has higher precedence than a version without a variant but with a matching country.

## Help / Support

Please raise issues on the Grails user mailing list.

## Licensing

[ASL 2.0](http://www.apache.org/licenses/LICENSE-2.0.html "Apache License, Version 2.0")