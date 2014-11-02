
	precision mediump float;
	
	varying vec2 vTextureCoord0;
	varying vec2 vTextureCoord1;
	
	varying vec3 vTransformedNormal;
	varying vec4 vPosition;
	
	uniform float uMaterialShininess;
	
	uniform bool uShowSpecularHighlights;
	uniform bool uUseLighting;
	
	uniform bool uUseTexture0;
	uniform bool uUseColoredTexture0;
	uniform bool uUseTexture1;
	
	uniform vec3 uAmbientColor;
	uniform vec4 uDiffuseColor;
	
	uniform vec3 uPointLightingLocation;
	uniform vec3 uPointLightingSpecularColor;
	uniform vec3 uPointLightingDiffuseColor;
	
	uniform sampler2D uSampler0;
	uniform sampler2D uSampler1;
	
	
	void main(void) {
	    vec3 lightWeighting;
	    if (!uUseLighting) {
	        lightWeighting = vec3(1.0, 1.0, 1.0);
	    } else {
	        vec3 lightDirection = normalize(uPointLightingLocation - vPosition.xyz);
	        vec3 normal = normalize(vTransformedNormal);
	
	        float specularLightWeighting = 0.0;
	        if (uShowSpecularHighlights) {
	            vec3 eyeDirection = normalize(-vPosition.xyz);
	            vec3 reflectionDirection = reflect(-lightDirection, normal);
	
	            specularLightWeighting = pow(max(dot(reflectionDirection, eyeDirection), 0.0), uMaterialShininess);
	        }
	
	        float diffuseLightWeighting = max(dot(normal, lightDirection), 0.0);
	        lightWeighting = uAmbientColor
	            + uPointLightingSpecularColor * specularLightWeighting
	            + uPointLightingDiffuseColor * diffuseLightWeighting;
	    }
	
		//lightWeighting = min(lightWeighting, 0.2)
		
	    vec4 fragmentColor;
	    if (uUseTexture0) {
	
			fragmentColor = texture2D(uSampler0, vec2(vTextureCoord0.s, vTextureCoord0.t));
	
			if (uUseColoredTexture0) {
				fragmentColor = vec4(uDiffuseColor.rgb * uDiffuseColor.a + fragmentColor.rgb * (1.0 - uDiffuseColor.a) , fragmentColor.a);
			}
	
			if (uUseTexture1) {
	
				vec4 texture1Color = texture2D(uSampler1, vec2(vTextureCoord1.s, vTextureCoord1.t));
	
				fragmentColor =  (vec4(
					fragmentColor.rgb * (1.0 - texture1Color.a) + texture1Color.rgb * texture1Color.a, 
					fragmentColor.a + texture1Color.a));
			}
	
	    } else {
	        fragmentColor = vec4(uDiffuseColor.rgb, 1.0);
	    }
	    gl_FragColor = vec4(fragmentColor.rgb * lightWeighting, fragmentColor.a);
	}