function Kendzi3dQuadRender () {
	this.kendziRender = new KendziRender();	
	this.models = [];
}

Kendzi3dQuadRender.prototype.render= function(cameraMatrix) {
	
	var matrix = mat4.create();
	var iden = mat4.create();
	mat4.identity(iden);
	
	if (this.models ) {
		for (var i = 0; i < this.models.length; i++) {
			var quadObject = this.models[i];
			 
			mat4.multiply(cameraMatrix,quadObject.matrix, 
					matrix);
			
			this.kendziRender.setTranslateMatrixUniforms(matrix);
			
			for (var mesh =0; mesh < quadObject.model.length; mesh++) { 
				this.kendziRender.render(quadObject.model[mesh]);
			}
		}
	}	
};



Kendzi3dQuadRender.prototype.loadQuad = function(quadData, urlFactory) {
	this.quadData = quadData;
	this.models = [];
	if (quadData.quadModel) {
		for (var i = 0; i < quadData.quadModel.length; i++) {
			var quadObject = quadData.quadModel[i];
			
			var modelRenderData = {};
			modelRenderData.model = [];
			
			var translateMatrix = mat4.create();
			mat4.identity(translateMatrix);
			mat4.translate(translateMatrix, [quadObject.x, 0, quadObject.y]);			
			modelRenderData.matrix = translateMatrix;
			
			for (var mesh = 0; mesh < quadObject.model.length; mesh++) {
				
				modelRenderData.model.push(KendziRender.loadModel(quadObject.model[mesh], urlFactory));
				
			}
			this.models.push(modelRenderData);
		}
	}
};