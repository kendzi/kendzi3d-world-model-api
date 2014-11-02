

function getShader3(gl, name, type) {
	   var shaderScript = getFile(name);
       if (!shaderScript) {
           return null;
       }
 
       var shader;
       if (type == "fragment") {
           shader = gl.createShader(gl.FRAGMENT_SHADER);
       } else if (type == "vertex") {
           shader = gl.createShader(gl.VERTEX_SHADER);
       } else {
           return null;
       }

       gl.shaderSource(shader, shaderScript);
       gl.compileShader(shader);

       if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
           alert(gl.getShaderInfoLog(shader));
           return null;
       }

       return shader;
}

function getFile(name) {
	var request = new XMLHttpRequest();
	
	request.open("GET",name,false);
	request.send();
	
	if (request.readyState == 4) {	 
		return request.responseText;
	}
}
 