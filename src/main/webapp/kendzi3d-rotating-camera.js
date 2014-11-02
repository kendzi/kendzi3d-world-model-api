
function KendziRotatingCamera() {
	this.lastTime = 0;
	this.cameraAngleX = 0;
	this.cameraAngleY = 15;
	
	this.cameraX = 0;
	this.cameraY = -30;
	this.cameraZ = 140;

	this.isAnimated = true;
};

KendziRotatingCamera.prototype.animate = function() {

	var timeNow = new Date().getTime();
	if (this.lastTime != 0 && this.isAnimated) {
		
		var elapsed = timeNow - this.lastTime;
		this.cameraAngleX += 0.05 * elapsed;
	}
	this.lastTime = timeNow;
};

KendziRotatingCamera.prototype.zoom = function(deltaZoom) {
	
	if (this.cameraZ + deltaZoom > 0) {
		this.cameraZ += deltaZoom;
	} 
};

KendziRotatingCamera.prototype.rotate = function(deltaX, deltaY) {
	this.isAnimated = false;
	if (this.cameraAngleY + deltaY < 90 && this.cameraAngleY + deltaY > -90) {
		this.cameraAngleY += deltaY;
	}
	this.cameraAngleX += deltaX;
};

KendziRotatingCamera.prototype.matrix = function() {
	var newRotationMatrix = mat4.create();
	mat4.identity(newRotationMatrix);
	
	mat4.translate(newRotationMatrix, [0, 0, -this.cameraZ]);//[this.cameraX , this.cameraY ,this.cameraZ ]);
	mat4.rotate(newRotationMatrix, degToRad(this.cameraAngleY), [ 1, 0, 0 ]);
	mat4.rotate(newRotationMatrix, degToRad(this.cameraAngleX), [ 0, 1, 0 ]);
	
	return newRotationMatrix;
};
