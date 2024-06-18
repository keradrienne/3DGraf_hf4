import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import org.khronos.webgl.WebGLRenderingContext
import vision.gears.webglmath.Geometry

class InfinitePlaneGeometry(val gl : WebGL2RenderingContext):Geometry() {
    val vertexBuffer = gl.createBuffer()
    init{
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer)
        gl.bufferData(
            WebGLRenderingContext.ARRAY_BUFFER,
            Float32Array( arrayOf<Float>(
                0.0f, 0.0f, 0.0f, 1.0f,
                -1.0f, 1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f, 0.0f,
                0.0f, -1.0f, 0.0f, 0.0f
            )),
            WebGLRenderingContext.STATIC_DRAW)
    }

    val vertexNormalBuffer = gl.createBuffer()
    init{
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexNormalBuffer)
        gl.bufferData(
            WebGLRenderingContext.ARRAY_BUFFER,
            Float32Array( arrayOf<Float>(
                0.0f,  0.0f, 1.0f,
                0.0f,  0.0f, 1.0f,
                0.0f,  0.0f, 1.0f,
                0.0f,  0.0f, 1.0f
            )),
            WebGLRenderingContext.STATIC_DRAW)
    }

    val vertexTexCoordBuffer = gl.createBuffer()
    init{
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexTexCoordBuffer)
        gl.bufferData(
            WebGLRenderingContext.ARRAY_BUFFER,
            Float32Array( arrayOf<Float>(
                0.0f,  1.0f, 0.0f, 1.0f,
                0.0f,  0.0f, 0.0f, 0.0f,
                1.0f,  1.0f, 0.0f, 0.0f,
                1.0f,  0.0f, 0.0f, 0.0f
            )),
            WebGLRenderingContext.STATIC_DRAW)
    }

    val indexBuffer = gl.createBuffer()
    init{
        gl.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer)
        gl.bufferData(
            WebGLRenderingContext.ELEMENT_ARRAY_BUFFER,
            Uint16Array( arrayOf<Short>(
                0, 1, 2,
                0, 1, 3,
                0, 2, 3
            )),
            WebGLRenderingContext.STATIC_DRAW)
    }

    val inputLayout = gl.createVertexArray()
    init{
        gl.bindVertexArray(inputLayout)

        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer)
        gl.enableVertexAttribArray(0)
        gl.vertexAttribPointer(0,
            4, WebGLRenderingContext.FLOAT, //< three pieces of float
            false, //< do not normalize (make unit length)
            0, //< tightly packed
            0 //< data starts at array start
        )
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexNormalBuffer)
        gl.enableVertexAttribArray(1)
        gl.vertexAttribPointer(1,
            3, WebGLRenderingContext.FLOAT, //< three pieces of float
            false, //< do not normalize (make unit length)
            0, //< tightly packed
            0 //< data starts at array start
        )
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexTexCoordBuffer)
        gl.enableVertexAttribArray(2)
        gl.vertexAttribPointer(2,
            4, WebGLRenderingContext.FLOAT, //< two pieces of float
            false, //< do not normalize (make unit length)
            0, //< tightly packed
            0 //< data starts at array start
        )
        gl.bindVertexArray(null)
    }

    override fun draw() {
        gl.bindVertexArray(inputLayout)
        gl.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer)

        gl.drawElements(WebGLRenderingContext.TRIANGLE_FAN, 9, WebGLRenderingContext.UNSIGNED_SHORT, 0)
    }
}