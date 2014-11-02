
    attribute vec3 aVertexPosition;
    attribute vec3 aVertexNormal;
    attribute vec2 aTextureCoord0;
    attribute vec2 aTextureCoord1;

    uniform mat4 uMVMatrix;
    uniform mat4 uPMatrix;
    uniform mat3 uNMatrix;

    varying vec4 vPosition;
    varying vec3 vTransformedNormal;

    varying vec2 vTextureCoord0;
	varying vec2 vTextureCoord1;

    void main(void) {
        vPosition = uMVMatrix * vec4(aVertexPosition, 1.0);
        gl_Position = uPMatrix * vPosition;
        vTextureCoord0 = aTextureCoord0;
        vTextureCoord1 = aTextureCoord1;
        vTransformedNormal = uNMatrix * aVertexNormal;
    }