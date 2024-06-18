import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL
import vision.gears.webglmath.UniformProvider
import kotlin.js.Date
import kotlin.math.PI

class Scene(
    val gl: WebGL2RenderingContext
) : UniformProvider("scene") {

    val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")

    val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")
    val fsEnvmapped = Shader(gl, GL.FRAGMENT_SHADER, "envmapped-fs.glsl")
    val fsnormalmapped = Shader(gl, GL.FRAGMENT_SHADER, "normalmapped-fs.glsl")
    val fsMaxBlinn = Shader(gl, GL.FRAGMENT_SHADER, "maxblinn-fs.glsl")
    val fsMaxBlinnSpot = Shader(gl, GL.FRAGMENT_SHADER, "maxblinn-spot-fs.glsl")

    val vsNormalMapped = Shader(gl, GL.VERTEX_SHADER, "normalmapped-vs.glsl")

    val texturedProgram = Program(gl, vsTrafo, fsTextured)
    val envmappedProgram = Program(gl, vsTrafo, fsEnvmapped)
    //val normalmappedProgram = Program(gl, vsTrafo, fsnormalmapped)
    val normalmappedProgram = Program(gl, vsNormalMapped, fsnormalmapped)

    val maxBlinnProgram = Program(gl, vsTrafo, fsMaxBlinn)
    val maxBlinnSpotProgram = Program(gl, vsTrafo, fsMaxBlinnSpot)

    val vsInfinitePlane = Shader(gl, GL.VERTEX_SHADER, "infinite-plane-vs.glsl")
    val fsInfinitePlane = Shader(gl, GL.FRAGMENT_SHADER, "infinite-plane-fs.glsl")
    val infinitePlaneProgram = Program(gl, vsInfinitePlane, fsInfinitePlane)

    val vsQuad = Shader(gl, GL.VERTEX_SHADER, "quad-vs.glsl")
    val fsBackground = Shader(gl, GL.FRAGMENT_SHADER, "background-fs.glsl")
    val backgroundProgram = Program(gl, vsQuad, fsBackground)
    val backgroundMaterial = Material(backgroundProgram)
    val skyCubeTexture = TextureCube(
        gl,
        "media/posx512.jpg", "media/negx512.jpg",
        "media/posy512.jpg", "media/negy512.jpg",
        "media/posz512.jpg", "media/negz512.jpg"
    )
    init {
        backgroundMaterial["envTexture"]?.set(skyCubeTexture)
    }

    val quadGeometry = TexturedQuadGeometry(gl)
    val backgroundMesh = Mesh(backgroundMaterial, quadGeometry)

    val slowpokeTexture = Texture2D(gl, "media/slowpoke/YadonDh.png")
    val slowpokeEyeTexture = Texture2D(gl, "media/slowpoke/YadonEyeDh.png")
    val slowpokeMaterial = Material(maxBlinnSpotProgram)
    init {
        slowpokeMaterial["colorTexture"]?.set(slowpokeTexture)
    }

    val slowpokeEyeMaterial = Material(maxBlinnSpotProgram)
    init {
        slowpokeEyeMaterial["colorTexture"]?.set(slowpokeEyeTexture)
    }

    val jsonLoader = JsonLoader()
    val slowpokeGeometries = jsonLoader.loadGeometries(
        gl,
        "media/slowpoke/slowpoke.json"
    )
    val slowpokeMeshes = arrayOf(
        Mesh(
            Material(maxBlinnSpotProgram).apply {
                this["colorTexture"]?.set(
                    Texture2D(gl, "media/slowpoke/YadonDh.png")
                )
            }, slowpokeGeometries[0]
        ),
        Mesh(
            Material(maxBlinnSpotProgram).apply {
                this["colorTexture"]?.set(
                    Texture2D(gl, "media/slowpoke/YadonEyeDh.png")
                )
            }, slowpokeGeometries[1]
        ),
    )

    val normalMap = Texture2D(gl, "media/normalMap.png")

    val normalmappedMaterial = Material(normalmappedProgram)
    init {
        //LABTODO: set environment to env mapped material
        normalmappedMaterial["texture"]?.set(normalMap)
    }

    val normalmappedSlowpokeMeshes = arrayOf(
        Mesh(normalmappedMaterial, slowpokeGeometries[0]),
        Mesh(normalmappedMaterial, slowpokeGeometries[1])
    )

    val groundMaterial = Material(infinitePlaneProgram)
    val InfinitePlaneGeometry = InfinitePlaneGeometry(gl)
    val groundMesh = Mesh(groundMaterial, InfinitePlaneGeometry)

    val gameObjects = ArrayList<GameObject>()
    val avatar = GameObject(*slowpokeMeshes)
    init {
        gameObjects.add(avatar)
        val normalmappedObject = GameObject(*normalmappedSlowpokeMeshes).apply {
            position.set(20.0f)
        }
        gameObjects += normalmappedObject
        //gameObjects += GameObject(groundMesh)
    }



    //LABTODO: lights
    val lights = Array<Light>(4) { Light(it, *Program.all) }
    init {
        lights[0].position.set(0.0f, 10.0f, 0.0f, 1.0f)
        lights[0].powerDensity.set(0.0f, 1.0f, 0.91f)
        lights[0].direction.set(0.0f, -1.0f, 0.0f)
        lights[0].exponentAndAngle.set(3.0f, 150.0f)

        lights[1].position.set(-10.0f, 5.0f, 0.0f, 1.0f)
        lights[1].powerDensity.set(1.0f, 0.0f, 0.91f)
        lights[1].direction.set(1.0f, 0.0f, 0.0f)
        lights[1].exponentAndAngle.set(5.0f, 150.0f)

        lights[2].position.set(25.0f, 5.0f, 0.0f, 1.0f)
        lights[2].powerDensity.set(0.0f, 1.0f, 0.0f)
        lights[2].direction.set(0.0f, -1.0f, 0.0f)
        lights[2].exponentAndAngle.set(2.0f, 150.0f)

        lights[3].position.set(25.0f, 10.0f, 0.0f, 1.0f)
        lights[3].powerDensity.set(1.0f, 0.42f, 0.0f)
        lights[3].direction.set(0.0f, -1.0f, 0.0f)
        lights[3].exponentAndAngle.set(3.0f, 150.0f)


    }

    val camera = PerspectiveCamera(*Program.all)
    val mirrorCamera = PerspectiveCamera(*Program.all).apply {
        mirror = true
        yaw = PI.toFloat()
    }

    val mirrorQuadGeometry = FlipQuadGeometry(gl)
    //LABTODO: resources for multipass rendering
    val fsPostProc = Shader(gl, GL.FRAGMENT_SHADER, "postproc-fs.glsl")
    val vsPostProc = Shader(gl, GL.VERTEX_SHADER, "postproc-vs.glsl")
    val postProcProgram = Program(gl, vsPostProc, fsPostProc)
    val postProcMaterial = Material(postProcProgram)
    val postProcQuadMesh = Mesh(postProcMaterial, mirrorQuadGeometry)

    val timeAtFirstFrame = Date().getTime()
    var timeAtLastFrame = timeAtFirstFrame

    init {
        gl.enable(GL.DEPTH_TEST)
        addComponentsAndGatherUniforms(*Program.all)
    }

    lateinit var defaultFramebuffer : DefaultFramebuffer
    lateinit var postProcFramebuffer : Framebuffer

    fun resize(gl: WebGL2RenderingContext, canvas: HTMLCanvasElement) {
        gl.viewport(0, 0, canvas.width, canvas.height)
        camera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())
        camera.position.set(10.0f, 5.0f, 20.0f)

        mirrorCamera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())
        mirrorCamera.position.set(10.0f, 5.0f, 20.0f)

        //LABTODO: create and bind framebuffer resources
        defaultFramebuffer = DefaultFramebuffer(
            canvas.width, canvas.height)
        postProcFramebuffer = Framebuffer(gl, 1,
            canvas.width, canvas.height)
        postProcMaterial["rawTexture"]?.set(
            postProcFramebuffer.targets[0] )

    }

    fun update(gl: WebGL2RenderingContext, keysPressed: Set<String>) {

        val timeAtThisFrame = Date().getTime()
        val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
        val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
        timeAtLastFrame = timeAtThisFrame

        camera.move(dt, keysPressed)


        //LABTODO: set render target
        postProcFramebuffer.bind(gl)

        // clear the screen
        gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)
        gl.clearDepth(1.0f)
        gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)

        val spawn = ArrayList<GameObject>()
        val killList = ArrayList<GameObject>()
        gameObjects.forEach {
            if (!it.move(t, dt, keysPressed, gameObjects, spawn)) {
                killList.add(it)
            }
        }
        killList.forEach { gameObjects.remove(it) }
        spawn.forEach { gameObjects.add(it) }

        mirrorCamera.move(dt, keysPressed)

        gameObjects.forEach { it.update() }

        backgroundMesh.draw(mirrorCamera)
        gameObjects.forEach { it.draw(mirrorCamera, *lights /* LABTODO: pass lights*/) }

        //LABTODO: post processing
        defaultFramebuffer.bind(gl)

        postProcQuadMesh.draw(mirrorCamera)

        gameObjects.forEach { it.update() }
        backgroundMesh.draw(camera)
        gameObjects.forEach { it.draw(camera, *lights /* LABTODO: pass lights*/) }
    }
}
