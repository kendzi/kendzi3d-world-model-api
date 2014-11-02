
function KendziRender (textureUrlPrefix) {
	
	if (textureUrlPrefix) {
		this.textureUrlPrefix = textureUrlPrefix;
	} else { 
		this.textureUrlPrefix = null;
	}
	
	this.shaderProgram = null;	
};

KendziRender.prototype.setTranslateMatrixUniforms = function(mvMatrix) {

        gl.uniformMatrix4fv(this.shaderProgram.mvMatrixUniform, false, mvMatrix);

        var normalMatrix = mat3.create();
        mat4.toInverseMat3(mvMatrix, normalMatrix);
        mat3.transpose(normalMatrix);
        gl.uniformMatrix3fv(this.shaderProgram.nMatrixUniform, false, normalMatrix);
};

KendziRender.prototype.render= function(renderModel) {

    gl.uniform1i(this.shaderProgram.samplerUniform0, 0);
    gl.uniform1i(this.shaderProgram.samplerUniform1, 1);
    
	var shininess = 20;

	gl.bindBuffer(gl.ARRAY_BUFFER, renderModel.vertexBuffer);
	gl.vertexAttribPointer(this.shaderProgram.vertexPositionAttribute,
			renderModel.vertexBuffer.itemSize, gl.FLOAT, false, 0, 0);

	if (renderModel.textureCoordBuffer.length > 0) {
		gl.enableVertexAttribArray(this.shaderProgram.textureCoord0Attribute);
		
		gl.bindBuffer(gl.ARRAY_BUFFER, renderModel.textureCoordBuffer[0]);
		gl.vertexAttribPointer(this.shaderProgram.textureCoord0Attribute,
				renderModel.textureCoordBuffer[0].itemSize, gl.FLOAT, false, 0, 0);
		
	} else {
		gl.disableVertexAttribArray(this.shaderProgram.textureCoord0Attribute);
	}
	 
	if (renderModel.textureCoordBuffer.length > 1) {
		gl.enableVertexAttribArray(this.shaderProgram.textureCoord1Attribute);
		
		gl.bindBuffer(gl.ARRAY_BUFFER, renderModel.textureCoordBuffer[1]);
		gl.vertexAttribPointer(this.shaderProgram.textureCoord1Attribute,
				renderModel.textureCoordBuffer[1].itemSize, gl.FLOAT, false, 0, 0);
		
	} else {
		gl.disableVertexAttribArray(this.shaderProgram.textureCoord1Attribute);
	}
	
	gl.bindBuffer(gl.ARRAY_BUFFER, renderModel.normalBuffer);
	gl.vertexAttribPointer(this.shaderProgram.vertexNormalAttribute,
			renderModel.normalBuffer.itemSize, gl.FLOAT, false, 0, 0);

	//setMatrixUniforms();

	for (var i = 0; i < renderModel.faces.length; i++) {
		var face = renderModel.faces[i];
		
		this.bindTextures(face.material);
		// setupMaterial(face.material);
		var useColoredTexture0 = 0;
		var diffuse = face.material.diffuse;
		if (diffuse ) {
			if (diffuse.color) {
				gl.uniform4f(
	                this.shaderProgram.diffuseColorUniform,
	                diffuse.color.r, 
	                diffuse.color.g, 
	                diffuse.color.b, 0.5
	            );
			
				if (diffuse.texture0) {
					useColoredTexture0 = 1;
				}
			}
		}
		gl.uniform1i(this.shaderProgram.useColoredTexture0Uniform, useColoredTexture0);
		
		gl.uniform1i(this.shaderProgram.samplerUniform, 0);

		gl.uniform1f(this.shaderProgram.materialShininessUniform, shininess);

		gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, face.indexBuffer);
		
		gl.drawElements(gl.TRIANGLES, face.indexBuffer.numItems,
				gl.UNSIGNED_SHORT, 0);

	}
};

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16)/255,
        g: parseInt(result[2], 16)/255,
        b: parseInt(result[3], 16)/255
    } : null;
}

