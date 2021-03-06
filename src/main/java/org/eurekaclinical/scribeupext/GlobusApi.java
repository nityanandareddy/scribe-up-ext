package org.eurekaclinical.scribeupext;

/*
 * #%L
 * Eureka! Clinical ScribeUP Extensions
 * %%
 * Copyright (C) 2014 Emory University
 * %%
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
 * #L%
 */
import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * The Globus API for authorizing the user and getting an access token.
 * 
 * @author Andrew Post
 */
public class GlobusApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://auth.globus.org/v2/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";

    /**
     * Space-delimited set of scopes.
     */
    private static final String SCOPES = "openid email profile";

    /**
     * Returns the URL for requesting an access token.
     * 
     * @return the URL for requesting an access token.
     */
    @Override
    public String getAccessTokenEndpoint() {
        return "https://auth.globus.org/v2/oauth2/token";
    }

    /**
     * Returns the verb to use for requesting an access token.
     * 
     * @return the verb to use for requesting an access token.
     */
    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    /**
     * Returns an object for parsing a successful response from the 
     * authorization call and extracting the access token.
     * 
     * @return an object that can extract the access token from Globus' 
     * response.
     */
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    /**
     * Returns the URL for authorizing the user.
     * 
     * @param config query parameters to add to the base URL.
     * 
     * @return the URL for authorizing the user.
     */
    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback.");
        return String.format(AUTHORIZE_URL, config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()), SCOPES);
    }
}