KendziRender.prototype.bindTextures = function(material) {
	
	var useTexture0 = false;
	var useTexture1 = false;
	
	if (material.diffuse) {
		if (material.diffuse.texture0) {
			gl.activeTexture(gl.TEXTURE0);		
			gl.bindTexture(gl.TEXTURE_2D, material.diffuse.texture0);
			useTexture0 = true;
		}
		
		if (material.diffuse.texture1) {
			gl.activeTexture(gl.TEXTURE1);		
			gl.bindTexture(gl.TEXTURE_2D, material.diffuse.texture1);		
			useTexture1 = true;
		}
	}
	 
	gl.uniform1i(this.shaderProgram.useTexture0Uniform, useTexture0);
	gl.uniform1i(this.shaderProgram.useTexture1Uniform, useTexture1);
};

KendziRender.validateIndexesMaxSize = function(indexes, size) { 
	for (var i=0; i < indexes.length; i++ ) {
		if (indexes[i] >= size) {
			throw ("index out of range: " + size);
		}
	}
};

KendziRender.loadModel = function(modelData, urlFactory) {

	var model = new RenderModel();
	
	model.vertexBuffer = KendziRender.createArray(modelData.vertex, 3);
	var size = modelData.vertex.length;
	if (size % 3 != 0) {
		throw new Error("wrong size of indexes");
	}
	
	model.normalBuffer = KendziRender.createArray(modelData.normals, 3);
	if (size != modelData.normals.length) {
		throw new Error("wrong size of normals");
	}	
	
	model.textureCoordBuffer = [];
	if (modelData.textureCoords) { // XXX
	for (var i = 0; i < modelData.textureCoords.length; i++) {
		if (modelData.textureCoords[i] == null || modelData.textureCoords[i].length ==0 ) {
			// XXX fix it in json serializer!
			break;
		}
		model.textureCoordBuffer[i] = KendziRender.createArray(modelData.textureCoords[i], 2);
		var expectedTcSize = size * 2 / 3;
		if (expectedTcSize != modelData.textureCoords[i].length) {
			throw new Error("wrong size of tc. expected: " + expectedTcSize + " found: " + modelData.textureCoords[i].length);
		}	
	}
	}
 
	model.faces = [];

	for (var i = 0; i < modelData.faces.length; i++) {
		var indexes = modelData.faces[i].indexes;
		var material = modelData.faces[i].material;
		KendziRender.validateIndexesMaxSize(indexes, size/3);

		var indexBuffer = gl.createBuffer();
		gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indexes),
				gl.STATIC_DRAW);
		indexBuffer.itemSize = 1;
		indexBuffer.numItems = indexes.length;
		
		var face = {};

		var renderMaterial = {"enabled":false};
		KendziRender.loadMaterial(material, renderMaterial, urlFactory);
		face.material = renderMaterial;
		
		face.indexBuffer = indexBuffer;

		model.faces.push(face);
	}
	
	return model;
};

KendziRender.createArray = function(array, size) {
	
	var debug = true;
	
	var buffer = gl.createBuffer();
	
	gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(array),
			gl.STATIC_DRAW);
	
	buffer.itemSize = size;
	buffer.numItems = array.length / size;
	
	if (debug) {
		buffer.debug = array;
	}
	
	return buffer;
};

KendziRender.loadMaterial = function(modelData, renderMaterial, urlFactory) {
	
	var diffuse = modelData.diffuse;
	
	if (diffuse) {
		
		renderMaterial.diffuse = {};
		if (diffuse.color) {
			var hex = hexToRgb(diffuse.color);
			
			if (hex) {
				renderMaterial.diffuse.color = hex;
			}			
		}
		
		if (diffuse.texture0) {
			renderMaterial.diffuse.texture0 = KendziRender.loadTextureUrl(modelData.diffuse.texture0, urlFactory);
		}
		if (diffuse.texture1) {
			renderMaterial.diffuse.texture1 = KendziRender.loadTextureUrl(modelData.diffuse.texture1, urlFactory);
		}
		
	}
	
	renderMaterial.enabled = true;
};

KendziRender.prototype.getTextureUrl = function(textureKey) {
	if (this.textureUrlPrefix) {
		return this.textureUrlPrefix + textureKey;
	}
	return textureKey;
};

KendziRender.loadTextureUrl = function(textureUrl, urlFactory) {
	
	var texture = gl.createTexture();
	texture.image = new Image();
	texture.image.crossOrigin = "anonymous";
	
	texture.image.onload = function() {
		KendziRender.handleLoadedTexture(texture);
	};
	if (urlFactory) {
		texture.image.src = urlFactory.getTextureUrl(textureUrl);
	} else {
		texture.image.src = textureUrl;
	}
	
	return texture;
};



KendziRender.handleLoadedTexture = function(modelData) {
//function handleLoadedTexture(texture) {
    gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
    gl.bindTexture(gl.TEXTURE_2D, modelData);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, modelData.image);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR_MIPMAP_NEAREST);
    gl.generateMipmap(gl.TEXTURE_2D);

    gl.bindTexture(gl.TEXTURE_2D, null);
};


KendziRender.prototype.initShaders = function() {
	
	var fragmentShader = getShader3(gl, "shader/kendzi3d-multi-layer-fs.c", "fragment");
    var vertexShader = getShader3(gl, "shader/kendzi3d-multi-layer-vs.c", "vertex");

    this.shaderProgram = gl.createProgram();
    gl.attachShader(this.shaderProgram, vertexShader);
    gl.attachShader(this.shaderProgram, fragmentShader);
    gl.linkProgram(this.shaderProgram);

    if (!gl.getProgramParameter(this.shaderProgram, gl.LINK_STATUS)) {
        alert("Could not initialise shaders");
    }

    gl.useProgram(this.shaderProgram);

    this.shaderProgram.vertexPositionAttribute = gl.getAttribLocation(this.shaderProgram, "aVertexPosition");
    gl.enableVertexAttribArray(this.shaderProgram.vertexPositionAttribute);

    this.shaderProgram.vertexNormalAttribute = gl.getAttribLocation(this.shaderProgram, "aVertexNormal");
    gl.enableVertexAttribArray(this.shaderProgram.vertexNormalAttribute);

    this.shaderProgram.textureCoord0Attribute = gl.getAttribLocation(this.shaderProgram, "aTextureCoord0");
    gl.enableVertexAttribArray(this.shaderProgram.textureCoord0Attribute);
    
    this.shaderProgram.textureCoord1Attribute = gl.getAttribLocation(this.shaderProgram, "aTextureCoord1");
    gl.enableVertexAttribArray(this.shaderProgram.textureCoord1Attribute);
    
    this.shaderProgram.pMatrixUniform = gl.getUniformLocation(this.shaderProgram, "uPMatrix");
    this.shaderProgram.mvMatrixUniform = gl.getUniformLocation(this.shaderProgram, "uMVMatrix");
    this.shaderProgram.nMatrixUniform = gl.getUniformLocation(this.shaderProgram, "uNMatrix");
    this.shaderProgram.samplerUniform1 = gl.getUniformLocation(this.shaderProgram, "uSampler1");
    this.shaderProgram.samplerUniform2 = gl.getUniformLocation(this.shaderProgram, "uSampler2");
    this.shaderProgram.materialShininessUniform = gl.getUniformLocation(this.shaderProgram, "uMaterialShininess");
    this.shaderProgram.showSpecularHighlightsUniform = gl.getUniformLocation(this.shaderProgram, "uShowSpecularHighlights");
    this.shaderProgram.useTexture0Uniform = gl.getUniformLocation(this.shaderProgram, "uUseTexture0");
    this.shaderProgram.useTexture1Uniform = gl.getUniformLocation(this.shaderProgram, "uUseTexture1");
    this.shaderProgram.useColoredTexture0Uniform = gl.getUniformLocation(this.shaderProgram, "uUseColoredTexture0");
    this.shaderProgram.useLightingUniform = gl.getUniformLocation(this.shaderProgram, "uUseLighting");
    this.shaderProgram.ambientColorUniform = gl.getUniformLocation(this.shaderProgram, "uAmbientColor");
    this.shaderProgram.diffuseColorUniform = gl.getUniformLocation(this.shaderProgram, "uDiffuseColor");
    this.shaderProgram.pointLightingLocationUniform = gl.getUniformLocation(this.shaderProgram, "uPointLightingLocation");
    this.shaderProgram.pointLightingSpecularColorUniform = gl.getUniformLocation(this.shaderProgram, "uPointLightingSpecularColor");
    this.shaderProgram.pointLightingDiffuseColorUniform = gl.getUniformLocation(this.shaderProgram, "uPointLightingDiffuseColor");
}

function RenderModel () {
	this.textureId = null;
	this.normalBufferId = null;
	this.textureCoordBufferId = null;
	this.vertexPositionBufferId = null;
};
